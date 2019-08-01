package com.cmgun.service.impl;

import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.Task;
import com.cmgun.api.model.TaskAuditRequest;
import com.cmgun.api.model.ToDoTaskRequest;
import com.cmgun.service.BusiRequestService;
import com.cmgun.service.ProcTaskService;
import com.cmgun.utils.ExceptionUtil;
import com.cmgun.utils.PageUtil;
import com.cmgun.utils.TaskUtil;
import com.google.common.collect.Lists;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 任务管理
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
@Service
public class ProcTaskServiceImpl implements ProcTaskService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private BusiRequestService busiRequestService;

    @Override
    public PageResult<Task> queryToDoList(ToDoTaskRequest request) {
        TaskQuery query = todoTaskQuery(request);
        // 查询结果
        List<org.activiti.engine.task.Task> tasks = Lists.newArrayList();
        // 总数
        long count = PageUtil.query(query, request, tasks);
        // 封装对象
        List<Task> taskList = new ArrayList<>();
        for (org.activiti.engine.task.Task rawTask : tasks) {
            Task.TaskBuilder builder = Task.builder()
                    .taskId(rawTask.getId())
                    .dueDate(rawTask.getDueDate())
                    .createTime(rawTask.getCreateTime())
                    .processDefinitionId(rawTask.getProcessDefinitionId())
                    .category(rawTask.getCategory());
            // 流程变量
            Map<String, Object> variables = taskService.getVariables(rawTask.getId());
            builder.data(TaskUtil.getProcessData(variables));
            // 封装上个任务审批结果
            TaskUtil.parseLastTaskAuditInfo(builder, variables);
            // 流程实例
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(rawTask.getProcessInstanceId())
                    .list();
            if (CollectionUtils.isNotEmpty(processInstances)) {
                builder.businessKey(processInstances.get(0).getBusinessKey());
                builder.processDefinitionKey(processInstances.get(0).getProcessDefinitionKey());
            }
            taskList.add(builder.build());
        }
        return PageResult.result(request, count, taskList);
    }

    /**
     * 构建待办任务查询，进行必要查询条件校验
     *
     * @param request 查询参数
     * @return 待办任务查询
     */
    private TaskQuery todoTaskQuery(ToDoTaskRequest request) {
        // 用户组或用户必须存在一个非空
        ExceptionUtil.businessException(request.getAssignee() == null && request.getCandidateGroup() == null
                && request.getCandidateUser() == null, "用户组或用户查询条件不能都为空");
        TaskQuery query = taskService.createTaskQuery()
                .taskCategory(request.getCategory())
                .active();
        if (request.getCandidateGroup() != null) {
            query.taskCandidateGroup(request.getCandidateGroup());
        }
        if (request.getCandidateUser() != null) {
            query.taskCandidateUser(request.getCandidateUser());
        }
        if (request.getAssignee() != null) {
            query.taskAssignee(request.getAssignee());
        }
        query.orderByTaskCreateTime();
        query.desc();
        return query;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void audit(TaskAuditRequest request) {
        String taskId = request.getTaskId();
        // 任务是否存在
        long existsTask = taskService.createTaskQuery()
                .taskId(taskId)
                .active()
                .count();
        ExceptionUtil.businessException(existsTask <= 0, "任务id不存在");
        // 判断当前组是否有权限处理该任务
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        boolean authResult = false;
        for (IdentityLink identityLink : identityLinks) {
            if (request.getCandidateGroup().equals(identityLink.getGroupId())) {
                authResult = true;
                break;
            }
        }
        ExceptionUtil.authorizationException(!authResult, "当前用户组没有权限做此操作");

        // 设置审批人名称，这里是组名
        Authentication.setAuthenticatedUserId(request.getAuditor());
        taskService.addComment(taskId, null, request.getComment());

        // 更新全局上下文
        Map<String, Object> globalContext = taskService.getVariables(taskId);
        TaskUtil.globalTaskContext(globalContext, request.getTaskContext());
        taskService.setVariables(taskId, globalContext);
        // 下个任务节点上下文
        Map<String, Object> localContext = TaskUtil.auditTaskLocalContext(request);
        taskService.setVariablesLocal(taskId, localContext);
        // 当前用户签收任务
        taskService.claim(taskId, request.getAuditor());
        // 任务审批
        taskService.complete(taskId, localContext);
        // 请求更新
        busiRequestService.updateSuccessReq(request);
    }


}

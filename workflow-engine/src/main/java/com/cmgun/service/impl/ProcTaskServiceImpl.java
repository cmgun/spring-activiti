package com.cmgun.service.impl;

import com.cmgun.api.model.Task;
import com.google.common.collect.Lists;
import com.cmgun.api.common.PageResult;
import com.cmgun.api.model.*;
import com.cmgun.service.BusiRequestService;
import com.cmgun.service.ProcTaskService;
import com.cmgun.utils.ExceptionUtil;
import com.cmgun.utils.PageUtil;
import com.cmgun.utils.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务管理
 *
 * @author chenqilin
 * @date 2019/7/23
 */
@Slf4j
@Service
public class ProcTaskServiceImpl implements ProcTaskService {

    @Autowired
    private RepositoryService repositoryService;
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
            taskList.add(this.getTaskInfo(rawTask));
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
            .active();
        if (StringUtils.isNotEmpty(request.getTaskDefinitionKey())) {
            query.taskDefinitionKey(request.getTaskDefinitionKey());
        }
        if (StringUtils.isNotEmpty(request.getTaskId())) {
            query.taskId(request.getTaskId());
        }
        if (StringUtils.isNotEmpty(request.getTaskCategory())) {
            query.taskCategory(request.getTaskCategory());
        }
        if (StringUtils.isNotEmpty(request.getProcCategory())) {
            query.processCategoryIn(Lists.newArrayList(request.getProcCategory()));
        }
        if (StringUtils.isNotEmpty(request.getBusinessKey())) {
            query.processInstanceBusinessKey(request.getBusinessKey());
        }
        if (CollectionUtils.isNotEmpty(request.getCandidateGroup())) {
            query.taskCandidateGroupIn(request.getCandidateGroup());
        }
        if (request.getCandidateUser() != null) {
            query.taskCandidateUser(request.getCandidateUser());
        }
        if (request.getAssignee() != null) {
            query.taskAssignee(request.getAssignee());
        }
        // 任务创建时间
        if (request.getTaskCreateTimeBegin() != null) {
            query.taskCreatedAfter(request.getTaskCreateTimeBegin());
        }
        if (request.getTaskCreateTimeEnd() != null) {
            query.taskCreatedBefore(request.getTaskCreateTimeEnd());
        }
        query.orderByTaskCreateTime();
        query.desc();
        return query;
    }

    /**
     * 封装任务信息
     *
     * @param rawTask 任务
     * @return 封装后的任务信息
     */
    private Task getTaskInfo(org.activiti.engine.task.Task rawTask) {
        Task.TaskBuilder builder = Task.builder()
            .taskId(rawTask.getId())
            .taskDefKey(rawTask.getTaskDefinitionKey())
            .taskName(rawTask.getName())
            .dueDate(rawTask.getDueDate())
            .createTime(rawTask.getCreateTime())
            .processInstanceId(rawTask.getProcessInstanceId())
            .category(rawTask.getCategory());
        // 流程变量
        Map<String, Object> variables = taskService.getVariables(rawTask.getId());
        builder.data(TaskUtil.getProcessData(variables));
        // 封装上个任务审批结果
        TaskUtil.parseLastTaskAuditInfo(builder, variables);
        // 流程实例
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(rawTask.getProcessInstanceId()).list();
        if (CollectionUtils.isNotEmpty(processInstances)) {
            ProcessInstance processInstance = processInstances.get(0);
            builder.businessKey(processInstance.getBusinessKey());
            builder.processDefinitionKey(processInstance.getProcessDefinitionKey());
        }
        return builder.build();
    }

    @Override
    public Task queryDetail(String taskId) {
        // 查询结果
        List<org.activiti.engine.task.Task> rawTasks = taskService.createTaskQuery()
            .taskId(taskId)
            .list();
        if (CollectionUtils.isEmpty(rawTasks)) {
            return null;
        }
        // 封装结果
        return this.getTaskInfo(rawTasks.get(0));
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
        List<String> groupIds = identityLinks.stream().map(IdentityLink::getGroupId).collect(Collectors.toList());
        // 判断是否有交集
        Collection<String> intersection = CollectionUtils.intersection(groupIds, request.getCandidateGroup());
        ExceptionUtil.authorizationException(CollectionUtils.isEmpty(intersection), "当前用户组没有权限做此操作");

        // 设置审批人名称，这里是组名
        Authentication.setAuthenticatedUserId(request.getAuditor());
        // 备注为空则不填写
        if (StringUtils.isNotBlank(request.getComment())) {
            taskService.addComment(taskId, null, request.getComment());
        }

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

    @Override
    public List<Task> queryActiveTaskByProcess(ProcessActiveTaskRequest request) {
        TaskQuery query = taskService.createTaskQuery()
            .processDefinitionKey(request.getProcessDefinitionKey())
            .processInstanceBusinessKey(request.getBusinessKey())
            .active();
        if (StringUtils.isNotBlank(request.getCategory())) {
            query = query.taskCategory(request.getCategory());
        }
        List<org.activiti.engine.task.Task> rawTasks = query.list();
        if (CollectionUtils.isEmpty(rawTasks)) {
            return Lists.newArrayList();
        }

        // 封装查询结果
        List<Task> tasks = Lists.newArrayList();
        for (org.activiti.engine.task.Task rawTask : rawTasks) {
            // 流程定义
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(rawTask.getProcessDefinitionId())
                .list();
            if (CollectionUtils.isEmpty(processDefinitions)) {
                // 后续不填充流程命名空间
                log.warn("流程定义不存在，流程id:{}", rawTask.getProcessDefinitionId());
            }

            Task task = Task.builder()
                .taskId(rawTask.getId())
                .taskDefKey(rawTask.getTaskDefinitionKey())
                .taskName(rawTask.getName())
                .dueDate(rawTask.getDueDate())
                .createTime(rawTask.getCreateTime())
                .processInstanceId(rawTask.getProcessInstanceId())
                .processDefinitionId(rawTask.getProcessDefinitionId())
                // 流程命名空间
                .procNameSpace(CollectionUtils.isEmpty(processDefinitions) ? null : processDefinitions.get(0).getCategory())
                .category(rawTask.getCategory())
                .businessKey(request.getBusinessKey())
                .build();
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public List<TaskIdentity> queryIdentityLink(List<String> taskIds) {
        List<TaskIdentity> identities = Lists.newArrayList();
        for (String taskId : taskIds) {
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
            identities.addAll(identityLinks.stream().map(e -> TaskIdentity.builder()
                .taskId(taskId)
                .type(e.getType())
                .groupId(e.getGroupId())
                .userId(e.getUserId())
                .processId(e.getProcessInstanceId())
                .build()).collect(Collectors.toList()));
        }
        return identities;
    }
}

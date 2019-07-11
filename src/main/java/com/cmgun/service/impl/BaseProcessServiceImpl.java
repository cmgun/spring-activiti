package com.cmgun.service.impl;

import com.cmgun.command.BaseTaskCommand;
import com.cmgun.entity.TaskContext;
import com.cmgun.entity.vo.HistoryVo;
import com.cmgun.entity.vo.TaskVo;
import com.cmgun.service.BaseProcessService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BaseProcessServiceImpl implements BaseProcessService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private BaseTaskCommand baseTaskCommand;

    @Override
    public ProcessInstance startProcess(Object data, String businessKey) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", data);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("add-process", businessKey, variables);
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("add-process", businessKey);
        log.debug(String.format("id:%s,activitiId:%s", processInstance.getId(), processInstance.getActivityId()));
        return processInstance;
    }

    @Override
    public List<TaskVo> queryGroupToDoTasks(String groupName) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateGroup(groupName);
        List<Task> tasks = taskQuery.list();
        if (CollectionUtils.isEmpty(tasks)) {
            return new ArrayList<>();
        }
        // 封装对象
        List<TaskVo> taskVos = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            taskVos.add(new TaskVo(task.getId(), variables));
        }
        return taskVos;
    }

    @Transactional
    @Override
    public void approveTask(String taskId, String groupName, String approveResult, String comments) {
        // 判断当前组是否有权限处理该任务
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        boolean authResult = false;
        for (IdentityLink identityLink : identityLinks) {
            if (groupName.equals(identityLink.getGroupId())) {
                authResult = true;
                break;
            }
        }
        if (!authResult) {
            throw new RuntimeException("No Authentication to do this");
        }

        // 设置审批人名称，这里是组名
        Authentication.setAuthenticatedUserId(groupName + "user");
        taskService.addComment(taskId, null, comments);

        // 业务逻辑处理
        baseTaskCommand.execute(new TaskContext<>(taskId, groupName, approveResult, comments));

        Map<String, Object> params = new HashMap<>();
        params.put("result", approveResult);
//        params.put("applicationContext", applicationContext);
        taskService.setVariablesLocal(taskId, params);
        // 当前用户签收任务
        taskService.claim(taskId, groupName + "user");
        // 任务审批
        taskService.complete(taskId, params);

        // throw runtime exception
//        throw new RuntimeException("mock exception");
    }

    @Override
    public List<HistoryVo> queryHistory(String userId, String businessKey) {
        // 新建查询
        // 业务id必传
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(businessKey);
//                .finished();
        // 组和Assignee不能同时查，否则会添加ASSIGNEE_ IS NULL
        if (StringUtils.isNotBlank(userId)) {
            taskQuery = taskQuery.taskAssignee(userId);
        }
        List<HistoricTaskInstance> taskInstances = taskQuery.list();
        if (CollectionUtils.isEmpty(taskInstances)) {
            return new ArrayList<>();
        }
        List<HistoryVo> historyVoList = new ArrayList<>();
        // 填充列表
        for (HistoricTaskInstance taskInstance : taskInstances) {
            // 审批备注，目前只有单人审批，记录只有一条
            List<Comment> comments = taskService.getTaskComments(taskInstance.getId());
            // 查询流程参数
            List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                    .excludeTaskVariables()
                    .processInstanceId(taskInstance.getProcessInstanceId())
                    .list();
            // 查询任务审批参数
            List<HistoricVariableInstance> taskLocalVariables = historyService.createHistoricVariableInstanceQuery()
                    .taskId(taskInstance.getId())
                    .list();
            HistoryVo historyVo = new HistoryVo(taskInstance, comments, variables, taskLocalVariables);
            historyVoList.add(historyVo);
        }
        return historyVoList;
    }
}

package com.cmgun.service.impl;

import com.cmgun.entity.vo.TaskVo;
import com.cmgun.service.BaseProcessService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Authentication.setAuthenticatedUserId(groupName);
        taskService.addComment(taskId, null, comments);

        Map<String, Object> params = new HashMap<>();
        params.put("result", approveResult);
//        params.put("applicationContext", applicationContext);
        taskService.setVariablesLocal(taskId, params);

        taskService.complete(taskId, params);
    }
}

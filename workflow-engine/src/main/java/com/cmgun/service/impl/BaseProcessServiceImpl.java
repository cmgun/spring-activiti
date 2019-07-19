package com.cmgun.service.impl;

//import com.cmgun.command.BaseTaskCommand;

import com.cmgun.entity.vo.HistoryVO;
import com.cmgun.entity.vo.ProcessVO;
import com.cmgun.entity.vo.TaskVO;
import com.cmgun.service.BaseProcessService;
import com.cmgun.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
//    @Autowired
//    private BaseTaskCommand baseTaskCommand;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessVO deployProcess(String processName, String key, MultipartFile multipartFile) throws IOException {
        log.info("准备部署流程, name:{}, key:{}", processName, key);
        Deployment deployment = repositoryService.createDeployment()
                .name(processName)
                .key(key)
                .addInputStream(multipartFile.getOriginalFilename(), multipartFile.getInputStream())
                .deploy();
        ExceptionUtil.businessException(deployment == null, "部署失败");
        log.info("部署流程结束, name:{}, key:{}, id:{}", processName, key, deployment.getId());
        // 查询对应流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .list();
        ExceptionUtil.businessException(CollectionUtils.isEmpty(processDefinitions), "部署失败，流程定义不存在");
        return new ProcessVO(deployment, processDefinitions.get(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessVO startProcess(String processDefinitionKey, String businessKey, Object processData) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", processData);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        ExceptionUtil.businessException(processInstance == null, "启动流程失败");
        log.info("开启流程, processId:{}, defKey:{}, businessKey:{}", processInstance.getId()
                , processInstance.getProcessDefinitionKey()
                , processInstance.getBusinessKey());
        return new ProcessVO(processInstance);
    }

    @Override
    public List<TaskVO> queryGroupToDoTasks(String groupName) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateGroup(groupName);
        List<Task> tasks = taskQuery.list();
        if (CollectionUtils.isEmpty(tasks)) {
            return new ArrayList<>();
        }
        // 封装对象
        List<TaskVO> taskVOs = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            taskVOs.add(new TaskVO(task.getId(), variables));
        }
        return taskVOs;
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
//        baseTaskCommand.execute(new TaskContext<>(taskId, groupName, approveResult, comments));

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
    public List<HistoryVO> queryHistory(String userId, String businessKey) {
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
        List<HistoryVO> historyVOList = new ArrayList<>();
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
            HistoryVO historyVo = new HistoryVO(taskInstance, comments, variables, taskLocalVariables);
            historyVOList.add(historyVo);
        }
        return historyVOList;
    }
}

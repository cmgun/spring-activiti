package com.cmgun.service.impl;

//import com.cmgun.command.BaseTaskCommand;

import com.cmgun.api.common.TaskContext;
import com.cmgun.api.model.Process;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.entity.dto.DeployDTO;
import com.cmgun.entity.vo.HistoryVO;
import com.cmgun.service.BusiRequestService;
import com.cmgun.service.ProcessService;
import com.cmgun.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
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
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private BusiRequestService busiRequestService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Process deployProcess(DeployDTO deployDTO) throws IOException {
        log.info("准备部署流程, name:{}, key:{}, category:{}", deployDTO.getProcessName()
                , deployDTO.getKey(), deployDTO.getCategory());
        MultipartFile file = deployDTO.getFile();
        Deployment deployment = repositoryService.createDeployment()
                .name(deployDTO.getProcessName())
                .key(deployDTO.getKey())
                .category(deployDTO.getCategory())
                .addInputStream(file.getOriginalFilename(), file.getInputStream())
                .deploy();
        ExceptionUtil.businessException(deployment == null, "部署失败");
        log.info("部署流程结束, name:{}, key:{}, category:{}, id:{}", deployDTO.getProcessName()
                , deployDTO.getKey(), deployDTO.getCategory(), deployment.getId());
        // 查询对应流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .list();
        ExceptionUtil.businessException(CollectionUtils.isEmpty(processDefinitions), "部署失败，流程定义不存在");
        ProcessDefinition processDefinition = processDefinitions.get(0);
        return Process.builder()
                .id(processDefinition.getId())
                .processDefinitionKey(processDefinition.getKey())
                .category(deployment.getCategory())
                .name(processDefinition.getName())
                .deploymentTime(deployment.getDeploymentTime())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Process startProcess(ProcessStartRequest request) {
        Map<String, Object> variables = new HashMap<>();
        TaskContext taskContext = request.getTaskContext();
        // 流程变量
        variables.put("data", taskContext.getGlobalPayload());
        // 下个节点的参与组
        variables.put("candidateGroups", taskContext.getCandidateGroups());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(request.getProcessDefinitionKey()
                , request.getBusinessKey(), variables);
        ExceptionUtil.businessException(processInstance == null, "启动流程失败");
        log.info("开启流程, processId:{}, defKey:{}, businessKey:{}", processInstance.getId()
                , processInstance.getProcessDefinitionKey()
                , processInstance.getBusinessKey());
        // 记录成功请求记录
        busiRequestService.updateSuccessReq(request);
        return Process.builder()
                .id(processInstance.getId())
                .processDefinitionKey(processInstance.getProcessDefinitionKey())
                .businessKey(processInstance.getBusinessKey())
                .build();
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

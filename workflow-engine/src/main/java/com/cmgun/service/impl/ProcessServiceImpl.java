package com.cmgun.service.impl;

//import com.cmgun.command.BaseTaskCommand;

import com.cmgun.api.common.TaskContext;
import com.cmgun.api.model.Process;
import com.cmgun.api.model.ProcessStartRequest;
import com.cmgun.entity.dto.DeployDTO;
import com.cmgun.service.BusiRequestService;
import com.cmgun.service.ProcessService;
import com.cmgun.utils.ExceptionUtil;
import com.cmgun.utils.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private RuntimeService runtimeService;
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
        TaskContext taskContext = request.getTaskContext();
        Map<String, Object> context = new HashMap<>();
        // 初始化流程上下文
        TaskUtil.globalTaskContext(context, taskContext);
        // 流程开启
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(request.getProcessDefinitionKey()
                , request.getBusinessKey(), context);
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
}

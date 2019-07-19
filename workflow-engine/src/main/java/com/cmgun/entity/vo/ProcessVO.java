package com.cmgun.entity.vo;

import lombok.Data;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程信息实体
 *
 * @author chenqilin
 * @Date 2019/7/17
 */
@Data
public class ProcessVO implements Serializable {

    private static final long serialVersionUID = 5323636808959576145L;

    /**
     * 流程主键
     */
    private String id;

    /**
     * 流程定义id
     */
    private String processDefinitionKey;

    /**
     * 流程业务key
     */
    private String businessKey;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 部署时间
     */
    private Date deploymentTime;

    public ProcessVO(Deployment deployment, ProcessDefinition processDefinition) {
        this.id = processDefinition.getId();
        this.processDefinitionKey = processDefinition.getKey();
        this.name = processDefinition.getName();
        this.deploymentTime = deployment.getDeploymentTime();
    }

    public ProcessVO(ProcessInstance processInstance) {
        this.id = processInstance.getId();
        this.processDefinitionKey = processInstance.getProcessDefinitionKey();
        this.name = processInstance.getName();
        this.businessKey = processInstance.getBusinessKey();
    }
}

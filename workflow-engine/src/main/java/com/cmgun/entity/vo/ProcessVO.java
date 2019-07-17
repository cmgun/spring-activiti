package com.cmgun.entity.vo;

import lombok.Data;
import org.activiti.engine.repository.Deployment;

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

    private String processId;

    private String name;

    private String key;

    private Date deploymentTime;

    public ProcessVO(Deployment deployment) {
        this.processId = deployment.getId();
        this.name = deployment.getName();
        this.key = deployment.getKey();
        this.deploymentTime = deployment.getDeploymentTime();
    }
}

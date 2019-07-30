package com.cmgun.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程信息实体
 *
 * @author chenqilin
 * @Date 2019/7/17
 */
@Data
@Builder
public class Process implements Serializable {

    private static final long serialVersionUID = 5323636808959576145L;

    @ApiModelProperty("流程主键")
    private String id;

    @ApiModelProperty("流程定义key")
    private String processDefinitionKey;

    @ApiModelProperty("流程业务key")
    private String businessKey;

    @ApiModelProperty("流程类别")
    private String category;

    @ApiModelProperty("流程名称")
    private String name;

    @ApiModelProperty("部署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;
}

package com.cmgun.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 任务信息
 *
 * @author chenqilin
 * @Date 2019/7/17
 */
@Data
@Builder
public class Task implements Serializable {

    private static final long serialVersionUID = 2679246220675811368L;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("流程key")
    private String processDefinitionKey;

    @ApiModelProperty("流程定义id")
    private String processDefinitionId;

    @ApiModelProperty("业务id")
    private String businessKey;

    @ApiModelProperty("流程类型")
    private String category;

    @ApiModelProperty("流程变量")
    private Object data;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("到期时间")
    private Date dueDate;
}

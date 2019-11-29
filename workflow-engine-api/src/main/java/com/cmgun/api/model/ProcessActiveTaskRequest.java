package com.cmgun.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 查询流程激活中任务
 *
 * @author chenqilin
 * @date 2019/8/13
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("查询流程激活中任务")
public class ProcessActiveTaskRequest {

    @NotNull(message = "流程定义key不能为空")
    @ApiModelProperty("流程定义key，process标签的id")
    private String processDefinitionKey;

    @NotNull(message = "流程的业务id不能为空")
    @ApiModelProperty("流程的业务id，业务系统保证唯一性")
    private String businessKey;

    @ApiModelProperty("流程类型，process标签的namespace")
    private String category;
}

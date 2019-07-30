package com.cmgun.api.model;

import com.cmgun.api.common.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 待办任务查询
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("待办任务查询参数")
public class ToDoTaskRequest extends PageQuery {

    private static final long serialVersionUID = -2251320373702545747L;

    @ApiModelProperty("任务候选组")
    private String candidateGroup;

    @ApiModelProperty("任务候选人")
    private String candidateUser;

    @ApiModelProperty("任务指派人")
    private String assignee;

    @NotBlank(message = "流程类型不能为空")
    @ApiModelProperty("流程类型，定义在流程图任务节点中的category属性")
    private String category;
}

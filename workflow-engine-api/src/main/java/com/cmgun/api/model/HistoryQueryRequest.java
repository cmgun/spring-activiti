package com.cmgun.api.model;

import com.cmgun.api.common.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 历史任务查询
 *
 * @author chenqilin
 * @date 2019/7/29
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("历史任务查询参数")
public class HistoryQueryRequest extends PageQuery {

    private static final long serialVersionUID = 2881703300036639827L;

    @ApiModelProperty(value = "任务候选组", notes = "该字段非空时，忽略其他用户查询条件")
    private String candidateGroup;

    @ApiModelProperty(value = "任务候选人")
    private String candidateUser;

    @ApiModelProperty(value = "任务指派人")
    private String assignee;

    @NotBlank(message = "流程类型不能为空")
    @ApiModelProperty("流程类型，定义在流程图任务节点中的category属性")
    private String category;

    @ApiModelProperty("流程的业务id，命名规则：业务模块标志:业务id")
    private String businessKey;

    @NotNull(message = "任务是否结束标识不能为空")
    @ApiModelProperty(value = "任务是否结束标识", notes = "true:已结束，false:未结束")
    private Boolean completeTask;
}

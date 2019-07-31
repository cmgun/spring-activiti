package com.cmgun.api.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 流程任务上下文
 *
 * @author chenqilin
 * @Date 2019/7/19
 */
@Data
@ApiModel("流程任务上下文")
public class TaskContext {

    /**
     * 下个任务节点的参与组，多个用逗号分隔
     */
    @NotBlank(message = "下个任务节点参与组不能为空")
    @ApiModelProperty("下个任务节点的参与组，多个使用逗号分隔")
    private String candidateGroups;

    /**
     * 流程上下文业务数据，贯穿整个流程，如果已有则直接覆盖
     */
    @ApiModelProperty("流程上下文")
    private Object globalPayload;

    /**
     * 下个任务节点的上下文，只存活于下一个任务节点内，不会传递下去
     */
    @ApiModelProperty("下个任务节点的上下文")
    private Object localPayload;
}

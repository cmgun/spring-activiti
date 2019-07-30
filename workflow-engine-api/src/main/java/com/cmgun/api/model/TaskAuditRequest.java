package com.cmgun.api.model;

import com.cmgun.api.common.Request;
import com.cmgun.api.common.TaskContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 任务审批请求
 *
 * @author chenqilin
 * @Date 2019/7/25
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("任务审批请求参数")
public class TaskAuditRequest extends Request {

    private static final long serialVersionUID = -1992424046597943733L;

    @NotNull(message = "任务id不能为空")
    @ApiModelProperty("任务id")
    private String taskId;

    @NotNull(message = "任务候选组不能为空")
    @ApiModelProperty("任务候选组")
    private String candidateGroup;

    @NotNull(message = "任务审批人id不能为空")
    @ApiModelProperty("任务审批人id")
    private String auditor;

    @ApiModelProperty("任务审批人名称")
    private String auditorName;

    @NotNull(message = "审批结果不能为空")
    @ApiModelProperty("审批结果")
    private Object auditResult;

    @ApiModelProperty("审批意见")
    private String comment;

    @NotNull(message = "流程上下文不能为空")
    @ApiModelProperty("流程上下文，包含必要的业务数据")
    private TaskContext taskContext;
}

package com.cmgun.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 历史任务信息
 *
 * @author chenqilin
 * @date 2019/7/29
 */
@Data
@Builder
@ApiModel("历史任务信息")
public class History implements Serializable {

    private static final long serialVersionUID = 8333177339412394436L;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务定义id")
    private String taskDefKey;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("流程命名空间，流程类别")
    private String procNameSpace;

    @ApiModelProperty("业务唯一id")
    private String businessKey;

    @ApiModelProperty("流程变量，业务数据")
    private Object data;

    @ApiModelProperty(value = "任务指派人/审批人")
    private String assignee;

    @ApiModelProperty(value = "审批用户id")
    private String auditor;

    @ApiModelProperty(value = "审批用户名称")
    private String auditorName;

    @ApiModelProperty("审批结果")
    private Object auditResult;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    @ApiModelProperty("审批备注")
    private List<Comment> comments;

    @ApiModelProperty("上一个任务审批人id")
    private String lastAuditor;

    @ApiModelProperty("上一个任务审批人名称")
    private String lastAuditorName;

    @ApiModelProperty("上一个任务审批结果")
    private Object lastAuditResult;

    @ApiModelProperty("上一个任务审批意见")
    private String lastComment;

    @ApiModelProperty("上一个任务审批时间")
    private Date lastAuditTime;
}



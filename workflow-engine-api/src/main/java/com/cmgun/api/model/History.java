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
 * @Date 2019/7/29
 */
@Data
@Builder
@ApiModel("历史任务信息")
public class History implements Serializable {

    private static final long serialVersionUID = 8333177339412394436L;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("业务唯一id")
    private String businessKey;

    @ApiModelProperty("流程变量，业务数据")
    private Object data;

    @ApiModelProperty(value = "任务指派人/审批人")
    private String assignee;

    @ApiModelProperty("审批结果")
    private Object auditResult;

    @ApiModelProperty("审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    @ApiModelProperty("审批备注")
    private List<Comment> comments;
}

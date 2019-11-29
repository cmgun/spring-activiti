package com.cmgun.api.model;

import com.cmgun.api.common.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

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
    private List<String> candidateGroup;

    @ApiModelProperty(value = "任务候选人")
    private String candidateUser;

    @ApiModelProperty(value = "任务指派人")
    private String assignee;

    @ApiModelProperty("流程类型，定义在流程图流程中的namespace属性")
    private String procCategory;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("任务定义id，定义在流程图任务节点中的id属性")
    private String taskDefId;

    @ApiModelProperty("任务类型，定义在流程图任务节点中的category属性")
    private String category;

    @ApiModelProperty("流程的业务id，命名规则：业务模块标志:业务id")
    private String businessKey;

    @ApiModelProperty(value = "任务是否结束标识", notes = "true:已结束，false:未结束")
    private Boolean completeTask;

    @ApiModelProperty("任务开始时间查询最小值")
    private Date taskCreateTimeBegin;

    @ApiModelProperty("任务开始时间查询最大值")
    private Date taskCreateTimeEnd;
}

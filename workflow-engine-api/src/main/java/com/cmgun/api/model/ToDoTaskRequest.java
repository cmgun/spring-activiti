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
 * 待办任务查询
 *
 * @author chenqilin
 * @date 2019/7/23
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel("待办任务查询参数")
public class ToDoTaskRequest extends PageQuery {

    private static final long serialVersionUID = -2251320373702545747L;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("任务候选组")
    private List<String> candidateGroup;

    @ApiModelProperty("任务候选人")
    private String candidateUser;

    @ApiModelProperty("任务指派人")
    private String assignee;

    @ApiModelProperty("任务类别，定义在流程图任务节点中的category属性")
    private String taskCategory;

    @ApiModelProperty("流程类别，定义在流程图流程中的namespace属性")
    private String procCategory;

    @ApiModelProperty("任务定义id，定义再流程图任务节点中的id属性")
    private String taskDefinitionKey;

    @ApiModelProperty("业务编码")
    private String businessKey;

    @ApiModelProperty("任务开始时间查询最小值")
    private Date taskCreateTimeBegin;

    @ApiModelProperty("任务开始时间查询最大值")
    private Date taskCreateTimeEnd;
}

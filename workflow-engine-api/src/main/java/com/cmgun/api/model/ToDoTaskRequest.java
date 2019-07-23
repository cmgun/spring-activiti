package com.cmgun.api.model;

import com.cmgun.api.common.Request;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 待办任务查询
 *
 * @author chenqilin
 * @Date 2019/7/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ToDoTaskRequest implements Serializable {

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

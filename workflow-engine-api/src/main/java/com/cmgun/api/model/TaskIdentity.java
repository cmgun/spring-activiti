package com.cmgun.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 任务分配权限
 *
 * @author chenqilin
 * @date 2019/8/26
 */
@Data
@Builder
@ApiModel("任务权限信息")
public class TaskIdentity {

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("流程实例id")
    private String processId;

    @ApiModelProperty("组id")
    private String groupId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("身份权限类型")
    private String type;
}

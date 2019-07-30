package com.cmgun.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 任务审批备注
 *
 * @author chenqilin
 * @Date 2019/7/30
 */
@Data
@Builder
@ApiModel("历史任务信息")
public class Comment implements Serializable {

    private static final long serialVersionUID = 8822320770692691283L;

    @ApiModelProperty("备注人id")
    private String userId;

    @ApiModelProperty("审批备注")
    private String comment;
}

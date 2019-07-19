package com.cmgun.api.model;

import com.cmgun.api.common.Request;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 流程发布请求
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessDeployRequest extends Request {

    private static final long serialVersionUID = -1873951875518899989L;

    @NotBlank(message = "流程名称不能为空")
    @ApiModelProperty("流程名称")
    private String processName;

    @NotBlank(message = "流程key不能为空")
    @ApiModelProperty("流程key，流程的唯一标识")
    private String key;
}

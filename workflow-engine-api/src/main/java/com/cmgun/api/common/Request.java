package com.cmgun.api.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 通用请求体
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Data
public class Request implements Serializable {

    @NotBlank(message = "请求流水号不能为空")
    @ApiModelProperty("请求流水号")
    protected String requestNo;

    @NotBlank(message = "请求来源")
    @ApiModelProperty("请求来源")
    protected String source;
}

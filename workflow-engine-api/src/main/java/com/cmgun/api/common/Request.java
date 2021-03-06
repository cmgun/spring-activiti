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
 * @date 2019/7/16
 */
@Data
public class Request implements Serializable {

    private static final long serialVersionUID = 2956649160187048898L;

    @NotBlank(message = "请求流水号不能为空")
    @ApiModelProperty("请求流水号")
    protected String requestNo;

    @NotBlank(message = "请求来源")
    @ApiModelProperty("请求来源")
    protected String source;
}

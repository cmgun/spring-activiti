package com.cmgun.api.model;

import com.cmgun.api.common.Request;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;

/**
 * 流程发布请求
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProgressDeployRequest extends Request {

    @ApiModelProperty("流程名称")
    private String progressName;

    @ApiModelProperty("流程key，流程的唯一标识")
    private String key;

    @ApiModelProperty("流程图文件流")
    private InputStream progressFileStream;
}

package com.cmgun.api.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 通用响应体
 *
 * @author chenqilin
 * @date 2019/7/16
 */
@Data
@Builder
@ApiModel("响应结果")
public class Response<T> {

    /**
     * 成功
     */
    public static final Long OK = 200L;

    /**
     * 请求参数不合法
     */
    public static final Long BAD_REQUEST = 400L;

    /**
     * 无权限
     */
    public static final Long UNAUTHORIZED = 401L;

    /**
     * 重复请求
     */
    public static final Long REPEAT_REQUEST = 402L;

    /**
     * 业务重复处理
     */
    public static final Long REPEAT_BUSINESS = 403L;

    /**
     * 系统异常
     */
    public static final Long ERROR = 500L;

    /**
     * 业务异常
     */
    public static final Long BUSINESS_ERROR = 900L;

    /**
     * 响应码
     */
    @ApiModelProperty(value = "响应码", dataType = "Long", required = true)
    private Long code;

    /**
     * 信息
     */
    @ApiModelProperty(value = "描述信息", dataType = "String", required = true)
    private String message;

    /**
     * 业务数据
     */
    @ApiModelProperty(value = "业务数据")
    private T payload;

    /**
     * 成功
     *
     * @param message 信息
     * @return 响应体
     */
    public static Response success(String message) {
        return Response.builder()
                .code(OK)
                .message(message)
                .build();
    }

    /**
     * 成功
     *
     * @param message 信息
     * @return 响应体
     */
    public static <T> Response<T> success(String message, T payload) {
        return Response.<T>builder()
                .code(OK)
                .message(message)
                .payload(payload)
                .build();
    }

    /**
     * 业务异常
     *
     * @param message 异常信息
     * @return 响应体
     */
    public static Response businessError(String message) {
        return Response.builder()
                .code(BUSINESS_ERROR)
                .message(message)
                .build();
    }
}

package com.cmgun.api.common;

import lombok.Builder;
import lombok.Data;

/**
 * 通用响应体
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Data
@Builder
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
    private Long code;

    /**
     * 信息
     */
    private String message;

    /**
     * 业务数据
     */
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

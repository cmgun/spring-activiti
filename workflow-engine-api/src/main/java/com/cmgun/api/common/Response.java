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
public class Response {

    public static final Long OK = 200L;

    public static final Long BAD_REQUEST = 400L;

    public static final Long UNAUTHORIZED = 401L;

    public static final Long ERROR = 500L;

    public static final Long BUSINESS_ERROR = 9999L;

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
    private Object payload;

    /**
     * 成功
     *
     * @param message 信息
     * @param payload 业务数据
     * @return 响应体
     */
    public static Response success(String message, Object payload) {
        return Response.builder()
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

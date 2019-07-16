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
}

package com.cmgun.entity.enums;

import lombok.Getter;

/**
 * 请求处理状态
 *
 * @author chenqilin
 * @date 2019/7/22
 */
@Getter
public enum RequestStatusEnum {

    /**
     * 初始化
     */
    INIT(0),
    /**
     * 处理成功
     */
    SUCCESS(1),
    /**
     * 处理失败
     */
    FAIL(2);

    /**
     * value
     */
    private final int value;

    RequestStatusEnum(int value) {
        this.value = value;
    }
}

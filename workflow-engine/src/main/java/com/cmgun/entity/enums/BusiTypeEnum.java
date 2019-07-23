package com.cmgun.entity.enums;

import lombok.Getter;

/**
 * 请求业务类型
 *
 * @author chenqilin
 * @Date 2019/7/22
 */
@Getter
public enum BusiTypeEnum {

    /**
     * 默认，无分类
     */
    DEFAULT(0),
    /**
     * 流程部署
     */
    PROC_DEPLOY(10),
    /**
     * 流程开启
     */
    PROC_START(20);

    /**
     * value
     */
    private final int value;

    BusiTypeEnum(int value) {
        this.value = value;
    }
}

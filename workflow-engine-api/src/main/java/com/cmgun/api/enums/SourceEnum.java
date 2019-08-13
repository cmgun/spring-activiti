package com.cmgun.api.enums;

/**
 * 请求来源枚举类
 *
 * @author chenqilin
 * @date 2019/8/1
 */
public enum SourceEnum {

    /**
     * 发票融资系统
     */
    INVOICE_LOAN("01", "发票融资");

    /**
     * 系统编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    SourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

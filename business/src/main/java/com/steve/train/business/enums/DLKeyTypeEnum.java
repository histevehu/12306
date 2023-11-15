package com.steve.train.business.enums;

/**
 * 分布式锁类型枚举
 */
public enum DLKeyTypeEnum {

    CONFIRM_ORDER("DL_CONFIRM_ORDER", "购票锁"),
    SK_TOKEN("DL_SK_TOKEN", "令牌锁"),
    SK_TOKEN_COUNT("SK_TOKEN_COUNT", "令牌数");

    private final String code;

    private final String desc;

    DLKeyTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

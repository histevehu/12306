package com.steve.train.business.enums;

/**
 * Redis Key类型枚举
 */
public enum RedisKeyTypeEnum {

    // 分布式锁类型枚举
    DL_CONFIRM_ORDER("DL_CONFIRM_ORDER", "购票锁"),
    DL_SK_TOKEN("DL_SK_TOKEN", "令牌锁"),
    SK_TOKEN_COUNT("SK_TOKEN_COUNT", "令牌数"),
    KAPTCHA("KAPTCHA", "图形验证码");

    private final String code;

    private final String desc;

    RedisKeyTypeEnum(String code, String desc) {
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

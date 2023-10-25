package com.steve.train.common.exception;

public enum BusinessExceptionEnum {
    MEMBER_MOBILE_EXIST("手机号已经注册"),
    MEMBER_MOBILE_NOT_EXIST("手机号未注册，请先获取手机短信验证码"),
    MEMBER_MOBILE_CODE_ERROR("短信验证码错误");
    private final String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "BusinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                '}';
    }


}

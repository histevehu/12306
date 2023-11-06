package com.steve.train.business.enums;

public enum ConfirmOrderStatusEnum {
    // 订单刚进系统
    INIT("I", "初始"),
    // 订单选票中
    PENDING("P", "处理中"),
    SUCCESS("S", "成功"),
    FAILURE("F", "失败"),
    // 库存不足
    EMPTY("E", "无票"),
    // 排队选票中取消
    CANCEL("C", "取消");

    private String code;

    private String desc;

    ConfirmOrderStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ConfirmOrderStatusEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}

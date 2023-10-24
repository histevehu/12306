package com.steve.train.member.req;

import jakarta.validation.constraints.NotBlank;

/**
 * Member模块请求封装类。
 * 注意里面的变量名要和controller接收到的参数名对应。
 * 请求参数的mobile和Req类里的属性名字保持一致，springboot就会自动将请求参数映射到req类里
 */
public class MemberRegisterReq {
    // req类的参数都是由springboot框架自动赋值的，不需要写带参数的构造函数。

    // 使用Spring Validation进行参数校验
    // 注意：需要在调用该封装类作为Controller传入参数的参数前加@Valid才可生效
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberRegisterReq{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}

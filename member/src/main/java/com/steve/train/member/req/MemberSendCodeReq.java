package com.steve.train.member.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/25 14:55
 * @description: 用户登录手机验证码发送请求封装类
 */
public class MemberSendCodeReq {
    @NotBlank(message = "手机号不能为空")
    // 使用正则表达式对手机号格式进行参数校验
    @Pattern(regexp = "^1\\d{10}$", message = "手机号码格式错误")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberSendCodeReq{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
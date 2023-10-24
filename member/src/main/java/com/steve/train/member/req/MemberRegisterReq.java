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
    // 补充：在数据校验方面，常用的注解包括 @NotEmpty、@NotBlank 和 @NotNull：
    //      @NotEmpty 用于验证一个字符串、集合或数组是否为空。它检查目标对象是否为 null，并且长度是否为零。即：被 @NotEmpty 注解标注的字段必须至少包含一个非空字符、元素或项。
    //      @NotBlank 会去除字符串两端的空格，并验证处理后的字符串是否为空。因此，@NotBlank 只适用于字符串类型的字段。
    //      @NotNull 更加简单，它只检查目标字段是否为 null。这意味着它适用于所有类型的字段，包括字符串、对象、基本数据类型等
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

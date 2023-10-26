package com.steve.train.member.config;

import com.steve.train.common.interceptor.LogInterceptor;
import com.steve.train.common.interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MemberInterceptorConfig implements WebMvcConfigurer {

    @Resource
    MemberInterceptor memberInterceptor;
    @Resource
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有接口都启用
        registry.addInterceptor(logInterceptor);

        // 路径不要包含context-path(applications.properties中配置的模块名前缀)，仅Controller接口部分即可
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/member/sendCode",
                        "/member/login"
                );
    }
}

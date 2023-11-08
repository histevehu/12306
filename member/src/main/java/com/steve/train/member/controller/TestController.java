package com.steve.train.member.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Nacos配置动态刷新（SpringCloud注释，与Nacos组件解耦）
@RefreshScope
public class TestController {

    @Value("${test.nacos}")
    private String testNacos;

    @GetMapping("/test-nacos")
    public String test_nacos() {
        return String.format("Hello %s!", testNacos);
    }
}

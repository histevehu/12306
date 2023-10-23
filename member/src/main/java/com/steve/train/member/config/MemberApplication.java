package com.steve.train.member.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//Springboot默认扫描Application所在包及其子包内的组件。
//若bean在其他目录，需要手动加上@ComponentScan注解并指定那个bean所在的包
@ComponentScan("com.steve")
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}

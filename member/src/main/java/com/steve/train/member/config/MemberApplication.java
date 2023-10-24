package com.steve.train.member.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
//Springboot默认扫描Application所在包及其子包内的组件。
//若bean在其他目录，需要手动加上@ComponentScan注解并指定那个bean所在的包
@ComponentScan("com.steve")
//持久层的Java代码放在mapper中，xml配置文件放在resources的mapper中
//通过配置MapperScan让Spring扫描MyBatis的代码
@MapperScan("com.steve.train.member.mapper")
public class MemberApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MemberApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MemberApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("Member Application，启动！");
        LOG.info("测试地址：http://localhost:{}{}/hello", env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
    }

}

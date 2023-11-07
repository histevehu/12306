package com.steve.train.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
// Springboot默认扫描Application所在包及其子包内的组件。
// 若bean在其他目录，需要手动加上@ComponentScan注解并指定那个bean所在的包
@ComponentScan("com.steve")
// 持久层的Java代码放在mapper中，xml配置文件放在resources的mapper中
// 通过配置MapperScan让Spring扫描MyBatis的代码
// @MapperScan("com.steve.train.batch.mapper")
// 启用Feign客户端，并指定扫描的包名
@EnableFeignClients(basePackages = "com.steve.train.batch.feign")
public class BatchApplication {

    private static final Logger LOG = LoggerFactory.getLogger(BatchApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("Batch Application，启动！");
        LOG.info("Root URL: http://localhost:{}{}", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }

}

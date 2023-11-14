package com.steve.train.business.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
// Springboot默认扫描Application所在包及其子包内的组件。
// 若bean在其他目录，需要手动加上@ComponentScan注解并指定那个bean所在的包
@ComponentScan("com.steve")
// 持久层的Java代码放在mapper中，xml配置文件放在resources的mapper中
// 通过配置MapperScan让Spring扫描MyBatis的代码
@MapperScan("com.steve.train.business.mapper")
@EnableFeignClients(basePackages = "com.steve.train.business.feign")
// Spring内置缓存，需要配合具体的服务上加@cacheable一起使用
// Spring内置缓存可以解决MyBatis二级缓存需要修改mapper的问题，但同样存在多个节点缓存不一致的问题
@EnableCaching
public class BusinessApplication {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("Business Application，启动！");
        LOG.info("Root URL: http://localhost:{}{}", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
        // Business模块的Sentinel限流规则已经通过Nacos持久化
        // initFlowRules();
        // LOG.info("Sentinel限流规则已定义");
    }

    /**
     * 初始化限流规则。
     * 也可通过Sentinel控制台动态覆盖配置，但是此方法是通过推送到应用生效的，规则保存在应用的内存中。若应用重启则规则丢失了。
     */
    /*private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("doConfirm");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(20);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }*/

}

package com.steve.train.batch.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Quartz数据库模式动态管理定时调度任务触发器，需要配合{@link JobFactory}类使用
 */
@Configuration
public class SchedulerConfig {

    @Resource
    private JobFactory jobFactory;

    /**
     * @param dataSource Springboot的application.properties内配置的数据源
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        // 启动后延迟多少秒启动Quartz开始执行任务
        factory.setStartupDelay(2);
        return factory;
    }
}

package com.steve.train.batch.config;

import com.steve.train.batch.job.QuartzTestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;

// Quartz默认开启并发定时调度执行，若需要禁用，使用@DisallowConcurrentExecution注解。是否启用并发执行在跑批任务设计中需要考虑
// @DisallowConcurrentExecution
// @Configuration
/**
 * 声明式声明定时调度任务触发器
 * 注意：实际应用中，我们不会像下面这样在代码中显式声明定时调度任务，而是使用数据库模式，在控制台页面动态管理
 */
public class QuartzConfig {

    /**
     * 声明一个任务
     *
     * @return
     */
    // @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(QuartzTestJob.class)
                .withIdentity("QuartzTestJob", "Test")
                .storeDurably()
                .build();
    }

    /**
     * 声明一个触发器，什么时候触发这个任务
     *
     * @return
     */
    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("QuartzTestJobTrigger", "trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
    }
}

package com.steve.train.batch.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/30 19:14
 * @description: Springboot 自带定时任务 测试类
 *               Springboot自带的定时任务适合单体应用，不适合集群。且没法实时更改定时任务状态和策略
 */

// @Component
@EnableScheduling
public class SpringbootTestJob {
    static int count = 0;

    // cron表达式：从左到右（用空格隔开），秒 分 小时 月份中的日期 月份 星期中的日期 年份
    // 0/5：秒数%5=0，即每五秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    private void test() {
        System.out.println("Springboot 定时任务测试: " + (count++));
    }
}

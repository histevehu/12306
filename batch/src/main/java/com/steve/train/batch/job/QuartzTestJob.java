package com.steve.train.batch.job;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/30 19:14
 * @description: Quartz定时调度模块测试，需要配合QuartzConfig类使用
 */

import cn.hutool.core.date.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzTestJob implements Job {
    static int count = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Quartz定时调度模块测试[" + (count++) + "]: " + DateTime.now());
    }
}

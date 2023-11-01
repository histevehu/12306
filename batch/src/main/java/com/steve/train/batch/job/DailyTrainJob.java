package com.steve.train.batch.job;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/30 19:14
 * @description: Quartz每日车次生成定时调度任务
 */

import cn.hutool.core.util.RandomUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DailyTrainJob implements Job {
    private final static Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 增加日志流水号
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("生成15天后的车次数据开始");
    }
}

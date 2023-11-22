package com.steve.train.batch.job;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/30 19:14
 * @description: Quartz NPC乘客定时生成调度任务
 */

import cn.hutool.core.util.RandomUtil;
import com.steve.train.batch.feign.MemberFeign;
import jakarta.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@DisallowConcurrentExecution
public class PassengerJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(PassengerJob.class);

    @Resource
    MemberFeign memberFeign;

    /**
     * 初始化NPC乘客，防止线上体验时乘客被删光
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 增加日志流水号
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("初始化乘客数据开始");
        memberFeign.init();
        LOG.info("初始化乘客数据结束");
    }
}

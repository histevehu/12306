package com.steve.train.common.util;

import cn.hutool.core.util.IdUtil;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/24 22:24
 * @description: 雪花算法工具类
 */
public class SnowFlakeUtil {
    private static long workerId = 1;
    private static long datercenterId = 1;

    public static long getSnowFlakeNextId() {
        return IdUtil.getSnowflake(workerId, datercenterId).nextId();
    }

    public static String getSnowFlakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, datercenterId).nextIdStr();
    }
}

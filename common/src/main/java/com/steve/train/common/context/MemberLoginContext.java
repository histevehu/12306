package com.steve.train.common.context;

import com.steve.train.common.resp.MemberLoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/26 15:23
 * @description: 线程本地变量，用于存储用户登录信息
 */
public class MemberLoginContext {
    private static final Logger LOG = LoggerFactory.getLogger(MemberLoginContext.class);

    private static ThreadLocal<MemberLoginResp> member = new ThreadLocal<>();

    public static MemberLoginResp getMember() {
        return member.get();
    }

    public static void setMember(MemberLoginResp member) {
        MemberLoginContext.member.set(member);
    }

    public static Long getId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            LOG.error("获取用户登录信息异常", e);
            throw e;
        }
    }
}

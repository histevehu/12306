package com.steve.train.member.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.steve.train.common.exception.BusinessException;
import com.steve.train.common.exception.BusinessExceptionEnum;
import com.steve.train.common.util.SnowFlakeUtil;
import com.steve.train.member.domain.Member;
import com.steve.train.member.domain.MemberExample;
import com.steve.train.member.mapper.MemberMapper;
import com.steve.train.member.req.MemberRegisterReq;
import com.steve.train.member.req.MemberSendCodeReq;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Resource
    MemberMapper memberMapper;

    private final static Logger LOG = LoggerFactory.getLogger(MemberService.class);

    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq req) {
        // 查询手机号是否已经被注册
        // memberexample类即查询条件
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        // 也可使用hutool的CollUtil.isNotEmpty(list)判空
        if (!(null == list || list.isEmpty())) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member = new Member();
        // 使用雪花算法生成ID
        // 补充：其他生成ID的方法：
        //      1.自增ID:不适合分布式、分库分表场景。仅适合小型项目
        //      2.UUID：值不连续，对于索引的构建有性能问题
        // 问题1：关于workerId和datacenterId的动态分配
        //      可以数据库加一张表，保存服务器IP+索引号（类似自增ID），索引号从1开始，唯一键，服务器刚启动时就去库里搜一下当前IP有没有数据，有的话，就取得库里的索引，没有的话，就创建一条记录，索引号就是最大值加一。只要有记录了，以后这台服务器的索引号就固定下来了。
        // 问题2：时钟回拨，会将旧ID再次生成
        //      暂时关闭服务器，直到时间重合
        member.setId(SnowFlakeUtil.getSnowFlakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    public String sendCode(MemberSendCodeReq req) {
        // 查询手机号是否已经被注册
        // memberexample类即查询条件
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        // 若手机号未被注册，自动注册
        if (CollUtil.isEmpty(list)) {
            LOG.info("手机验证码：{}，请求，该手机号不存在，自动注册",mobile);
            Member member = new Member();
            member.setId(SnowFlakeUtil.getSnowFlakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }
        else{
            LOG.info("手机验证码：{}，请求",mobile);
        }
        // 生成短信验证码
        String code = RandomUtil.randomString(6);
        LOG.info("手机验证码：{}，生成验证码：{}",mobile,code);
        // 保存短信验证码记录（略）。数据表结构：手机号、验证码、有效期、是否已使用、业务类型、发送时间、使用时间
        LOG.info("手机验证码：{}，保存到短信验证码记录表",mobile);
        // 对接短信发送平台（略）
        LOG.info("手机验证码：{}，短信发送平台发送成功",mobile);
        return code;
    }
}

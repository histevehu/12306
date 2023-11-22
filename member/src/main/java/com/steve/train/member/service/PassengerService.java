package com.steve.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steve.train.common.context.MemberLoginContext;
import com.steve.train.common.resp.PageResp;
import com.steve.train.common.util.SnowFlakeUtil;
import com.steve.train.member.domain.Member;
import com.steve.train.member.domain.MemberExample;
import com.steve.train.member.domain.Passenger;
import com.steve.train.member.domain.PassengerExample;
import com.steve.train.member.enums.PassengerTypeEnum;
import com.steve.train.member.mapper.MemberMapper;
import com.steve.train.member.mapper.PassengerMapper;
import com.steve.train.member.req.PassengerQueryReq;
import com.steve.train.member.req.PassengerSaveReq;
import com.steve.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/26 14:39
 * @description: 乘客服务
 */
@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;
    @Resource
    private MemberMapper memberMapper;

    private final static Logger LOG = LoggerFactory.getLogger(PassengerService.class);

    /**
     * 乘客信息保存服务（新增、修改）
     *
     * @param req
     */
    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        // 若请求的id为空，则该请求为新增请求
        if (ObjectUtil.isNull(passenger.getId())) {
            passenger.setId(SnowFlakeUtil.getSnowFlakeNextId());
            // 读取线程本地变量中当前用户的ID
            passenger.setMemberId(MemberLoginContext.getId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        }
        // 否则是修改请求
        else {
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }
    }

    /**
     * 乘客信息查询
     *
     * @param req
     * @return PassengerQueryResp乘客查询请求结果封装类。
     * 注意：开发规范是Controller不使用持久层实体类，应该返回结果封装类（方便对结果进行处理等）
     */
    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req) {
        PassengerExample passengerExample = new PassengerExample();
        // 查询结果按照id升序排列
        passengerExample.setOrderByClause("id asc");
        // 复用criteria对象。当存在多个判断添加条件时可以复用。
        // 注意：若每次都通过passengerExample.createCriteria().XXX添加条件，只能以最后一个为准
        PassengerExample.Criteria passengerCriteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            passengerCriteria.andMemberIdEqualTo(req.getMemberId());
        }
        // PageHelper.startPage参数：pageNum指定查询第几页数据（从1开始），pageSize指定每页数据数量。分页数量由PageHelper自动计算
        // 对这句往下遇到的第一个SQL做拦截，增加分页 limit
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        // 将List中的持久层实体类转为业务结果封装类
        List<PassengerQueryResp> list = BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
        // 将业务结果封装类的列表放入分页查询结果返回封装类中
        PageResp<PassengerQueryResp> objectPageResp = new PageResp<>();
        objectPageResp.setTotal(pageInfo.getTotal());
        objectPageResp.setList(list);
        return objectPageResp;
    }

    public void delete(long id) {
        passengerMapper.deleteByPrimaryKey(id);
    }

    public List<PassengerQueryResp> queryMine() {
        PassengerExample passengerExample = new PassengerExample();
        passengerExample.setOrderByClause("name asc");
        PassengerExample.Criteria criteria = passengerExample.createCriteria();
        criteria.andMemberIdEqualTo(MemberLoginContext.getId());
        List<Passenger> list = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(list, PassengerQueryResp.class);
    }


    /**
     * 初始化若干NPC乘客，防止上线体验时乘客被删光
     */
    public void init() {
        DateTime now = DateTime.now();
        MemberExample memberExample = new MemberExample();
        // 18000000000用户是默认用户，用于开发测试跑批
        memberExample.createCriteria().andMobileEqualTo("18000000000");
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        Member member = memberList.get(0);
        List<Passenger> passengerList = new ArrayList<>();
        List<String> nameList = Arrays.asList("NPC_1", "NPC_2", "NPC_3");
        for (String s : nameList) {
            Passenger passenger = new Passenger();
            passenger.setId(SnowFlakeUtil.getSnowFlakeNextId());
            passenger.setMemberId(member.getId());
            passenger.setName(s);
            passenger.setIdCard("123456789123456789");
            passenger.setType(PassengerTypeEnum.ADULT.getCode());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerList.add(passenger);
        }
        // 若NPC用户不存在，则新增
        for (Passenger passenger : passengerList) {
            PassengerExample passengerExample = new PassengerExample();
            passengerExample.createCriteria().andNameEqualTo(passenger.getName());
            long l = passengerMapper.countByExample(passengerExample);
            if (l == 0) {
                passengerMapper.insert(passenger);
            }
        }
    }
}

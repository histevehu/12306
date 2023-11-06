package com.steve.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steve.train.business.domain.confirmOrder;
import com.steve.train.business.domain.confirmOrderExample;
import com.steve.train.business.mapper.confirmOrderMapper;
import com.steve.train.business.req.confirmOrderQueryReq;
import com.steve.train.business.req.confirmOrderSaveReq;
import com.steve.train.business.resp.confirmOrderQueryResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.common.util.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-06 14:21:19
 * @description: 确认订单服务（FreeMarker生成）
 */
@Service
public class confirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(confirmOrderService.class);

    @Resource
    private confirmOrderMapper confirmOrderMapper;

    public void save(confirmOrderSaveReq req) {
        DateTime now = DateTime.now();
        confirmOrder confirmOrder = BeanUtil.copyProperties(req, confirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowFlakeUtil.getSnowFlakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<confirmOrderQueryResp> queryList(confirmOrderQueryReq req) {
        confirmOrderExample confirmOrderExample = new confirmOrderExample();
        confirmOrderExample.setOrderByClause("id asc");
        confirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<confirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<confirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<confirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, confirmOrderQueryResp.class);

        PageResp<confirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }
}

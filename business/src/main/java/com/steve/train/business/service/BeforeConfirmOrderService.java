package com.steve.train.business.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.steve.train.business.domain.ConfirmOrder;
import com.steve.train.business.dto.ConfirmOrderMQDTO;
import com.steve.train.business.enums.ConfirmOrderStatusEnum;
import com.steve.train.business.enums.RocketMQTopicEnum;
import com.steve.train.business.mapper.ConfirmOrderMapper;
import com.steve.train.business.req.ConfirmOrderDoReq;
import com.steve.train.business.req.ConfirmOrderTicketReq;
import com.steve.train.common.context.MemberLoginContext;
import com.steve.train.common.exception.BusinessException;
import com.steve.train.common.exception.BusinessExceptionEnum;
import com.steve.train.common.util.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BeforeConfirmOrderService {

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private SkTokenService skTokenService;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private final static Logger LOG = LoggerFactory.getLogger(BeforeConfirmOrderService.class);

    /**
     * 业务前逻辑。先进行令牌校验，若通过则将订单发送MQ以完成购票的后半程业务流程完成购票
     */
    @SentinelResource(value = "beforeDoConfirm", blockHandler = "beforeDoConfirmBlock")
    public void beforeDoConfirm(ConfirmOrderDoReq req) throws InterruptedException {
        // 根据线程上下文获取用户ID插入到请求中
        req.setMemberId(MemberLoginContext.getId());
        // 校验并尝试获取令牌
        String dlSkTokenKey = skTokenService.validSkToken(req.getDate(), req.getTrainCode(), req.getMemberId());
        if (StrUtil.isNotEmpty(dlSkTokenKey)) {
            LOG.info("令牌校验通过");
        } else {
            LOG.info("令牌校验不通过");
            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_SK_TOKEN_FAIL);
        }
        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        DateTime now = DateTime.now();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        // 初始状态订单保存到确认订单表
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowFlakeUtil.getSnowFlakeNextId());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setMemberId(req.getMemberId());
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setTickets(JSON.toJSONString(tickets));
        confirmOrderMapper.insert(confirmOrder);
        // 发送MQ排队购票
        // MQ内消息代表指定日期车次的订单来了，等待处理。待分配到消费者后，对该消息指定的日期车次的订单循环逐张出票，直到所有订单处理完毕
        ConfirmOrderMQDTO confirmOrderMQDTO = new ConfirmOrderMQDTO();
        confirmOrderMQDTO.setDate(req.getDate());
        confirmOrderMQDTO.setTrainCode(req.getTrainCode());
        // 由于使用MQ异步处理启用了新的线程，所以原服务中的事件流水号不会自动传递过来，需要通过req对象传递
        // 同理，若使用Spring自带的@Async，也需要手动传递事件流水号
        confirmOrderMQDTO.setLogId(MDC.get("LOG_ID"));
        String reqJson = JSON.toJSONString(confirmOrderMQDTO);
        LOG.info("购票请求，发送mq开始，消息：{}", reqJson);
        rocketMQTemplate.convertAndSend(RocketMQTopicEnum.CONFIRM_ORDER.getCode(), reqJson);
        LOG.info("购票请求，发送mq结束");
    }

    public void beforeDoConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        LOG.info("购票请求被限流：{}", req);
        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION);
    }
}
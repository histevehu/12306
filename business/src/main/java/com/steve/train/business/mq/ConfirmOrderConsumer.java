package com.steve.train.business.mq;

import com.alibaba.fastjson.JSON;
import com.steve.train.business.dto.ConfirmOrderMQDTO;
import com.steve.train.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(consumerGroup = "default", topic = "CONFIRM_ORDER")
public class ConfirmOrderConsumer implements RocketMQListener<MessageExt> {

    private final static Logger LOG = LoggerFactory.getLogger(ConfirmOrderConsumer.class);

    @Resource
    private ConfirmOrderService confirmOrderService;

    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        ConfirmOrderMQDTO dto = JSON.parseObject(new String(body), ConfirmOrderMQDTO.class);
        MDC.put("LOG_ID", dto.getLogId());
        LOG.info("ROCKETMQ收到消息：{}", new String(body));
        confirmOrderService.doConfirm(dto);
    }
}
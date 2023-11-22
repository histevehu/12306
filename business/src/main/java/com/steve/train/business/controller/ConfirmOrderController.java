package com.steve.train.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.steve.train.business.enums.RedisKeyTypeEnum;
import com.steve.train.business.req.ConfirmOrderDoReq;
import com.steve.train.business.service.BeforeConfirmOrderService;
import com.steve.train.business.service.ConfirmOrderService;
import com.steve.train.common.exception.BusinessExceptionEnum;
import com.steve.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-06 14:21:19
 * @description: 确认订单管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/confirmOrder")
public class ConfirmOrderController {

    private final static Logger LOG = LoggerFactory.getLogger(ConfirmOrderController.class);
    @Resource
    private BeforeConfirmOrderService beforeOrderService;
    @Resource
    private ConfirmOrderService confirmOrderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${spring.profiles.active}")
    private String env;

    @SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) throws InterruptedException {
        // 图形验证码校验
        // 若为开发环境则跳过图形验证码校验
        if (!env.equals("dev")) {
            String imageCodeToken = req.getImageCodeToken();
            String imageCode = req.getImageCode();
            String imageCodeRedis = redisTemplate.opsForValue().get(RedisKeyTypeEnum.KAPTCHA.getCode() + "-" + imageCodeToken);
            LOG.info("从redis中获取到的验证码：{}", imageCodeRedis);
            if (ObjectUtils.isEmpty(imageCodeRedis)) {
                return new CommonResp<>(false, "验证码已过期", null);
            }
            // 验证码校验，大小写忽略，提升体验，比如Oo Vv Ww容易混
            if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
                return new CommonResp<>(false, "验证码不正确", null);
            } else {
                // 验证通过后，移除验证码缓存
                redisTemplate.delete(imageCodeToken);
            }
        }
        // 后端验证码校验通过，执行业务前逻辑，若成功则返回订单id
        long id = beforeOrderService.beforeDoConfirm(req);
        // long类型需要手动转为字符串类型，否则出现精度丢失
        return new CommonResp<>(String.valueOf(id));
    }

    @GetMapping("/queryLineCount/{id}")
    public CommonResp<Integer> queryLineCount(@PathVariable Long id) {
        Integer count = confirmOrderService.queryLineCount(id);
        return new CommonResp<>(count);
    }

    /**
     * 降级方法，需包含限流方法的所有参数+BlockException参数，且返回值要保持一致
     */
    public CommonResp<Object> doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        LOG.info("ConfirmOrderController购票请求被限流：{}", req);
        // throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION);
        CommonResp<Object> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION.getDesc());
        return commonResp;
    }

    @GetMapping("/cancel/{id}")
    public CommonResp<Integer> cancel(@PathVariable Long id) {
        Integer count = confirmOrderService.cancel(id);
        return new CommonResp<>(count);
    }

}
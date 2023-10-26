package com.steve.train.common.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/25 22:55
 * @description: JWT token生成工具类
 */
public class JWTUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * 盐值很重要，不能泄漏，且每个项目都应该不一样，可以放到配置文件中
     */
    private static final String key = "SteveHu_12306Train";

    public static String createToken(Long id, String mobile) {
        LOG.info("开始生成JWT，id：{}，mobile：{}", id, mobile);
        // BouncyCastle类是一个加密的第三方类，关闭它使用jdk自带的加密算法
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        DateTime now = DateTime.now();
        // 设置JWT token过期时间
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);
        Map<String, Object> payload = new HashMap<>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 内容
        payload.put("id", id);
        payload.put("mobile", mobile);
        String token = cn.hutool.jwt.JWTUtil.createToken(payload, key.getBytes());
        LOG.info("生成JWT：{}", token);
        return token;
    }

    public static boolean validate(String token) {
        LOG.info("开始JWT校验，token：{}", token);
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        JWT jwt = cn.hutool.jwt.JWTUtil.parseToken(token).setKey(key.getBytes());
        // validate包含了verify
        boolean validate = jwt.validate(0);
        LOG.info("JWT校验结果：{}", validate);
        return validate;
    }

    /**
     * token由id,mobile等其他信息经加密后生成。本函数将token解密并剥离其他数据，返回id,mobile
     *
     * @param token
     * @return Json格式的id, mobile字段
     */
    public static JSONObject getJSONObject(String token) {
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        JWT jwt = cn.hutool.jwt.JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        LOG.info("根据token获取原始内容：{}", payloads);
        return payloads;
    }
}

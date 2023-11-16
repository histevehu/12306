package com.steve.train.business.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.steve.train.business.enums.RedisKeyTypeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {
    @Qualifier("getDefaultKaptcha")
    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Resource
    public StringRedisTemplate stringRedisTemplate;

    @GetMapping("/image-code/{imageCodeToken}")
    public void imageCode(@PathVariable(value = "imageCodeToken") String imageCodeToken, HttpServletResponse httpServletResponse) throws Exception {
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生成验证码字符串
            String createText = defaultKaptcha.createText();
            String redisKaptchaKey = RedisKeyTypeEnum.KAPTCHA.getCode() + "-" + imageCodeToken;
            // 将生成的验证码以<token,验证码>形式存入redis缓存中，并设置过期时间
            stringRedisTemplate.opsForValue().set(redisKaptchaKey, createText, 300, TimeUnit.SECONDS);
            // 使用验证码字符串生成验证码图片
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}

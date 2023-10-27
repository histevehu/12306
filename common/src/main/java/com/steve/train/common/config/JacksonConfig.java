package com.steve.train.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 全局注解，将后端返回的所有long类型数字转为字符串以解决前后端交互Long类型精度丢失的问题
 * 注意：可能会造成前端要求接收数字类型控件的警告或报错
 *      一种更好的做法是在对请求返回结果的封装类中对精度丢失的属性单独配置
 */
// @Configuration
public class JacksonConfig {
    // @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}

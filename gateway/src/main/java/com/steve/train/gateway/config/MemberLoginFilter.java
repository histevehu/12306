package com.steve.train.gateway.config;

import com.steve.train.gateway.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/26 9:35
 * @description: 用户登录过滤器。在gateway统一检查JWT的有效性并过滤。需要添加@Component注入并实现GlobalFilter接口
 */
@Component
public class MemberLoginFilter implements GlobalFilter, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(MemberLoginFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 排除不需要拦截的请求
        // /admin开头的所有请求都不需要验证登录
        if (path.contains("/admin")
                || path.contains("/batch/hello")
                || path.contains("/business/hello")
                || path.contains("/member/hello")
                || path.contains("/redis")
                || path.contains("/member/member/login")
                || path.contains("/member/member/sendCode")
                || path.contains("/business/kaptcha")) {
            LOG.info("请求：{}，不需要登录验证", path);
            return chain.filter(exchange);
        } else {
            LOG.info("请求：{}，需要登录验证", path);
        }
        // 获取header的token参数
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("用户登录验证开始，token：{}", token);
        if (token == null || token.isEmpty()) {
            LOG.info("token为空，请求被拦截");
            // 验证错误，设置返回HTTP ERROR 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // 拦截链结束，返回错误结果
            return exchange.getResponse().setComplete();
        }

        // 校验token是否有效，包括token是否被改过，是否过期
        boolean validate = JWTUtil.validate(token);
        if (validate) {
            LOG.info("token有效，请求放行");
            // 拦截链继续
            return chain.filter(exchange);
        } else {
            LOG.warn("token无效，请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }

    /**
     * 当存在多个过滤器时实现Ordered接口来设置优先级。越小，优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

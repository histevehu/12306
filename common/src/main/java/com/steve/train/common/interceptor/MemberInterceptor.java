package com.steve.train.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.steve.train.common.context.MemberLoginContext;
import com.steve.train.common.resp.MemberLoginResp;
import com.steve.train.common.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/26 15:42
 * @description: 用户拦截器，用于从用户请求的header中提取JWT，转为登录信息并保存在线程本地变量中
 *               本类只声明了这一公共拦截器，若需要启用，需要在具体的模块中配置注入
 *               知识：Spring中请求依次到达：Web容器->Filter过滤器->Interceptor拦截器->Controller->Service
 *               流程：用户请求首先到达gateway，经过MemberLoginFilter过滤器检查JWT有效后，再转发给相应模块，相应模块若启用本拦截器，则进一步从JWT中提取登录信息。
 *                    在接口入口获取会员信息，并放到线程本地变量，则在controller、service都可直接从线程本地变量获取会员信息
 *
 */
@Component
public class MemberInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MemberInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("### MemberInterceptor开始 ###");
        // 获取header的token参数
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            LOG.info("获取会员登录token：{}", token);
            JSONObject loginMember = JWTUtil.getJSONObject(token);
            LOG.info("当前登录会员：{}", loginMember);
            // 根据token还原后的字段生成登录信息类
            MemberLoginResp member = JSONUtil.toBean(loginMember, MemberLoginResp.class);
            // 只要当前线程完成上述过程，即将登录信息保存在该线程的本地变量中
            MemberLoginContext.setMember(member);
        }
        LOG.info("### MemberInterceptor结束 ###");
        return true;
    }
}

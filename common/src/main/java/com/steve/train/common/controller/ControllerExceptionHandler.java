package com.steve.train.common.controller;

import com.steve.train.common.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 * @ControllerAdvice 一般和三个以下注解一块使用，实现对业务异常进行统一处理
 *
 * @ExceptionHandler: 该注解作用于方法上，，可以捕获到controller中抛出的一些自定义异常，统一进行处理，一般用于进行一些特定的异常处理。
 * @InitBinder: 该注解作用于方法上,用于将前端请求的特定类型的参数在到达controller之前进行处理，从而达到转换请求参数格式的目的。
 * @ModelAttribute： 该注解作用于方法和请求参数上，在方法上时设置一个值，可以直接在进入controller后传入该参数。
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 所有异常统一处理
     *
     * @param e
     * @return
     */
    // @ExceptionHandler参数是某个异常类的class，代表这个方法专门处理该类异常，比如这样：
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp exceptionHandler(Exception e) throws Exception {
        // // 如果是在一次全局事务里出异常了，就不要包装返回值，将异常抛给调用方，让调用方回滚事务
        // if (StrUtil.isNotBlank(RootContext.getXID())) {
        //     throw e;
        // }
        CommonResp commonResp = new CommonResp();
        LOG.error("系统异常：", e);
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getMessage());
        return commonResp;
    }

}

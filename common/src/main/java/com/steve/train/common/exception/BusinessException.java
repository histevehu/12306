package com.steve.train.common.exception;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/24 16:58
 * @description: 业务自定义异常类
 */
public class BusinessException extends RuntimeException {
    private BusinessExceptionEnum e;

    public BusinessException(BusinessExceptionEnum e) {
        this.e = e;
    }

    public BusinessExceptionEnum getE() {
        return e;
    }

    public void setE(BusinessExceptionEnum e) {
        this.e = e;
    }

    // 不写入堆栈信息，提高性能
    @Override
    public Throwable fillInStackTrace(){
        return this;
    }
}

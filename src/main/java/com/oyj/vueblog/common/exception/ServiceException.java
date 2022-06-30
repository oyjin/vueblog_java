package com.oyj.vueblog.common.exception;

/**
 * 自定义service层的异常，手动抛出具体业务信息
 * @author a123
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}

package com.lt.puredesign.exception;

import lombok.Getter;

/**
 * @description: 自定义异常
 * @author: Lt
 * @date: 2022/3/14 22:12
 */
@Getter
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}

package com.lt.puredesign.config;

import java.lang.annotation.*;

/**
 * @description: 自定义注解
 * @author: Lt
 * @date: 2022/3/16 19:25
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
}

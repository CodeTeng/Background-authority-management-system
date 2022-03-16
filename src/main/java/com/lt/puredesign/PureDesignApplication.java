package com.lt.puredesign;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 启动类
 *
 * @author 腾腾
 */
@SpringBootApplication
@EnableWebMvc
public class PureDesignApplication {

    public static void main(String[] args) {
        SpringApplication.run(PureDesignApplication.class, args);
    }

}

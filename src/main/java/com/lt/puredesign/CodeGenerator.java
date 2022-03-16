package com.lt.puredesign;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @description: 代码生成器模板
 * @author: Lt
 * @date: 2022/3/14 17:17
 */
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/puredesign?serverTimezone=GMT%2b8", "root", "806823")
                .globalConfig(builder -> {
                    builder.author("狂小腾") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\javaProject2\\pure-design\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.lt.puredesign") // 设置父包名
                            .entity("model")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\javaProject2\\pure-design\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    // 开启驼峰连字符和开启@RestController控制器
                    builder.controllerBuilder().enableHyphenStyle().enableRestStyle();
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}

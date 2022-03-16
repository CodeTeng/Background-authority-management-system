package com.lt.puredesign.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.lt.puredesign.common.Constants;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.config.AuthAccess;
import com.lt.puredesign.entity.Files;
import com.lt.puredesign.entity.User;
import com.lt.puredesign.service.FileService;
import com.lt.puredesign.service.UserService;
import com.lt.puredesign.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 图表控制器
 * @author: Lt
 * @date: 2022/3/15 18:55
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/total")
    public Result total() {
        return Result.success(userService.count());
    }

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        // TODO 模拟数据 应该从数据库中获取
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members() {
        // 季度人数
        List<User> list = userService.list();
        // 第一季度
        int q1 = 0;
        // 第二季度
        int q2 = 0;
        // 第三季度
        int q3 = 0;
        // 第四季度
        int q4 = 0;
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3 += 1;
                    break;
                case Q4:
                    q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    @GetMapping("/file/front/all")
    @AuthAccess
//    @Cacheable(value = "files", key = "'frontAll'")
    public Result frontAll() {
        // 1. 从缓存获取数据
        String jsonStr = RedisUtils.get(Constants.FILES_KEY);
        List<Files> files;
        if (StringUtils.isBlank(jsonStr)) {
            // 2.如果取出的json为空，则从数据库中获取
            files = fileService.list();
            // 3.再存到缓存中
            RedisUtils.setCache(Constants.FILES_KEY, JSONUtil.toJsonStr(files));
        } else {
            // 减轻数据库的压力
            // 4.如果有, 从redis缓存中获取数据
            files = JSONUtil.toBean(jsonStr, new TypeReference<List<Files>>() {}, true);
        }
        return Result.success(files);
    }
}

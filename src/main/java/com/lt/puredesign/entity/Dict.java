package com.lt.puredesign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description:
 * @author: Lt
 * @date: 2022/3/16 11:40
 */
@TableName("sys_dict")
@Data
public class Dict {
    private String name;
    private String value;
    private String type;
}

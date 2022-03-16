package com.lt.puredesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 课程实体
 * @author: Lt
 * @date: 2022/3/15 19:13
 */
@Data
@ApiModel("Course对象")
@TableName("course")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("课程名称")
    private String name;

    @ApiModelProperty("学分")
    private Integer score;

    @ApiModelProperty("上课时间")
    private String times;

    @ApiModelProperty("是否开课")
    private Boolean state;

    @ApiModelProperty("授课老师id")
    private Integer teacherId;

    @TableField(exist = false)
    private String teacher;
}

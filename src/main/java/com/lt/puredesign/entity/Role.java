package com.lt.puredesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 角色实体类
 * @author: Lt
 * @date: 2022/3/15 19:05
 */
@Data
@TableName("sys_role")
@ApiModel("Role对象")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    /**
     * ROLE_ADMIN ROLE_STUDENT ROLE_TEACHER
     */
    @ApiModelProperty("唯一标识")
    private String flag;
}

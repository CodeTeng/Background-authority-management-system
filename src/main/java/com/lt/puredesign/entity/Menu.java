package com.lt.puredesign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 权限实体类
 * @author: Lt
 * @date: 2022/3/15 19:08
 */
@Data
@TableName("sys_menu")
@ApiModel("Menu对象")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("描述")
    private String description;

    /**
     * 子权限
     */
    @TableField(exist = false)
    private List<Menu> children;

    /**
     * 父权限id
     */
    private Integer pid;

    /**
     * 页面路径
     */
    private String pagePath;

    /**
     * 排序
     */
    private String sortNum;
}

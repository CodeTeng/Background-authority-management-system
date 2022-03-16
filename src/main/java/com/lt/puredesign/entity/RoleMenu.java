package com.lt.puredesign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: 角色-权限表
 * @author: Lt
 * @date: 2022/3/15 19:11
 */
@Data
@TableName("sys_role_menu")
public class RoleMenu {
    private Integer roleId;
    private Integer menuId;
}

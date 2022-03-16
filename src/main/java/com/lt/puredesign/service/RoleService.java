package com.lt.puredesign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Role;

import java.util.List;

/**
 * @description: 角色服务接口
 * @author: Lt
 * @date: 2022/3/15 20:14
 */
public interface RoleService extends IService<Role> {
    /**
     * 分页查询
     *
     * @param name     角色名称
     * @param pageNum  当前页数
     * @param pageSize 当前页数显示多少页
     * @return 结果集
     */
    Result findPage(String name, Integer pageNum, Integer pageSize);

    /**
     * 绑定角色和菜单的关系
     *
     * @param roleId  角色id
     * @param menuIds 菜单id的集合
     * @return 结果集
     */
    Result setRoleMenu(Integer roleId, List<Integer> menuIds);

    /**
     * 获取该角色与其绑定菜单的集合
     *
     * @param roleId 角色id
     * @return 结果集
     */
    Result getRoleMenu(Integer roleId);
}

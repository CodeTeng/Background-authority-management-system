package com.lt.puredesign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Menu;

import java.util.List;

/**
 * @description: 权限服务接口
 * @author: Lt
 * @date: 2022/3/15 20:14
 */
public interface MenuService extends IService<Menu> {
    /**
     * 查出系统所有的菜单(树形)
     *
     * @param name 菜单名称
     * @return 菜单集合
     */
    List<Menu> findMenus(String name);

    /**
     * 分页查询
     *
     * @param name     菜单名称
     * @param pageNum  当前页
     * @param pageSize 当前页显示多少
     * @return 结果集
     */
    Result findPage(String name, Integer pageNum, Integer pageSize);
}

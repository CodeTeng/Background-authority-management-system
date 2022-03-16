package com.lt.puredesign.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Menu;
import com.lt.puredesign.entity.Role;
import com.lt.puredesign.entity.RoleMenu;
import com.lt.puredesign.mapper.MenuMapper;
import com.lt.puredesign.mapper.RoleMapper;
import com.lt.puredesign.mapper.RoleMenuMapper;
import com.lt.puredesign.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @description: 角色服务实现类
 * @author: Lt
 * @date: 2022/3/15 20:14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private static final Log LOG = Log.get(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Result findPage(String name, Integer pageNum, Integer pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        Page<Role> rolePage = roleMapper.selectPage(page, queryWrapper);
        return Result.success(rolePage);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Result setRoleMenu(Integer roleId, List<Integer> menuIds) {
        try {
            // 先删除当前角色id所有的绑定关系
            roleMenuMapper.deleteByRoleId(roleId);
            // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
            List<Integer> menuIdCopy = CollUtil.newArrayList(menuIds);
            for (Integer menuId : menuIds) {
                Menu menu = menuMapper.selectById(menuId);
                // 二级菜单 并且传过来的menuId数组里面没有它的父级id 根据menu表要进行判断处理
                if (menu.getPid() != null && !menuIdCopy.contains(menu.getPid())) {
                    // 那么我们就得补上这个父级id
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menu.getPid());
                    // 存入数据库
                    roleMenuMapper.insert(roleMenu);
                    menuIdCopy.add(menu.getPid());
                }
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
            return Result.success();
        } catch (Exception e) {
            LOG.error(e);
            // 手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error();
        }
    }

    @Override
    public Result getRoleMenu(Integer roleId) {
        List<Integer> list = roleMenuMapper.selectByRoleId(roleId);
        return Result.success(list);
    }
}

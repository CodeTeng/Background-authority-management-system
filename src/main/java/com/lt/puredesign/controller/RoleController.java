package com.lt.puredesign.controller;

import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Role;
import com.lt.puredesign.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 角色控制器
 * @author: Lt
 * @date: 2022/3/16 10:33
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 新增或者更新
     *
     * @param role 角色
     * @return 结果集
     */
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    /**
     * 删除一个
     *
     * @param id 角色id
     * @return 结果集
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    /**
     * 删除多个
     *
     * @param ids 角色的ids
     * @return 结果集
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return Result.success();
    }

    /**
     * 查照所有的角色
     *
     * @return 结果集
     */
    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }

    /**
     * 根据id查找一个角色
     *
     * @param id 角色id
     * @return 结果集
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    /**
     * 分页查询
     *
     * @param name     角色名称
     * @param pageNum  当前页数
     * @param pageSize 当前页数显示多少页
     * @return 结果集
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        return roleService.findPage(name, pageNum, pageSize);
    }

    /**
     * 绑定角色和菜单的关系
     *
     * @param roleId  角色id
     * @param menuIds 菜单id的集合
     * @return 结果集
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result setRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        return roleService.setRoleMenu(roleId, menuIds);
    }

    /**
     * 获取该角色与其绑定菜单的集合
     *
     * @param roleId 角色id
     * @return 结果集
     */
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return roleService.getRoleMenu(roleId);
    }
}

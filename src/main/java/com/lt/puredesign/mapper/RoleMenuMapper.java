package com.lt.puredesign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lt.puredesign.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: Lt
 * @date: 2022/3/15 20:17
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    /**
     * 根据角色id获取其菜单权限的id集合
     *
     * @param roleId 角色id
     * @return 菜单权限id的集合
     */
    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据角色id删除相关菜单权限
     *
     * @param roleId 角色id
     */
    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    void deleteByRoleId(Integer roleId);
}

package com.lt.puredesign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.entity.RoleMenu;
import com.lt.puredesign.mapper.RoleMenuMapper;
import com.lt.puredesign.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @description: 角色权限服务实现类
 * @author: Lt
 * @date: 2022/3/15 20:18
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}

package com.lt.puredesign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.entity.Role;
import com.lt.puredesign.mapper.RoleMapper;
import com.lt.puredesign.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @description: 角色服务实现类
 * @author: Lt
 * @date: 2022/3/15 20:14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}

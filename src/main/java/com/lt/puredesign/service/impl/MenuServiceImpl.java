package com.lt.puredesign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.entity.Menu;
import com.lt.puredesign.mapper.MenuMapper;
import com.lt.puredesign.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * @description: 权限服务实现类
 * @author: Lt
 * @date: 2022/3/15 20:16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}

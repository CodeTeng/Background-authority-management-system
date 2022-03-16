package com.lt.puredesign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.entity.Dict;
import com.lt.puredesign.mapper.DictMapper;
import com.lt.puredesign.service.DictService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Lt
 * @date: 2022/3/16 11:45
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
}

package com.lt.puredesign.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.entity.Course;
import com.lt.puredesign.mapper.CourseMapper;
import com.lt.puredesign.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 课程服务实现类
 * @author: Lt
 * @date: 2022/3/16 18:12
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private static final Log LOG = Log.get(CourseServiceImpl.class);

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Page<Course> findPage(Page<Course> page, String name) {
        return courseMapper.findPage(page, name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        try {
            courseMapper.deleteStudentCourse(courseId, studentId);
            courseMapper.setStudentCourse(courseId, studentId);
        } catch (Exception e) {
            LOG.error(e);
        }
    }
}

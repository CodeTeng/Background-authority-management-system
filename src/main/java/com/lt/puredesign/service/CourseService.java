package com.lt.puredesign.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.puredesign.entity.Course;

/**
 * @description: 课程服务接口
 * @author: Lt
 * @date: 2022/3/16 18:11
 */
public interface CourseService extends IService<Course> {

    /**
     * 分页查询
     *
     * @param page 分页条件
     * @param name 课程名称
     * @return 分页结果
     */
    Page<Course> findPage(Page<Course> page, String name);

    /**
     * 设置学生的课程
     *
     * @param courseId  课程id号
     * @param studentId 学生id号
     */
    void setStudentCourse(Integer courseId, Integer studentId);
}

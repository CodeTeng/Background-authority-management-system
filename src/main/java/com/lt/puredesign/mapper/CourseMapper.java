package com.lt.puredesign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.puredesign.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Lt
 * @date: 2022/3/16 18:10
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 分页查询
     *
     * @param page 分页条件
     * @param name 课程名称
     * @return 分页结果
     */
    Page<Course> findPage(@Param("page") Page<Course> page, @Param("name") String name);

    /**
     * 根据课程号和学生id号删除学生课程
     *
     * @param courseId  课程id
     * @param studentId 学生id
     */
    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    /***
     * 设置学生课程
     * @param courseId 课程id
     * @param studentId 学生id
     */
    void setStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
}

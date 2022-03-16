package com.lt.puredesign.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.User;
import com.lt.puredesign.entity.dto.UserDTO;
import com.lt.puredesign.entity.dto.UserPasswordDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 用户服务
 * @author: Lt
 * @date: 2022/3/13 20:17
 */
public interface UserService extends IService<User> {
    /**
     * 登录
     *
     * @param userDTO 接受前端登录请求的参数
     * @return 封装的结果集
     */
    Result login(UserDTO userDTO);

    /**
     * 注册
     *
     * @param userDTO 接受前端登录请求的参数
     * @return 封装的结果集
     */
    Result register(UserDTO userDTO);

    /**
     * 导出接口
     *
     * @param response 响应客户端
     */
    void export(HttpServletResponse response) throws Exception;

    /**
     * 导入接口
     *
     * @param file 文件参数
     * @return 封装的结果集
     */
    Result imp(MultipartFile file) throws Exception;

    /**
     * 分页
     *
     * @param objectPage 分页参数
     * @param username   用户名
     * @param email      邮箱
     * @param address    地址
     * @return 分页返回值
     */
    Page<User> findPage(Page<User> objectPage, String username, String email, String address);

    /**
     * 修改密码
     *
     * @param userPasswordDTO 修改密码的参数
     */
    void modifyPassword(UserPasswordDTO userPasswordDTO);

    /**
     * 根据用户名去查找
     *
     * @param username 用户名
     * @return 查找到的用户
     */
    User findByUsername(String username);

    /**
     * 根据用户角色查询用户
     *
     * @param role 用户角色
     * @return 封装的结果集
     */
    Result findUserByRole(String role);
}

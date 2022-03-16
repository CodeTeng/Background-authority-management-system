package com.lt.puredesign.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.User;
import com.lt.puredesign.entity.dto.UserDTO;
import com.lt.puredesign.entity.dto.UserPasswordDTO;
import com.lt.puredesign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description: 用户控制器
 * @author: Lt
 * @date: 2022/3/13 20:10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     *
     * @return 所有用户
     */
    @GetMapping
    public Result index() {
        return Result.success(userService.list());
    }

    /**
     * 根据id查找一个用户
     *
     * @param id 用户id
     * @return 封装的结果集
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 根据用户名查找
     *
     * @param username 用户用户名
     * @return 封装的结果集
     */
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        return Result.success(userService.findByUsername(username));
    }

    /**
     * 根据用户角色查询用户
     *
     * @param role 用户角色
     * @return 封装的结果集
     */
    @GetMapping("/role/{role}")
    public Result findUserByRole(@PathVariable String role) {
        return userService.findUserByRole(role);
    }

    /**
     * 新增和修改
     *
     * @param user 参数
     * @return 是否成功
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    /**
     * 删除
     *
     * @param id 用户id
     * @return 是否成功
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    /**
     * 分页
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), username, email, address));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        userService.export(response);
    }

    /**
     * 导入接口
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        return userService.imp(file);
    }

    /**
     * 登录
     *
     * @param userDTO 接受前端登录请求的参数
     * @return 封装的结果集
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    /**
     * 注册接口
     *
     * @param userDTO 接受前端登录请求的参数
     * @return 封装的结果集
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/password")
    public Result modifyPassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.modifyPassword(userPasswordDTO);
        return Result.success();
    }
}

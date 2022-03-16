package com.lt.puredesign.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.common.Constants;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.common.RoleEnum;
import com.lt.puredesign.entity.Menu;
import com.lt.puredesign.entity.Role;
import com.lt.puredesign.entity.User;
import com.lt.puredesign.entity.dto.UserDTO;
import com.lt.puredesign.entity.dto.UserPasswordDTO;
import com.lt.puredesign.exception.ServiceException;
import com.lt.puredesign.mapper.MenuMapper;
import com.lt.puredesign.mapper.RoleMapper;
import com.lt.puredesign.mapper.RoleMenuMapper;
import com.lt.puredesign.mapper.UserMapper;
import com.lt.puredesign.service.UserService;
import com.lt.puredesign.util.TokenUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @description: 用户服务实现类
 * @author: Lt
 * @date: 2022/3/13 20:17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Log LOG = Log.get();

    private static final String SLAT = "LT";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Page<User> findPage(Page<User> page, String username, String email, String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }
        if (StringUtils.isNotBlank(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("id");
        return userMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void modifyPassword(UserPasswordDTO userPasswordDTO) {
        if (userPasswordDTO.getNewPassword().length() < 6 || userPasswordDTO.getNewPassword().length() > 20) {
            throw new ServiceException(Constants.CODE_400, "修改密码格式输入有误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userPasswordDTO.getUsername());
        // 对输入的密码进行加密 然后于数据库进行匹配查找
        byte[] bytes = DigestUtils.md5Hex((SLAT + userPasswordDTO.getPassword())).getBytes(StandardCharsets.UTF_8);
        String password = new String(bytes);
        queryWrapper.eq("password", password);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new ServiceException(Constants.CODE_400, "该用户不存在");
        }
        // 对新密码进行加密
        bytes = DigestUtils.md5Hex((SLAT + userPasswordDTO.getNewPassword())).getBytes(StandardCharsets.UTF_8);
        password = new String(bytes);
        user.setPassword(password);
        // 对数据库中的用户进行更新
        userMapper.updateById(user);
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new ServiceException(Constants.CODE_400, "不存在该用户");
        }
        return user;
    }

    @Override
    public Result findUserByRole(String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        List<User> list = userMapper.selectList(queryWrapper);
        return Result.success(list);
    }

    @Override
    public Result login(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user != null) {
            BeanUtil.copyProperties(user, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(user.getId().toString(), user.getPassword());
            userDTO.setToken(token);
            // 获取该用户的角色 admin
            String role = user.getRole();
            // 获取用户角色的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return Result.success(userDTO);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    /**
     * 获取当前角色的菜单列表
     *
     * @param roleFlag 当前用户的角色
     * @return 当前用户角色的菜单权限
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", roleFlag).last("limit 1");
        Role role = roleMapper.selectOne(queryWrapper);
        // 当前用户角色的id
        Integer roleId = role.getId();
        // 获取该角色的所有权限菜单的id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuMapper.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

    @Override
    public Result register(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user == null) {
            user = new User();
            BeanUtil.copyProperties(userDTO, user, true);
            // 密码加密
            byte[] bytes = DigestUtils.md5Hex((SLAT + userDTO.getPassword())).getBytes(StandardCharsets.UTF_8);
            String encryptPassword = new String(bytes);
            System.out.println(encryptPassword);
            user.setPassword(encryptPassword);
            // 默认一个普通用户的角色
            user.setRole(RoleEnum.ROLE_USER.toString());
            // 存入数据库
            this.save(user);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return Result.success(user);
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库中查寻出所有数据
        List<User> list = userMapper.selectList(null);
        // 通过工具类创建write 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(fileUploadPath + "/用户信息.xlsx");
        // 在内存中操作 写出到浏览器中
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    @Override
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1(推荐)：通过javaBean的方式读取Excel的对象，但是要求表头必须是英文，跟javaBean的属性要对应起来
//        List<User> list = reader.readAll(User.class);
//        System.out.println(list);
        // 方式2：忽略表头的中英文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }
        this.saveBatch(users);
        return Result.success(true);
    }

    private User getUserInfo(UserDTO userDTO) {
        User user = null;
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (username.length() < 2 || username.length() > 10) {
            throw new ServiceException(Constants.CODE_400, "用户名格式输入有误");
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new ServiceException(Constants.CODE_400, "密码格式输入有误");
        }
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return user;
        }
        // 密码加密
        byte[] bytes = DigestUtils.md5Hex((SLAT + password)).getBytes(StandardCharsets.UTF_8);
        String encryptPassword = new String(bytes);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", encryptPassword);

        // 处理异常情况
        try {
            user = this.getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return user;
    }
}

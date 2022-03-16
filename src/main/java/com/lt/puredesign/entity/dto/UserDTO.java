package com.lt.puredesign.entity.dto;

import com.lt.puredesign.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @description: 接受前端登录请求的参数
 * @author: Lt
 * @date: 2022/3/14 21:49
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;

    /**
     * 用户的角色
     */
    private String role;

    /**
     * 用户的菜单权限
     */
    private List<Menu> menus;
}

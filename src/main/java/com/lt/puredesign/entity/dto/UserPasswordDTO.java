package com.lt.puredesign.entity.dto;

import lombok.Data;

/**
 * @description: 修改密码封装的参数
 * @author: Lt
 * @date: 2022/3/15 19:17
 */
@Data
public class UserPasswordDTO {
    private String username;
    private String password;
    private String newPassword;
}

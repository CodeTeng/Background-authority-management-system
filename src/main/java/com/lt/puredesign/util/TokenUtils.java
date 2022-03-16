package com.lt.puredesign.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lt.puredesign.entity.User;
import com.lt.puredesign.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @description: JWT工具类
 * @author: Lt
 * @date: 2022/3/14 23:05
 */
@Component
public class TokenUtils {

    private static UserService staticUserService;

    private static final int OFFSET = 2;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * 生成token
     *
     * @return jwt
     */
    public static String genToken(String userId, String sign) {
        // 将 userId 保存到 token 里面,作为载荷
        return JWT.create().withAudience(userId)
                // 2小时后token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), OFFSET))
                // 以 password 作为 token 的密钥
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}

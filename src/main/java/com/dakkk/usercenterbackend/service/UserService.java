package com.dakkk.usercenterbackend.service;

import com.dakkk.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mikey
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-04-11 20:30:08
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册功能
     *
     * @param userAccont    用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccont, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccont 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccont, String userPassword, HttpServletRequest req);

    /**
     * 用户脱敏方法
     * @param originUser 从数据库获取的原始用户
     * @return 返回脱敏后的用户
     */
    User getSafetyUser(User originUser);
}

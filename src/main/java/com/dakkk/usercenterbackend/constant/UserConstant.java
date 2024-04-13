package com.dakkk.usercenterbackend.constant;

/**
 * ClassName: UserConstant
 * Package: com.dakkk.usercenterbackend.constant
 *
 * @Create 2024/4/12 17:11
 * @Author dakkk
 * Description: 用户常量
 */
public interface UserConstant {
    /**
     * 用户登录态的键
     */
    String USER_LOGIN_STATE = "userLoginState";

    // ----- 权限 -----

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}

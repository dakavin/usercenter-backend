package com.dakkk.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserRegisterRequest
 * Package: com.dakkk.usercenterbackend.model.domain.request
 *
 * @Create 2024/4/12 14:53
 * @Author dakkk
 * Description: 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -3081732840333386033L;
    private String userAccount;
    private String userPassword;
}

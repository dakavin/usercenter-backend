package com.dakkk.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserRegisterRequest
 * Package: com.dakkk.usercenterbackend.model.domain.request
 *
 * @Create 2024/4/12 14:53
 * @Author dakkk
 * Description: 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 8628383983087108526L;
    private String userAccont;
    private String userPassword;
    private String checkPassword;
}

package com.dakkk.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dakkk.usercenterbackend.common.BaseResponse;
import com.dakkk.usercenterbackend.common.ErrorCode;
import com.dakkk.usercenterbackend.common.ResultUtils;
import com.dakkk.usercenterbackend.exception.BusinessException;
import com.dakkk.usercenterbackend.model.domain.User;
import com.dakkk.usercenterbackend.model.domain.request.UserLoginRequest;
import com.dakkk.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.dakkk.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.dakkk.usercenterbackend.constant.UserConstant.*;

/**
 * ClassName: UserController
 * Package: com.dakkk.usercenterbackend.controller
 *
 * @Create 2024/4/12 14:35
 * @Author dakkk
 * Description: 用户接口
 */
@RestController
@RequestMapping("/user")
// @CrossOrigin(origins = {"http://203.195.193.58"},allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册请求参数封装错误");
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String inviteCode = userRegisterRequest.getInviteCode();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, inviteCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册请求参数存在空值");
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword, inviteCode);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest req) {
        Object userObj = req.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "当前没有用户登录");
        }
        // 注意不能直接返回当前用户信息，最好实时更新一下（查库）
        long userId = currentUser.getId();
        // todo 没有判断用户是否被封号
        User user = userService.getById(userId);
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest req) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录请求参数封装错误");
        }

        String userAccont = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccont, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录请求参数存在空值");
        }
        User loginUser = userService.userLogin(userAccont, userPassword, req);
        return ResultUtils.success(loginUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest req) {
        // 鉴权
        if (!isAdmin(req)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "当前用户不是管理员");
        }

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            lqw.like(User::getUsername, username);
        }
        List<User> userList = userService.list(lqw);
        // 返回的信息要脱密
        List<User> userSafetyList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userSafetyList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest req) {
        // 鉴权
        if (!isAdmin(req)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "当前用户不是管理员");
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "需要删除的用户id错误");
        }
        return ResultUtils.success(userService.removeById(id));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest req) {
        if (req == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "连接错误");
        }
        return ResultUtils.success(userService.userLogout(req));
    }

    /**
     * 用户鉴权方法
     *
     * @param req 用户的请求
     * @return 用户是否为管理员，是则为true
     */
    private boolean isAdmin(HttpServletRequest req) {
        // 鉴权
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        // 注意空指针异常
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}

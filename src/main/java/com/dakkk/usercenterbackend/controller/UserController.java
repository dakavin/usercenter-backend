package com.dakkk.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dakkk.usercenterbackend.model.domain.User;
import com.dakkk.usercenterbackend.model.domain.request.UserLoginRequest;
import com.dakkk.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.dakkk.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest req){
        Object userObj = req.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            return null;
        }
        //注意不能直接返回当前用户信息，最好实时更新一下（查库）
        long userId = currentUser.getId();
        //todo 没有判断用户是否被封号
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    @PostMapping("login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest req) {
        if (userLoginRequest == null) {
            return null;
        }

        String userAccont = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccont, userPassword)) {
            return null;
        }
        User loginUser = userService.userLogin(userAccont, userPassword, req);
        return loginUser;
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest req) {
        //鉴权
        if(!isAdmin(req)){
            return new ArrayList<>();
        }

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            lqw.like(User::getUsername, username);
        }
        List<User> userList = userService.list(lqw);
        //返回的信息要脱密
        return userList.stream().map(user->userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public Boolean deleteUser(@RequestBody long id,HttpServletRequest req) {
        //鉴权
        if(!isAdmin(req)){
            return false;
        }

        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 用户鉴权方法
     * @param req 用户的请求
     * @return 用户是否为管理员，是则为true
     */
    private boolean isAdmin(HttpServletRequest req){
        //鉴权
        HttpSession session = req.getSession();
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        //注意空指针异常
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}

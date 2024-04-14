package com.dakkk.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dakkk.usercenterbackend.common.ErrorCode;
import com.dakkk.usercenterbackend.constant.InviteCodeEnum;
import com.dakkk.usercenterbackend.exception.BusinessException;
import com.dakkk.usercenterbackend.model.domain.User;
import com.dakkk.usercenterbackend.service.UserService;
import com.dakkk.usercenterbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dakkk.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author mikey
 * @description 用户服务实现类
 * @createDate 2024-04-11 20:30:08
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值，用于混淆密码
     */
    private static final String SALT = "dakkk";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String inviteCode) {
        // 1.校验
        // 输入内容不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, inviteCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (inviteCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邀请码过长");
        }
        // 账户不包含特殊字符(放在账户不能重复前面)
        String regEx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号含非法字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不相同");
        }
        // 邀请码必须等于指定邀请码
        boolean flag = false;
        for (InviteCodeEnum code : InviteCodeEnum.values()) {
            if (code.getCode().equals(inviteCode)) {
                flag = true;
                break;
            }
        }
        // 如果遍历了所有邀请码都不对，则邀请码不对
        if (!flag) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邀请码错误");
        }

        // 账户不能重复
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserAccount, userAccount);
        long count = this.count(lqw);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在相同账户");
        }

        // 2.密码加密(使用spring自带的工具库进行加密)
        String encryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryPassword);
        user.setInviteCode(inviteCode);
        boolean saveResult = this.save(user);
        return saveResult ? user.getId() : -1;
    }

    @Override
    public User userLogin(String userAccont, String userPassword, HttpServletRequest req) {
        // 1.校验
        // 输入内容不能为空
        if (StringUtils.isAnyBlank(userAccont, userPassword)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录参数存在空值");
        }
        if (userAccont.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户长度过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }
        // 账户不包含特殊字符(放在账户不能重复前面)
        String regEx = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccont);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户包含特殊字符");
        }

        // 2.密码加密(使用spring自带的工具库进行加密)
        String encryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3.查询用户是否存在
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUserAccount, userAccont);
        lqw.eq(User::getUserPassword, encryPassword);
        User loginUser = this.getOne(lqw);

        // todo 可以添加用户限流，如果用户在单ip下登录次数过多

        // 4. 判断数据库中是否存在该用户
        if (null == loginUser) {
            log.info("user login failed,userAccount connot matched!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "此用户不存在");
        }

        // 5.用户信息脱敏(调用脱敏方法)
        User safetyUser = getSafetyUser(loginUser);

        // 6.记录用户的登录态(使用request)
        HttpSession session = req.getSession();
        session.setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest req) {
        req.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setInviteCode(originUser.getInviteCode());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

}





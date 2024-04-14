package com.dakkk.usercenterbackend.service;

import com.dakkk.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * ClassName: UserServiceTest
 * Package: com.dakkk.usercenterbackend.service
 *
 * @Create 2024/4/11 20:36
 * @Author dakkk
 * Description:
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("testdakkk");
        user.setUserAccount("123");
        user.setAvatarUrl("https://cn.bing.com/images/search?q=%E5%A4%B4%E5%83%8F&FORM=IQFRBA&id=FF86368499A7EECCCF1282E03FAB533584B0EA52");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        //校验密码等不能为空
        String userAccount = "mikey";
        String userPassword = "";
        String checkPassword = "123456";
        String inviteCode = "DAKKK";
        long res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertEquals(-1,res);

        //校验账户不能小于4位
        userAccount="da";
        res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertEquals(-1,res);

        //校验密码不能小于8位
        userAccount="mikey";
        userPassword="123456";
        res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertEquals(-1,res);

        //校验账户不能包含特殊字符
        userAccount="da  kkk";
        userPassword = "12345678";
        res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertEquals(-1,res);

        //校验 密码和校验密码需要相同
        checkPassword = "123456789";
        res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertEquals(-1,res);

        //校验 用户不能重复
        userAccount = "mikeylay";
        checkPassword = "12345678";
        res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertEquals(-1,res);

        // 最后一次成功
        userAccount="dakkkkk";
        res = userService.userRegister(userAccount, userPassword, checkPassword,inviteCode);
        Assertions.assertTrue(res>0);
    }
}
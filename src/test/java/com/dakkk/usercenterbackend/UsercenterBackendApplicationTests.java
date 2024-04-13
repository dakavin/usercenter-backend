package com.dakkk.usercenterbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UsercenterBackendApplicationTests {

    @Test
    void contextLoads() {
        String userAccont = "da kkk";
        String regEx="\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccont);
        System.out.println(matcher.find());
    }

}

package com.dakkk.usercenterbackend.constant;

/**
 * ClassName: InviteCodeEnum
 * Package: com.dakkk.usercenterbackend.constant
 *
 * @Create 2024/4/14 16:18
 * @Author dakkk
 * Description:
 */
public enum InviteCodeEnum {
    CODE_1("DAKKK"),
    CODE_2("RPP66"),
    CODE_3("DK666"),
    CODE_4("DK888"),
    CODE_5("DREAM");

    private String code;

    InviteCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}


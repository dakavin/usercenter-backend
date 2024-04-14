package com.dakkk.usercenterbackend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: BaseResponse
 * Package: com.dakkk.usercenterbackend.common
 *
 * @Create 2024/4/14 17:15
 * @Author dakkk
 * Description: 通用返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 3465127617799476957L;
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(int code, T data, String message) {
        this(code,data,message,"");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}

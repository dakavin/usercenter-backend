package com.dakkk.usercenterbackend.common;

/**
 * ClassName: ResultUtil
 * Package: com.dakkk.usercenterbackend.common
 *
 * @Create 2024/4/14 17:20
 * @Author dakkk
 * Description: 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    /**
     * 失败
     * @param code
     * @param messgae
     * @param description
     * @return
     */
    public static BaseResponse error(int code,String messgae,String description){
        return new BaseResponse(code,null,messgae,description);
    }

    /**
     * 失败
     * @param errorCode
     * @param messgae
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String messgae,String description){
        return new BaseResponse(errorCode.getCode(),null,messgae,description);
    }

    /**
     * 失败
     * @param errorCode
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMessage(),description);
    }

}

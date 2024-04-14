package com.dakkk.usercenterbackend.exception;

import com.dakkk.usercenterbackend.common.BaseResponse;
import com.dakkk.usercenterbackend.common.ErrorCode;
import com.dakkk.usercenterbackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.dakkk.usercenterbackend.exception
 *
 * @Create 2024/4/14 18:41
 * @Author dakkk
 * Description:
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException:" + e.getMessage() + "->" + e.getDescription(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e) {
        log.error("runtimeException:" + e.getMessage() + "->" + e.getDescription(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}

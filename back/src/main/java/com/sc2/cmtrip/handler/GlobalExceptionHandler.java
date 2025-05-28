package com.sc2.cmtrip.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import com.sc2.cmtrip.common.ApiResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

// 该注解保证所有返回值以JSON格式存在
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Global exception interceptor: input type `Exception` indicates it intercepts all types of exceptions
     * 全局异常拦截：输入值为Exception表明它拦截所有类型的异常
     * @param e
     * @return
     */
//    @ExceptionHandler
//    public SaResult handlerException(Exception e) {
//        e.printStackTrace();
//        return SaResult.error(e.getMessage()); // error是500
//    }

    /**
     * Catch the unauthenticated errors thrown by Sa-Token
     * 捕获Sa-Token发出的未登录错误
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public ApiResult handleNotLoginException(NotLoginException e) {
        return ApiResult.error(1000, "Not logged in or login has expired.");
    }

    /**
     * Intercept actively thrown `RuntimeException`
     * 主动抛出的Runtime异常拦截
     * @param r
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResult handlerRuntimeException(RuntimeException r) {
        r.printStackTrace();
        return ApiResult.error(1001, r.getMessage()); // error是500
    }

    /**
     * Catch `NullPointerException` exception
     * 捕获 NullPointerException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ApiResult handlerNullPointerException(NullPointerException e) {
        e.printStackTrace();
        return ApiResult.error(1002, "Null pointer exception, please check the validity of the related data: " + e.getMessage());
    }

    /**
     * Catch `IllegalArgumentException` exception
     * 捕获 IllegalArgumentException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult handlerIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        return ApiResult.error(1003, "Invalid parameter passed: " + e.getMessage());
    }

    /**
     * Catch `SQLException` exception
     * 捕获 SQLException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ApiResult handlerSQLException(SQLException e) {
        e.printStackTrace();
        return ApiResult.error(1004, "Database operation error, please try again later: " + e.getMessage());
    }

    /**
     * Catch `IOException` exception
     * 捕获 IOException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ApiResult handlerIOException(IOException e) {
        e.printStackTrace();
        return ApiResult.error(1005, "File processing error, please check the file path or format: " + e.getMessage());
    }

    /**
     * Catch `ClassNotFoundException` exception
     * 捕获 ClassNotFoundException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(ClassNotFoundException.class)
    public ApiResult handlerClassNotFoundException(ClassNotFoundException e) {
        e.printStackTrace();
        return ApiResult.error(1007, "Class not found, please check if the dependencies are correctly configured: " + e.getMessage());
    }

    /**
     * Catch `OutOfMemoryError` exception
     * 捕获 OutOfMemoryError 异常
     * @param e
     * @return
     */
    @ExceptionHandler(OutOfMemoryError.class)
    public ApiResult handlerOutOfMemoryError(OutOfMemoryError e) {
        e.printStackTrace();
        return ApiResult.error(1008, "Out of memory error, insufficient system resources.");
    }

    /**
     * Catch `TimeoutException` exception
     * 捕获 TimeoutException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(TimeoutException.class)
    public ApiResult handlerTimeoutException(TimeoutException e) {
        e.printStackTrace();
        return ApiResult.error(1009, "Operation timed out, please try again later.");
    }

    /**
     * Catch `UnsupportedOperationException` exception.
     * 捕获 UnsupportedOperationException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnsupportedOperationException.class)
    public ApiResult handlerUnsupportedOperationException(UnsupportedOperationException e) {
        e.printStackTrace();
        return ApiResult.error(1010, "Unsupported operation, please verify your action steps.");
    }
}

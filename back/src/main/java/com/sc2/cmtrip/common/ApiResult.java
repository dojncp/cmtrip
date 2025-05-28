package com.sc2.cmtrip.common;

import org.springframework.stereotype.Component;

/**
 * Common Api Result
  */

@Component
public class ApiResult<T> {

    private int code;

    private String message;

    private T data;

    // default
    private ApiResult() {}

    // success (null)
    public static ApiResult<Void> success() {
        return success(null, null);
    }

    // success (message & data)
    public static <T> ApiResult<T> success(String message, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(200);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    // success (message only)
    public static <T> ApiResult<T> success(String message) {
        return success(message, null);
    }

    // success (data only)
    public static <T> ApiResult<T> success(T data) {
        return success(null, data);
    }

    // error
    public static <T> ApiResult<T> error(int code, String message) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // Getter & Setter Function
    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }


}

package com.alioo.format.domain.base;

import org.springframework.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午18:02
 */
public class RestApiResponse<T> {

    private String errorInfo;

    private Integer code;

    private T data;

    public static RestApiResponse fail(Integer code, String errorInfo) {
        RestApiResponse fragment = new RestApiResponse();
        fragment.setCode(code);
        fragment.setErrorInfo(errorInfo);
        return fragment;
    }

    public static <T> RestApiResponse success(T data) {
        RestApiResponse fragment = new RestApiResponse();
        fragment.setCode(HttpStatus.OK.value());
        fragment.setData(data);
        return fragment;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

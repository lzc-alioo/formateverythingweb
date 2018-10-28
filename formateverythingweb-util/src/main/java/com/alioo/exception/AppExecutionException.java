package com.alioo.exception;

/**
 * 无法归结到其他具体异常类型时，用此类，返回500错误
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class AppExecutionException extends AppException {

    public AppExecutionException(String errorInfo) {
        super(errorInfo);
    }

    public AppExecutionException() {
        super("error happened when code running");
    }
}

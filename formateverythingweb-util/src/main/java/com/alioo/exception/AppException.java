package com.alioo.exception;

/**
 * 区域落地项目异常基类
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class AppException extends RuntimeException {

    private String errorInfo;

    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public AppException(String errorInfo) {
        super(errorInfo);
        this.errorInfo = errorInfo;
    }

    @Override
    public String getMessage() {
        return errorInfo;
    }
}

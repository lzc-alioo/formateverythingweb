package com.alioo.exception;

/**
 * 参数不合法或缺失参数。
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class InvalidParameterException extends AppException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param errorInfo
     */
    public InvalidParameterException(String errorInfo) {
        super(errorInfo);
    }

    public InvalidParameterException() {
        super("无效的参数");
    }
}

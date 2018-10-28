package com.alioo.exception;

/**
 * 无操作权限
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class OperationForbiddenException extends AppException {

    public OperationForbiddenException(String errorInfo) {
        super(errorInfo);
    }

    public OperationForbiddenException() {
        super("无权限执行此操作");
    }
}

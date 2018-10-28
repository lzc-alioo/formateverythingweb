package com.alioo.exception;

/**
 * 用户请求访问的资源不存在
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException() {
        super("找不到相应资源");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

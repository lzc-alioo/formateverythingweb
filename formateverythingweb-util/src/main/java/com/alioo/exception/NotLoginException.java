package com.alioo.exception;

/**
 * 没有认证(没有登录本系统)或登录的用户名与密码错误
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class NotLoginException extends AppException {

    public NotLoginException() {
        super("尚未登录");
    }
}

package com.alioo.exception;


/**
 * 主要表达通过HTTP接口传递非法参数、请求数据格式有问题，主要用在controller中
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/9
 * Time: 下午4:59
 */
public class BadRequestException extends AppException {

    public BadRequestException(String fieldName, String message) {
        super(fieldName + ": " + message);
    }

    public BadRequestException(String fieldName) {
        super("参数异常：" + fieldName);
    }
}

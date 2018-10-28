package com.alioo.util;

import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

/**
 * spring mvn handlerMethod
 * Created by Josie on 16/10/20.
 */
public final class HandlerMethodUtils {
    /**
     * 拦截器中使用,获得handler
     *
     * @author zouzhili [zouzhili@alioo.com]
     * @Date 16/10/20 下午5:09
     **/
    public static final HandlerMethod getHandleMethod(
        Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod;
        }
        throw new IllegalStateException("can't case handler to handlerMethod");
    }

    /**
     * 获得处理请求的控制器
     *
     * @author zouzhili [zouzhili@alioo.com]
     * @Date 16/10/20 下午5:09
     **/
    public static final Method getHandlerMethodExecutor(
        HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        return method;
    }

    /**
     * 获得处理请求的控制器
     *
     * @author zouzhili [zouzhili@alioo.com]
     * @Date 16/10/20 下午5:10
     **/
    public static final Method getHandlerMethodExecutor(
        Object handler) {
        Method method = getHandleMethod(handler).getMethod();
        return method;
    }
}

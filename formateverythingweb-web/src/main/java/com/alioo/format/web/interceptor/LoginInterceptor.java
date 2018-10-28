package com.alioo.format.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 **/
@Component
@ConditionalOnWebApplication
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        Exception {
//        //判断是否添加@NoLoginRequired注解，如果添加，则返回true
//        HandlerMethod handlerMethod = HandlerMethodUtils.getHandleMethod(handler);
//        if (handlerMethod.getMethodAnnotation(LoginNoRequired.class) != null) {
//            return true;
//        }
//        String username = CookieUtil.getCookieValue(request, Constant.KylinUsernameCookie);
//        if (StringUtil.isEmpty(username)) {
//            username = request.getHeader(Constant.KylinUsernameCookie);
//        }
//        logger.info("loginInterceptor username:{}", username);
//        if (StringUtils.isEmpty(username)) {
//            throw new NotLoginException();
//        }
//        Constant.threadUsername.set(username);
        return true;
    }

}

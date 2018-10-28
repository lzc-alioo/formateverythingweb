//package com.alioo.web.config;
//
//import LoginInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * 加载拦截器的容器
// *
// * @author zouzhili [zouzhili@alioo.com]
// * @Date 16/10/31 下午3:11
// **/
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurerAdapter {
//
//    @Autowired
//    private LoginInterceptor loginInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        //记录访问日志,all
//        registry.addInterceptor(loginInterceptor);
//
//    }
//
//}

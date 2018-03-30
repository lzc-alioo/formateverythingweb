package com.lzc.home.spring.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanInitLog implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BeanInitLog.class);
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("开始加载bean("+beanName+")"+bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("完成加载bean("+beanName+")"+bean);
        return bean;
    }
}
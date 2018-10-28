package com.alioo.util.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @ClassName: com.alioo.util.spring.SpringInitLog
 * @Description:打印spring bean log
 * @author: liuzhichong@alioo.com
 * @date: 2018/07/30
 * @Copyright: 2018 www.alioo.com Inc. All rights reserved.
 */
public class SpringInitLog implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpringInitLog.class);


    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        logger.info("正在初始化spring对象["+s+"]"+o);
        return o;
    }
}

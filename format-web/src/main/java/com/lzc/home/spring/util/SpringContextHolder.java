package com.lzc.home.spring.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);
    private static ApplicationContext applicationContext = null;

    public SpringContextHolder() {
    }

    public void setApplicationContext(ApplicationContext _applicationContext) {
        logger.info("注入ApplicationContext到SpringContextHolder:" + _applicationContext);
        if(applicationContext != null) {
            logger.error("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + applicationContext);
        }

        applicationContext = _applicationContext;
    }

    public void destroy() throws Exception {
        clear();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        assertContext();
        return (T)applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        assertContext();
        return applicationContext.getBean(name, requiredType);
    }

    public static void clear() {
        logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }

    private static void assertContext() {
        getApplicationContext();
        if(applicationContext == null) {
            throw new IllegalStateException("applicaitonContext为空.");
        }
    }
}

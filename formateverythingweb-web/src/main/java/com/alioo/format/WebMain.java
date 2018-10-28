package com.alioo.format;

import com.alioo.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration()
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"com.alioo.format","com.alioo.util.spring"})
public class WebMain {
    private static Logger logger = LoggerFactory.getLogger(WebMain.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(WebMain.class, args);

            printLogo();

            logger.info("formateverythingweb-web启动成功...");
        } catch (Throwable e) {
            logger.error("formateverythingweb-web启动异常", e);
        }


    }

    /**
     * 打印logo
     */
    private static void printLogo() {
        String logo = FileUtil.readFile("logo");
        logger.info(logo);

    }

}

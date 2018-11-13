package com.alioo.format.service.test;

import com.alioo.format.service.test.deploy.CsdnDeployServiceImpl;
import com.alioo.format.service.test.deploy.DeployService;
import com.alioo.util.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName: com.alioo.format.service.ArticleImport
 * @Description:
 * @author: liuzhichong
 * @date: 2018/10/28
 */


@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration()
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"com.alioo.format","com.alioo.util.spring"})
public class ArticleImport {
    private static Logger logger = LoggerFactory.getLogger(ArticleImport.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(ArticleImport.class, args);

            logger.info("formateverythingweb-web启动成功...");


            DeployService deployService = SpringUtil.getBean(CsdnDeployServiceImpl.class);
            deployService.deploy();

        } catch (Throwable e) {
            logger.error("formateverythingweb-web启动异常", e);
            e.printStackTrace();
        }


    }

}

package com.lzc.home.article.launcher;

import com.jd.o2o.commons.utils.spring.SpringContextHolder;
import com.lzc.home.article.deploy.DeployService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务加载器 
 * User: wenjun 
 * Date: 2015-03-10 
 * Time: 10:50
 */
public class ServiceLauncher {
	private static final Logger logger = LoggerFactory.getLogger(ServiceLauncher.class);
	private static final String bootPath = ServiceLauncher.class.getName();

	public static void main(String[] args) {
		try{
			new ClassPathXmlApplicationContext(new String[] { "spring-config.xml" });
			logger.info("{}服务已启动", new Object[]{ bootPath });

		}catch(Throwable ex){
			logger.error("服务已启动异常:{}", new Object[]{ ex.getLocalizedMessage(), ex });
		}

        DeployService deployService = null;
        try {

            //deployService = SpringContextHolder.getBean("csdnDeployServiceImpl");
//            deployService = SpringContextHolder.getBean("shouqiDeployServiceImpl");
            deployService = SpringContextHolder.getBean("missfreshDeployServiceImpl");
            deployService.deploy();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //String content=" <link rel=\"stylesheet\" href=\"http://static.blog.csdn.net/public/res-min/markdown_views.css?v=2.0\" /> ";
        //String str = "http\\:\\/\\/static\\.blog\\.csdn\\.net\\/public\\/res-min\\/markdown_views\\.css\\?v=2.0";
        //String strReplace = "/static/markdown/markdown_views.css\\?v=2.0";
        //content= content.replaceAll(str, strReplace);
        //System.out.println("content="+content);

	}
}
package com.alioo.format.service.test.deploy;

import com.alioo.format.dao.ArticleDAO;
import com.alioo.format.domain.Article;
import com.alioo.format.domain.base.Page;
import com.alioo.util.JsonUtil;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;


/**
 * Created by liuzhichong on 2016/9/8.
 */
@Service
public class CsdnDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(CsdnDeployServiceImpl.class);

    @Resource
    private ArticleDAO articleDao;

    @Value(value = "${spring.cookieStr}")
    private String cookieStr;


    public void deploy() {

//        List<Article> list = DeployUtil.postlist(null);
//        for (int i = 0; i < list.size(); i++) {
//            Article e = list.get(i);
//            if (e.getTitle().contains("原帝都的凛冬")) {
//                continue;
//            }
//            logger.info("[" + i + "]准备插入e" + JsonUtil.toJson(e));
//            articleDao.insert(e);
//        }

//        Article e = new Article();
//        e.setCsdnLink("http://blog.csdn.net/hl_java/article/details/78344667");
//        e = DeployUtil.getContent(e, cookieStr);


        List<Article> list2 = null;

        Article query = new Article();
        try {
            list2 = articleDao.listQueryPage(query, 0, 10000, null);
        } catch (Exception e) {
            logger.error("操作时异常", e);
            return;
        }
        logger.info("数据加载完成，总条数：" + list2.size());
        int i = 0;
        try {
            for (i = 0; i < list2.size(); i++) {
                Article e = list2.get(i);
                e = DeployUtil.getContent(e, cookieStr);
                articleDao.update(e);

                logger.info("i="+i+", url=" + e.getCsdnLink() + " ,content=" + e.getContent().substring(0, 100));

                Thread.sleep(new Random().nextInt(1000));
            }
        } catch (Exception e) {
            logger.error("操作时异常[i=" + i + ",content.length=" + list2.get(i).getContent().length() + "]", e);
        }


    }

}

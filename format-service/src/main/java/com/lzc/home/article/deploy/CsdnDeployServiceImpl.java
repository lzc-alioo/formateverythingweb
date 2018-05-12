package com.lzc.home.article.deploy;

import com.jd.o2o.commons.domain.PageBean;
import com.lzc.home.article.launcher.DeployUtil;
import com.lzc.home.dao.ArticleDao;
import com.lzc.home.domain.entity.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuzhichong on 2016/9/8.
 */
@Service
public class CsdnDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(CsdnDeployServiceImpl.class);

//    @Resource
//    private ArticleDao articleDao;

    @Value(value = "${cookieStr}")
    private String cookieStr;

    public void deploy() {
//
//        List<Article> list = DeployUtil.postlist(null);
//        for (int i=0;i<list.size();i++) {
//            Article e=list.get(i);
//            logger.info("["+i+"]准备插入e"+ e);
//            articleDao.insert(e);
//        }

        //Article e = new Article();
        //e.setCsdnLink("http://blog.csdn.net/hl_java/article/details/78344667");
        //DeployUtil.getContent(e, cookieStr);


//        Article articleParam = null;
//        PageBean<Article> pageBean = null;
//        try {
//            pageBean = new PageBean<Article>();
//            pageBean.setPageNo(1);
//            pageBean.setPageSize(10000);
//            articleParam = new Article();
//
//            pageBean = articleDao.pageQuery(articleParam, pageBean.getPageNo(), pageBean.getPageSize());
//        } catch (Exception e) {
//            logger.error("操作时异常", e);
//            return;
//        }
//        List<Article> list2 = pageBean.getResultList();
//        logger.info("数据加载完成，总条数：" + list2.size());
//        int i = 0;
//        try {
//            for (i = 0; i < list2.size(); i++) {
//                Article e = list2.get(i);
//                e = DeployUtil.getContent(e, null);
////                articleDao.update(e);
//            }
//        } catch (Exception e) {
//            logger.error("操作时异常[i=" + i + ",content.length=" + list2.get(i).getContent().length() + "]", e);
//        }


    }

}

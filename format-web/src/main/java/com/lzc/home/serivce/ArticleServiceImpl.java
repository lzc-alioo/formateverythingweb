package com.lzc.home.serivce;

import com.jd.o2o.commons.domain.PageBean;
import com.lzc.home.dao.ArticleDao;
import com.lzc.home.domain.entity.Article;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by alioo on 2017/10/28.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleDao articleDao;


    @Override
    public PageBean<Article> pageQuery(Article articleParam, PageBean<Article> pageBean) {

        pageBean= articleDao.pageQuery(articleParam, pageBean.getPageNo(),pageBean.getPageSize());

        return pageBean;
    }

    public Article get(Long id){

        Article result = articleDao.get(id);

        return result;
    }


}

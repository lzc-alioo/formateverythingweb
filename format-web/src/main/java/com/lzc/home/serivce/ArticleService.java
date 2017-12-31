package com.lzc.home.serivce;

import com.jd.o2o.commons.domain.PageBean;
import com.lzc.home.domain.entity.Article;

/**
 * Created by alioo on 2017/10/28.
 */
public interface ArticleService {


    public PageBean<Article> pageQuery(Article articleParam, PageBean<Article> pageBean);

    public Article get(Long id);
}
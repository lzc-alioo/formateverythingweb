package com.alioo.format.service;

import com.alioo.format.domain.Article;
import com.alioo.format.domain.base.Page;

/**
 * Created by alioo on 2017/10/28.
 */
public interface ArticleService {


    public Page<Article> pageQuery(Article articleParam, int page,int size,String sortOrder);

    public Article get(Long id);
}
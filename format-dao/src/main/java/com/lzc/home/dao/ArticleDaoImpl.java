package com.lzc.home.dao;


import com.jd.o2o.commons.dao.GenericDAOImpl;
import com.jd.o2o.commons.dao.annotation.TableDesc;
import com.lzc.home.domain.entity.Article;
import org.springframework.stereotype.Repository;

/**
 * Created with GenerateCode.
 * User: 京东到家
 * DateTime: 2016-11-23T16:16:26.1989210+08:00
 */
@TableDesc(nameSpace = "ArticleMapper", tableName = "Article")
@Repository
public class ArticleDaoImpl extends GenericDAOImpl<Article,Long> implements ArticleDao {


}

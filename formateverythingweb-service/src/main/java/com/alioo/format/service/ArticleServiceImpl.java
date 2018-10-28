package com.alioo.format.service;

import com.alioo.format.dao.ArticleDAO;
import com.alioo.format.domain.Article;
import com.alioo.format.domain.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by alioo on 2017/10/28.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDAO articleDAO;


    public ArticleServiceImpl() {
        System.out.println("init ArticleServiceImpl");
    }

    @Override
    public Page<Article> pageQuery(Article query, int page, int size,String sortOrder){

        int count=articleDAO.listQueryCount(query);
        List<Article> list=null;
        if(count>0){
            int start=(page-1)*size;
            list= articleDAO.listQueryPage(query, start, size, sortOrder);
        }
        Page<Article> pageBean = new Page<>(list, count);
        return pageBean;
    }

    public Article get(Long id){

        Article result = articleDAO.findById(id);

        return result;
    }


}

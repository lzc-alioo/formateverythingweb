package com.lzc.home.controller;

import com.jd.o2o.commons.domain.PageBean;
import com.jd.o2o.commons.utils.json.JsonUtils;
import com.lzc.home.domain.entity.Article;
import com.lzc.home.serivce.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/article")
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleService articleService;

    /**
     * 主页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String result = "article/articleList";

        logger.info("跳转页面：" + result);
        return result;
    }

    /**
     * 主页
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> list(Integer page, Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        Article articleParam=null;
        PageBean<Article> pageBean = null;
        try {
            pageBean = new PageBean<Article>();
            pageBean.setPageNo(page);
            pageBean.setPageSize(pageSize);
            articleParam=new Article();

            pageBean = articleService.pageQuery(articleParam, pageBean);
        } catch (Exception e) {
            logger.error("操作时异常", e);
            resultMap.put("success", 0);
            resultMap.put("msg", "操作时异常" + e.getMessage());
            return resultMap;
        }


        long total = 0;
        List<Article> rows = new ArrayList<Article>();

        if (pageBean != null && pageBean.getTotalCount() > 0) {
            total = pageBean.getTotalCount();
            rows = pageBean.<List<Article>>getResultList();
        }

        resultMap.put("success", 1);
        resultMap.put("total", total);
        resultMap.put("rows", rows);

        logger.info("获取文章page:{}，pageSize:{}，返回结果total:{}", new Object[]{page, pageSize, total});
        return resultMap;
    }

    /**
     * 主页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toDetail", method = {RequestMethod.GET, RequestMethod.POST})
    public String toDetail(Long id,HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String result = "article/articleDetail";

        request.setAttribute("id",id);
        logger.info("跳转页面：" + result);
        return result;
    }

    /**
     * 获取单条文章
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> detail(Long id, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        Article article=null;
        try {
             article=articleService.get(id);

        } catch (Exception e) {
            logger.error("操作时异常", e);
            resultMap.put("success", 0);
            resultMap.put("msg", "操作时异常" + e.getMessage());
            return resultMap;
        }

        resultMap.put("success", 1);
        resultMap.put("article", article);

        long contentLength=0;
        if(article!=null){
            contentLength=article.getContent().length();
        }
        logger.info("获取单条文章id:{}，返回数据字符数量:{}", new Object[]{id, contentLength});
        logger.info("获取单条文章id:{}，返回数据:{}", new Object[]{id, JsonUtils.toJson(article)});
        return resultMap;
    }


}

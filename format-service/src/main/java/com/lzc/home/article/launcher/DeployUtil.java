package com.lzc.home.article.launcher;

import com.jd.o2o.commons.utils.DateTimeUtil;
import com.lzc.home.domain.entity.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzhichong on 2016/9/8.
 */
public class DeployUtil {
    private static final Logger logger = LoggerFactory.getLogger(DeployUtil.class);

    public static List<Article> postlist(String cookieStr) {
        String graspTime = DateTimeUtil.getDateTime14String();


        String url = "http://blog.csdn.net/hl_java/";
        HashMap<String, Object> requestParams = new HashMap<String, Object>();
        String result = HttpUtil.httpGet(url, requestParams, cookieStr);
//        logger.info("result=\n" + result);

        Document doc = Jsoup.parse(result);
        boolean needReLogin = doc.text().contains("帐号登录");
        if (needReLogin) {
            logger.error("您的cookie过期了//////////，doc=" + doc.text());
            return null;
        }

        //String pageNav = doc.getElementById("papelist").getElementsByTag("span").text();
        //Pattern p = Pattern.compile("(\\d+)条\\s+共(\\d+)页");
        //Matcher matcher = p.matcher(pageNav);
        //String totalStr = null;
        //String pageNumStr = null;
        //int tmp = 0;
        //while (matcher.find()) {
        //    //总条数，总页数
        //    totalStr = matcher.group(1);
        //    pageNumStr = matcher.group(2);
        //    logger.error("解析到相应的总条数=" + totalStr + "，总页数=" + pageNumStr);
        //    tmp++;
        //}
        //if (tmp != 1) {
        //    logger.error("解析出错了哟，没有匹配到相应的总条数，总页数");
        //}

        String pageNumStr = "10";
        //当前页列表
        Element lstBox = doc.getElementsByClass("blog-units-box").get(0);
        List<Article> list = getArticleList(lstBox, graspTime);

        //剩下的页列表
        int pageNum = Integer.parseInt(pageNumStr);
        for (int i = 2; i <= pageNum; i++) {
            //http://blog.csdn.net/hl_java/article/list/2
//            String tmpurl = "http://write.blog.csdn.net/postlist/0/0/enabled/" + i;
            String tmpurl = "http://blog.csdn.net/hl_java/article/list/" + i;
            List<Article> tmplist = postlist(cookieStr, tmpurl, graspTime);
            if(tmplist==null ||tmplist.isEmpty()){
                break;
            }
            //持久化
            list.addAll(tmplist);
        }

        return list;
    }

    //剩下的页列表
    public static List<Article> postlist(String cookieStr, String url, String graspTime) {
        //String url = "http://write.blog.csdn.net/postlist";
        HashMap<String, Object> requestParams = new HashMap<String, Object>();
        String urlEncode = "UTF-8";
        String result = HttpUtil.httpGet(url, requestParams, cookieStr);
        //logger.info("result=\n" + result);
        Document doc = Jsoup.parse(result);
        //当前页列表
        Element lstBox = doc.getElementById("article_list");

        List<Article> list = getArticleList(lstBox, graspTime);

        return list;
    }


    private static List<Article> getArticleList(Element lstBox, String graspTime) {
        List<Article> list = new ArrayList<Article>();

        Elements trs = lstBox.getElementsByClass("blog-unit");
        //忽略列标题
        for (int i = 0; i < trs.size(); i++) {
//            Elements tds = trs.get(i).getElementsByTag("td");
            Element tmpa=trs.get(i).getElementsByClass("blog-title").get(0);
            String title = tmpa.text();
            String csdnLink = trs.get(i).getElementsByTag("a").get(0).attr("href");
            csdnLink="http://blog.csdn.net"+csdnLink;


            String contentDesc=trs.get(i).getElementsByClass("text").get(0).html();
            String tmpCotentType=trs.get(i).getElementsByClass("tag").text();
            int contentType=1;
            if(!tmpCotentType.equals("原创")){
                contentType=2;
            }
            //updateTimeStr格式：2017-10-25 16:02
            String updateTimeStr = trs.get(i).getElementsByClass("control").get(0).getElementsByClass("left-dis-24").get(0).text();
//            updateTimeStr = updateTimeStr.substring(1, updateTimeStr.length() - 1);
            String readCountStr = trs.get(i).getElementsByClass("control").get(0).getElementsByClass("left-dis-24").get(1).text();// 阅读(32)
            int readCount = Integer.parseInt(readCountStr);

            String commentCountStr =trs.get(i).getElementsByClass("control").get(0).getElementsByClass("left-dis-24").get(2).text();
            int commentCount = Integer.parseInt(commentCountStr);
            logger.info("title=" + title + "(" + updateTimeStr + "),readCount=" + readCount + " ,commentCount=" + commentCount + ",csdnLink=" + csdnLink);

            Article a = new Article(title, "", readCount, commentCount, csdnLink, updateTimeStr, graspTime,contentType,contentDesc);
            list.add(a);
        }

        return list;
    }

    public static Article getContent(Article e, String cookieStr) {
        String csdnLink = e.getCsdnLink();

        String url = csdnLink;
        HashMap<String, Object> requestParams = new HashMap<String, Object>();
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        String result = null;
        try {
            result = HttpUtil.httpGet(url, requestParams, headerMap,"UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        Document doc = Jsoup.parse(result);
        boolean needReLogin = doc.text().contains("帐号登录");
        if (needReLogin) {
            logger.error("您的cookie过期了//////////，doc=" + doc.text());
            return null;
        }

        String content = doc.getElementById("article_content").outerHtml();
        logger.info("content=" + content.substring(0, 100));

        content = etl(content);
        e.setContent(content);

        return e;

    }

    public static String etl(String content) {
        //<link rel="stylesheet" href="http://static.blog.csdn.net/public/res-min/markdown_views.css?v=2.0">
        String str = "http\\:\\/\\/static\\.blog\\.csdn\\.net\\/public\\/res-min\\/markdown_views\\.css\\?v=2.0";
        String strReplace = "/static/markdown/markdown_views.css\\?v=2.0";
        content= content.replaceAll(str, strReplace);

        return content;
    }


}

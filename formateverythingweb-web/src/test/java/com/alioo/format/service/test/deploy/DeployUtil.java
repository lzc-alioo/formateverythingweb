package com.alioo.format.service.test.deploy;

import com.alioo.format.domain.Article;
import com.alioo.util.DateTimeUtil;
import com.alioo.util.HttpUtil;
import org.apache.http.Header;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzhichong on 2016/9/8.
 */
public class DeployUtil {
    private static final Logger logger = LoggerFactory.getLogger(DeployUtil.class);

    static ThreadLocal<Header[]> setCookieHeadersThreadLocal = new ThreadLocal<>();

    public static List<Article> postlist(String cookieStr) {
        String graspTime = DateTimeUtil.getDateTime14String();


        String url = "http://blog.csdn.net/hl_java/";
        HashMap<String, Object> requestParams = new HashMap<String, Object>();
        String result = HttpUtil.httpGet(url, requestParams, cookieStr);
        logger.info("result=\n" + result);

        Document doc = Jsoup.parse(result);
        boolean needReLogin = doc.text().contains("帐号登录");
        if (needReLogin) {
            logger.error("您的cookie过期了//////////，doc=" + doc.text());
            return null;
        }

//        String pageNav = doc.getElementById("mainBox").getElementsByTag("span").text();
//        Pattern p = Pattern.compile("(\\d+)条\\s+共(\\d+)页");
//        Matcher matcher = p.matcher(pageNav);
//        String totalStr = null;
//        String pageNumStr = null;
//        int tmp = 0;
//        while (matcher.find()) {
//            //总条数，总页数
//            totalStr = matcher.group(1);
//            pageNumStr = matcher.group(2);
//            logger.error("解析到相应的总条数=" + totalStr + "，总页数=" + pageNumStr);
//            tmp++;
//        }
//        if (tmp != 1) {
//            logger.error("解析出错了哟，没有匹配到相应的总条数，总页数");
//        }
//
//        pageNumStr = "10";

        //当前页列表
        Element lstBox = doc.getElementById("mainBox").getElementsByClass("article-list").get(0);
        List<Article> list = getArticleList(lstBox, graspTime);

        //剩下的页列表
        int pageNum = 10000;
        for (int i = 2; i <= pageNum; i++) {
            //http://blog.csdn.net/hl_java/article/list/2
            String tmpurl = "http://blog.csdn.net/hl_java/article/list/" + i;
            List<Article> tmplist = postlist(cookieStr, tmpurl, graspTime);
            if (tmplist == null || tmplist.isEmpty()) {
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
        if (doc.getElementById("mainBox").getElementsByClass("article-list").size() == 0) {
            return Lists.newArrayList();
        }
        Element lstBox = doc.getElementById("mainBox").getElementsByClass("article-list").get(0);
        List<Article> list = getArticleList(lstBox, graspTime);

        return list;
    }


    private static List<Article> getArticleList(Element lstBox, String graspTime) {
        List<Article> list = new ArrayList<Article>();

        Elements trs = lstBox.getElementsByClass("article-item-box");
        //忽略列标题
        for (int i = 0; i < trs.size(); i++) {
            Element tmpa = trs.get(i).getElementsByTag("a").get(0);
            String title = tmpa.text();
            String csdnLink = trs.get(i).getElementsByTag("a").get(0).attr("href");
//            csdnLink="http://blog.csdn.net"+csdnLink;


            String contentDesc = trs.get(i).getElementsByClass("content").get(0).getElementsByTag("a").text();
            String tmpCotentType = trs.get(i).getElementsByClass("article-type").text();
            int contentType = 1;
            if (!tmpCotentType.equals("原")) {
                contentType = 2;
            }
            String updateTimeStr = trs.get(i).getElementsByClass("info-box").get(0).getElementsByClass("date").get(0).text();
            Date updateTime = DateTimeUtil.toDateFromStr(updateTimeStr, "yyyy-MM-dd HH:mm:ss");
            String readCountStr = trs.get(i).getElementsByClass("info-box").get(0).getElementsByClass("read-num").get(0).text();// 阅读数：13
            int readCount = Integer.parseInt(readCountStr.split("：")[1]);

            String commentCountStr = trs.get(i).getElementsByClass("info-box").get(0).getElementsByClass("read-num").get(1).text();
            int commentCount = Integer.parseInt(commentCountStr.split("：")[1]);
            logger.info("(" + i + ")title=" + title + "(" + updateTimeStr + "),readCount=" + readCount + " ," +
                    "commentCount=" +
                    commentCount + ",csdnLink=" + csdnLink);

            Article a = new Article(title, "", readCount, commentCount, csdnLink, graspTime, contentType, contentDesc, updateTime);
            list.add(a);
        }

        return list;
    }

    public static Article getContent(Article e, String cookieStr) {

        Header[] setCookieHeaders = setCookieHeadersThreadLocal.get();
        if (setCookieHeaders != null && setCookieHeaders.length > 0) {
            String tmp = "";
            for (Header header : setCookieHeaders) {
                tmp += (header.getValue() + ";");
            }
            cookieStr = tmp;
        }

        String csdnLink = e.getCsdnLink();

        String url = csdnLink;
        HashMap<String, Object> requestParams = new HashMap<String, Object>();
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        String result = null;
        try {
            result = HttpUtil.httpGet(url, requestParams, headerMap, "UTF-8");
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
//        logger.info("url="+e.getCsdnLink()+" ,content=" + content.substring(0, 100));

        content = etl(content);
        e.setContent(content);

        Header[] setCookieHeadersNew = (Header[]) headerMap.get("Set-Cookie");
        if (setCookieHeadersNew != null && setCookieHeadersNew.length > 0) {
            setCookieHeaders = setCookieHeadersThreadLocal.get();
            if (setCookieHeaders == null) {
                setCookieHeadersThreadLocal.set(setCookieHeadersNew);
            } else {

                setCookieHeadersNew = concatAll(setCookieHeaders, setCookieHeadersNew);
                setCookieHeadersThreadLocal.set(setCookieHeadersNew);

            }
        }

        return e;

    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {

        int totalLength = first.length;

        for (T[] array : rest) {

            totalLength += array.length;

        }

        T[] result = Arrays.copyOf(first, totalLength);

        int offset = first.length;

        for (T[] array : rest) {

            System.arraycopy(array, 0, result, offset, array.length);

            offset += array.length;

        }

        return result;

    }




    public static String etl(String content) {
        //<link rel="stylesheet" href="http://static.blog.csdn.net/public/res-min/markdown_views.css?v=2.0">
        String str = "http\\:\\/\\/static\\.blog\\.csdn\\.net\\/public\\/res-min\\/markdown_views\\.css\\?v=2.0";
        String strReplace = "/static/markdown/markdown_views.css\\?v=2.0";
        content = content.replaceAll(str, strReplace);

        return content;
    }


}

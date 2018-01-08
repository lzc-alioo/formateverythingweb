package com.lzc.home.article.deploy;

import com.jd.o2o.commons.domain.PageBean;
import com.jd.o2o.commons.utils.DateTimeUtil;
import com.jd.o2o.commons.utils.file.FileUtils;
import com.lzc.home.article.launcher.DeployUtil;
import com.lzc.home.article.launcher.HttpUtil;
import com.lzc.home.dao.ArticleDao;
import com.lzc.home.domain.entity.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzhichong on 2016/9/8.
 */
@Service
public class ShouqiDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(ShouqiDeployServiceImpl.class);


    @Value(value = "${shouqiCookieStr}")
    private String cookieStr;

    private String startDate="2018-01-04";
    private String endDate="2018-01-05";

    public void deploy() {

        String time ="("+endDate+")"+ DateTimeUtil.getDateTime14String();

        List<String> dataList = postlist(cookieStr);

        //awk -F '####'  '{$1="";print $0}' framework-output.log>2324.csv
        //sublime 转带BOM的csv
        //excel 转成excel
        //vlookup
        String rootpath="/export/Logs/f.alioo.online/";

        String path = rootpath + time + ".csv";
        writeFileWithBom(path, dataList);

        String userpath="/Users/alioo/mygithub/formateverythingweb/format-service/src/main/resources/user.data";
//        String userpath="/Users/alioo/mygit/lzc-format/lzc-format-service/src/main/resources/user.data";
//        String userpath="E:\\github\\formateverythingweb\\format-service\\src\\main\\resources\\user.data";
        List<String> userList = readFile(userpath);

        List<String> newList=filter(dataList,userList);

        String newpath =rootpath+ time + "-2"+".csv";
        writeFileWithBom(newpath, newList);

        List<String> newList2=filtertime(newList);

        String newpath2 = rootpath+ time + "-3"+".csv";
        writeFileWithBom(newpath2, newList2);

//        //test
//        List<String> list = new ArrayList<String>();
//        list.add("京Q5T0K9,牟冬亮,点检项'充电'完成点检,2017-12-29 15:31:24\n" +
//                "京Q5T0K9,牟冬亮,车辆'京Q5T0K9'充电完成出场,2017-12-29 15:31:20\n" +
//                "京Q5T0K9,牟冬亮,车辆'京Q5T0K9'完成充电,2017-12-29 15:31:17\n" +
//                "京Q8S2E0,牟冬亮,点检项'充电'完成点检,2017-12-29 15:31:03\n" +
//                "京Q8S2E0,牟冬亮,车辆'京Q8S2E0'充电完成出场,2017-12-29 15:30:54");
//        String newpath2 = "/export/Logs/f.alioo.online/" + time + "-3"+".csv";
//        writeFileWithBom(newpath2,list);

    }

    public  List<String> postlist(String cookieStr) {
        String graspTime = DateTimeUtil.getDateTime14String();


        String url = "http://crm.shouqiev.com/check/getCheckLogList.do";
        HashMap<String, Object> requestParams = new HashMap<String, Object>();

//        pageNum:1
//        itemName:
//        plateNum:
//        checkMemberName:
//        checkAction:
//        checkStartTimeDesc:2017-12-22
//        checkEndTimeDesc:2017-12-23
        requestParams.put("pageNum", "1");
        requestParams.put("itemName", "");
        requestParams.put("plateNum", "");
        requestParams.put("checkMemberName", "");
        requestParams.put("checkAction", "");
        requestParams.put("checkStartTimeDesc", startDate);
        requestParams.put("checkEndTimeDesc", endDate);

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headerMap.put("Referer", "http://crm.shouqiev.com/check/getCheckLogList.do");
        headerMap.put("Upgrade-Insecure-Requests", "1");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Connection", "keep-alive");


        String result = HttpUtil.httpPost(url, requestParams, headerMap, "UTF-8");
//        logger.info("result=\n" + result);

        Document doc = Jsoup.parse(result);
//        boolean needReLogin = doc.text().contains("帐号登录");
//        if (needReLogin) {
//            logger.error("您的cookie过期了//////////，doc=" + doc.text());
//            return null;
//        }
//


        String pageNav = doc.getElementById("datatable1_info").text();
        Pattern p = Pattern.compile("第\\s*1/(\\d+)\\s*页\\s+-\\s*共\\s*(\\d+)\\s*条");

//        String pageNav = doc.getElementById("papelist").getElementsByTag("span").text();
//        Pattern p = Pattern.compile("(\\d+)条\\s+共(\\d+)页");
        Matcher matcher = p.matcher(pageNav);
        String totalStr = null;
        String pageNumStr = null;
        int tmp = 0;
        while (matcher.find()) {
            //总条数，总页数
            pageNumStr = matcher.group(1);
            totalStr = matcher.group(2);
            logger.error("解析到相应的总条数=" + totalStr + "，总页数=" + pageNumStr);
            tmp++;
        }
        if (tmp != 1) {
            logger.error("解析出错了哟，没有匹配到相应的总条数，总页数");
        }

        //当前页列表
        List<String> list = getPageData(1, doc);

        //剩下的页列表
        int pageNum = Integer.parseInt(pageNumStr);
        for (int i = 2; i <= pageNum; i++) {

            url = "http://crm.shouqiev.com/check/getCheckLogList.do";
            requestParams = new HashMap<String, Object>();

//        pageNum:1
//        itemName:
//        plateNum:
//        checkMemberName:
//        checkAction:
//        checkStartTimeDesc:2017-12-22
//        checkEndTimeDesc:2017-12-23
            requestParams.put("pageNum", "" + i);
            requestParams.put("itemName", "");
            requestParams.put("plateNum", "");
            requestParams.put("checkMemberName", "");
            requestParams.put("checkAction", "");
            requestParams.put("checkStartTimeDesc", startDate);
            requestParams.put("checkEndTimeDesc", endDate);

            headerMap = new HashMap<String, Object>();
            headerMap.put("Cookie", cookieStr);
            headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            headerMap.put("Referer", "http://crm.shouqiev.com/check/getCheckLogList.do");
            headerMap.put("Upgrade-Insecure-Requests", "1");
            headerMap.put("Content-Type", "application/x-www-form-urlencoded");
            headerMap.put("Connection", "keep-alive");


            result = HttpUtil.httpPost(url, requestParams, headerMap, "UTF-8");
            doc = Jsoup.parse(result);

            List<String> tmplist = getPageData(i, doc);

            //持久化
            list.addAll(tmplist);
        }

        return list;
    }


    public  List<String> getPageData(int page, Document doc) {
        List<String> dataList = new ArrayList<String>();

        Element dataTable = doc.getElementsByClass("dataTable").get(0);
//        Elements trs=dataTable.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Elements trs = dataTable.getElementsByTag("tr");
        for (int i = 0; i < trs.size(); i++) {
            Element tr = trs.get(i);
            Elements tds = tr.getElementsByTag("td");
            if (tds.size() == 0) {
                continue;
            }
            String car = tds.get(0).text();
            String name = tds.get(2).text();
            String action = tds.get(3).text();
            String createtime = tds.get(4).text();

            String data = car + "," + name + "," + action + "," + createtime;
            if(i==1){
                logger.info("page(" + page + ")####" + data);
            }
            dataList.add(data);
        }

        return dataList;
    }

       List<String>  filter(List<String> datalist,List<String> userlist){
        List<String> newList = new ArrayList<String>();

        for (int i = 0; i < datalist.size(); i++) {
            String data = datalist.get(i);
            String[] tmparr = data.split(",");

            //京QL32K9,张旭,关门,2017-12-25 10:59:52
            String name = tmparr[1];

            boolean flag=false;
            for(String userName:userlist){
                //if(name.indexOf(userName)>-1){
                if(name.equals(userName)||name.equals(userName+"-xhb")){
                    flag=true;
                    break;
                }
            }
            if(flag){
                newList.add(data);

            }
        }
        return newList;

    }

       List<String>  filtertime(List<String> newlist){
        List<String> newList2 = new ArrayList<String>();

        for (int i = 0; i < newlist.size(); i++) {
            String data = newlist.get(i);
            String[] tmparr = data.split(",");

            //京QL32K9,张旭,关门,2017-12-25 10:59:52
            String datetimestr = tmparr[3];
//            String timerstr = datetimestr.split(" ")[1];

            if(datetimestr.compareTo(startDate+" 12:00:00")>=0 && datetimestr.compareTo(endDate+" 12:00:00")<=0){
                newList2.add(data);

            }
        }
        return newList2;

    }


    static void writeFile(String path, List<String> list) {

        try {


            FileWriter fw = new FileWriter(path);
            for (String str : list) {
                fw.write(str);
                fw.write("\n");
            }
            fw.flush();
            fw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFileWithBom( String path, List<String> list) {

        try {
            FileWriter fw = new FileWriter(path);
            fw.write(new String(  new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF})   );

            for (String str : list) {
                fw.write(str);
                fw.write("\n");
            }
            fw.flush();
            fw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static List<String> readFile(String path) {
        List<String> list = new ArrayList<String>();
        try {


            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

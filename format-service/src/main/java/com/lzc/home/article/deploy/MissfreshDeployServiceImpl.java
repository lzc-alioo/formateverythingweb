package com.lzc.home.article.deploy;

import com.jd.o2o.commons.utils.DateTimeUtil;
import com.lzc.home.article.launcher.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 每日优鲜爬虫
 */
@Service
public class MissfreshDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(MissfreshDeployServiceImpl.class);


    private String cookieStr = "smidV2=201803301422227029bc226b1061a64b74f53dcd9bd8210080c901124137ca0; _fmdata=R772MFb8sU2RUTbQx5N38pJriNZxLAQJOQqSRauGkXDAKK8s8vZ4CUczCZ3YJhLy6hBjD44zBOUlU7FIcO4UxGqGRzp%2BG3zUAnXzN5be1g4%3D\n";

    //格式"2018-01-15"
    private String startDate = "2018-01-30";

    //格式"2018-01-16"
    private String endDate = "";

    public void deploy() {
        //startDate为空时设置成昨天
        if (startDate == null || startDate.isEmpty()) {
            startDate = DateTimeUtil.getPreviousDateString(); //"2018-01-15"
        }

        //endDate为空时设置成今天
        if (endDate == null || endDate.isEmpty()) {
            endDate = DateTimeUtil.getDateString(); //"2018-01-16"
        }

        logger.info("startDate=" + startDate);
        logger.info("endDate=" + endDate);

        String time = "(" + endDate + ")" + DateTimeUtil.getDateTime14String();

        List<String> dataList = postlist(cookieStr);

        //awk -F '####'  '{$1="";print $0}' framework-output.log>2324.csv
        //sublime 转带BOM的csv
        //excel 转成excel
        //vlookup
        String rootpath = "/export/Logs/f.alioo.online/";

//        String path = rootpath + time + ".csv";
//        writeFileWithBom(path, dataList);


    }

    public List<String> postlist(String cookieStr) {
        String graspTime = DateTimeUtil.getDateTime14String();


        String url = "http://as-vip.missfresh.cn//v2/product/home/index?device_id=23c452d99005c7dfdabf55d9625a443b&env=web&fromSource=baiduzd01&platform=web&thirdParams=eyJmcm9tU291cmNlIjoiYmFpZHV6ZDAxIn0%3D&uuid=23c452d99005c7dfdabf55d9625a443b&version=4.2.0.1";

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Pragma", "no-cache");
        headerMap.put("Cache-Control", "no-cache");
        headerMap.put("Origin", "https://as-vip.missfresh.cn");
        headerMap.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36");
        headerMap.put("X-Tingyun-Id", "Q1KLryMuSto;r=90960552");
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("Accept", "application/json, text/plain, */*");
        headerMap.put("x-region", "{\"station_code\":\"MRYX|mryx_yzpx\",\"address_code\":110115}");
        headerMap.put("platform", "web");
        headerMap.put("version", "4.2.0.1");
        headerMap.put("Referer", "https://as-vip.missfresh.cn/frontend/");
        headerMap.put("accept-encoding", "gzip, deflate, br");
        headerMap.put("accept", "application/json, text/plain, */*");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headerMap.put("cache-control", "no-cache");
        headerMap.put("Cookie", cookieStr);

//        HashMap<String, Object> requestParams = new HashMap<String, Object>();
//        device_id: 05cfdbecedb09b15246eaaee656ad5f3
//        env: web
//        fromSource: baiduzd01
//        platform: web
//        smsdk_deviceid: WHJMrwNw1k/HfFe3kcuad2iZngBrR7PtTmJClY72g0hKKuGDNvabJxmzfLgrm5+8tRjkxHFcQ6Rwv0VZh3r50JSVKqSieFEm9zzLamCuvf1KJyJqSEtsqlpSD3EmPbcN1kEyzONpVkfGmDd12n5Teoc3ADuAEQjJcAm6Jd4YVl5tkhoMiJ81hY7hUeUMEH8ORuUWofc/w1ZZiNVCcw5ywKGKaMTZxWybt67bpc7R1KfHCWzyB5cvq1bBPL2wXgfTHBYP2DI4NDfEEy5HbCBhxDd4A3puZnmpP1487582755342
//        tdk: eyJ2IjoienJuVzNIYngwVnQwWUVNQkJrZEM3NURmVHBQQ2tPaTM0eGJXZ2ZtZkRDeT0iLCJvcyI6IndlYiIsIml0Ijo0MDE1MywidCI6IkRPaENpYzJkYjhRM0ZCTVZFa0ptSDNJQzFvdmJCNnQySGYwYzR3LzBFaldYM240U0RvZmdCTVhuRkJTM0VqbDV3dHM5dWYwYlNVSnU0SjVIZEY2cE42d1BRMG9iMk5acjRaOXBNaEVCeUNvPSJ9
//        thirdParams: eyJmcm9tU291cmNlIjoiYmFpZHV6ZDAxIn0=
//        uuid: 05cfdbecedb09b15246eaaee656ad5f3
//        version: 4.2.0.1
//        requestParams.put("device_id", "05cfdbecedb09b15246eaaee656ad5f3");
//        requestParams.put("env", "web");
//        requestParams.put("fromSource", "baiduzd01");
//        requestParams.put("platform", "web");
//        requestParams.put("smsdk_deviceid", "WHJMrwNw1k/HfFe3kcuad2iZngBrR7PtTmJClY72g0hKKuGDNvabJxmzfLgrm5+8tRjkxHFcQ6Rwv0VZh3r50JSVKqSieFEm9zzLamCuvf1KJyJqSEtsqlpSD3EmPbcN1kEyzONpVkfGmDd12n5Teoc3ADuAEQjJcAm6Jd4YVl5tkhoMiJ81hY7hUeUMEH8ORuUWofc/w1ZZiNVCcw5ywKGKaMTZxWybt67bpc7R1KfHCWzyB5cvq1bBPL2wXgfTHBYP2DI4NDfEEy5HbCBhxDd4A3puZnmpP1487582755342");
//        requestParams.put("tdk", "eyJ2IjoienJuVzNIYngwVnQwWUVNQkJrZEM3NURmVHBQQ2tPaTM0eGJXZ2ZtZkRDeT0iLCJvcyI6IndlYiIsIml0Ijo0MDE1MywidCI6IkRPaENpYzJkYjhRM0ZCTVZFa0ptSDNJQzFvdmJCNnQySGYwYzR3LzBFaldYM240U0RvZmdCTVhuRkJTM0VqbDV3dHM5dWYwYlNVSnU0SjVIZEY2cE42d1BRMG9iMk5acjRaOXBNaEVCeUNvPSJ9");
//        requestParams.put("thirdParams", "eyJmcm9tU291cmNlIjoiYmFpZHV6ZDAxIn0");
//        requestParams.put("uuid", "05cfdbecedb09b15246eaaee656ad5f3");
//        requestParams.put("version", "4.2.0.1");

        // request body
//        {lat: 39.79506, lng: 116.50951}
//        requestParams.put("lat", "39.79506");
//        requestParams.put("lng", "116.50951");

        String body="{lat: 39.76019, lng: 116.31019} ";

        String result = HttpUtil.httpPostBody(url, headerMap, body, "GBK");
        logger.info("result=\n" + result);

//        Document doc = Jsoup.parse(result);
//        boolean needReLogin = doc.text().contains("帐号登录");
//        if (needReLogin) {
//            logger.error("您的cookie过期了//////////，doc=" + doc.text());
//            return null;
//        }
//

//
//        String pageNav = doc.getElementById("datatable1_info").text();
//        Pattern p = Pattern.compile("第\\s*1/(\\d+)\\s*页\\s+-\\s*共\\s*(\\d+)\\s*条");
//
////        String pageNav = doc.getElementById("papelist").getElementsByTag("span").text();
////        Pattern p = Pattern.compile("(\\d+)条\\s+共(\\d+)页");
//        Matcher matcher = p.matcher(pageNav);
//        String totalStr = null;
//        String pageNumStr = null;
//        int tmp = 0;
//        while (matcher.find()) {
//            //总条数，总页数
//            pageNumStr = matcher.group(1);
//            totalStr = matcher.group(2);
//            logger.error("解析到相应的总条数=" + totalStr + "，总页数=" + pageNumStr);
//            tmp++;
//        }
//        if (tmp != 1) {
//            logger.error("解析出错了哟，没有匹配到相应的总条数，总页数");
//        }

        //当前页列表
//        List<String> list = getPageData(1, doc);
        List<String> list = null;

//        //剩下的页列表
//        int pageNum = Integer.parseInt(pageNumStr);
//        for (int i = 2; i <= pageNum; i++) {
//
//            url = "http://crm.shouqiev.com/check/getCheckLogList.do";
//            requestParams = new HashMap<String, Object>();
//
////        pageNum:1
////        itemName:
////        plateNum:
////        checkMemberName:
////        checkAction:
////        checkStartTimeDesc:2017-12-22
////        checkEndTimeDesc:2017-12-23
//            requestParams.put("pageNum", "" + i);
//            requestParams.put("itemName", "");
//            requestParams.put("plateNum", "");
//            requestParams.put("checkMemberName", "");
//            requestParams.put("checkAction", "");
//            requestParams.put("checkStartTimeDesc", startDate);
//            requestParams.put("checkEndTimeDesc", endDate);
//
//            headerMap = new HashMap<String, Object>();
//            headerMap.put("Cookie", cookieStr);
//            headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
//            headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//            headerMap.put("Referer", "http://crm.shouqiev.com/check/getCheckLogList.do");
//            headerMap.put("Upgrade-Insecure-Requests", "1");
//            headerMap.put("Content-Type", "application/x-www-form-urlencoded");
//            headerMap.put("Connection", "keep-alive");
//
//
//            result = HttpUtil.httpPost(url, requestParams, headerMap, "UTF-8");
//            doc = Jsoup.parse(result);
//
//            List<String> tmplist = getPageData(i, doc);
//
//            //持久化
//            list.addAll(tmplist);
//        }

        return list;
    }


    public List<String> getPageData(int page, Document doc) {
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
            if (i == 1) {
                if (page % 5 == 0) { //减少日志输出
                    logger.info("page(" + page + ")####" + data);
                }
            }
            dataList.add(data);
        }

        return dataList;
    }

    List<String> filter(List<String> datalist, List<String> userlist) {
        List<String> newList = new ArrayList<String>();

        for (int i = 0; i < datalist.size(); i++) {
            String data = datalist.get(i);
            String[] tmparr = data.split(",");

            //京QL32K9,张旭,关门,2017-12-25 10:59:52
            String name = tmparr[1];

            boolean flag = false;
            for (String userName : userlist) {
                //if(name.indexOf(userName)>-1){
                if (name.equals(userName) || name.equals(userName + "-xhb")) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                newList.add(data);

            }
        }
        return newList;

    }

    List<String> filtertime(List<String> newlist) {
        List<String> newList2 = new ArrayList<String>();

        for (int i = 0; i < newlist.size(); i++) {
            String data = newlist.get(i);
            String[] tmparr = data.split(",");

            //京QL32K9,张旭,关门,2017-12-25 10:59:52
            String datetimestr = tmparr[3];
//            String timerstr = datetimestr.split(" ")[1];

            if (datetimestr.compareTo(startDate + " 12:00:00") >= 0 && datetimestr.compareTo(endDate + " 12:00:00") <= 0) {
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

    private void writeFileWithBom(String path, List<String> list) {

        try {
            FileWriter fw = new FileWriter(path);
            fw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));

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

package com.lzc.home.article.deploy;

import com.google.gson.Gson;
import com.jd.o2o.commons.utils.DateTimeUtil;
import com.jd.o2o.commons.utils.json.JsonUtils;
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
 * Created by liuzhichong on 2016/9/8.
 */
@Service
public class RouteDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(RouteDeployServiceImpl.class);


    private String cookieStr=null;

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
//        String rootpath = "/export/Logs/f.alioo.online/";
//
//        String path = rootpath + time + ".csv";
//        writeFileWithBom(path, dataList);


    }

    public List<String> postlist(String cookieStr) {
        String graspTime = DateTimeUtil.getDateTime14String();


        String url = "http://192.168.16.1/";
        HashMap<String, Object> requestParams = new HashMap<String, Object>();

//        requestParams.put("pageNum", "1");
//        requestParams.put("itemName", "");

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("Referer","http://192.168.16.1/");


        String result = HttpUtil.httpGet(url, requestParams, headerMap, "UTF-8");
        logger.info("result=\n" + result);




//        String url2 = "http://192.168.16.1/protocol.csp?fname=system&opt=login&function=set&usrid=f6fdffe48c908deb0f4c3bd36c032e72&math=0.09967855470004516";

//        HashMap<String, Object> requestParams2 = new HashMap<String, Object>();
////        fname: system
////        opt: login
////        function: set
////        usrid: c80274b66697ae04e80190190411cea3
//        requestParams2.put("fname", "system");
//        requestParams2.put("opt", "login");
//        requestParams2.put("function", "set");
//        requestParams2.put("usrid","f6fdffe48c908deb0f4c3bd36c032e72");
//        requestParams2.put("math","0.09967855470004516");
//        String result2 =HttpUtil.httpPost(url2,requestParams2,headerMap,cookieStr);
//        logger.info("result2=\n"+result2);

        String url3 = "http://192.168.16.1/protocol.csp?fname=system&opt=login&function=set&usrid=c80274b66697ae04e80190190411cea3";
        HashMap<String, Object>   requestParams2 = new HashMap<String, Object>();
//        fname: system
//        opt: login
//        function: set
//        usrid: c80274b66697ae04e80190190411cea3
        requestParams2.put("fname", "system");
        requestParams2.put("opt", "login");
        requestParams2.put("function", "set");
        requestParams2.put("usrid","c80274b66697ae04e80190190411cea3");

        String result3 =HttpUtil.httpPost(url3,requestParams2,headerMap,cookieStr);
        logger.info("result3=\n"+result3);

        Gson gson=new Gson();

        Result ret=gson.fromJson(result3,Result.class);

        cookieStr = "lstatus=true; token="+ret.getToken();

        url3 = "http://192.168.16.1/protocol.csp?token="+ret.getToken()+"&fname=system&opt=firmstatus&function=get&flag=local&math=0.6632699207532109";

        requestParams2 = new HashMap<String, Object>();
        requestParams2.put("token", ret.getToken());
        requestParams2.put("fname", "system");
        requestParams2.put("opt", "firmstatus");
        requestParams2.put("function", "get");
        requestParams2.put("flag","local");

        result3 =HttpUtil.httpPost(url3,requestParams2,headerMap, "UTF-8");
        logger.info("result4=\n"+result3);


//        Document doc = Jsoup.parse(result);
//        boolean needReLogin = doc.text().contains("帐号登录");
//        if (needReLogin) {
//            logger.error("您的cookie过期了//////////，doc=" + doc.text());
//            return null;
//        }
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
//
//        //当前页列表
//        List<String> list = getPageData(1, doc);
//
//        //剩下的页列表
//        int pageNum = Integer.parseInt(pageNumStr);

        return null;
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

class Result{
   private String token;
    private int error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}




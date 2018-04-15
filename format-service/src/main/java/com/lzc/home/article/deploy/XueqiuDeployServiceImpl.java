package com.lzc.home.article.deploy;

import com.google.gson.Gson;
import com.jd.o2o.commons.utils.DateTimeUtil;
import com.lzc.home.article.launcher.HttpUtil;
import com.lzc.home.article.launcher.SslUtils;
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
public class XueqiuDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(XueqiuDeployServiceImpl.class);


    @Value(value = "${shouqiCookieStr}")
    private String cookieStr;

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

        //String userpath="/Users/alioo/mygithub/formateverythingweb/format-service/src/main/resources/user.data";


    }

    public List<String> postlist(String cookieStr) {
        String graspTime = DateTimeUtil.getDateTime14String();


        String url = "https://xueqiu.com/stock/cata/stocklist.json?page=1&size=90&order=desc&orderby=percent&type=11%2C12&_=1523111500651";
        HashMap<String, Object> requestParams = new HashMap<String, Object>();

//        page: 1
//        size: 90
//        order: desc
//        orderby: percent
//        type: 11,12
//        _: 1523111500651
        requestParams.put("page", "1");
        requestParams.put("size", "90");
        requestParams.put("order", "desc");
        requestParams.put("orderby", "percent");
        requestParams.put("type", "11,12");
        requestParams.put("_", "1523111500651");

        cookieStr = "aliyungf_tc=AQAAAGk5v3UF1QIA2k1tO17MU0HU412U; xq_a_token=229a3a53d49b5d0078125899e528279b0e54b5fe; xq_a_token.sig=oI-FfEMvVYbAuj7Ho7Z9mPjGjjI; " +
                "xq_r_token=8a43eb9046efe1c0a8437476082dc9aac6db2626; xq_r_token.sig=Efl_JMfn071_BmxcpNvmjMmUP40; u=921523110010183; __utma=1.850499265.1523110010.1523110010.1523110010.1; __utmc=1; __utmz=1.1523110010.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); Hm_lvt_1db88642e346389874251b5a1eded6e3=1523110010; device_id=7fa6cb6cded3733e30b25de4c30df4b0; s=fk11dgpvo7; __utmb=1.24.10.1523110010; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1523111048";

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headerMap.put("Referer", "https://xueqiu.com/hq");
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Connection", "keep-alive");


        String result = HttpUtil.httpGet(url, requestParams, headerMap, "UTF-8");
        logger.info("result=\n" + result);

//        Document doc = Jsoup.parse(result);
//        boolean needReLogin = doc.text().contains("帐号登录");
//        if (needReLogin) {
//            logger.error("您的cookie过期了//////////，doc=" + doc.text());
//            return null;
//        }
//

        Gson gson = new Gson();

        StockPage stockPage = gson.fromJson(result, StockPage.class);
        logger.info("stockPage=" + gson.toJson(stockPage));

//        String pageNav = doc.getElementById("datatable1_info").text();
//        Pattern p = Pattern.compile("第\\s*1/(\\d+)\\s*页\\s+-\\s*共\\s*(\\d+)\\s*条");

//        String pageNav = doc.getElementById("papelist").getElementsByTag("span").text();
//        Pattern p = Pattern.compile("(\\d+)条\\s+共(\\d+)页");
//        Matcher;
//        matcher = p.matcher(pageNav);
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
////            doc = Jsoup.parse(result);
//
//            List<String> tmplist = getPageData(i, doc);
//
//            //持久化
//            list.addAll(tmplist);
//        }

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

class StockPage {
    private String success;

    private CountObj countObj;

    private List<Stock> stocks;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public CountObj getCountObj() {
        return countObj;
    }

    public void setCountObj(CountObj countObj) {
        this.countObj = countObj;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}

class CountObj {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

class Stock {
    private String symbol;
    private String code;
    private String name;
    private String current;
    private String percent;
    private String change;
    private String high;
    private String low;
    private String high52w;
    private String low52w;
    private String marketcapital;
    private String amount;
    private String type;
    private String pettm;
    private String volume;
    private String hasexist;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh52w() {
        return high52w;
    }

    public void setHigh52w(String high52w) {
        this.high52w = high52w;
    }

    public String getLow52w() {
        return low52w;
    }

    public void setLow52w(String low52w) {
        this.low52w = low52w;
    }

    public String getMarketcapital() {
        return marketcapital;
    }

    public void setMarketcapital(String marketcapital) {
        this.marketcapital = marketcapital;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPettm() {
        return pettm;
    }

    public void setPettm(String pettm) {
        this.pettm = pettm;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHasexist() {
        return hasexist;
    }

    public void setHasexist(String hasexist) {
        this.hasexist = hasexist;
    }
}
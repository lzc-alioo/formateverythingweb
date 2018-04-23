package com.lzc.home.article.deploy;

import com.google.gson.Gson;
import com.lzc.home.article.launcher.DateTimeUtil;
import com.lzc.home.article.launcher.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhichong on 2016/9/8.
 */
@Service
public class XueqiuDeployServiceImpl implements DeployService {
    private static final Logger logger = LoggerFactory.getLogger(XueqiuDeployServiceImpl.class);


    @Value(value = "${shouqiCookieStr}")
    private String cookieStr;

    public void deploy() {
        logger.info("开始执行");
        List<Stock> dataList = postlist();

        List<String> formatList = new ArrayList<String>();
        for (Stock s : dataList) {
            formatList.add(s.toString());
        }

        String rootpath = "/export/Logs/f.alioo.online/";
        String fileName = "stock" + DateTimeUtil.getDateString() + ".csv";
        this.writeFileWithBom(rootpath + fileName, formatList);

    }

    public List<Stock> postlist() {
        List<Stock> list = new ArrayList<Stock>();

        int size = 200;
        for (int i = 1; i < size; i++) {
            List<Stock> tmplist = this.getPageData(i);
            if (tmplist == null || tmplist.isEmpty()) {
                break;
            }
            list.addAll(tmplist);
        }

        return list;
    }


    public List<Stock> getPageData(int page) {

        String url = "https://xueqiu.com/stock/cata/stocklist.json?page=" + page + "&size=90&order=desc&orderby=percent&type=11%2C12&_=1524461649806";

        HashMap<String, Object> requestParams = new HashMap<String, Object>();
//        page: 1
//        size: 90
//        order: desc
//        orderby: percent
//        type: 11,12
//        _: 1523111500651
//        requestParams.put("page", "" + page);
//        requestParams.put("size", "90");
//        requestParams.put("order", "desc");
//        requestParams.put("orderby", "percent");
//        requestParams.put("type", "11,12");
//        requestParams.put("_", "1523111500651");

        cookieStr = "aliyungf_tc=AQAAAKza0Hs1nw0AG9oCaknNVBkVA2kd; xq_a_token=0d524219cf0dd2d0a4d48f15e36f37ef9ebcbee1; xq_a_token.sig=P0rdE1K6FJmvC2XfH5vucrIHsnw; xq_r_token=7095ce0c820e0a53c304a6ead234a6c6eca38488; xq_r_token.sig=xBQzKLc4EP4eZvezKxqxXNtB7K0; __utma=1.1606630902.1524461634.1524461634.1524461634.1; __utmc=1; __utmz=1.1524461634.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmt=1; u=901524461634301; device_id=d2ae2cf089446cb0db21b2f013444290; s=ed15km0t3j; __utmb=1.2.10.1524461634";

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

        Gson gson = new Gson();
        StockPage stockPage = gson.fromJson(result, StockPage.class);
        logger.info("i=" + page + ",stockPage=" + gson.toJson(stockPage));

        if (stockPage == null || stockPage.getStocks() == null || stockPage.getStocks().size() == 0) {
            return null;
        }
        logger.info("page=" + page + ",总条数" + stockPage.getCount().getCount());

        //持久化
        List<Stock> list = stockPage.getStocks();
        return list;
    }


    static void writeFile(String path, List<String> list) {

        new File(path).deleteOnExit();

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
//            fw.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));

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

    private Count count;

    private List<Stock> stocks;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }


    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}

class Count {
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

    @Override
    public String toString() {
        return symbol + "," +
                code + "," +
                name + "," +
                current + "," +
                percent + "," +
                change + "," +
                high + "," +
                low + "," +
                high52w + "," +
                low52w + "," +
                marketcapital + "," +
                amount + "," +
                type + "," +
                pettm + "," +
                volume + "," +
                hasexist;

    }
}
package com.lzc.home.article.deploy;

import com.google.gson.Gson;
import com.lzc.home.article.launcher.DateTimeUtil;
import com.lzc.home.article.launcher.HttpUtil;
import com.lzc.home.article.vo.Stock;
import com.lzc.home.article.vo.StockPage;
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


    @Value(value = "${cookieStr}")
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

//        cookieStr = "evice_id=6bebda03f1df19b52b9584413a5a1924; __utmz=1.1526146152.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.301831906.1526146152; s=fo11sg4sxv; aliyungf_tc=AQAAAJzYnyjAjg4AgvIN0hs7aTN4ydF3; xq_a_token=019174f18bf425d22c8e965e48243d9fcfbd2cc0; xq_a_token.sig=_pB0kKy3fV9fvtvkOzxduQTrp7E; xq_r_token=2d465aa5d312fbe8d88b4e7de81e1e915de7989a; xq_r_token.sig=lOCElS5ycgbih9P-Ny3cohQ-FSA; _gid=GA1.2.601592551.1529498187; _gat_gtag_UA_16079156_4=1; u=701529498187452; Hm_lvt_1db88642e346389874251b5a1eded6e3=1527056942,1528439693,1529498187; __utma=1.301831906.1526146152.1528439701.1529498191.4; __utmc=1; __utmt=1; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1529498203; __utmb=1.2.10.1529498191";

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headerMap.put("Referer", "https://xueqiu.com/hq");
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Connection", "keep-alive");


        String result = null;
        try {
            result = HttpUtil.httpGet(url, requestParams, headerMap, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
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


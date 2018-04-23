package com.lzc.home.article.stock;

import com.google.gson.Gson;
import com.lzc.home.article.launcher.DateTimeUtil;
import com.lzc.home.article.launcher.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StockDemo {
    private static final Logger logger = LoggerFactory.getLogger(StockDemo.class);

    static String fileName = "stock" + DateTimeUtil.getDateString() + ".csv";

    public static void main(String[] args) throws ParseException {

        String rootpath = "/export/Logs/f.alioo.online/";

        List<String> stockList = readFile(rootpath + fileName);
        String filterStr = "0028";

//        List<String> newList = filter(stockList, filterStr);

        List<StockStatistic> resultList = new ArrayList<StockStatistic>();
        for (int i = 0; i < stockList.size(); i++) {
            String stockString = stockList.get(i);
            String stockStr = stockString.split(",")[0];
            if (stockStr != null && stockStr.length() > 8) {
                stockStr = stockStr.substring(stockStr.length() - 8);
            }
            logger.info("i=" + i + "stockString=" + stockString);
            StockStatistic stockStatistic = one(stockStr);
            resultList.add(stockStatistic);
        }

        Collections.sort(resultList, new Comparator<StockStatistic>() {
            @Override
            public int compare(StockStatistic o1, StockStatistic o2) {
                if(o1==null&&o2==null){
                    return 0;
                }else if(o1==null&&o2!=null){
                    return -1;
                }else if(o1!=null&&o2==null){
                    return 1;
                }else  {
                    long tmp=o1.getValumeAfterAvg()-o2.getValumeAfterAvg();
                    return (int)tmp;
                }
            }
        });
        fileName= "stock" + DateTimeUtil.getDateString() + "After.csv";
        writeFile(rootpath+fileName,resultList);

        //
        Collections.sort(resultList, new Comparator<StockStatistic>() {
            @Override
            public int compare(StockStatistic o1, StockStatistic o2) {
                if(o1==null&&o2==null){
                    return 0;
                }else if(o1==null&&o2!=null){
                    return -1;
                }else if(o1!=null&&o2==null){
                    return 1;
                }else  {
                    long a=0;
                    long b=0;
                    if(o1.getValumeBeforeAvg()!=0){
                        a=o1.getValumeAfterAvg()/o1.getValumeBeforeAvg();
                    }
                    if(o2.getValumeBeforeAvg()!=0){
                        b=o2.getValumeAfterAvg()/o2.getValumeBeforeAvg();
                    }
                    long tmp=a-b;
                    return (int)tmp;
                }
            }
        });
        fileName= "stock" + DateTimeUtil.getDateString() + "After%Before.csv";
        writeFile(rootpath+fileName,resultList);






    }

    static void writeFile(String path, List list) {

        try {
            FileWriter fw = new FileWriter(path);
            for (Object obj : list) {
                fw.write(obj.toString());
                fw.write("\n");
            }
            fw.flush();
            fw.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static StockStatistic one(String symbol) throws ParseException {
//        String symbol = "SZ300277";
        String result = forchart(symbol);
        Gson gson = new Gson();
        StockChart stockChart = gson.fromJson(result, StockChart.class);
//        "EEE, d MMM yyyy HH:mm:ss Z"	Wed, 4 Jul 2001 12:08:56 -0700
        //"time": "Fri Apr 13 09:30:00 +0800 2018"
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);

        String tmpTime = stockChart.getChartlist().get(0).getTime();
        Date tmpTimeDate = sdf1.parse(tmpTime);
        String dayStr = DateTimeUtil.toDateTimeString(tmpTimeDate, "yyyy-MM-dd");

        long beforeTime = DateTimeUtil.toDate(dayStr, "14:30:00").getTime();

        long volumeBeforeCount = 0;
        long volumeBeforeSum = 0;
        long valumeBeforeAvg = 0;

        long volumeAfterCount = 0;
        long volumeAfterSum = 0;
        long valumeAfterAvg = 0;

        for (StockDetail stockDeail : stockChart.getChartlist()) {
            String time = stockDeail.getTime();
            long longTime = sdf1.parse(time).getTime();
            if (longTime > beforeTime) {
                volumeAfterSum += Long.parseLong(stockDeail.getVolume());
                volumeAfterCount++;
            } else {
                volumeBeforeSum += Long.parseLong(stockDeail.getVolume());
                volumeBeforeCount++;
            }
        }
        if (volumeAfterCount == 0) {
            valumeAfterAvg = 0;
        } else {
            valumeAfterAvg = volumeAfterSum / volumeAfterCount;
        }

        if (volumeAfterCount == 0) {
            valumeBeforeAvg=0;
        } else {
            valumeBeforeAvg = volumeBeforeSum / volumeBeforeCount;
        }

        logger.info("symbol=" + symbol + ",valumeAfterAvg=" + valumeAfterAvg + ",valumeBeforeAvg=" + valumeBeforeAvg);
        StockStatistic stockStatistic = new StockStatistic(symbol, valumeAfterAvg, valumeBeforeAvg);

        return stockStatistic;
    }


    static String forchart(String symbol) {
        String url = "https://xueqiu.com/stock/forchart/stocklist.json?symbol=" + symbol + "&period=1d&one_min=1";

        HashMap<String, Object> requestParams = new HashMap<String, Object>();

        String cookieStr = "aliyungf_tc=AQAAAKza0Hs1nw0AG9oCaknNVBkVA2kd; xq_a_token=0d524219cf0dd2d0a4d48f15e36f37ef9ebcbee1; xq_a_token.sig=P0rdE1K6FJmvC2XfH5vucrIHsnw; xq_r_token=7095ce0c820e0a53c304a6ead234a6c6eca38488; xq_r_token.sig=xBQzKLc4EP4eZvezKxqxXNtB7K0; __utma=1.1606630902.1524461634.1524461634.1524461634.1; __utmc=1; __utmz=1.1524461634.1.1.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmt=1; u=901524461634301; device_id=d2ae2cf089446cb0db21b2f013444290; s=ed15km0t3j; __utmb=1.2.10.1524461634";

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("Cookie", cookieStr);
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headerMap.put("Referer", "https://xueqiu.com/hq");
        headerMap.put("X-Requested-With", "XMLHttpRequest");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("Connection", "keep-alive");

        String result = HttpUtil.httpGet(url, requestParams, headerMap, "UTF-8");
//        logger.info("result=\n" + result);

        return result;

    }


    static List<String> filter(List<String> stockList, String filterStr) {
        List<String> newList = new ArrayList<String>();

        for (int i = 0; i < stockList.size(); i++) {

            String stockString = stockList.get(i);
            String stockStr = stockString.split(",")[0];

            boolean flag = check(stockStr, filterStr);
            if (flag) {
                newList.add(stockString);
            }

        }
        return newList;
    }

    /**
     * 完全匹配则返回true,否则false
     *
     * @param stockstr
     * @param filterStr
     * @return
     */
    static boolean check(String stockstr, String filterStr) {


        for (char filterChar : filterStr.toCharArray()) {

            if (!stockstr.contains("" + filterChar)) {
                return false;
            }
        }

        return true;

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


class StockChart {
    private boolean success;
    private List<StockDetail> chartlist;
    private Stock stock;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<StockDetail> getChartlist() {
        return chartlist;
    }

    public void setChartlist(List<StockDetail> chartlist) {
        this.chartlist = chartlist;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}

class Stock {
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

class StockDetail {

    //"volume": 195400,
    //"avg_price": 10.16,
    //"lot_volume": 1954,
    //"timestamp": 1523583000000,
    //"current": 10.17,
    //"time": "Fri Apr 13 09:30:00 +0800 2018"
    private String volume;
    private String avg_price;
    private String lot_volume;
    private String timestamp;
    private String current;
    private String time;

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getLot_volume() {
        return lot_volume;
    }

    public void setLot_volume(String lot_volume) {
        this.lot_volume = lot_volume;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


class StockStatistic {

    //    logger.info("symbol=" + symbol + ",valumeAfterAvg=" + valumeAfterAvg + ",valumeBeforeAvg=" + valumeBeforeAvg);
    private String symbol;
    private long valumeAfterAvg;
    private long valumeBeforeAvg;

    public StockStatistic(String symbol, long valumeAfterAvg, long valumeBeforeAvg) {
        this.symbol = symbol;
        this.valumeAfterAvg = valumeAfterAvg;
        this.valumeBeforeAvg = valumeBeforeAvg;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getValumeAfterAvg() {
        return valumeAfterAvg;
    }

    public void setValumeAfterAvg(long valumeAfterAvg) {
        this.valumeAfterAvg = valumeAfterAvg;
    }

    public long getValumeBeforeAvg() {
        return valumeBeforeAvg;
    }

    public void setValumeBeforeAvg(long valumeBeforeAvg) {
        this.valumeBeforeAvg = valumeBeforeAvg;
    }

    @Override
    public String toString() {
        return  symbol + "," + valumeAfterAvg + ", " + valumeBeforeAvg ;
    }
}

package com.lzc.home.article.stock;

import com.google.gson.Gson;
import com.lzc.home.article.PropertyPlaceholder;
import com.lzc.home.article.launcher.DateTimeUtil;
import com.lzc.home.article.launcher.HttpUtil;
import com.lzc.home.article.mail.MailUtil;
import com.lzc.home.article.vo.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


public class StockDemo {
    private static final Logger logger = LoggerFactory.getLogger(StockDemo.class);
    private static final String bootPath = StockDemo.class.getName();


    private static String cookieStr = "device_id=6bebda03f1df19b52b9584413a5a1924; __utmz=1.1526146152.1.1.utmcsr=(direct)" +
            "|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.301831906.1526146152; s=fo11sg4sxv; aliyungf_tc=AQAAAJzYnyjAjg4AgvIN0hs7aTN4ydF3; xq_a_token=019174f18bf425d22c8e965e48243d9fcfbd2cc0; xq_a_token.sig=_pB0kKy3fV9fvtvkOzxduQTrp7E; xq_r_token=2d465aa5d312fbe8d88b4e7de81e1e915de7989a; xq_r_token.sig=lOCElS5ycgbih9P-Ny3cohQ-FSA; u=701529498187452; __utmc=1; Hm_lvt_1db88642e346389874251b5a1eded6e3=1528439693,1529498187; _gid=GA1.2.1073051851.1529649167; _gat_gtag_UA_16079156_4=1; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1529649184; __utma=1.301831906.1526146152.1529545676.1529649185.6; __utmt=1; __utmb=1.1.10.1529649185";

    public static void main(String[] args) throws ParseException {

        //重新从配置文件中获取cookieStr
        try{
            new ClassPathXmlApplicationContext(new String[] { "spring-config.xml" });
            logger.info("{}服务已启动", new Object[]{ bootPath });

        }catch(Throwable ex){
            logger.error("服务已启动异常:{}", new Object[]{ ex.getLocalizedMessage(), ex });
        }
        cookieStr= PropertyPlaceholder.getProperty("cookieStr");


        String rootpath = "/export/Logs/f.alioo.online/";

        //纠正文件名
        String fileName = getRightFileName(rootpath);

        List<String> stockList = readFile(rootpath + fileName);

        //过滤 SH6开头 或者  SZ0开头 的股票
        stockList = filter(stockList);

        //将stockList转成map
        HashMap stockMap = formap(stockList);



        long a = System.currentTimeMillis();

        List<StockStatistic> resultList = batch(stockList);

        long b = System.currentTimeMillis();


        Collections.sort(resultList, new Comparator<StockStatistic>() {
            @Override
            public int compare(StockStatistic o1, StockStatistic o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null && o2 != null) {
                    return -1;
                } else if (o1 != null && o2 == null) {
                    return -1;
                } else {
                    long tmp = o1.getValumeAfterAvg() - o2.getValumeAfterAvg();
                    return -(int) tmp;
                }
            }
        });

        //stock2018-05-13-sortbyvalumeAfterAvg.csv  按14:30之后的交易量排名
        String fileName1= fileName.substring(0, fileName.length() - 4) + "-sortbyvalumeAfterAvg.csv";
        writeFile(rootpath + fileName1, resultList);

        //
        Collections.sort(resultList, new Comparator<StockStatistic>() {
            @Override
            public int compare(StockStatistic o1, StockStatistic o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null && o2 != null) {
                    return -1;
                } else if (o1 != null && o2 == null) {
                    return -1;
                } else {
                    long a = 0;
                    long b = 0;
                    if (o1.getValumeBeforeAvg() != 0) {
                        a = o1.getValumeAfterAvg() / o1.getValumeBeforeAvg();
                    }
                    if (o2.getValumeBeforeAvg() != 0) {
                        b = o2.getValumeAfterAvg() / o2.getValumeBeforeAvg();
                    }
                    long tmp = a - b;
                    return -(int) tmp;
                }
            }
        });

        //按14：30之后的交易量与当前之前时间的交易量比例排名
        String fileName2 = fileName.substring(0, fileName.length() - 4) + "-sortbyvalumeAfterAvgRate.csv";
        writeFile(rootpath + fileName2, resultList);


        //增加发邮件的功能 //补全股票名称，所属板块
        sendMail(rootpath, fileName2, (b - a),stockMap);



    }

    private static HashMap formap(List<String> stockList) {
        HashMap map = new HashMap();
        for (String str : stockList) {
            String arr[]=str.split(",");

            Stock stock=new Stock();
            stock.setSymbol(arr[0]);
            stock.setCode(arr[1]);
            stock.setName(arr[2]);
            stock.setCurrent(arr[3]);
            stock.setPercent(arr[4]);
            stock.setChange(arr[5]);
            stock.setHigh(arr[6]);
            stock.setLow(arr[7]);
            stock.setHigh52w(arr[8]);
            stock.setLow52w(arr[9]);
            stock.setMarketcapital(arr[10]);
            stock.setAmount(arr[11]);
            stock.setType(arr[12]);
            stock.setPettm(arr[13]);
            stock.setVolume(arr[14]);
            stock.setHasexist(arr[15]);

            map.put(stock.getSymbol(), stock);
        }


        return map;
    }

    /**
     * 过滤 SH6开头 或者  SZ0开头 的股票
     *
     * @param stockList
     * @return
     */
    private static List<String> filter(List<String> stockList) {
        List<String> retList = new ArrayList<String>();

        for (String str : stockList) {
            if (str.startsWith("SH6") || str.startsWith("SZ0")) {
                retList.add(str);
            }
        }

        return retList;

    }


    private static List<StockStatistic> batch(List<String> stockList) {
        List<StockStatistic> resultList = new ArrayList<StockStatistic>();

        ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20, 0, TimeUnit.DAYS, new
                LinkedBlockingDeque<Runnable>());

        List<Callable<StockStatistic>> callableList = new ArrayList<Callable<StockStatistic>>(stockList.size());
        for (int i = 0; i < stockList.size(); i++) {
            //SZ300116,300116,坚瑞沃能,4.03,10.11,0.37,4.03,3.65,12.64,3.59,9.80307399292E9,5.030432611E8,11,,127262694,false
            String stockString = stockList.get(i);

            //SZ300116
            String stockStr = stockString.split(",")[0];
            if (stockStr != null && stockStr.length() > 8) {
                stockStr = stockStr.substring(stockStr.length() - 8);
            }
            logger.info("i=" + i + "stockString=" + stockString);


            final String finalStockStr = stockStr;
            final int finalI = i;
            Callable<StockStatistic> callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    StockStatistic stockStatistic = null;

                    for (int j = 0; j < 5; j++) {
                        try {
                            stockStatistic = one(finalI, finalStockStr);
                            if (stockStatistic != null) {
                                break;
                            }
                            long sleepTime = 1000L + new Random().nextInt(500);

                        } catch (Exception e) {

                            if (e instanceof SocketException) {
                                long sleepTime = 2000L + new Random().nextInt(2000);
                                Thread.sleep(sleepTime);

                            } else {
                                logger.error("出现异常了(i" + finalI + ")", e);
                            }
                        }

                    }

                    return stockStatistic;
                }
            };
            callableList.add(callable);
        }

        List<Future<StockStatistic>> futureList = null;
        try {
            futureList = pool.invokeAll(callableList);

            for (int j = 0; j < futureList.size(); j++) {
                try {
                    StockStatistic obj = futureList.get(j).get();
                    if (obj != null) {
                        resultList.add(obj);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        pool.shutdown();

//            StockStatistic stockStatistic = null;
//            try {
//                stockStatistic = one(stockStr);
//            } catch (ParseException e) {
//               logger.error("出现异常了(i"+i+")",e);
//            }
//            resultList.add(stockStatistic);

        return resultList;
    }

    private static void sendMail(String rootpath, String fileName, long useTime,HashMap<String,Stock> stockMap) {
        List<String> stockList = readFile(rootpath + fileName);
        String[] to = new String[]{"lzc.java@icloud.com", "lzc_alioo@163.com"};
        String subject = "股票信息#耗时" + useTime + "ms,#"+ fileName ;
        StringBuffer html = new StringBuffer("<html><head></head><body>") ;
        html.append("<table>");
        html.append("<tr>")
                .append("<td>序号</td>")
                .append("<td>symbol</td>")
                .append("<td>当前股价</td>")
                .append("<td>今日涨幅</td>")
                .append("<td>14:30前交易量均值</td>")
                .append("<td>14:30后交易量均值</td>")
            .append("</tr>");


        int len = stockList.size() > 100 ? 100 : stockList.size();

        for (int i = 0; i < len; i++) {
            String arr[]=stockList.get(i).split(",");
            String symbol = arr[0];
            Stock stock = stockMap.get(symbol);

            html.append("<tr>")
                    .append("<td>"+i+"</td>")
                    .append("<td>"+symbol+"</td>")
                    .append("<td>"+stock.getCurrent()+"</td>")
                    .append("<td>"+stock.getPercent()+"</td>")
                    .append("<td>"+arr[1]+"</td>")
                    .append("<td>"+arr[2]+"</td>")
            .append("</tr>");

        }

        html.append("</table>");
        html.append("</body></html>");

        MailUtil.inlineFileMail(to, subject, html.toString(), null);
    }





    static String getRightFileName(String rootpath) {
        String fileName = "";
        int tmp = 0;
        while (tmp < 30) {
//            yyyy-MM-dd
            fileName = "stock" + DateTimeUtil.getNextDateString(-tmp) + ".csv";
            File tmpFile = new File(rootpath + fileName);
            if (tmpFile.exists()) {
                break;
            }

            tmp++;
        }


        return fileName;
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


    static StockStatistic one(int i, String symbol) throws Exception {
//        String symbol = "SZ300277";
        String result = forchart(symbol);
        if (result == null) {
            return null;
        }

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

        if (volumeBeforeCount == 0) {
            valumeBeforeAvg = 0;
        } else {
            valumeBeforeAvg = volumeBeforeSum / volumeBeforeCount;
        }

        logger.info("[" + i + "]symbol=" + symbol + ",valumeAfterAvg=" + valumeAfterAvg + ",valumeBeforeAvg=" + valumeBeforeAvg);
        StockStatistic stockStatistic = new StockStatistic(symbol, valumeAfterAvg, valumeBeforeAvg);

        return stockStatistic;
    }


    static String forchart(String symbol) throws Exception {
        String url = "https://xueqiu.com/stock/forchart/stocklist.json?symbol=" + symbol + "&period=1d&one_min=1";

        HashMap<String, Object> requestParams = new HashMap<String, Object>();

//        String cookieStr = "device_id=6bebda03f1df19b52b9584413a5a1924; __utmz=1.1526146152.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ga=GA1.2.301831906.1526146152; s=fo11sg4sxv; aliyungf_tc=AQAAAJzYnyjAjg4AgvIN0hs7aTN4ydF3; xq_a_token=019174f18bf425d22c8e965e48243d9fcfbd2cc0; xq_a_token.sig=_pB0kKy3fV9fvtvkOzxduQTrp7E; xq_r_token=2d465aa5d312fbe8d88b4e7de81e1e915de7989a; xq_r_token.sig=lOCElS5ycgbih9P-Ny3cohQ-FSA; _gid=GA1.2.601592551.1529498187; _gat_gtag_UA_16079156_4=1; u=701529498187452; Hm_lvt_1db88642e346389874251b5a1eded6e3=1527056942,1528439693,1529498187; __utma=1.301831906.1526146152.1528439701.1529498191.4; __utmc=1; __utmt=1; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1529498203; __utmb=1.2.10.1529498191";

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
    private StockInfo stock;

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

    public StockInfo getStock() {
        return stock;
    }

    public void setStock(StockInfo stock) {
        this.stock = stock;
    }
}

class StockInfo {
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

    DecimalFormat df = new DecimalFormat("######0.00");

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
        double b = -1;
        if (valumeBeforeAvg != 0) {
            b = valumeAfterAvg * 1.0 / valumeBeforeAvg;
        }

        return symbol + ", " + valumeAfterAvg + ", " + valumeBeforeAvg + "," + df.format(b);
    }
}


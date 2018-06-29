package com.lzc.home.article.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StockFilter {
    private static final Logger logger = LoggerFactory.getLogger(StockFilter.class);

    public static void main(String[] args){
        logger.info("StockFilter started...");

        String rootpath = "/export/Logs/f.alioo.online/";

        //String fileName = "stock" + DateTimeUtil.getDateString() + ".csv";
        //纠正文件名
        String fileName = StockAnalysis.getRightFileName(rootpath);

        //stock2018-05-13After.csv
        fileName = fileName.substring(0, fileName.length() - 4) + "After.csv";
        fileName = fileName.substring(0, fileName.length() - 4) + "-Before.csv";

        List<String> stockList = StockAnalysis.readFile(rootpath + fileName);
        String filterStr = "6007";
        List<String> newList = filter(stockList, filterStr);

        fileName = fileName.substring(0, fileName.length() - 4) + "-"+filterStr+".csv";
        StockAnalysis.writeFile(rootpath+fileName,newList);

        logger.info("StockFilter end...");

        try {
            Thread.sleep(1000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static List<String> filter(List<String> stockList, String filterStr) {
        List<String> newList = new ArrayList<String>();

        for (int i = 0; i < stockList.size(); i++) {

            String stockString = stockList.get(i);
            String stockStr = stockString.split(",")[0];

            boolean flag = check(stockStr, filterStr);
            if (flag) {
                newList.add("["+i+"]"+stockString);
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

}

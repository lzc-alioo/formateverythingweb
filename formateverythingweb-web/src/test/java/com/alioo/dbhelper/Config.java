package com.alioo.dbhelper;

/**
 * Created by yujiayuan on 17/11/28.
 */
public class Config {
    public static String url = "jdbc:mysql://127.0.0.1:3317/alioo_dev?useUnicode=true&" +
            "characterEncoding=utf8&autoReconnect=true&&useSSL=false";
    public static String user = "root";
    public static String password = "123456";
    public static String modelBasePackage = "com.alioo.format.domain";
    public static String daoBasePackage = "com.alioo.format.dao";
}

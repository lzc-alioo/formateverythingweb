package com.alioo.format.domain.constant;

import java.util.HashSet;
import java.util.Set;

public class Constant {

    public static String KylinUsernameCookie = "_kylin_username";

    public static String CachePrefix_User = "_santong_user_";



    public static Integer CacheTimeout_User = 60 * 30;//缓存30分钟

    public static final Set<String> AssistFileType = new HashSet(){
        {
            add(".zip");
            add(".7z");
            add(".rar");
            add(".tgz");
            add(".png");
            add(".jpg");
            add(".jpeg");
            add(".gif");
            add(".doc");
            add(".docx");
            add(".xls");
            add(".xlsx");
            add(".ppt");
            add(".pptx");
            add(".pdf");
            add(".xmind");
            add(".msg");
            add(".gz");
            add(".txt");
            add(".rtf");
            add(".csv");
            add(".sql");
            add(".mp4");
            add(".mp3");
            add(".avi");
            add(".mov");
            add(".mpg");
            add(".swf");
            add(".key");
            add(".pages");
            add(".numbers");
        }
    };

}

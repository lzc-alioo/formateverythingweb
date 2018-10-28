package com.alioo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p/>
 * Copyright: Copyright (c) 2018年7月14日 上午9:33:12
 * <p/>
 * Company: 滴滴出行
 * <p/>
 * Author: liuzhichong@alioo.com
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class DateTimeUtil {

    /**
     * 获取两个日期对象相差天数
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return int 日差值
     */
    public static int compareDay(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return 0;

        long time1 = date1.getTime();
        long time2 = date2.getTime();

        long margin = time1 - time2;

        /* 转化成天数 */
        int ret = (int) Math.floor((double) margin / (1000 * 60 * 60 * 24));

        return ret;
    }


    /**
     * 得到指定年月的最后一天.
     *
     * @param monthStr 年月字符串，格式：yyyy-MM
     */
    public static String endDayOfMoth(String monthStr) {
        int day[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int year = Integer.parseInt(monthStr.substring(0, 4));
        int month = Integer.parseInt(monthStr.substring(5));

        int endDay = day[month];

        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                endDay = 29;
            }
        }
        return endDay + "";
    }

    /**
     * 每周的第一天和最后一天
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static String getFirstAndLastOfWeek(Date date, String format) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
//            cal.setTime(new SimpleDateFormat(dateFormat).parse(dataStr));
            int d = 0;
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                d = -6;
            } else {
                d = 2 - cal.get(Calendar.DAY_OF_WEEK);
            }
            cal.add(Calendar.DAY_OF_WEEK, d);
            // 所在周开始日期
            String data1 = new SimpleDateFormat(format).format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 6);
            // 所在周结束日期
            String data2 = new SimpleDateFormat(format).format(cal.getTime());
            return data1 + "_" + data2;
        } catch (Exception e) {
            return null;
        }


    }

    /**
     * 获取当前时间的星期数:星期日=7;星期一=1;星期二=2;星期三=3;星期四=4;星期五=5;星期六=6;
     *
     * @return 周数值
     */
    public static int getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        week = week - 1;
        if (week == 0)
            week = 7;

        return week;
    }

    /**
     * 获取指定日期是星期几<br>
     *
     * @param date 日期
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0) {
            week = 0;
        }
        return weekDays[week];
    }


    /**
     * 取得当前的日期时间字符串YYYY-MM-DD
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateString() {
        String format = "yyyy-MM-dd";
        return getDateTimeString(format);
    }


    /**
     * 取得当前的日期时间字符串YYYYMMDDHHMISS
     *
     * @return String 取得当前的日期时间字符串YYYYMMDDHHMISS
     */
    public static String getDateTime14String() {
        String format = "yyyyMMddHHmmss";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYYMM
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTime6String() {
        String format = "yyyyMM";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYYMMDD
     *
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTime8String() {
        String format = "yyyyMMdd";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串YYYY-MM-DD HH:mm:ss
     *
     * @return String 取得当前的日期时间字符串YYYY-MM-DD HH:mm:ss
     */
    public static String getDateTimeString() {
        String format = "yyyy-MM-dd HH:mm:ss";
        return getDateTimeString(format);
    }

    /**
     * 取得当前的日期时间字符串
     *
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得当前的日期时间字符串
     */
    public static String getDateTimeString(String format) {
        return toDateTimeString(new Date(), format);
    }


    /**
     * 获取后几天对应的当前时间
     *
     * @param format 格式化如 yyyy-MM-dd
     * @return String
     */
    public static String getNextDateString(int days, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 获取指定日期后几天对应的当前时间
     *
     * @param format 格式化如 yyyy-MM-dd
     * @return String
     */
    public static String getNextDateString(Date nowDate, int days, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 获取后几天对应的当前时间
     *
     * @return String 格式化如 yyyy-MM-dd
     */
    public static String getNextDateString(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return toDateString(calendar);
    }


    /**
     * 获取后几小时对应的当前时间
     *
     * @param hours
     * @return String 格式化如 yyyy-MM-dd
     */
    public static String getNextDateStringByHour(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return toDateString(calendar);
    }

    /**
     * 获取后几秒对应的当前时间
     *
     * @param seconds
     * @return String  格式化如 yyyy-MM-dd
     */
    public static String getNextDateStringBySecond(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);

        return toDateString(calendar);
    }



    /**
     * 获取指定日期后几秒对应的当前时间
     *
     * @param startDate
     * @param seconds
     * @return String  格式化如 yyyy-MM-dd
     */
    public static Date getNextDateTimeBySecond(Date startDate, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.SECOND, seconds);

        return calendar.getTime();
    }


    /**
     * 取得当前的日期时间 按默认格式YYYY-MM-DD HH:mm:ss不对则返回null
     *
     * @param datestr 字符串
     * @return 取得当前的日期时间 按默认格式不对则返回null
     */
    public static Date toDateFromStr(String datestr) {
        try {
            String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(datestr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 取得当前的日期时间
     *
     * @param str    字符串
     * @param format 格式
     * @return 取得当前的日期时间 如果格式不对则返回null
     */
    public static Date toDateFromStr(String str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 取得给定日历,给定格式的日期字符串
     *
     * @param calendar 日历,给定一个日历
     * @return String 取得默认的日期时间字符串"yyyy-MM-dd"
     */
    public static String toDateString(Calendar calendar) {
        String format = "yyyy-MM-dd";
        return toDateTimeString(calendar.getTime(), format);
    }


    /**
     * 取得给定日历,给定格式的日期时间字符串
     *
     * @param calendar 日历,给定一个日历
     * @return String 取得默认的日期时间字符串"yyyy-MM-dd HH:mm:ss"
     */
    public static String toDateTimeString(Calendar calendar) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 取得给定日历,给定格式的日期时间字符串
     *
     * @param calendar 日历,给定一个日历
     * @param format   格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定日历,给定格式的日期时间字符串
     */
    public static String toDateTimeString(Calendar calendar, String format) {
        return toDateTimeString(calendar.getTime(), format);
    }

    /**
     * 生成标准格式的字符串 格式为: "MM-DD-YYYY HH:MM:SS"
     *
     * @param date The Date
     * @return 生成默认格式的字符串 格式为: "MM-DD-YYYY HH:MM:SS"
     */
    public static String toDateTimeString(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return toDateTimeString(date, format);
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串
     *
     * @param date   日期,给定一个时间
     * @param format 格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串,标准格式:"yyyy-MM-dd HH:mm:ss";
     *
     * @param datetime 日期,给定一个时间的毫秒数
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(long datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(datetime));
    }

    /**
     * 取得给定时间,给定格式的日期时间字符串
     *
     * @param datetime 日期,给定一个时间的毫秒数
     * @param format   格式,如String format = "yyyy-MM-dd HH:mm:ss";
     * @return String 取得给定时间,给定格式的日期时间字符串
     */
    public static String toDateTimeString(long datetime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(datetime));
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            String datestr = DateTimeUtil.getNextDateString(i);
            Date date = DateTimeUtil.toDateFromStr(datestr+" 00:00:00");
            String str = DateTimeUtil.getFirstAndLastOfWeek(date, "yyyy-MM-dd");
            System.out.println(datestr+"=" + str);
        }

    }
}
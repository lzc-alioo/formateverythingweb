//package com.alioo.util;
//
//import com.alioo.exception.AppExecutionException;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.BeanUtilsBean;
//import org.apache.commons.beanutils.ConvertUtils;
//import org.apache.commons.beanutils.Converter;
//import org.apache.commons.beanutils.converters.*;
//
//import java.lang.reflect.InvocationTargetException;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//
///**
// * copy from lean, created by tianmeng on 2017.09.20
// * Created with IntelliJ IDEA.
// * User: liudi
// * Date: 2018/7/13
// * Time: 下午7:47
// */
//public class CommonBeanUtil extends BeanUtils {
//
//    static {
//        ConvertUtils.register(new DateConvert(), java.util.Date.class);
//        ConvertUtils.register(new DateToStringFormatConvert(), String.class);
//    }
//
//    public static Map<String, String> describe(Object bean) {
//
//        ConvertUtils.register(new DateConvert(), java.util.Date.class);
//        ConvertUtils.register(new DateToStringFormatConvert(), String.class);
//        Map<String, String> map;
//        try {
//            map = BeanUtilsBean.getInstance().describe(bean);
//        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            throw new AppExecutionException("describe error.");
//        }
//        map.remove("class");
//        return map;
//    }
//
//
//    public static void populates(Object bean, Map<String, ? extends Object> properties) {
//
//        ConvertUtils.register(new DateConvert(), java.util.Date.class);
//        ConvertUtils.register(new DateToStringFormatConvert(), String.class);
//        try {
//            BeanUtilsBean.getInstance().populate(bean, properties);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//
//            throw new AppExecutionException("populates error.");
//        }
//    }
//
//    public static void copyProperties(Object dest, Object orig) {
//        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
//        ConvertUtils.register(new LongConverter(null), Long.class);
//        ConvertUtils.register(new ShortConverter(null), Short.class);
//        ConvertUtils.register(new IntegerConverter(null), Integer.class);
//        ConvertUtils.register(new DoubleConverter(null), Double.class);
//        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
//        try {
//            BeanUtilsBean.getInstance().copyProperties(dest, orig);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new AppExecutionException("copyProperties error.");
//        }
//
//    }
//
//    private static class DateToStringFormatConvert implements Converter {
//
//        //private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        @Override
//        public String convert(Class clazz, Object o) {
//            if (o == null) {
//                return null;
//            }
//            if (o instanceof Date) {
//
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                return format.format(o);
//            } else {
//                return o.toString();
//            }
//
//        }
//    }
//
//    private static class DateConvert<T> implements Converter {
//
//        DateConvert() {
//        }
//
//        public DateConvert(String formatPattern) {
//            if (StringUtil.isNotBlank(formatPattern)) {
//                //format = new SimpleDateFormat(formatPattern);
//            }
//        }
//
//
//        @Override
//        public Date convert(Class clazz, Object o) {
//            if (o == null) {
//                return null;
//            }
//            if (o instanceof String) {
//                String str = (String) o;
//                if (StringUtil.isNotEmpty(str)) {
//                    try {
//                        if (str.contains("-")) {
//                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                            return format.parse(str);
//                        }
//                        SimpleDateFormat sdf = new
//                            SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.US);
//                        return sdf.parse(str);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            if (o instanceof Date) {
//                return (Date) o;
//            }
//            return null;
//        }
//    }
//}

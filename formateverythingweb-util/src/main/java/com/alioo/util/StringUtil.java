package com.alioo.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/10
 * Time: 下午2:41
 */
public class StringUtil extends StringUtils {

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    private static final String FILE_FLAGT = "/static/lean";
    private static final String SRC_FLAG = "src";
    private static final String REGEX_CHAR_NUM = "[a-zA-Z0-9-]{1,10}";
    private static final String REGEX_STRING_NUM = "[a-zA-Z0-9_]{1,10}";
    private static final String REGEX_PKEY = "[^a-zA-Z0-9_]";


    public static String subStringByByte(String orignal, int count, String apender) {
        // 原始字符不为null，也不是空字符串
        if (orignal != null && !"".equals(orignal)) {
            // 将原始字符串转换为GBK编码格式
            orignal = new String(orignal.getBytes());
            // 要截取的字节数大于0，且小于原始字符串的字节数
            if (count > 0 && count < orignal.getBytes().length) {
                StringBuffer buff = new StringBuffer();
                char c;
                for (int i = 0; i < count && i < orignal.length(); i++) {
                    c = orignal.charAt(i);
                    buff.append(c);
                    if (String.valueOf(c).getBytes().length > 1) {
                        // 遇到中文汉字，截取字节总数减1
                        --count;
                    }
                }
                return buff.append(apender).toString();
            }
        }
        return orignal;
    }

    public static boolean isIds(String ids) {
        if (isEmpty(ids)) {
            return false;
        }
        return Pattern.matches("(\\d+,)*\\d+", ids);
    }

    /**
     * 将map转换成key1:value1;key2:value2格式的字符串.<br/>
     *
     * @param map
     * @return
     * @author miaohongbin
     * Date:2016年6月21日下午2:39:24
     */
    public static String convertMapToStr(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return "";
        }
        StringBuffer sbStr = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sbStr.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        sbStr.deleteCharAt(sbStr.lastIndexOf(";"));
        return sbStr.toString();
    }

    /**
     * 返回第一个不为空的值
     *
     * @param strs
     * @return
     */
    public static String coalesce(String... strs) {
        for (String str : strs) {
            if (StringUtils.isNotEmpty(str)) {
                return str;
            }
        }
        return null;
    }

    /**
     * 转换为字节数组
     *
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换为字节数组
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++) {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    /**
     * 将字符串转化为String的list
     *
     * @param content
     * @param regex
     * @return
     * @author yujiayuan
     * Date:2016年10月10日上午9:48:50
     */
    public static List<String> getListByString(String content, String regex) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(regex)) {
            return null;
        }
        String[] array = content.split(regex);
        for (String s : array) {
            if (StringUtils.isNotEmpty(s)) {
                result.add(s);
            }
        }
        return result;
    }


    public static List<Integer> getIntegerListByString(String content, String regex) {
        List<Integer> result = new ArrayList<>();
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(regex)) {
            return null;
        }
        String[] array = content.split(regex);
        for (String s : array) {
            if (StringUtils.isNotEmpty(s) && NumberUtils.isNumber(s)) {
                result.add(Integer.parseInt(s));
            }
        }
        return result;
    }

    /**
     * 限制字符串长度
     *
     * @author yujiayuan
     * Date: 16/12/6 下午3:45
     */
    public static String limit(String s, int leagth) {
        if (null == s || s.length() < leagth || leagth < 1) {
            return s;
        }
        return s.substring(0, leagth) + "...";
    }


    public static String justLimit(String s, int leagth) {
        if (null == s || s.length() < leagth || leagth < 1) {
            return s;
        }
        return s.substring(0, leagth);
    }


    /**
     * 截取20个字符，多则加...
     *
     * @param s
     * @return
     */
    public static String limit20(String s) {
        return limit(s, 20);
    }

    /**
     * 截取50个字符，多则加...
     *
     * @param s
     * @return
     */
    public static String limit50(String s) {
        return limit(s, 50);
    }

    /**
     * 去掉所有html标签
     *
     * @author yujiayuan
     * Date: 16/12/6 下午5:10
     */
    public static String removeHtmlLabel(String htmlContent) {
        if (StringUtil.isEmpty(htmlContent)) {
            return htmlContent;
        }
        return htmlContent.replaceAll("</?[^>]+>", "");
    }

    /**
     * check htmlContent whether contain img
     *
     * @param htmlContent
     * @return
     */
    public static Boolean isContainFile(String htmlContent) {
        //static/lean为云图路径文件标识
        if (StringUtil.isNotBlank(htmlContent) && htmlContent.contains(FILE_FLAGT) && htmlContent.contains(SRC_FLAG)) {
            return true;
        }
        return false;
    }

    /**
     * check whether only have [A-z][1-9]-
     *
     * @param key
     * @return
     */
    public static Boolean checkKey(String key) {
        if (StringUtil.isNotBlank(key) && key.matches(REGEX_CHAR_NUM)) {
            return true;
        }
        return false;
    }

    /**
     * check whether only have [A-z][1-9]_
     *
     * @param key
     * @return
     */
    public static Boolean checkpKey(String key) {
        if (StringUtil.isNotBlank(key) && key.matches(REGEX_STRING_NUM)) {
            return true;
        }
        return false;
    }

    /**
     * 替换
     *
     * @param inputStr
     * @param start
     * @param end
     * @param replaceStr
     * @return
     */
    public static String replaceByIndex(String inputStr, int start, int end, String replaceStr) {
        if (StringUtil.isBlank(inputStr)) {
            return null;
        }
        if (start > end || inputStr.length() < end) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inputStr.subSequence(0, start)).append(replaceStr).append(inputStr.subSequence(end + 1,
            inputStr.length()));

        return stringBuilder.toString();
    }


    public static String removeNotMatchNameRule(String str) {
        if (StringUtil.isBlank(str)) {
            return str;
        }
        return str.replaceAll(REGEX_PKEY, "");
    }

    /**
     * 添加超链接
     *
     * @param content
     * @param url
     * @return
     */
    public static String addHyperLink(String content, String url) {
        if (StringUtil.isNotBlank(content) && StringUtil.isNotBlank(url)) {
            content = new StringBuilder().append("<a href=\"").append(url).append("\">").append(content).append("</a>")
                .toString();
        }
        return content;
    }


    /**
     * @param projectIds
     * @param split
     * @return
     */
    public static List<Integer> strToIntList(String projectIds, String split) {
        List<Integer> projectList = new ArrayList<>();
        if (StringUtil.isNotBlank(projectIds)) {
            String[] array = projectIds.split(split);
            if (null != array) {
                for (String arrInt : array) {
                    projectList.add(Integer.parseInt(arrInt));
                }
            }
        }
        return projectList;
    }

    /**
     * @param projectIds
     * @param split
     * @return
     */
    public static List<String> strToStrList(String projectIds, String split) {
        List<String> projectList = new ArrayList<>();
        if (StringUtil.isNotBlank(projectIds)) {
            String[] array = projectIds.split(split);
            Collections.addAll(projectList, array);
        }
        return projectList;
    }

    public static List<Integer> splitToIntList(String str, String separatorChars) {
        String[] strs = split(str, separatorChars);
        List<Integer> intIds = new ArrayList<>();
        for (String s : strs) {
            intIds.add(Integer.valueOf(s));
        }
        return intIds;
    }

    public static String codeTranslate(String content, String oldCode, String newCode) {
        if (isEmpty(content)) {
            return content;
        }
        try {
            return new String(content.getBytes(oldCode), newCode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String urlEncode(String content, String charSet) {
        try {
            return java.net.URLEncoder.encode(content, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * Integer 转 String
     * 当value为NULL时，不作处理直接返回NULL
     *
     * @param value
     * @return
     */
    public static String transByInteger(Integer value) {
        return value == null ? null : String.valueOf(value);
    }

    public static <T> T ifNull(T t, T dfValue) {
        if (null == t) {
            return dfValue;
        }
        return t;
    }

    public static <T> String implode(List<T> list, String delimiter) {
        if (list == null) {
            return null;
        }
        if (delimiter == null) {
            delimiter = ",";
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (T val : list) {
            if (val != null) {
                if (flag) {
                    sb.append(delimiter);
                } else {
                    flag = true;
                }
                sb.append(val.toString());
            }
        }
        return sb.toString();
    }

    /**
     * 生成固定长度随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chr[random.nextInt(36)]);
        }
        return stringBuilder.toString();
    }

}

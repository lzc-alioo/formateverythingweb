package com.alioo.dbhelper;

import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库dao，model生成工具
 *
 * @author yujiayuan
 *         Date: 16/10/23 上午10:38
 */
public class Utils {

    /**
     * 下划线转驼峰
     *
     * @param name
     * @return
     */
    public static String handleName(
            final String name) {
        Pattern pattern = Pattern.compile("_\\w");
        Matcher matcher = pattern.matcher(name);

        String result = name;

        while (matcher.find()) {
            String src = matcher.group();
            result = result.replace(src, src.replace("_", "").toUpperCase());
        }

        return result;
    }

    //下划线转首字母大写驼峰
    public static String handleMethodName(final String name) {
        String tempName = handleName(name);
        if (!StringUtils.isNullOrEmpty(tempName) && tempName.charAt(0) >= 'a' && tempName.charAt(0) <= 'z') {
            return (char) (tempName.charAt(0) - 32) + tempName.substring(1);
        }
        return tempName;

    }


    public static String handleClassName(
            String name) {
        String handleName = handleName(name);
        Pattern pattern = Pattern.compile("\\w");
        Matcher matcher = pattern.matcher(handleName);
        if (matcher.find()) {
            String group = matcher.group();
            return handleName.replaceFirst("\\w", group.toUpperCase());
        }
        return handleName;
    }

    /**
     * 处理数据库类型到java类型
     *
     * @param type
     * @return
     */
    public static String handleType(
            final String type) {
        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }
        String handleType = type;
        handleType = handleType.toLowerCase();
        if (handleType.contains("tinyint(1")) {
            return "boolean ";
        } else if (handleType.contains("int")) {
            return "int ";
        } else if (handleType.contains("timestamp")) {
            return "Date ";
        } else if (handleType.contains("date")) {
            return "Date ";
        } else if (handleType.contains("varchar")) {
            return "String ";
        } else if (handleType.contains("text")) {
            return "String ";
        } else if (handleType.contains("longblob")) {
            return "String ";
        } else if (handleType.contains("float")) {
            return "float ";
        } else if (handleType.contains("double")) {
            return "double ";
        } else if (handleType.contains("decimal")) {
            return "BigDecimal ";
        } else {
            throw new IllegalStateException("Unkown type: " + type);
        }
    }

    public static Statement generateStatement(
            String url,
            String user,
            String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, user, password);

        Statement statement = connection.createStatement();
        return statement;
    }

    public static String tryToGetJavaType(String columnName) {
        if (columnName == null) {
            return "";
        }
        if (columnName.toUpperCase().contains("ID")) {
            return " int ";
        }
        return " String ";
    }
}

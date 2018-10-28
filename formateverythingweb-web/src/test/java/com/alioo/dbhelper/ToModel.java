package com.alioo.dbhelper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yujiayuan on 17/11/28.
 */
public class ToModel {
    private static String url = Config.url;
    private static String user = Config.user;
    private static String password = Config.password;
    private static String modelBasePackage = Config.modelBasePackage;

    public static void main(
            String[] args) throws ClassNotFoundException, SQLException, IOException {

//      init
        File directory = new File("model");
        FileUtils.deleteDirectory(directory);
        FileUtils.forceMkdir(directory);

        Statement statement = Utils.generateStatement(url, user, password);
        try {
            ResultSet rs = statement.executeQuery("show tables");
            while (rs.next()) {
                String tableName = rs.getString(1);
                String modelContent = generateTableClass(" " + tableName + " ");
                modelContent = generateImport(modelContent);

                String modelName = Utils.handleClassName(tableName);
                File file = new File("model/" + modelName + ".java");
                FileUtils.write(file, modelContent, "UTF-8");
                System.out.println(file.toString() + " created ");
            }
        } finally {
            statement.close();
        }


    }

    private static String generateTableClass(
            String tableName) throws ClassNotFoundException, SQLException {
        Statement statement = Utils.generateStatement(url, user, password);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("package ").append(modelBasePackage).append(";\n\n");
            ResultSet rs = statement.executeQuery("show create table " + tableName);
            if (null != rs && rs.next()) {
                String ddl = rs.getString(2);
                int index = ddl.indexOf("COMMENT=");
                if (index >= 0) {
                    ddl = ddl.substring(index + 9, ddl.length() - 1);
                    builder.append("//").append(ddl).append("\n");
                }
            }
            builder.append("public class ");
            builder.append(Utils.handleClassName(tableName));
            builder.append("{\n");
            rs = statement.executeQuery("show full columns from " + tableName);
            StringBuilder getterSetterBuilder = new StringBuilder();
            while (rs.next()) {
                String FIELD = rs.getString("Field");
                String TYPE = rs.getString("Type");
                String COMMENT = rs.getString("Comment");

                String handleType = Utils.handleType(TYPE);
                String handleName = Utils.handleName(FIELD);
                String handleMethodName = Utils.handleMethodName(FIELD);

                builder.append("\n    ");
                builder.append("private ");
                builder.append(handleType);
                builder.append(handleName);
                builder.append("; //").append(COMMENT).append("\n");

                getterSetterBuilder.append("\n    public void set");
                getterSetterBuilder.append(handleMethodName);
                getterSetterBuilder.append("(");
                getterSetterBuilder.append(handleType);
                getterSetterBuilder.append(handleName);
                getterSetterBuilder.append("){\n        this.");
                getterSetterBuilder.append(handleName);
                getterSetterBuilder.append(" = ");
                getterSetterBuilder.append(handleName);
                getterSetterBuilder.append(";");
                getterSetterBuilder.append("\n    }\n");

                getterSetterBuilder.append("    public ");
                getterSetterBuilder.append(handleType);
                getterSetterBuilder.append("get");
                getterSetterBuilder.append(handleMethodName);
                getterSetterBuilder.append("(){\n        ");
                getterSetterBuilder.append("return ");
                getterSetterBuilder.append("this.");
                getterSetterBuilder.append(handleName);
                getterSetterBuilder.append(";\n    }\n");
            }
            builder.append(getterSetterBuilder.toString()).append("\n");
            builder.append("}");
            return builder.toString();
        } finally {
            statement.close();
        }

    }


    private static String generateImport(String modelContent) {
        StringBuilder builder = new StringBuilder(modelContent);
        if (modelContent.indexOf("Date") > -1) {
            int offset = modelContent.split("\\n")[0].length() + 2;

            builder.insert(offset, "import java.util.Date;\n");
        }
        return builder.toString();

    }
}

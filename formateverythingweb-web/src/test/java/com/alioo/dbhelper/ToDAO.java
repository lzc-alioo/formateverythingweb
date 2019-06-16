package com.alioo.dbhelper;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by yujiayuan on 17/11/28.
 */
public class ToDAO {
    private static String url = Config.url;
    private static String user = Config.user;
    private static String password = Config.password;
    private static String modelBasePackage = Config.modelBasePackage;
    private static String daoBasePackage = Config.daoBasePackage;

    public static void main(
            String[] args) throws ClassNotFoundException, SQLException, IOException {

        // init
        File directory = new File("dao");
        FileUtils.deleteDirectory(directory);
        directory.mkdirs();

        Statement statement = Utils.generateStatement(url, user, password);
        try {
            ResultSet rs = statement.executeQuery("show tables");
            while (rs.next()) {
                String tableName = rs.getString(1);

                String daoContent = generateDao(tableName);
                String ClassName = Utils.handleClassName(tableName);

                File file = new File("dao/" + ClassName + "DAO.java");

                FileUtils.write(file, daoContent, "utf-8");

                System.out.println(file.toString() + " created ");
            }
        } finally {
            statement.close();
        }


    }

    private static String generateDao(
            String tableName) throws ClassNotFoundException, SQLException {
        Statement statement = Utils.generateStatement(url, user, password);

        try {
            ResultSet rs = statement.executeQuery("DESCRIBE " + tableName);
            String ClassName = Utils.handleClassName(tableName);
            StringBuilder resultBuilder = new StringBuilder();
            // import
            resultBuilder.append("package ").append(daoBasePackage).append(";\n\n");
            resultBuilder.append("import java.util.List;\n");
            resultBuilder.append("\n");
            resultBuilder.append("import org.apache.ibatis.annotations.*;\n");
            resultBuilder.append("\n");
            resultBuilder.append("import ").append(modelBasePackage).append(".").append(ClassName).append(";\n\n");
            // define
            resultBuilder.append("@Mapper\n");
            resultBuilder.append("public interface ");
            resultBuilder.append(ClassName);
            resultBuilder.append("DAO ");
            resultBuilder.append("{\n");
            List<String> fieldList = new ArrayList<String>();
            Set<String> fieldAuto = new HashSet<String>();
            StringBuilder fieldBuilder = new StringBuilder();
            while (rs.next()) {
                String FIELD = rs.getString("FIELD");
                if (null != rs.getString("EXTRA") && rs.getString("EXTRA").equals("auto_increment")) {
                    fieldAuto.add(FIELD);
                }
                fieldList.add(FIELD);
            }
            for (String field : fieldList) {
                fieldBuilder.append(field);
                fieldBuilder.append(", ");
            }
            String fields = fieldBuilder.toString().replaceAll(", $", "");
            // col all
            resultBuilder.append("    ");
            resultBuilder.append("public static final String COL_ALL = \" ");
            resultBuilder.append(fields);
            resultBuilder.append(" \"; \n");
            // table
            resultBuilder.append("    ");
            resultBuilder.append("public static final String TABLE = \" ");
            resultBuilder.append(tableName);
            resultBuilder.append(" \"; \n");
            // find by id
            resultBuilder.append("\n");
            resultBuilder
                    .append("    @Select(\" select \" + COL_ALL + \" from \" + TABLE + \" where id = #{id} \") \n");
            resultBuilder.append("    public ");
            resultBuilder.append(ClassName);
            resultBuilder.append(" findById(@Param(\"id\")int id); \n");
            // find by page
            resultBuilder.append("\n");
            resultBuilder.append(
                    "    @Select(\" select \" + COL_ALL + \" from \" + TABLE + \" limit #{start}, #{count} \") \n");
            resultBuilder.append("    public ");
            resultBuilder.append("List<" + ClassName + ">");
            resultBuilder.append(" list(@Param(\"start\")int start, @Param(\"count\")int count); \n");
            // find by index
            rs = statement.executeQuery("show index from " + tableName);
            Map<String, List<String>> indexMap = new HashMap<String, List<String>>();
            while (rs.next()) {
                // 索引名称
                String keyName = rs.getString("key_name");
                // key顺序
                int seqInIndex = rs.getInt("seq_in_index");
                // 列名
                String columnName = rs.getString("column_name");

                // 跳过主键
                if (keyName.toUpperCase().equals("PRIMARY")) {
                    continue;
                }

                List<String> indexList = indexMap.get(keyName);
                if (indexList == null) {
                    indexList = new ArrayList<String>();
                    indexMap.put(keyName, indexList);
                }
                indexList.add(columnName);
                indexList.set(seqInIndex - 1, columnName);
            }
            Set<String> indexNameSet = indexMap.keySet();
            for (String indexName : indexNameSet) {
                List<String> indexColumes = indexMap.get(indexName);

                resultBuilder.append("\n");
                resultBuilder.append("    @Select(\" select \" + COL_ALL + \" from \" + TABLE + \" where ");

                if (indexColumes.size() > 0) {
                    String indexColume = indexColumes.get(0);
                    resultBuilder.append("`");
                    resultBuilder.append(indexColume);
                    resultBuilder.append("` = #{");
                    resultBuilder.append(Utils.handleName(indexColume));
                    resultBuilder.append("}\"");
                }

                for (int i = 1; i < indexColumes.size(); i++) {
                    String indexColume = indexColumes.get(i);
                    resultBuilder.append("        + \" and `");
                    resultBuilder.append(indexColume);
                    resultBuilder.append("` = #{");
                    resultBuilder.append(Utils.handleName(indexColume));
                    resultBuilder.append("}\"");

                }

                resultBuilder.append(") \n");
                resultBuilder.append("    public");
                resultBuilder.append(" List<" + ClassName + ">");
                resultBuilder.append(" findByIndex");
                resultBuilder.append(Utils.handleName(indexName).replace("idx", ""));
                resultBuilder.append("(");
                if (indexColumes.size() > 0) {
                    String indexColume = indexColumes.get(0);
                    resultBuilder.append("@Param(\"");
                    resultBuilder.append(Utils.handleName(indexColume));
                    resultBuilder.append("\")");
                    resultBuilder.append(Utils.tryToGetJavaType(indexColume));
                    resultBuilder.append(Utils.handleName(indexColume));
                }

                for (int i = 1; i < indexColumes.size(); i++) {
                    String indexColume = indexColumes.get(0);
                    resultBuilder.append(", @Param(\"");
                    resultBuilder.append(Utils.handleName(indexColume));
                    resultBuilder.append("\")");
                    resultBuilder.append(Utils.tryToGetJavaType(indexColume));
                    resultBuilder.append(Utils.handleName(indexColume));
                }
                resultBuilder.append(");\n");
            }
            // insert
            resultBuilder.append("\n");
            resultBuilder.append("    @Insert(\" insert into \" + TABLE + \" set \"\n");
            for (int i = 0; i < fieldList.size(); i++) {

                String field = fieldList.get(i);
                String javaFieldName = Utils.handleName(field);
                String tail = ", \"\n";
                if (i == fieldList.size() - 1) {
                    tail = "\"";
                }
                if (field.equals("create_time") || field.equals("last_update_time")) {
                    resultBuilder.append("        + \" " + field + " = now()" + tail);
                } else if (!fieldAuto.contains(field)) {
                    resultBuilder.append("        + \" " + field + " = #{" + javaFieldName + "}" + tail);
                }
            }
            resultBuilder.append(")\n");
            resultBuilder.append("    public int insert(");
            resultBuilder.append(ClassName);
            resultBuilder.append(" bean);");

            // insertBatch
            resultBuilder.append("\n\n");
            resultBuilder.append("    @Insert({\"<script>\"+\" insert into \" + TABLE + \" ( \"\n");
            for (int i = 0; i < fieldList.size(); i++) {

                String field = fieldList.get(i);
                String javaFieldName = Utils.handleName(field);
                String tail = ", \"\n";
                if (i == fieldList.size() - 1) {
                    tail = "\"\n";
                }
                if (field.equals("create_time") || field.equals("last_update_time")) {
                    resultBuilder.append("        + \" " + field + tail);
                } else if (!fieldAuto.contains(field)) {
                    resultBuilder.append("        + \" " + field + tail);
                }
            }
            resultBuilder.append("        + \" ) values  \"\n");
//            "<foreach collection='testLists' item='item' index='index' separator=','>",
//                    "(#{item.实体属性1}, #{item.实体属性2}, #{item.实体属性3})",
//                    "</foreach>",

            resultBuilder.append("        + \"<foreach collection='list' item='item' index='index' separator=','>\"\n");
            resultBuilder.append("        + \"(\"\n");
            for (int i = 0; i < fieldList.size(); i++) {
                String field = fieldList.get(i);
                String javaFieldName = Utils.handleName(field);
                String tail = ", \"\n";
                if (i == fieldList.size() - 1) {
                    tail = "\"\n";
                }
                if (field.equals("create_time") || field.equals("last_update_time")) {
                    resultBuilder.append("        + \" " + " #{item." + javaFieldName + "}" + tail);
                } else if (!fieldAuto.contains(field)) {
                    resultBuilder.append("        + \" " + " #{item." + javaFieldName + "}" + tail);
                }
            }
            resultBuilder.append("        + \")\"\n");
            resultBuilder.append("        + \"</foreach>\"\n");
            resultBuilder.append("        + \"</script>\"})\n");
            resultBuilder.append("    public int insertBatch(");
            resultBuilder.append("List<" + ClassName + "> ");
            resultBuilder.append(" list);");


            // update
            resultBuilder.append("\n\n");
            resultBuilder.append("    @Update(\" update \" + TABLE + \" set \"\n");
            for (int i = 0; i < fieldList.size(); i++) {
                String field = fieldList.get(i);
                if (field.equals("id")) {
                    continue;
                }
                String javaFieldName = Utils.handleName(field);
                String tail = ", \"\n";
                if (i == fieldList.size() - 1) {
                    tail = "\"";
                }
                if (field.equals("create_time")) {
                    resultBuilder.append("        + \" " + field + " = now()" + tail);
                } else {
                    resultBuilder.append("        + \" " + field + " = #{" + javaFieldName + "}" + tail);
                }
            }
            resultBuilder.append("\n        + \" where id = #{id} \"");
            resultBuilder.append(")\n");
            resultBuilder.append("    public int update(");
            resultBuilder.append(ClassName);
            resultBuilder.append(" bean);");
            resultBuilder.append("\n}");
            return resultBuilder.toString();
        } finally {
            statement.close();
        }
    }
}

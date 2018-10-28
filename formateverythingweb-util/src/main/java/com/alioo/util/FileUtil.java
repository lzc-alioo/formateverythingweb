package com.alioo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final String TEMP_DIR = "/WEB-INF/notice/";


    /**
     * 读取文件
     * 兼容从目录下读取与jar包中读取两种方式
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {
        String content = null;
        BufferedReader in = null;
        try {
            String lineSeparator = System.getProperty("line.separator", "\n");

            in = new BufferedReader(new InputStreamReader(
                    FileUtil.class.getClassLoader().getResourceAsStream(path)));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line).append(lineSeparator);
            }
            content = buffer.toString();
        } catch (Exception e) {
            logger.error("读取文件["+path+"]时异常", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return content;


    }
    /**
     * 读取文件
     * 兼容从目录下读取与jar包中读取两种方式
     *
     * @param path
     * @return
     */
    public static List<String> readFile2List(String path) {
        List<String> list = new ArrayList<>();
//        String content = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    FileUtil.class.getClassLoader().getResourceAsStream(path)));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
//                buffer.append(line).append(lineSeparator);
                list.add(line);
            }
//            content = buffer.toString();
        } catch (Exception e) {
            logger.error("读取文件["+path+"]时异常", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return list;


    }

    public static String getContextUploadPath(HttpServletRequest request) {
        return getContextUploadPath(request, TEMP_DIR);
    }

    public static String getContextUploadPath(HttpServletRequest request, String dir) {
        String path = request.getSession().getServletContext().getRealPath("/") + dir;
        File pathFolder = new File(path);
        if (!pathFolder.exists()) {
            pathFolder.mkdirs();
        }
        return path;
    }

    public static void deleteFile(File localFile) {
        if (localFile != null && localFile.exists()) {
            localFile.delete();
        }
    }


    public static ResponseEntity<byte[]> download(String fileName, File file) throws IOException {
        String dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        return new ResponseEntity<>(org.apache.commons.io.FileUtils.readFileToByteArray(file), headers, HttpStatus
            .CREATED);
    }


}

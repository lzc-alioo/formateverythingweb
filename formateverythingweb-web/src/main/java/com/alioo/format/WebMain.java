package com.alioo.format;

import com.alioo.format.dao.Md0DAO;
import com.alioo.format.domain.Md0;
import com.alioo.util.FileUtil;
import com.alioo.util.MD5Util;
import com.alioo.util.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration()
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"com.alioo.format", "com.alioo.util.spring"})
public class WebMain {
    private static Logger logger = LoggerFactory.getLogger(WebMain.class);

    private static Md0DAO md0DAO;

    public static void main(String[] args) {
        try {
            SpringApplication.run(WebMain.class, args);

            printLogo();

            logger.info("formateverythingweb-web启动成功...");
        } catch (Throwable e) {
            logger.error("formateverythingweb-web启动异常", e);
            e.printStackTrace();
        }

        md0DAO = SpringUtil.getBean(Md0DAO.class);
        logger.info(md0DAO.toString());

//        List<md0> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            md0 md0=new md0();
//            md0.setMd5B16("111"+i);
//            md0.setMd5B32("222");
//            md0.setData("333");
//            list.add(md0);
//        }
//
//        md0DAO.insertBatch(list);


//        List<String> list = FileUtil.readFile2List("/Users/alioo/Downloads/smzy_ruokonglignzidian/弱口令字典/rkolin1.txt");
//        for (int i = 0; i < list.size(); i++) {
//            List<md0> list2 = new ArrayList<>(1000);
//            String data = list.get(i);
//            String b32 = MD5Util.MD5(data);
//            md0 md0 = new md0();
//            md0.setMd5B16("");
//            md0.setMd5B32(b32);
//            md0.setData(data);
//            list2.add(md0);
//
//            if (list2.size() == 1000) {
//                md0DAO.insertBatch(list2);
//                list2.clear();
//            }
//
//        }

        //排重dataset
        Set<String> dataset = new HashSet<>(10000000);
        loadDataSet(md0DAO, dataset);


        String path = "/Users/alioo/temp/passwd";

        File[] filearr = new File(path).listFiles();
        for (int i = 0; i < filearr.length; i++) {
            logger.info("file==" + filearr[i].getPath());
//        String fileName = "/Users/alioo/Downloads/smzy_ruokonglignzidian/弱口令字典/rkolin3.TXT";
            String fileName = filearr[i].getPath();
            doFile(md0DAO, dataset, fileName);
        }

    }

    /**
     * 打印logo
     */
    private static void printLogo() {
        String logo = FileUtil.readFile("logo");
        logger.info(logo);

    }

    public static void loadDataSet(Md0DAO md0DAO, Set<String> dataset) {

        int start = 0;
        int count = 1000;

        while (true) {
            List<Md0> list = md0DAO.list(start, count);
            if (list == null || list.isEmpty()) {
                break;
            }

            for (Md0 md0 : list) {
                dataset.add(md0.getData());
            }

            start = start + count;

        }
    }

    public static void doFile(Md0DAO md0DAO, Set<String> dataset, String fileName) {
        //read file into stream, try-with-resources
        try {
            AtomicInteger i = new AtomicInteger(0);
            AtomicInteger muti = new AtomicInteger(0);

            List<Md0> list2 = new ArrayList<>(1000);

            Stream<String> stream = Files.lines(Paths.get(fileName), Charset.forName("GBK"));
            stream.forEach(line -> {
//                if (i.get() > 100) {
//                    return;
//                }
//                System.out.println("i=" + i.getAndIncrement() + " " + line);

                i.getAndIncrement();

                String data = line;
                String b32 = MD5Util.MD5(data);
                Md0 md0 = new Md0();
                md0.setB16("");
                md0.setB32(b32);
                md0.setData(data);

                if (dataset.contains(data)) {
                    muti.incrementAndGet();
                    return;
                }
                dataset.add(data);
                list2.add(md0);

                if (list2.size() == 1000) {
                    md0DAO.insertBatch(list2);
                    list2.clear();
                    logger.info("i=" + i.get() + ",muti+" + muti.get() + ",file=" + fileName);
                }

            });

            md0DAO.insertBatch(list2);
            list2.clear();
            logger.info("i=" + i.get() + ",muti+" + muti.get() + ",file=" + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("done..." + fileName);
    }

}

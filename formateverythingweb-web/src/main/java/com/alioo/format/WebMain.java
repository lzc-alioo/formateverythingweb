package com.alioo.format;

import com.alioo.format.dao.Md3DAO;
import com.alioo.format.domain.Md3;
import com.alioo.util.ExchangeUtil;
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

//import com.alioo.format.domain.Md3;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration()
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(value = {"com.alioo.format", "com.alioo.util.spring"})
public class WebMain {
    private static Logger logger = LoggerFactory.getLogger(WebMain.class);

    //    private static Md0DAO md0DAO;
//    private static Md1DAO md1DAO;
//    private static Md3DAO md2DAO;
    private static Md3DAO md3DAO;

    public static void main(String[] args) {
        try {
            SpringApplication.run(WebMain.class, args);

            printLogo();

            logger.info("formateverythingweb-web启动成功...");
        } catch (Throwable e) {
            logger.error("formateverythingweb-web启动异常", e);
            e.printStackTrace();
        }

//        md0DAO = SpringUtil.getBean(Md0DAO.class);
//        md1DAO = SpringUtil.getBean(Md1DAO.class);
        md3DAO = SpringUtil.getBean(Md3DAO.class);

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
        logger.info("排重dataset init ");
        Set<String> dataset = new HashSet<>(10000000);
        loadDataSet(md3DAO, dataset);
        logger.info("排重dataset init done");


        String path = "/Users/alioo/temp/passwd";

        File[] filearr = new File(path).listFiles();
        for (int i = 0; i < filearr.length; i++) {
            String fileName = filearr[i].getPath();
            ExchangeUtil s = new ExchangeUtil();
            String fileCode = ExchangeUtil.javaname[s.detectEncoding(new File(fileName))];

            logger.info("file==" + filearr[i].getPath() + "，fileCode=" + fileCode);
//        String fileName = "/Users/alioo/Downloads/smzy_ruokonglignzidian/弱口令字典/rkolin3.TXT";
            doFile(md3DAO, dataset, fileName, fileCode);


        }

    }

    /**
     * 打印logo
     */
    private static void printLogo() {
        String logo = FileUtil.readFile("logo");
        logger.info(logo);

    }

    public static void loadDataSet(Md3DAO mdDAO, Set<String> dataset) {

        int start = 0;
        int count = 10000;

        while (true) {
            List<Md3> list = mdDAO.list(start, count);
            if (list == null || list.isEmpty()) {
                break;
            }

            for (Md3 md : list) {
                dataset.add(md.getData());
            }

            start = start + count;
            logger.info("load.." + dataset.size());

        }
    }

    public static void doFile(Md3DAO md2DAO, Set<String>  dataset, String fileName, String fileCode) {
        //read file into stream, try-with-resources
        try {
            AtomicInteger i = new AtomicInteger(0);
            AtomicInteger muti = new AtomicInteger(0);

            List<Md3> list2 = new ArrayList<>(1000);

            Stream<String> stream = Files.lines(Paths.get(fileName), Charset.forName(fileCode));
            stream.forEach(line -> {
//                if (i.get() > 100) {
//                    return;
//                }
//                System.out.println("i=" + i.getAndIncrement() + " " + line);

                i.getAndIncrement();

                String data = line;
                String b32 = MD5Util.MD5(data);
                Md3 md = new Md3();
                md.setB16("");
                md.setB32(b32);
                md.setData(data);

//                if (dataset.contains(data)) {
//                    muti.incrementAndGet();
//                    return;
//                }
//                dataset.add(data);
                list2.add(md);

                if (list2.size() == 5000) {
                    try {
                        md2DAO.insertBatch(list2);
                    } catch (Exception e) {
                        logger.info("insert 异常", e);
                    }
                    list2.clear();
                    logger.info("i=" + i.get() + ",muti=" + muti.get() + ",file=" + fileName);
                }

            });

            try {
                md2DAO.insertBatch(list2);
            } catch (Exception e) {
                logger.info("insert 异常", e);
            }

            list2.clear();
            logger.info("i=" + i.get() + ",muti=" + muti.get() + ",file=" + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("done..." + fileName);
    }

}

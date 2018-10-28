package com.alioo.format.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * XXX控制器
 * author: wenjun
 * Time: 2015.03.11 09:47
 */
@Controller
@RequestMapping("/")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    ExecutorService threadPoolExecutor=Executors.newFixedThreadPool(2);

    public IndexController( ) {
        logger.info("构造器IndexController执行"+this);
    }

    @ResponseBody
    @RequestMapping(value = "/testqueue")
    public Object testqueue(final String idx, HttpServletRequest request, final HttpServletResponse response) {
        logger.debug(this+"request /testqueue,idx=" + idx);

        //threadPoolExecutor.execute(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            Thread.sleep(6000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        response.setHeader("header" + idx, idx);
        //        //try {
        //        //    response.flushBuffer();
        //        //} catch (IOException e) {
        //        //    e.printStackTrace();
        //        //}
        //        logger.debug(this+"testqueue,idx=" + idx);
        //    }
        //});
        return idx;
    }



    @ResponseBody
    @RequestMapping(value = "/testproject")
    public Object testproject( HttpServletRequest request, final HttpServletResponse response) {
        logger.debug(this+"request /testproject,idx=" );
        Map<String, Object> map = new HashMap<>();
        map.put("name","liuzchichong");
        map.put("age","23");

        return map;
    }

}

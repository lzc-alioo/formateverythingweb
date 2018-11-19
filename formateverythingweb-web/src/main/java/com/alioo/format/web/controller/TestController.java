package com.alioo.format.web.controller;

import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * XXX控制器
 * author: wenjun
 * Time: 2015.03.11 09:47
 */
@Controller
@RequestMapping("/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(2);

    public TestController() {
        logger.info("构造器IndexController执行" + this);
    }


    @ResponseBody
    @RequestMapping(value = "/list")
    public Object list(HttpServletRequest request, final HttpServletResponse response) {
        logger.debug(this + "request /list,idx=");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", i); //对象不带key的话 react-antd-table 会有警告内容
            map.put("name", "liuzchichong" + i);
            map.put("age", "" + i);
            map.put("address", "西湖区湖底公园2号" + i);

            list.add(map);
        }

        return list;
    }


    @ResponseBody
    @RequestMapping(value = "/list2")
    public Object list2(Integer pageSize, Integer pageNo, HttpServletRequest request, final HttpServletResponse response) {
        logger.debug(this + "request /list2,idx=");

        int count = 43;
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", i);
            map.put("name", "xxxx" + i);
            map.put("age", "" + i);
            map.put("address", "yyyyy" + i);

            list.add(map);
        }
        pageSize = pageSize == null ? 10 : pageSize;
        pageNo = pageNo == null ? 1 : pageNo;


        int start = (pageNo - 1) * pageSize;
        int end = start + pageSize;
//        end = end > list.size() ? list.size() : end;

        list = list.subList(start, end);


        Map<String, Object> retmap = new HashMap<>();
        retmap.put("results", list);
        retmap.put("total", count);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retmap;
    }


    @ResponseBody
    @RequestMapping(value = "/cardList")
    public Object cardList(Integer pageSize, Integer pageNo, HttpServletRequest request, final HttpServletResponse
            response) {
        logger.debug(this + "request /list2,idx=");


//        List<Map<String, Object>> list = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
        int i = (int) (Math.random() * 100);
        Map<String, Object> map = new HashMap<>();
        map.put("setup", "setup" + i);
        map.put("punchline", "punchline" + i);

//            list.add(map);
//        }
//
//        Map<String, Object> retmap = new HashMap<>();
//        retmap.put("data", list);


        return map;
    }


}

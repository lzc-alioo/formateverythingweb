package com.lzc.home.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * XXX控制器
 * author: wenjun
 * Time: 2015.03.11 09:47
 */
@Controller
@RequestMapping("/timer")
public class TimerController {
    private static final Logger logger = LoggerFactory.getLogger(TimerController.class);

    /**
     * 主页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String result = "timer/index";

        logger.info("request timer/index");
        return result;
    }




}

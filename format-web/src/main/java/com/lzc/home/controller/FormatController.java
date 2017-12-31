package com.lzc.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alioo.format.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * XXX控制器
 * author: wenjun
 * Time: 2015.03.11 09:47
 */
@Controller
@RequestMapping("/format")
public class FormatController {
    private static final Logger logger = LoggerFactory.getLogger(FormatController.class);

//    @Resource
//    private FormatServiceFactory formatServiceFactory;

    /**
     * 主页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String result = "format/index";

        logger.info("request format/index");
        return result;
    }

    /**
     * 主页
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/format.json", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> format(HttpServletRequest request, HttpServletResponse response, String source, ModelMap model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String target = null;
        try {
//            String newSource = formatServiceFactory.baseFormat(source);
//            if (newSource != null && newSource.length() > 0) {
//                FormatService formatService = formatServiceFactory.getFormatService(newSource);
//                target = formatService.format(source);
//            }
            target= FormatUtil.format(source);

        } catch (Exception e) {
            logger.error("格式化异常source:" + source, e);
            resultMap.put("success", 0);
            resultMap.put("msg", "格式化异常" + e.getMessage());
            resultMap.put("target", source);
            return resultMap;
        }
        resultMap.put("success", 1);
        resultMap.put("target", target);

        logger.info("格式化成功source:{}###target:{}", source, target);
        return resultMap;
    }


}

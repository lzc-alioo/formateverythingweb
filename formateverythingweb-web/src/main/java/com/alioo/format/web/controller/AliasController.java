package com.alioo.format.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AliasController {

    @RequestMapping(value = {"/swagger", "/sw"}, method = RequestMethod.GET)
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }
}

package com.gigaiot.nlostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cxm on 2017/9/18.
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}

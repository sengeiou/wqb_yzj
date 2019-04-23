package com.wqb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Shoven
 * @since 2019-04-10 17:56
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "/system/index";
    }
}

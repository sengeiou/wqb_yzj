package com.wqb.controller.print;

import com.wqb.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/print")
public class PrintController extends BaseController {
    @RequestMapping("/print")
    public ModelAndView print() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/print");
        return mav;
    }
}

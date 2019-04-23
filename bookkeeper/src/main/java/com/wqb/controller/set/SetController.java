package com.wqb.controller.set;

import com.wqb.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/setTing")
public class SetController extends BaseController {

    @RequestMapping("/list")
    public ModelAndView set() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("system/setTing");
        return mav;
    }
}

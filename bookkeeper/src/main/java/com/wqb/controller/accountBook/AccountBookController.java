package com.wqb.controller.accountBook;

import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/accountBook")
public class AccountBookController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(AccountBookController.class);
    @Autowired
    AccountService accountService;

    @RequestMapping("/queryView")
    public ModelAndView queryView() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("accountBook/list");
        return mav;
    }


}

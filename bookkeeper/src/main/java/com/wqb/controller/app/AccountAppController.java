package com.wqb.controller.app;

import com.wqb.common.Log4jLogger;
import com.wqb.service.app.AccountAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//app
@Controller
@RequestMapping("/accountApp")
public class AccountAppController {
    private static Log4jLogger logger = Log4jLogger.getLogger(AccountAppController.class);
    @Autowired
    AccountAppService accountAppService;


}

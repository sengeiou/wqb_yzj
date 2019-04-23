package com.wqb.controller.jzcl;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.jzcl.JzclService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//反结账处理
@Controller
@RequestMapping("/jzcl")
public class UnJzController extends BaseController {
    @Autowired
    JzclService jzclService;
    @Autowired
    VatService vatService;

    @Autowired
    private UserService userService;

    @RequestMapping("/unJzcl")
    @ResponseBody
    Map<String, Object> unJzcl() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<>();
        Account acc = getAccount();
        String userDate = getUserDate();
        try {
            result = jzclService.unJzcl(user, account, false);
            return result;
        } catch (BusinessException e) {
            vatService.errResetCache(acc, userDate);
            result.put("success", "fail");
            return result;
        } catch (Exception e) {
            vatService.errResetCache(acc, userDate);
            result.put("success", "fail");
            return result;
        }
    }
}

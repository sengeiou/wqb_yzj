package com.wqb.controller.jzcl;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.model.Account;
import com.wqb.service.jzcl.JzclService;
import com.wqb.service.qmjz.QmjzService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

//结账处理
@Controller
@RequestMapping("/jzcl")
public class JzclController extends BaseController {
    @Autowired
    QmjzService qmjzService;
    @Autowired
    JzclService jzclService;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    VatService vatService;

    @RequestMapping("/doJzcl")
    @ResponseBody
    Map<String, Object> doJzcl() {
        Map<String, Object> result = new HashMap<>();
        Account acc = getAccount();
        String userDate = getUserDate();
        try {
            result = jzclService.doJzcl(getSession());
            return result;
        } catch (BusinessException e) {
            vatService.errResetCache(acc, userDate);
            return null;
        } catch (Exception e) {
            vatService.errResetCache(acc, userDate);
            return null;
        }
    }
}

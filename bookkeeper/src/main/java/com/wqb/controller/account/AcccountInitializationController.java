package com.wqb.controller.account;

import com.wqb.controller.BaseController;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.Account;
import com.wqb.service.account.AcccountInitializationService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//账套初始化
@Controller
@RequestMapping("/accInit")
public class AcccountInitializationController extends BaseController {

    @Autowired
    VoucherHeadService vouHeadService;
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    VatService vatService;
    @Autowired
    AcccountInitializationService acccountInitService;
    @Autowired
    VatDao vatDao;

    // 导入科目
    @SuppressWarnings("unused")
    @RequestMapping("/initSub")
    @ResponseBody
    Map<String, Object> initSub() {
        Map<String, Object> result = new HashMap<>();

        try {
            Account acc = getAccount();
            String userDate = getUserDate();
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", getAccount().getAccountID());
            para.put("busDate", getUserDate());


            return null;
        } catch (Exception e) {

            e.printStackTrace();
            result.put("code", "999");
            result.put("msg", e.getMessage());
            return result;
        }
    }


    @SuppressWarnings({"unused", "unchecked"})
    @RequestMapping("/accountReinitialization")
    @ResponseBody
    Map<String, Object> accountReinitialization() {
        Map<String, Object> result = new HashMap<>();

        try {
            HttpSession session = getSession();
            Account acc = getAccount();
            Map<String, Object> res = acccountInitService.accountReinitialization(acc, getUserDate());

            acc.setMappingStates(0);
            acc.setInitialStates(0);

            //更新session 里面的账套数据
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            userDate.put("account", acc);
            session.setAttribute("userDate", userDate);

            return res;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "999");
            result.put("msg", e.getMessage());
            return result;
        }
    }


    @RequestMapping("/systemParameterView")
    ModelAndView systemParameterView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("system/system-parameter");
        Map<String, Object> map = new HashMap<>();
        map.put("code", "huanweiqi");
        modelAndView.addAllObjects(map);
        return modelAndView;
    }


}

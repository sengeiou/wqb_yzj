package com.wqb.controller.BusinessTax;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.service.businessTax.BusinessTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/bus")
public class BusinessTaxController extends BaseController {
    @Autowired
    BusinessTaxService busTaxService;

    @RequestMapping("/carry")
    ModelAndView business() {
        ModelAndView md = new ModelAndView();
        try {
            Map<String, Object> map = getUserInfo();
            busTaxService.queryBusinessTax(map);
            md.setViewName("gd");
            return md;
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md;
    }


    public Map<String, Object> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        String userID = getUser().getUserID(); //用户id
        String userName = getUser().getUserName(); //用户姓名
        String accountID = getAccount().getAccountID(); //账套id
        String account_period = getUserDate(); //账套id
        map.put("accountID", accountID);//账套id
        map.put("userID", userID);
        map.put("userName", userName);
        map.put("period", account_period);
        return map;

    }

}

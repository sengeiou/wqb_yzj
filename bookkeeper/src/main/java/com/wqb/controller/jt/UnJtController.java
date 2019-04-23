package com.wqb.controller.jt;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Account;
import com.wqb.model.Progress;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.qmjz.QmjzService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//期末反计提
@Controller
@RequestMapping("/unJt")
public class UnJtController extends BaseController {
    @Autowired
    QmjzService qmjzService;
    @Autowired
    VatService vatService;
    @Autowired
    ProgressDao progressDao;
    @Autowired
    private UserService userService;

    @RequestMapping("/doUnJt")
    @ResponseBody
    Map<String, Object> unQmjz() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        Account acc = getAccount();
        String userDate = getUserDate();
        try {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", getAccount().getAccountID());
            para.put("busDate", getUserDate());
            para.put("period", getUserDate());
            para.put("unJt", 1);
            List<Progress> proList = progressDao.queryProgress(para);
            if (proList != null && proList.size() == 1) {
                if (proList.get(0).getUnJt() == 1) {
                    result.put("code", "-1");
                    result.put("success", "fail");
                    result.put("info", "反计提正在进行中...");
                    return result;
                }
            }
            progressDao.chgProgress(para);
            result = qmjzService.unQmjz(user, account);
        } catch (BusinessException e) {
            vatService.errResetCache(acc, userDate);
            result.put("success", "false");
            result.put("info", "反计提失败!");
            return result;
        } catch (Exception e) {
            vatService.errResetCache(acc, userDate);
            result.put("success", "true");
            return result;
        } finally {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", getAccount().getAccountID());
            param.put("period", getUserDate());
            param.put("busDate", getUserDate());
            param.put("unJt", 0);
            try {
                progressDao.chgProgress(param);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

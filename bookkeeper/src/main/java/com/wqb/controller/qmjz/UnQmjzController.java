package com.wqb.controller.qmjz;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Account;
import com.wqb.model.Progress;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.detection.TaxRiskDetectionService;
import com.wqb.service.jzcl.JzclService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//反结转处理
@Controller
@RequestMapping("/unQmjz")
public class UnQmjzController extends BaseController {
    @Autowired
    JzclService jzclService;
    @Autowired
    VatService vatService;
    @Autowired
    ProgressDao progressDao;

    @Autowired
    TaxRiskDetectionService taxRiskDetectionService;

    @Autowired
    private UserService userService;

    @RequestMapping("/doUnQmjz")
    @ResponseBody
    Map<String, Object> unJzcl() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<>();
        Account acc = getAccount();
        String userDate = getUserDate();
        try {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", getAccount().getAccountID());
            para.put("busDate", getUserDate());
            para.put("period", getUserDate());
            para.put("unCarryState", 1);
            List<Progress> proList = progressDao.queryProgress(para);
            if (proList != null && proList.size() == 1) {
                if (proList.get(0).getUnCarryState() == 1) {
                    result.put("code", "-1");
                    result.put("success", "fail");
                    result.put("info", "反结转正在进行中...");
                    return result;
                }
            }
            progressDao.chgProgress(para);
            // 检查失败之后反结账后检查状态更改为0
            taxRiskDetectionService.updateDetection(user, account, 0);

            // 处理反结转逻辑
            result = jzclService.unJzcl(user, account, true);
        } catch (BusinessException e) {
            vatService.errResetCache(acc, userDate);
            result.put("success", "fail");
            result.put("message", "反结转失败");
            return result;
        } catch (Exception e) {
            vatService.errResetCache(acc, userDate);
            result.put("success", "fail");
            result.put("message", "反结转失败");
            return result;
        } finally {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", getAccount().getAccountID());
            param.put("period", getUserDate());
            param.put("busDate", getUserDate());
            param.put("unCarryState", 0);
            try {
                progressDao.chgProgress(param);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        result.put("success", "true");
        return result;
    }
}

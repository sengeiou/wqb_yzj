package com.wqb.controller.jt;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Account;
import com.wqb.model.Progress;
import com.wqb.model.StatusPeriod;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.qmjz.QmjzService;
import com.wqb.service.simallBusiness.SimallBusinessQMJZService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//计提
@Controller
@RequestMapping("/jt")
public class JtController extends BaseController {
    @Autowired
    QmjzService qmjzService;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    VoucherHeadService vouHeadService;

    @Autowired
    PeriodStatusService periodStatusService;

    @Autowired
    SimallBusinessQMJZService simallBusinessQMJZService; // 小规模计提
    @Autowired
    ProgressDao progressDao;
    @Autowired
    VatService vatService;

    @Autowired
    private UserService userService;

    // 一键计提操作
    @SuppressWarnings({"unchecked", "unused"})
    @RequestMapping("/oneKeyJt")
    @ResponseBody
    Map<String, Object> doQmjz() {
        Map<String, Object> result = new HashMap<>();
        Account acc = getAccount();
        String userDate = getUserDate();
        try {
            User user = userService.getCurrentUser();
            Account account = userService.getCurrentAccount(user);
            String period = account.getUseLastPeriod();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            param.put("userID", user.getUserID());
            param.put("user_id", user.getUserID());
            param.put("period", period);
            param.put("busDate", period);
            param.put("userName", getUser().getUserName());

            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", account.getAccountID());
            para.put("busDate", period);

            param.put("jt", 1);
            List<Progress> proList = progressDao.queryProgress(param);
            if (proList != null && proList.size() == 1) {
                if (proList.get(0).getJt() == 1) {
                    result.put("code", "-1");
                    result.put("success", "fail");
                    result.put("info", "一键计提正在进行中...");
                    return result;
                }
            }
            progressDao.chgProgress(param);
            List<StatusPeriod> statuList = periodStatusService.queryStatus(para);
            if (!statuList.isEmpty() && statuList.size() == 1) {
                StatusPeriod sp = statuList.get(0);
                if (null != statuList && statuList.size() > 0) {
                    if (sp.getIsJt() == 1) {
                        result.put("code", -1);
                        result.put("success", "fail");
                        result.put("info", "当前会计期间已一键计提,请按F5刷新界面查看最新进度");
                        result.put("msg", "当前会计期间已一键计提,请按F5刷新界面查看最新进度");
                        return result;
                    }
                }
                if (sp.getIsCreateVoucher() == 0) {
                    result.put("code", -1);
                    result.put("msg", "请先一键生成凭证，再计提。。。");
                    result.put("success", "fail");
                    result.put("info", "请先一键生成凭证，再计提。。。");
                    return result;
                }
                if (sp.getIsJt() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经计提，请勿再次计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经计提，请勿再次计提。。。");
                    return result;
                }
                if (sp.getIsCarryState() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结转，不能计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结转，不能计提。。。");
                    return result;
                }
                Integer isDetection = sp.getIsDetection();
                if (isDetection == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经检查通过，不能计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经检查通过，不能计提。。。");
                    return result;
                }
                // 是否结账(0：未结账1:已结账)
                if (sp.getIsJz() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结账，不能计提。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结账，不能计提。。。");
                    return result;
                }
            }

            vatService.errResetCache(acc, userDate);

            Map<String, Object> rs = new HashMap<String, Object>();
            if (account.getSsType() == 1) {
                rs = simallBusinessQMJZService.simallBusinessQMJZ(param, user, account);
            } else {
                rs = qmjzService.qmjz(param, user, account);
            }
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "fail");
            result.put("info", e.getMessage());
            return result;
        } finally {
            Map<String, Object> progressData = new HashMap<String, Object>();
            progressData.put("accountID", getAccount().getAccountID());
            progressData.put("period", getUserDate());
            progressData.put("jt", 0);
            try {
                progressDao.chgProgress(progressData);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
    }
}

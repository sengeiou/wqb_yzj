package com.wqb.controller.qmjz;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Account;
import com.wqb.model.Progress;
import com.wqb.model.StatusPeriod;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.jzqnjlr.JzqnjlrService;
import com.wqb.service.jzsy.JzsyService;
import com.wqb.service.periodStatus.PeriodStatusService;
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

@Controller
// 一键结转
@RequestMapping("/qmjz")
public class QmjzController extends BaseController {
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    JzsyService jzsyService;
    @Autowired
    PeriodStatusDao periodStatusDao;
    @Autowired
    JzqnjlrService jzqnjlrService;
    @Autowired
    VoucherHeadService vouHeadService;
    @Autowired
    ProgressDao progressDao;
    @Autowired
    VatService vatService;
    @Autowired
    private UserService userService;

    @RequestMapping("/doQmjz")
    @ResponseBody
    public Map<String, Object> qmjz() {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Account acc = getAccount();
        String userDate = account.getUseLastPeriod();
        try {
            String accountID = account.getAccountID();
            Map<String, Object> para1 = new HashMap<String, Object>();
            para1.put("accountID", accountID);
            para1.put("busDate", userDate);

            para1.put("carryState", 1);
            para1.put("period", userDate);
            List<Progress> proList = progressDao.queryProgress(para1);
            if (proList != null && proList.size() == 1) {
                if (proList.get(0).getCarryState() == 1) {
                    result.put("code", -1);
                    result.put("success", "fail");
                    result.put("info", "一键结转正在进行中...");
                    return result;
                }
            }
            progressDao.chgProgress(para1);
            List<StatusPeriod> statuList = periodStatusService.queryStatus(para1);
            if (!statuList.isEmpty() && statuList.size() == 1) {
                StatusPeriod sp = statuList.get(0);

                if (sp.getIsCarryState() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结转，不能再次结转。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结转，不能再次结转。。。");
                    return result;
                }

                if (sp.getIsCreateVoucher() == 0) {
                    result.put("code", -1);
                    result.put("msg", "结转失败，请先一键生成凭证。。。");
                    result.put("success", "fail");
                    result.put("info", "结转失败，请先一键生成凭证。。。");
                    return result;
                }

                if (sp.getIsJt() == 0) {
                    result.put("code", -1);
                    result.put("msg", "结转失败，请先计提。。。");
                    result.put("success", "fail");
                    result.put("info", "结转失败，请先计提。。。");
                    return result;
                }

                Integer isDetection = sp.getIsDetection();
                if (isDetection == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经检查通过，不能再结转。。。");
                    result.put("success", "fail");
                    result.put("info", "已经检查通过，不能再结转。。。");
                    return result;
                }

                if (sp.getIsJz() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结账，不能再结转。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结账，不能再结转。。。");
                    return result;
                }

                // 七：计提企业所得税
                // vatService.jtCarryover(pa);
                // 八、将本月收入结转到本年利润。(结转损溢)
                jzsyService.doJzsy(user, account);
                // 九、结转全年净利润。 
                jzqnjlrService.doJzqnjlr(user, account);

                // 一键结转操作
                Map<String, Object> param = new HashMap<>();
                param.put("period", getUserDate());
                param.put("accountID", accountID);

                vouHeadService.updatevoucherNo(param);

                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("accountID", account.getAccountID());
                pa.put("busDate", getUserDate());
                periodStatusDao.updStatu2(pa);
            }

        } catch (Exception e) {
            result.put("success", "false");
            return result;
        } finally {
            vatService.errResetCache(acc, userDate);
            Map<String, Object> progressData = new HashMap<String, Object>();
            progressData.put("accountID", getAccount().getAccountID());
            progressData.put("period", getUserDate());
            progressData.put("carryState", 0);
            try {
                progressDao.chgProgress(progressData);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        result.put("success", "true");
        result.put("message", "一键结转成功");
        return result;
    }

}

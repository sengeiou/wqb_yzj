package com.wqb.controller.checkout;

import com.wqb.common.BusinessException;
import com.wqb.controller.BaseController;
import com.wqb.dao.pay.DzPayDao;
import com.wqb.dao.pay.JzAmountDao;
import com.wqb.dao.pay.JzRecordDao;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.dao.user.UserDao;
import com.wqb.model.*;
import com.wqb.service.UserService;
import com.wqb.service.jzqnjlr.JzqnjlrService;
import com.wqb.service.jzsy.JzsyService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.report.TBasicBalanceSheetService;
import com.wqb.service.report.TBasicIncomeStatementService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.vat.VatService;
import com.wqb.service.voucher.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: CheckoutController
 * @Description: 结账处理Controller
 * @date 2018年1月24日 下午6:48:51
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController extends BaseController {
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    TBasicBalanceSheetService tBasicBalanceSheetService; // 资产负债表

    @Autowired
    TBasicIncomeStatementService tBasicIncomeStatementService; // 利润表

    @Autowired
    JzsyService jzsyService;
    @Autowired
    JzqnjlrService jzqnjlrService;
    @Autowired
    VoucherService voucherService;
    @Autowired
    VatService vatService;
    @Autowired
    DzPayDao dzPayDao;
    @Autowired
    UserDao userDao;
    @Autowired
    PeriodStatusService periodStatusService;

    @Autowired
    JzAmountDao jzAmountDao;
    @Autowired
    JzRecordDao jzRecordDao;

    @Autowired
    ProgressDao progressDao;

    @Autowired
    private UserService userService;

    /**
     * @return Map<String, Object> 返回类型
     * @Title: Checkout
     * @Description: 结账处理
     * @date 2018年1月24日 下午6:50:29
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/settleAccounts")
    @ResponseBody
    public Map<String, Object> checkout() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        try {

            HttpSession session = getSession();

            Map<String, Object> querySubMessage;

            final Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("accountID", getAccount().getAccountID());
            pa.put("userID", getUser().getUserID());
            pa.put("user_id", getUser().getUserID());
            pa.put("period", getUserDate());
            pa.put("busDate", getUserDate());
            pa.put("userName", getUser().getUserName());

            Map<String, Object> para1 = new HashMap<String, Object>();
            para1.put("accountID", getAccount().getAccountID());
            para1.put("busDate", getUserDate());
            para1.put("jz", 1);
            para1.put("period", getUserDate());
            List<Progress> proList = progressDao.queryProgress(para1);
            if (proList != null && proList.size() == 1) {
                if (proList.get(0).getJz() == 1) {
                    result.put("code", -1);
                    result.put("success", "fail");
                    result.put("info", "结账正在进行中...");
                    return result;
                }
            }
            progressDao.chgProgress(para1);

            List<StatusPeriod> statuList = periodStatusService.queryStatus(para1);
            if (!statuList.isEmpty() && statuList.size() == 1) {
                StatusPeriod sp = statuList.get(0);

                // `isJz` int(2) DEFAULT '0' COMMENT '是否结账(0：未结账1:已结账)',
                if (sp.getIsJz() == 1) {
                    result.put("code", -1);
                    result.put("msg", "已经结账，不能再次结账。。。");
                    result.put("success", "fail");
                    result.put("info", "已经结账，不能再次结账。。。");
                    return result;
                }

                // `isCreateVoucher` int(2) DEFAULT NULL COMMENT '是否已一键生成凭证（0：未
                // 1：已生成）',
                if (sp.getIsCreateVoucher() == 0) {
                    result.put("code", -1);
                    result.put("msg", "结账失败，请先一键生成凭证。。。");
                    result.put("success", "fail");
                    result.put("info", "结账失败，请先一键生成凭证。。。");
                    return result;
                }
                // `isCarryState` int(4) DEFAULT NULL COMMENT '是否结转（0否1是）',
                if (sp.getIsCarryState() == 0) {
                    result.put("code", -1);
                    result.put("msg", "结账失败，请先结转。。。");
                    result.put("success", "fail");
                    result.put("info", "结账失败，请先结转。。。");
                    return result;
                }
                // `isCheck` int(2) unsigned DEFAULT '0' COMMENT '0:未审核1:审核',
                /*
                 * if(sp.getIsCheck() == 0) { result.put("code", -1);
                 * result.put("msg", "结账失败，请先审核。。。"); result.put("success",
                 * "fail"); result.put("info", "结账失败，请先审核。。。"); return result; }
                 */
                // 是否检查通过（0否1是）
                Integer isDetection = sp.getIsDetection();
                if (isDetection == 0) {
                    result.put("code", -1);
                    result.put("msg", "结账失败，请先检测。。。");
                    result.put("success", "fail");
                    result.put("info", "结账失败，请先检测。。。");
                    return result;
                }
            }

            // 七：计提企业所得税
            // vatService.jtCarryover(pa);
            // 八、将本月收入结转到本年利润。(结转损溢)
            // jzsyService.doJzsy(session);
            // 九、结转全年净利润。 
            // jzqnjlrService.doJzqnjlr(session);

            // 十 数量金额表期间转移
            vatService.toNextKcCommPeriod(pa);

            Map<String, Object> param = new HashMap<String, Object>();
            String busDate = account.getUseLastPeriod();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("auditStatus", 1);
            voucherService.oneKeyCheckVoucher(param);
            querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);
            Map<String, Object> results = new HashMap<String, Object>();
            if (querySubMessage.get("subMessage") != null) {
                List<TBasicSubjectMessage> tBasicSubjectMessageList = (List<TBasicSubjectMessage>) querySubMessage
                        .get("subMessage");
                results = tBasicSubjectMessageService.addSubMessageMessageList(account, user, tBasicSubjectMessageList);

                // 结账之后 生成报表
                tBasicBalanceSheetService.addBalanceSheet(user, account);
                tBasicIncomeStatementService.addIncomeStatement(user, account);
            } else {
                results.put("msg", "CheckoutController.checkout{querySubMessage查询为空}");
            }

            result.putAll(results);
            result.put("code", 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
            result.put("msg", e.getMessage());
            return result;
        } finally {
            Map<String, Object> progressData = new HashMap<String, Object>();
            progressData.put("accountID", getAccount().getAccountID());
            progressData.put("period", getUserDate());
            progressData.put("jz", 0);
            progressDao.chgProgress(progressData);
        }
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: checkoutExamine
     * @Description: 结账检查
     * @date 2018年1月25日 上午11:07:19
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/checkoutExamine")
    @ResponseBody
    public Map<String, Object> checkoutExamine() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        HttpSession session = getSession();
        Map<String, Object> querySubMessage;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            querySubMessage = tBasicSubjectMessageService.querySubMessage(user, account);

            List<TBasicSubjectMessage> tBasicSubjectMessageList = (List<TBasicSubjectMessage>) querySubMessage
                    .get("subMessage");

            Map<String, Object> results = tBasicSubjectMessageService.addSubMessageMessageList(account, user,
                    tBasicSubjectMessageList);
            result.putAll(results);
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}

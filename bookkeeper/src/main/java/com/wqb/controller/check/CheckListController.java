package com.wqb.controller.check;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.model.Account;
import com.wqb.model.KcCommodity;
import com.wqb.model.StatusPeriod;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 朱述渊
 * @ClassName: CheckListController
 * @Description: 风险检测-其它接口统一调用
 * @date 2018年6月5日 上午8:51:55
 */
@Controller
@RequestMapping("/check")
public class CheckListController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(CheckListController.class);
    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    KcCommodityDao kcCommodityDao;

    @RequestMapping("/chectList")
    @ResponseBody
    Map<String, Object> chectList() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Account account = getAccount();
            String busDate = getUserDate();
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountID", account.getAccountID());
            para.put("busDate", busDate);
            List<StatusPeriod> statuList = periodStatusService.queryStatus(para);
            List<List<String>> list = new ArrayList<List<String>>();// 整体大集合
            List<String> list1 = new ArrayList<String>();// 凭证检查
            List<String> list2 = new ArrayList<String>();// 余额检查
            List<String> list3 = new ArrayList<String>();// 报表检查
            List<String> list4 = new ArrayList<String>();// 其它指标
            boolean flag = true;
            if (!statuList.isEmpty() && statuList.size() == 1) {
                StatusPeriod sp = statuList.get(0);
                // 是否结转（0否1是）
                int isCarryState = sp.getIsCarryState();
                if (isCarryState == 0) {
                    flag = false;
                    list1.add("false-工资和固定资产尚未计提折旧,检测未通过");
                } else {
                    list1.add("true-工资和固定资产已计提折旧,检测通过");
                }
            }
            if (list1.size() > 0 && !list1.isEmpty()) {
                list.add(list1);
            }
            list.add(list1);
            para.put("subjectID", "1001");
            List<TBasicSubjectMessage> subjectList1 = tBasicSubjectMessageMapper.querySubByAccAndCode(para);
            if (subjectList1 != null && subjectList1.size() == 1) {
                TBasicSubjectMessage subject = subjectList1.get(0);
                if (subject != null) {
                    BigDecimal qmyed = subject.getEndingBalanceDebit();
                    if (null != qmyed && qmyed.doubleValue() < 0) {
                        list2.add("false-库存现金出现负数，请仔细检查");
                        flag = false;
                    } else {
                        list2.add("true-库存现金为正,检测通过");
                    }
                }
            }
            para.put("subjectID", "1002");
            List<TBasicSubjectMessage> subjectList2 = tBasicSubjectMessageMapper.querySubByAccAndCode(para);
            if (subjectList2 != null && subjectList2.size() == 1) {
                TBasicSubjectMessage subject = subjectList2.get(0);
                if (subject != null) {
                    BigDecimal qmyed = subject.getEndingBalanceDebit();
                    if (null != qmyed && qmyed.doubleValue() < 0) {
                        list2.add("false-银行存款出现负数，请仔细检查");
                    } else {
                        list2.add("true-银行存款为正,检测通过");
                    }
                }
            }

            para.put("period", busDate);
            List<KcCommodity> kcList = kcCommodityDao.queryCommodityAll(para);
            if (null != kcList && kcList.size() > 0) {
                boolean temp = true;
                for (KcCommodity kc : kcList) {
                    if (kc.getSub_code().startsWith("1403") || kc.getSub_code().startsWith("1405")) {
                        // 期末结存数量
                        double qm_balanceNum = kc.getQm_balanceNum();
                        if (qm_balanceNum < 0) {
                            flag = false;
                            temp = false;
                            list2.add("false-期末结存数量为负数,请仔细检查");
                            break;
                        }
                        // 期末结存单价
                        BigDecimal qm_balancePrice = kc.getQm_balancePrice();
                        if (null != qm_balancePrice && qm_balancePrice.doubleValue() < 0) {
                            flag = false;
                            temp = false;
                            list2.add("false-期末结存单价为负数,请仔细检查");
                            break;
                        }
                        // 期末结存金额
                        BigDecimal qm_balanceAmount = kc.getQm_balanceAmount();
                        if (null != qm_balanceAmount && qm_balanceAmount.doubleValue() < 0) {
                            flag = false;
                            temp = false;
                            list2.add("false-期末结存金额为负数,请仔细检查");
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                if (temp) {
                    list2.add("true-期末结存数量,单价,金额没负数,检查通过");
                }
            }
            if (list2.size() > 0 && !list2.isEmpty()) {
                list.add(list2);
            }


            return result;
        } catch (BusinessException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

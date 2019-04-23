package com.wqb.service.detection.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.check.CheckListController;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.detection.TaxRiskDetectionService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.report.TBasicBalanceSheetService;
import com.wqb.service.report.TBasicIncomeStatementService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: TaxRiskDetectionServiceImpl
 * @Description: 税务风险检测
 * @date 2018年5月10日 下午3:07:02
 */
@Service("taxRiskDetectionService")
public class TaxRiskDetectionServiceImpl implements TaxRiskDetectionService {
    private static Log4jLogger logger = Log4jLogger.getLogger(CheckListController.class);
    // 科目表
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    // 利润表
    @Autowired
    TBasicIncomeStatementService tBasicIncomeStatementService;

    // 资产负债表
    @Autowired
    TBasicBalanceSheetService tBasicBalanceSheetService;

    // 风险状态 0 未通过  1已通过
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    PeriodStatusService periodStatusService;

    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Autowired
    KcCommodityDao kcCommodityDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    VoucherHeadDao voucherHeadDao;

    @Autowired
    VoucherBodyDao voucherBodyDao;

    @Override
    //@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> queryDetection(User user, Account account) {
        TBasicBalanceSheet tBasicBalanceSheet = new TBasicBalanceSheet();
        Map<String, Object> result = new HashMap<String, Object>();

        int no1 = 0;
        int no2 = 0;
        int no3 = 0;
        int no4 = 0;
        int no12 = 0;
        int no22 = 0;
        int no32 = 0;
        int no42 = 0;

        // 重新计算资产负债表
        Map<String, Object> addBalanceSheet = tBasicBalanceSheetService.addBalanceSheet(user, account);
        int code = (int) addBalanceSheet.get("code");
        if (code == -1) {
            result.put("msg", "资产负载表获取失败");
            return result;
        }

        // 重新计算利润表
        Map<String, Object> addIncomeStatement = tBasicIncomeStatementService.addIncomeStatement(user, account);
        int codeIncomeStatement = (int) addIncomeStatement.get("code");
        if (codeIncomeStatement == -1) {
            result.put("msg", "利润表获取失败");
            return result;
        }

        // 获取用户信息
        @SuppressWarnings("unchecked")

        String userId = user.getUserID();// 用户id
        tBasicBalanceSheet.setUserId(userId);

        String accountId = account.getAccountID();// 账套id
        tBasicBalanceSheet.setAccountId(accountId);
        String busDate = account.getUseLastPeriod();
        // 做帐期间
        tBasicBalanceSheet.setAccountPeriod(busDate);

        Map<String, Object> queryDetection = new HashMap<String, Object>();

        Map<String, String> parameters = new HashMap<String, String>();

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("accountID", account.getAccountID());
        para.put("busDate", busDate);
        para.put("lastDate", busDate);
        List<StatusPeriod> statuList = periodStatusService.queryStatus(para);
        if (!statuList.isEmpty() && statuList.size() == 1) {
            StatusPeriod sp = statuList.get(0);
            // 是否检查通过（0否1是）
            Integer isDetection = sp.getIsDetection();
            if (isDetection == 1) {
                updateDetection(user, account, 0);
                List<StatusPeriod> statuList2 = periodStatusService.queryStatus(para);
                if (statuList2 != null && statuList2.size() == 1) {
                    StatusPeriod sp2 = statuList2.get(0);
                    // 是否检查通过（0否1是）
                    Integer isDetection2 = sp2.getIsDetection();
                    result.put("code", 2);
                    result.put("msg", "检查成功!");
                }
                return result;
            }
//				`isCreateVoucher` int(2) DEFAULT NULL COMMENT '是否已一键生成凭证（0：未  1：已生成）',
            if (sp.getIsCreateVoucher() == 0) {
                result.put("code", -1);
                result.put("msg", "请先一键生成凭证，再检测。。。");
                return result;
            }
//				`isCarryState` int(4) DEFAULT NULL COMMENT '是否结转（0否1是）',
            if (sp.getIsCarryState() == 0) {
                result.put("code", -1);
                result.put("msg", "请先结转，再检测。。。");
                return result;
            }
//				`isCheck` int(2) unsigned DEFAULT '0' COMMENT '0:未审核1:审核',

			/*	if(sp.getIsCheck() == 0)
				{
					result.put("code", -1);
					result.put("msg", "请先审核，再检测。。。");
					return result;
				}
				*/
//				 `isJz` int(2) DEFAULT '0' COMMENT '是否结账(0：未结账1:已结账)',
            if (sp.getIsJz() == 1) {
                result.put("code", -1);
                result.put("msg", "请先反结账，再检测。。。");
                return result;
            }
        }
        if (!statuList.isEmpty() && statuList.size() > 1) {
            result.put("msg", "更改失败，查询帐套" + account.getAccountID() + "在" + busDate + "期间有" + statuList.size() + "条数据");
            return result;
        }

        List<List<String>> list = new ArrayList<List<String>>();// 整体大集合
        List<String> list1 = new ArrayList<String>();// 凭证检查
        List<String> list2 = new ArrayList<String>();// 余额检查
        List<String> list3 = new ArrayList<String>();// 报表检查
        List<String> list4 = new ArrayList<String>();// 其它指标

        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();// 其它指标

        // 查询科目
        Map<String, Object> tBasicSubjectMessageMap = tBasicSubjectMessageService.querySubMessage(user, account);

        // 明细账
        tBasicSubjectMessageService.queryLedgerByParameters(user, account, parameters);

        // 查询科目余额表
        Map<String, Object> querySubMessage = tBasicSubjectMessageService.querySbujectBalance(user, account, parameters);

        // 利润表
        Map<String, Object> queryIncomeStatrmentMap = tBasicIncomeStatementService.queryIncomeStatrment(user, account);

        // 资产负债表
        Map<String, Object> queryBalanceSheet = tBasicBalanceSheetService.queryBalanceSheet(user, account);

//			voucherHeadDao.q

        //  0未通过  1通过    2 风险提示

        // 1.凭证检查
        // 1.1 工资表、固定资产折旧有无计提
        // 人员薪资档案表

        HashMap<Object, Object> hashMap = null;

        if (!statuList.isEmpty() && statuList.size() == 1) {
            hashMap = new HashMap<>();
            StatusPeriod sp = statuList.get(0);
            // 是否结转（0否1是）
            int isCarryState = sp.getIsCarryState();
            if (isCarryState == 0) {
                //					list1.add("false-工资和固定资产尚未计提折旧,检测未通过");
                no12++;
                hashMap.put("state", 2);
                hashMap.put("msg", "工资和固定资产尚未计提折旧,检测未通过");
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("accountID", accountId);
                param.put("period", busDate);
                //凭证表中 是否6601 6602 2211 中是否包涵 《工资》 字样
                List<VoucherBody> voucherBodies = voucherBodyDao.querySalary(param);
                if (!voucherBodies.isEmpty() && voucherBodies.size() > 0) {
                    //					list1.add("true-工资和固定资产已计提折旧,检测通过");
                    hashMap.put("state", 1);
                    hashMap.put("msg", "工资和固定资产已计提折旧,检测通过");
                } else {
                    no12++;
                    hashMap.put("state", 2);
                    hashMap.put("msg", "工资未计提折旧,检测未通过");
                }
            }
            queryDetection.put("salary", hashMap);
        } else {
            hashMap = new HashMap<>();
//				hashMap.put("state", 1);
//				hashMap.put("msg", "工资和固定资产已计提折旧,检测通过");
            no12++;
            hashMap.put("state", 2);
            hashMap.put("msg", "工资和固定资产已计提折旧,检测未通过");
            queryDetection.put("salary", hashMap);
        }
        if (list1.size() > 0 && !list1.isEmpty()) {
            list.add(list1);
        }

        // 固定资产档案表(做)   如果有固定资产 必须计提
        //			queryDetection.put("t_assets", 1);


        //1.2 成本有无结转 （做 提醒）
        //			queryDetection.put("t_basic_balance_sheet", 1);

        // 1.3 凭证是否断号 没做断号重排
        Map<String, Object> voucherBrokenNo = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("period", busDate);
        param.put("accountID", accountId);
        List<Integer> voucherBrokenNoList = voucherHeadDao.queryVoucherBrokenNo(param);
        if (!voucherBrokenNoList.isEmpty() && voucherBrokenNoList.size() > 0) {
            queryDetection.put("voucherBrokenNo", voucherBrokenNoList);
            logger.info("帐套ID：" + accountId + "在" + busDate + "期间存在断号" + voucherBrokenNoList.size() + "个;   分别是" + voucherBrokenNoList);
            for (Integer integer : voucherBrokenNoList) {
                System.out.println(integer);
            }

            voucherBrokenNo.put("msg", "存在断号" + voucherBrokenNoList.size() + "个;   分别是" + voucherBrokenNoList);
            voucherBrokenNo.put("state", 0);
        } else {
            voucherBrokenNo.put("msg", "凭证是否断号 检查通过");
            voucherBrokenNo.put("state", 1);
        }
        queryDetection.put("voucherBrokenNo", voucherBrokenNo);
        // 1.4 成本结转是否合理（待定）  页面上面去除

        // 二．余额检查
        // 2.1 资金有无负数（只查询银行和现金科目  不能再贷方，或者借方为负）
        // （科目余额表--资金余额不能在贷方）


        // 科目余额表
        //			queryDetection.put("t_basic_subject_message", 1);

        // 2.2 存货有无负数
        // 库存表---库存商品，原材料，数量，单价，金额不允许为负数
        // 库存商品
        para.put("subjectID", "1001");
        List<TBasicSubjectMessage> subjectList1 = tBasicSubjectMessageMapper.querySubByAccAndCode(para);
        if (subjectList1 != null && subjectList1.size() == 1) {
            hashMap = new HashMap<>();
            TBasicSubjectMessage subject = subjectList1.get(0);
            if (subject != null) {
                BigDecimal qmyed = subject.getEndingBalanceDebit() == null ? new BigDecimal(0) : subject.getEndingBalanceDebit();
                ;
                BigDecimal qmyec = subject.getEndingBalanceCredit() == null ? new BigDecimal(0) : subject.getEndingBalanceCredit();
                ;
                // 现金期末 借方负数 或者 余额在贷方都为现金负数
                BigDecimal subtract = qmyed.subtract(qmyec);
                if (null != subtract && subtract.doubleValue() < 0) {
                    //						list2.add("false-库存现金出现负数，请仔细检查");
                    no2++;
                    hashMap.put("state", 0);
                    hashMap.put("msg", "库存现金出现负数，请仔细检查");
                } else {
                    //						list2.add("true-库存现金为正,检测通过");
                    hashMap.put("state", 1);
                    hashMap.put("msg", "库存现金为正,检测通过");
                }
            }
            queryDetection.put("inventory", hashMap);
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "库存现金为正,检测通过");
            queryDetection.put("inventory", hashMap);
        }

        // 银行期末 借方负数 或者 余额在贷方都为银行负数
        // 银行存款出现负数
        para.put("subjectID", "1002");
        List<TBasicSubjectMessage> subjectList2 = tBasicSubjectMessageMapper.querySubByAccAndCode(para);
        if (subjectList2 != null && subjectList2.size() == 1) {
            hashMap = new HashMap<>();
            TBasicSubjectMessage subject = subjectList2.get(0);
            if (subject != null) {
                BigDecimal qmyed = subject.getEndingBalanceDebit() == null ? new BigDecimal(0) : subject.getEndingBalanceDebit();
                BigDecimal qmyec = subject.getEndingBalanceCredit() == null ? new BigDecimal(0) : subject.getEndingBalanceCredit();
                BigDecimal subtract = qmyed.subtract(qmyec);
                if (null != subtract && subtract.doubleValue() < 0) {
                    //						list2.add("false-银行存款出现负数，请仔细检查");
                    hashMap.put("state", 0);
                    no2++;
                    hashMap.put("msg", "银行存款出现负数，请仔细检查");
                } else {
                    //						list2.add("true-银行存款为正,检测通过");
                    hashMap.put("state", 1);
                    hashMap.put("msg", "银行存款为正,检测通过");
                }
            }
            queryDetection.put("bank", hashMap);
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "银行存款为正,检测通过");
            queryDetection.put("bank", hashMap);
        }

        para.put("period", busDate);
        List<KcCommodity> kcList = kcCommodityDao.queryCommodityAll(para);
        if (null != kcList && kcList.size() > 0) {
            hashMap = new HashMap<>();
            boolean temp = true;
            for (KcCommodity kc : kcList) {
                if (kc.getSub_code() == null) {
                    temp = false;
                    no2++;
                    hashMap.put("state", 0);
                    hashMap.put("msg", "库存商品 - " + kc.getComNameSpec() + "科目不存在");
                    break;
                }
                if (kc.getSub_code().startsWith("1403") || kc.getSub_code().startsWith("1405")) {
                    // 期末结存数量
                    double qm_balanceNum = kc.getQm_balanceNum() == null ? 0.0 : kc.getQm_balanceNum();
                    if (qm_balanceNum < 0) {
                        temp = false;
                        //							list2.add("false-期末结存数量为负数,请仔细检查");
                        hashMap.put("state", 0);
                        no2++;
                        hashMap.put("msg", "期末结存数量为负数,请仔细检查");
                        break;
                    }
                    // 期末结存单价
                    BigDecimal qm_balancePrice = kc.getQm_balancePrice();
                    if (null != qm_balancePrice && qm_balancePrice.doubleValue() < 0) {
                        temp = false;
                        //							list2.add("false-期末结存单价为负数,请仔细检查");
                        no2++;
                        hashMap.put("state", 0);
                        hashMap.put("msg", "期末结存单价为负数,请仔细检查");
                        break;
                    }
                    // 期末结存金额
                    BigDecimal qm_balanceAmount = kc.getQm_balanceAmount();
                    if (null != qm_balanceAmount && qm_balanceAmount.doubleValue() < 0) {
                        temp = false;
                        //							list2.add("false-期末结存金额为负数,请仔细检查");
                        hashMap.put("state", 0);
                        no2++;
                        hashMap.put("msg", "期末结存金额为负数,请仔细检查");
                        break;
                    }
                } else {
                    continue;
                }
            }
            if (temp) {
                //					list2.add("true-期末结存数量,单价,金额没负数,检查通过");
                hashMap.put("state", 1);
                hashMap.put("msg", "期末结存数量,单价,金额没负数,检查通过");
            }
            queryDetection.put("kcCommodity", hashMap);
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "没有库存,检查通过");
            queryDetection.put("kcCommodity", hashMap);
        }

        // 2.3 往来科目有无长期挂账，超过一年没有收或付的要提醒（页面上面隐藏，暂时不做）

        // 2.4往来科目合并与对冲，应收、预收、应付、预付二级科目有没有合并与对冲的情况。（高经理现场教学）（页面上面隐藏，暂时不做）

        // 三．报表检查
        // 3.1资产负债表是否平衡
        // 2组数据，一一对等，如果不相等提示不平。
        if (queryBalanceSheet.get("queryBalanceSheet") != null
                && !((List<TBasicSubjectMessage>) queryBalanceSheet.get("queryBalanceSheet")).isEmpty()) {
            hashMap = new HashMap<>();
            List<TBasicBalanceSheet> tBasicBalanceSheets =
                    (List<TBasicBalanceSheet>) queryBalanceSheet.get("queryBalanceSheet");

            BigDecimal initTransactionMonetaryAssets = tBasicBalanceSheet.getInitTransactionMonetaryAssets();

            BigDecimal bigDecimal1 = new BigDecimal(0);
            BigDecimal bigDecimal2 = new BigDecimal(0);
            //  `init_total_of_liabilities_and_owners_equity` decimal(20,8) DEFAULT NULL COMMENT '负债和所有者权益总计Total of liabilities and owners'' equity',
            BigDecimal initTotalOfLiabilitiesAndOwnersEquity =
                    tBasicBalanceSheet.getInitTotalOfLiabilitiesAndOwnersEquity() == null ? new BigDecimal("0") : tBasicBalanceSheet.getInitTotalOfLiabilitiesAndOwnersEquity();
            // `end_total_of_liabilities_and_owners_equity` decimal(20,8) DEFAULT NULL COMMENT '负债和所有者权益总计Total of liabilities and owners'' equity',
            BigDecimal endTotalOfLiabilitiesAndOwnersEquity =
                    tBasicBalanceSheet.getEndTotalOfLiabilitiesAndOwnersEquity() == null ? new BigDecimal("0") : tBasicBalanceSheet.getEndTotalOfLiabilitiesAndOwnersEquity();
            //  `init_total_of_assets` decimal(20,8) DEFAULT NULL COMMENT '资产总计Total of Assets',
            BigDecimal initTotalOfAssets = tBasicBalanceSheet.getInitTotalOfAssets() == null ? new BigDecimal("0") : tBasicBalanceSheet.getInitTotalOfAssets();
            // `end_total_of_assets` decimal(20,8) DEFAULT NULL COMMENT '资产总计Total of Assets',
            BigDecimal endTotalOfAssets = tBasicBalanceSheet.getEndTotalOfAssets() == null ? new BigDecimal("0") : tBasicBalanceSheet.getEndTotalOfAssets();

            bigDecimal1 = initTotalOfLiabilitiesAndOwnersEquity.subtract(initTotalOfAssets);
            bigDecimal2 = endTotalOfLiabilitiesAndOwnersEquity.subtract(endTotalOfAssets);

            if (bigDecimal1.compareTo(BigDecimal.ZERO) == 0 || bigDecimal2.compareTo(BigDecimal.ZERO) == 0) {
                hashMap.put("state", 1);
                hashMap.put("msg", "资产负债表检测正常");
                queryDetection.put("balanceSheet", hashMap);
            } else {
                hashMap.put("state", 0);
                no3++;
                hashMap.put("msg", "资产负债表不平,请仔细检查");
                queryDetection.put("balanceSheet", hashMap);
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "资产负债表检测正常");
            queryDetection.put("balanceSheet", hashMap);
        }

        // 3.2成本倒挂：即指成本大于收入，成本不能大于收入。数据在利润表中取值。
        // 本期成本大于本期收入给出提示属于成本倒挂
        if (queryIncomeStatrmentMap.get("queryIncomeStatrment") != null
                && !((List<TBasicIncomeStatement>) queryIncomeStatrmentMap.get("queryIncomeStatrment")).isEmpty()) {
//				hashMap = new HashMap<>();
//				List<TBasicIncomeStatement> tBasicIncomeStatementList =
//						(List<TBasicIncomeStatement>) queryIncomeStatrmentMap.get("queryIncomeStatrment");
//				for (TBasicIncomeStatement tBasicIncomeStatement : tBasicIncomeStatementList) {

////					  `current_sales_from_operati` decimal(20,8) DEFAULT NULL COMMENT '一、营业收入 Sales from operations',
//					BigDecimal currentSalesFromOperati = tBasicIncomeStatement.getCurrentSalesFromOperati() == null ? new BigDecimal("0") : tBasicIncomeStatement.getCurrentSalesFromOperati();
////					  `current_less_operating_costs` decimal(20,8) DEFAULT NULL COMMENT '减：营业成本  Less: operating costs
//					BigDecimal currentLessOperatingCosts = tBasicIncomeStatement.getCurrentLessOperatingCosts() == null ? new BigDecimal("0") : tBasicIncomeStatement.getCurrentLessOperatingCosts();
//					if(currentLessOperatingCosts.compareTo(currentSalesFromOperati) >= 0)
//					{
//						hashMap = new HashMap<>();
//						hashMap.put("state", 2);
//						no32++;
//						hashMap.put("msg", "出现成本倒挂现象,检测有异常");
//						queryDetection.put("incomeStatement", hashMap);
//					}else{
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "无成本倒挂现象,检测正常");
            queryDetection.put("incomeStatement", hashMap);
//					}
//				}
        } else {
            hashMap = new HashMap<>();
            hashMap.put("msg", "无成本倒挂现象,检测正常");
            hashMap.put("state", 1);
            queryDetection.put("incomeStatement", hashMap);
        }


        // 3.3资不抵债：指企业的全部债务超过其资产总值以致不足以清偿债权人的财务状况。（数据在资产负债表中取值）资产总额不能小于负债总额。
        // 负债合计大于资产合计给出提示属于资不抵债。
        if (queryBalanceSheet.get("queryBalanceSheet") != null
                && !((List<TBasicSubjectMessage>) queryBalanceSheet.get("queryBalanceSheet")).isEmpty()) {
            hashMap = new HashMap<>();
            List<TBasicBalanceSheet> tBasicSubjectMessages =
                    (List<TBasicBalanceSheet>) queryBalanceSheet.get("queryBalanceSheet");

            BigDecimal initTransactionMonetaryAssets = tBasicBalanceSheet.getInitTransactionMonetaryAssets();

            BigDecimal bigDecimal1 = new BigDecimal(0);
            BigDecimal bigDecimal2 = new BigDecimal(0);
            // 负债合计Total of liabilities',
            BigDecimal endTotalOfLiabilities =
                    tBasicBalanceSheet.getEndTotalOfLiabilities() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndTotalOfLiabilities();

            // `end_total_of_assets` decimal(20,8) DEFAULT NULL COMMENT '资产总计Total of Assets',
            BigDecimal endTotalOfAssets = tBasicBalanceSheet.getEndTotalOfAssets() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndTotalOfAssets();

            int result1 = endTotalOfAssets.compareTo(endTotalOfLiabilities);

            if (result1 > 0) {
                hashMap.put("state", 1);
                hashMap.put("msg", "资产大于负债检测正常");
            } else if (result1 == 0) {
                hashMap.put("state", 1);
                hashMap.put("msg", "资产等于负债检测正常");
            } else {
                hashMap.put("state", 2);
                no32++;
                hashMap.put("msg", "资不抵债,请仔细检查");
            }
            queryDetection.put("insolvency", hashMap);
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "没有发现资不抵债现象");
            queryDetection.put("insolvency", hashMap);
        }

        // 3.4 科目余额表是否平衡

        // 四．其它指标
        // 4.1福利费扣除标准
        // 数据来源：结转损益凭证
        // 包含6602福利费，不能超过月工资的14%
        // 只查询6601和6602下面包含福利费的科目  汇总  不能大于 2211应付职工薪酬工资的 14%
        BigDecimal weal = new BigDecimal("0");
        para.put("subjectID", "6601");
        para.put("subName", "福利");
        List<TBasicSubjectMessage> subjectList6601 = tBasicSubjectMessageMapper.selectLastArch2(para);
        if (subjectList6601 != null && subjectList6601.size() == 1) {
            TBasicSubjectMessage tBasicSubjectMessage = subjectList6601.get(0);
            // 本期发生额 借方
            BigDecimal currentAmountDebit = tBasicSubjectMessage.getCurrentAmountDebit();
            weal = weal.add(currentAmountDebit);
        }
        para.put("subjectID", "6602");
        para.put("subName", "福利");
        List<TBasicSubjectMessage> subjectList6602 = tBasicSubjectMessageMapper.selectLastArch2(para);
        if (subjectList6602 != null && subjectList6602.size() == 1) {
            TBasicSubjectMessage tBasicSubjectMessage = subjectList6602.get(0);
            hashMap = new HashMap<>();
            // 本期发生额 借方
            BigDecimal currentAmountDebit = tBasicSubjectMessage.getCurrentAmountDebit();
            weal = weal.add(currentAmountDebit);
        }

        para.put("subjectID", "2211");
        para.put("subName", "工资");
        List<TBasicSubjectMessage> subjectList2211 = tBasicSubjectMessageMapper.selectLastArch2(para);
        if (subjectList2211 != null && subjectList2211.size() == 1) {
            TBasicSubjectMessage tBasicSubjectMessage = subjectList2211.get(0);
            hashMap = new HashMap<>();
            // 本期发生额 贷方
            BigDecimal currentAmountCredit = tBasicSubjectMessage.getCurrentAmountCredit();
            BigDecimal bigDecimal = new BigDecimal("0.14");
            currentAmountCredit = currentAmountCredit.multiply(bigDecimal);
            if (weal.compareTo(currentAmountCredit) > 0) {
                hashMap = new HashMap<>();
                hashMap.put("state", 2);
                no42++;
                hashMap.put("msg", "福利费扣除标准超标");
                queryDetection.put("weal", hashMap);
            } else {
                hashMap = new HashMap<>();
                hashMap.put("state", 1);
                hashMap.put("msg", "福利费扣除标准正常");
                queryDetection.put("weal", hashMap);

            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "福利费扣除标准正常");
            queryDetection.put("weal", hashMap);
        }

        // 4.2业务招待费----
        // 数据来源：结转损益凭证，业务招待费支出，1.所有科目包含  （业务）招待费  6601/6602总和
        // ，按照发生额的60%扣除，2.但最高不得超过当年销售(营业)收入（利润表/营业收入/本期） 利润表中一、营业收入本期余额*5‰。两个值对比，取低值。
        // 大于60% 给出提醒（其它待定）

        // 主营业务的 百分之五
        // 招待费的 百分之六十
        // 两者取最低值扣除
        para.put("subjectID", "6601");
        para.put("subName", "招待费");
        List<TBasicSubjectMessage> subjectList66012 = tBasicSubjectMessageMapper.selectLastArch2(para);
        if (subjectList6601 != null && subjectList6601.size() == 1) {
            hashMap = new HashMap<>();

        }
        para.put("subjectID", "6602");
        para.put("subName", "招待费");
        List<TBasicSubjectMessage> subjectList66022 = tBasicSubjectMessageMapper.selectLastArch2(para);
        if (subjectList6602 != null && subjectList6602.size() == 1) {
            hashMap = new HashMap<>();

        } else {
            hashMap = new HashMap<>();
        }

        // 4.3连续零申报----
        // 数据来源：利润表/收入
        // 先判断 当前日期是否是六月份之后，再判断启用帐套日期距今是否大于六个月
        // 本年内收入一直为0，给出提示 如果连续六个月为零
        if (queryIncomeStatrmentMap.get("queryIncomeStatrment") != null
                && !((List<TBasicIncomeStatement>) queryIncomeStatrmentMap.get("queryIncomeStatrment")).isEmpty()) {
            hashMap = new HashMap<>();
            List<TBasicIncomeStatement> tBasicIncomeStatementList =
                    (List<TBasicIncomeStatement>) queryIncomeStatrmentMap.get("queryIncomeStatrment");

            // 根据帐套id 查询帐套信息
            Account queryAccByID = accountDao.queryAccByID(accountId);
            // 获取帐套启用时间
            Date period = queryAccByID.getPeriod();

            for (TBasicIncomeStatement tBasicIncomeStatement : tBasicIncomeStatementList) {
//					  `current_sales_from_operati` decimal(20,8) DEFAULT NULL COMMENT '一、营业收入 Sales from operations',
                BigDecimal currentSalesFromOperati = tBasicIncomeStatement.getCurrentSalesFromOperati() == null ? new BigDecimal("0") : tBasicIncomeStatement.getCurrentSalesFromOperati();

                if (currentSalesFromOperati.compareTo(new BigDecimal("0")) >= 0) {
                    hashMap = new HashMap<>();
                    hashMap.put("state", 1);
                    hashMap.put("msg", "未出现连续零申报现象,检测正常");
                    queryDetection.put("zeroDeclaration", hashMap);
                } else {
                    hashMap = new HashMap<>();
                    hashMap.put("state", 2);
                    no42++;
                    hashMap.put("msg", "出现连续零申报现象,检测有异常");
                    queryDetection.put("zeroDeclaration", hashMap);
                }
            }
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "未出现连续零申报现象,检测正常");
            queryDetection.put("zeroDeclaration", hashMap);
        }

        // 4.4资产负债率----
        // 数据来源：资产负债表  是否正常  正常范围（负债总和除以资产总和 * 100%） 以百分比形式显示
        if (queryBalanceSheet.get("queryBalanceSheet") != null
                && !((List<TBasicSubjectMessage>) queryBalanceSheet.get("queryBalanceSheet")).isEmpty()) {
            hashMap = new HashMap<>();
            List<TBasicBalanceSheet> tBasicSubjectMessages =
                    (List<TBasicBalanceSheet>) queryBalanceSheet.get("queryBalanceSheet");

            BigDecimal initTransactionMonetaryAssets = tBasicBalanceSheet.getInitTransactionMonetaryAssets();

            BigDecimal bigDecimal1 = new BigDecimal(0);
            BigDecimal bigDecimal2 = new BigDecimal(0);
            // 负债合计Total of liabilities',
            BigDecimal endTotalOfLiabilities =
                    tBasicBalanceSheet.getEndTotalOfLiabilities() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndTotalOfLiabilities();

            // `end_total_of_assets` decimal(20,8) DEFAULT NULL COMMENT '资产总计Total of Assets',
            BigDecimal endTotalOfAssets = tBasicBalanceSheet.getEndTotalOfAssets() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndTotalOfAssets();

            int result1 = endTotalOfAssets.compareTo(endTotalOfLiabilities);

            if (result1 > 0) {
                hashMap.put("state", 1);
                hashMap.put("msg", "资产大于负债检测正常");
            } else if (result1 == 0) {
                hashMap.put("state", 1);
                hashMap.put("msg", "资产等于负债检测正常");
            } else {
                hashMap.put("state", 2);
                no32++;
                hashMap.put("msg", "资不抵债,请仔细检查");
            }
            queryDetection.put("insolvency", hashMap);
        } else {
            hashMap = new HashMap<>();
            hashMap.put("state", 1);
            hashMap.put("msg", "没有发现资不抵债现象");
            queryDetection.put("insolvency", hashMap);
        }

        // 4.5所得税税负率----（算本年的值 ）
        // 数据来源：利润表/所得税
        // 计算公式：所得税税负率=应纳所得税额÷销售收入×100%。
        // 应纳所得税额

        // 4.6增值税税负率
        // 数据来源：科目余额表/增值税
        // 计算公式：增值税税负率=当期应纳增值税/当期应税销售收入×100%
        // 新计算公式：2221下面 未交增值税  /   销售收入（利润表中营业收入 本期值）
        // 当期应纳增值税

        // 当期应税销售收入

        // 4.7毛利率
        // 数据来源：(利润表/营业收入/本期金额  )  (利润表/营业成本/本期金额 )
        // 计算公式：毛利率=毛利/营业收入×100%

        // 资产负债表
        //			queryDetection.put("t_basic_balance_sheet", 1);

        // 利润表
        //			queryDetection.put("t_basic_income_statement", 1);

        // 风险检测 是否检测通过0:未通过1:通过2:检测不通过'

        result.putAll(queryDetection);
        // 风险统计 no1 + no2 + no3 + no4 此四项必须为零才能结帐
        int no = no1 + no2 + no3 + no4;
        // no12 + no22 + no32 + no42 只做风险提醒 有风险也可以结帐
        int notwo = no12 + no22 + no32 + no42;
        if (no == 0 && notwo == 0) {
            updateDetection(user, account, 1);
            result.put("code", 1);
            result.put("msg", "检测通过");
            result.put("msg1", no1 + no12);
            result.put("msg2", no2 + no22);
            result.put("msg3", no3 + no32);
            result.put("msg4", no4 + no42);
        } else if (no > 0) {
            // 风险必须解决  否则不允许结账
//				result.put("code", 2);
            result.put("code", 1);
            updateDetection(user, account, 2);
            result.put("msg", "有  " + (no + notwo) + " 项风险,请检查通过后再进行结账");
            result.put("msg1", no1 + no12);
            result.put("msg2", no2 + no22);
            result.put("msg3", no3 + no32);
            result.put("msg4", no4 + no42);
        } else if (notwo > 0) {
            // 只是风险提醒  允许结账
            updateDetection(user, account, 1);
            result.put("code", 1);
            result.put("msg", "有  " + (no + notwo) + " 项风险,请检查通过后再进行结账");
            result.put("msg1", no1 + no12);
            result.put("msg2", no2 + no22);
            result.put("msg3", no3 + no32);
            result.put("msg4", no4 + no42);
        }

        return result;
    }

    /**
     * @return
     * @throws BusinessException
     * @Title: updateDetection
     * @Description: 风险检测 是否检测通过0:未通过1:通过'
     */
    @Override
    public Map<String, Object> updateDetection(User user, Account account, int number) throws BusinessException {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            String busDate = account.getUseLastPeriod();
            param.put("accountID", account.getAccountID());
            param.put("busDate", busDate);
            param.put("isDetection", number);
            //update t_status_period set isDetection=#{isDetection} where period=#{busDate} and accountID=#{accountID};
            periodStatusDao.updStatuIsDetection(param);
        } catch (BusinessException e) {
            result.put("msg", e.getMessage());
        }
        result.put("code", 1);
        return result;

    }

}

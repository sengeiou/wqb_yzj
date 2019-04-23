package com.wqb.service.report.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.report.TBasicBalanceSheetMapper;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.model.Account;
import com.wqb.model.TBasicBalanceSheet;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.User;
import com.wqb.service.report.TBasicBalanceSheetService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Transactional

/**
 * @author 司氏旭东
 * @ClassName: TBasicBalanceSheetServiceImpl
 * @Description: 资产负债表
 * @date 2018年5月10日 下午3:42:23
 */
@Service("tBasicBalanceSheetService")
public class TBasicBalanceSheetServiceImpl implements TBasicBalanceSheetService {
    @Autowired
    TBasicBalanceSheetMapper tBasicBalanceSheetMapper;

    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    /**
     * @return
     * @Title: queryBalanceSheet
     * @Description: 查询资产负债表
     */
    @Override
    //@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> queryBalanceSheet(User user, Account account) {
        TBasicBalanceSheet tBasicBalanceSheet = new TBasicBalanceSheet();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")

            String userId = user.getUserID();// 用户id
            tBasicBalanceSheet.setUserId(userId);

            String accountId = account.getAccountID();// 账套id
            tBasicBalanceSheet.setAccountId(accountId);
            String busDate = account.getUseLastPeriod();
            // 做帐期间
            tBasicBalanceSheet.setAccountPeriod(busDate);

            List<TBasicBalanceSheet> queryBalanceSheet = tBasicBalanceSheetMapper.queryBalanceSheet(tBasicBalanceSheet);
//			BigDecimal bigDecimal = new BigDecimal(0);

            //			for (TBasicBalanceSheet tBasicBalanceSheet2 : queryBalanceSheet)
            //			{
            // 空指針异常
            //				int compareTo1 = tBasicBalanceSheet2.getInventories().compareTo(new BigDecimal(0E-8));
            //				System.out.println(compareTo1);
            //				int compareTo = tBasicBalanceSheet2.getFixedAssets().compareTo(new BigDecimal(0E-8));
            //				System.out.println(compareTo);
            //				//				.compareTo(new BigDecimal("0E-8"))
            //				bigDecimal = tBasicBalanceSheet2.getFixedAssets() == bigDecimal ? bigDecimal
            //						: tBasicBalanceSheet2.getFixedAssets();
            //				tBasicBalanceSheet2.setFixedAssets(bigDecimal);

            //			}
            result.put("queryBalanceSheet", queryBalanceSheet);
            result.put("code", 1);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    //这是资产负债表 APP接口
    @Override
    public Map<String, Object> queryBalanceSheetApp(Map<String, Object> param) {
        TBasicBalanceSheet tBasicBalanceSheet = new TBasicBalanceSheet();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            tBasicBalanceSheet.setAccountId(param.get("accountID").toString());
            tBasicBalanceSheet.setAccountPeriod(param.get("period").toString());
            List<TBasicBalanceSheet> listSheet = tBasicBalanceSheetMapper.queryBalanceSheet2(tBasicBalanceSheet);
            if (listSheet != null && listSheet.size() > 0) {
                result.put("sheet", listSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    //@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> queryBalanceSheetMap(HttpSession session) {
        TBasicBalanceSheet tBasicBalanceSheet = new TBasicBalanceSheet();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user"); // 获取user信息

            String userId = user.getUserID();// 用户id
            tBasicBalanceSheet.setUserId(userId);

            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            tBasicBalanceSheet.setAccountId(accountId);
            String busDate = (String) sessionMap.get("busDate");
            // 做帐期间
            tBasicBalanceSheet.setAccountPeriod(busDate);

            Map<String, String> queryBalanceSheet = tBasicBalanceSheetMapper.queryBalanceSheetMap(tBasicBalanceSheet);
//			BigDecimal bigDecimal = new BigDecimal(0);

            result.put("queryBalanceSheet", queryBalanceSheet);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> addBalanceSheet(User user, Account account) throws BusinessException {
        TBasicBalanceSheet tBasicBalanceSheet = new TBasicBalanceSheet();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        // 获取用户信息

        /** 用户ID */
        String userId = user.getUserID();// 用户id
        tBasicBalanceSheet.setUserId(userId);

        /** 账套ID */
        String accountId = account.getAccountID();// 账套id
        tBasicBalanceSheet.setAccountId(accountId);

        /** 做帐的真实期间 年 - 月(帐套启用年-月份） */
        String busDate = account.getUseLastPeriod();
        tBasicBalanceSheet.setAccountPeriod(busDate);

        /** 资产负债表主键 */
        String pkBalanceSheetId = UUIDUtils.getUUID();

        tBasicBalanceSheet.setPkBalanceSheetId(pkBalanceSheetId);

        Map<String, String> parameters = new HashMap<String, String>();

        //查询科目余额表
        Map<String, Object> querySbujectBalance =
                tBasicSubjectMessageService.querySbujectBalance(user, account, parameters);

        //查询1月份的科目余额表
//			String[] splitBusDate = busDate.split("\\-");
//			String year = splitBusDate[0];
//			year = year + "-01";
//			sessionMap.put("busDate", year);
//			session.setAttribute("userDate", sessionMap);
//			Map<String, Object> queryJanuarySbujectBalance =
//					tBasicSubjectMessageService.querySbujectBalance(session, parameters);
//
        // 期间改回原始期间
//			sessionMap.put("busDate", busDate);
//			session.setAttribute("userDate", sessionMap);

        // 期末余额 取每月期末余额
        if (querySbujectBalance.get("subMessages") != null && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {
            List<TBasicSubjectMessage> tBasicSubjectMessages =
                    (List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages");

//				// 取出最末级科目代码
//				List<TBasicSubjectMessage> tBasicSubjectMessages2 = SubjectUtils.getMjSub(tBasicSubjectMessages);

            /** 货币资金 Cash */
            BigDecimal endCash = new BigDecimal("0.0");
            BigDecimal initCash = new BigDecimal("0.0");
            /** 应收帐款*/
            BigDecimal endAccountsReceivable = new BigDecimal("0.0");
            BigDecimal initAccountsReceivable = new BigDecimal("0.0");
            /** 预付帐款*/
            BigDecimal endAccountsPrepaid = new BigDecimal("0.0");
            BigDecimal initAccountsPrepaid = new BigDecimal("0.0");
            /** 存货 */
            BigDecimal endInventories = new BigDecimal("0.0");
            BigDecimal initInventories = new BigDecimal("0.0");
            /** 其他流动资产 */
            BigDecimal endOtherCurrentAssets = new BigDecimal("0.0");
            BigDecimal initOtherCurrentAssets = new BigDecimal("0.0");
            /** 持有至到期投*/
            BigDecimal endHeldToMaturityInvestmen = new BigDecimal("0.0");
            BigDecimal initHeldToMaturityInvestmen = new BigDecimal("0.0");
            /** 长期应收款*/
            BigDecimal endLongTermReceivables = new BigDecimal("0.0");
            BigDecimal initLongTermReceivables = new BigDecimal("0.0");
            /** 长期股权投资*/
            BigDecimal endLongTermEquityInvestment = new BigDecimal("0.0");
            BigDecimal initLongTermEquityInvestment = new BigDecimal("0.0");
            /** 固定资产*/
            BigDecimal endFixedAssets = new BigDecimal("0.0");
            BigDecimal initFixedAssets = new BigDecimal("0.0");
            /** 无形资产*/
            BigDecimal endIntangibelAssets = new BigDecimal("0.0");
            BigDecimal initIntangibelAssets = new BigDecimal("0.0");

            /** 负债和所有者权益（或股东权益）*/
            /** 应付帐款 Accounts payable*/
            BigDecimal endAccountsPayable = new BigDecimal("0.0");
            BigDecimal initAccountsPayable = new BigDecimal("0.0");
            /** 预收款项 advance receipts*/
            BigDecimal endAdvanceReceipts = new BigDecimal("0.0");
            BigDecimal initAdvanceReceipts = new BigDecimal("0.0");
            /** 其他流动负债other current liabilities*/
            BigDecimal endOtherCurrentLiabilities = new BigDecimal("0.0");
            BigDecimal initOtherCurrentLiabilities = new BigDecimal("0.0");
            /** 长期应付款long-term accounts payable*/
            BigDecimal endLongTermAccountsPayable = new BigDecimal("0.0");
            BigDecimal initLongTermAccountsPayable = new BigDecimal("0.0");

            /** 所有者权益（或股东权益）： */
            /** 未分配利润retained earnings*/
            BigDecimal endRetainedEarnings = new BigDecimal("0.0");
            BigDecimal initRetainedEarnings = new BigDecimal("0.0");
            // 科目代码最下级长度
//				int lowermostCoding1122 = 0;
//				int lowermostCoding2203 = 0;
//				int lowermostCoding1231 = 0;
//				int lowermostCoding1123 = 0;
//				int lowermostCoding2202 = 0;
//				int lowermostCoding3101 = 0;
//				int lowermostCoding3201 = 0;
//				int lowermostCoding3202 = 0;
//
//				for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages)
//				{
//					String subCode = tBasicSubjectMessage.getSubCode();
//					if(subCode.startsWith("1122"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding1122)
//						{
//							lowermostCoding1122 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//					if(subCode.startsWith("2203"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding2203)
//						{
//							lowermostCoding2203 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//					if(subCode.startsWith("1231"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding1231)
//						{
//							lowermostCoding1231 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//					if(subCode.startsWith("1123"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding1123)
//						{
//							lowermostCoding1123 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//					if(subCode.startsWith("2202"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding2202)
//						{
//							lowermostCoding2202 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//
//					}
//
//					if(subCode.startsWith("3101"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding3101)
//						{
//							lowermostCoding3101 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//					if(subCode.startsWith("3201"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding3201)
//						{
//							lowermostCoding3201 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//					if(subCode.startsWith("3202"))
//					{
//						if(tBasicSubjectMessage.getCodeLevel() > lowermostCoding3202)
//						{
//							lowermostCoding3202 = tBasicSubjectMessage.getCodeLevel();
//							continue;
//						}
//					}
//
//				}

            // 取出最末级科目代码
//				for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages2)
//				{
//				}

            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages) {
                String subCode = tBasicSubjectMessage.getSubCode();
//					Integer codeLevel = tBasicSubjectMessage.getCodeLevel();

                if (StringUtils.isNotBlank(subCode)) {
                    /** 货币资金 Cash */
                    if (subCode.equals("1001")) {
//							endCash = tBasicSubjectMessage.getEndingBalanceDebit();
                        endCash = endCash.add(endBalance(tBasicSubjectMessage));

                        initCash = initBalance(tBasicSubjectMessage);
                        continue;
                    }
                    if (subCode.equals("1002")) {
                        endCash = endCash.add(endBalance(tBasicSubjectMessage));
//							endCash.add(tBasicSubjectMessage.getEndingBalanceDebit());

                        initCash = initCash.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1012")) {
                        endCash = endCash.add(endBalance(tBasicSubjectMessage));
//							endCash.add(tBasicSubjectMessage.getEndingBalanceDebit());

                        initCash = initCash.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 交易性金融资产 */
                    if (subCode.equals("1101")) {
                        // 期末余额
                        BigDecimal endTransactionMonetaryAssets = endBalance(tBasicSubjectMessage);
//							BigDecimal endTransactionMonetaryAssets = tBasicSubjectMessage.getEndingBalanceDebit();
                        tBasicBalanceSheet.setEndTransactionMonetaryAssets(endTransactionMonetaryAssets);

                        // 年初余额
                        BigDecimal initTransactionMonetaryAssets = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitTransactionMonetaryAssets(initTransactionMonetaryAssets);
                        continue;
                    }

                    /** 应收票据 */
                    if (subCode.equals("1121")) {
                        BigDecimal endNotesReceivable = endBalance(tBasicSubjectMessage);
//							BigDecimal endNotesReceivable = tBasicSubjectMessage.getEndingBalanceDebit();
                        tBasicBalanceSheet.setEndNotesReceivable(endNotesReceivable);

                        // 年初余额
                        BigDecimal initNotesReceivable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitNotesReceivable(initNotesReceivable);
                        continue;
                    }

                    /** 应收帐款 这个特殊*/
//						if (subCode.startsWith("1122") && lowermostCoding1122 == codeLevel)
                    if (subCode.startsWith("1122")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期初余额   只取最下级别的 借方金额总和(借方  - 贷方 大于零 为借方)
                            BigDecimal endAmountDebit1122 = endBalance(tBasicSubjectMessage);
                            if (endAmountDebit1122.signum() > 0) {
                                endAccountsReceivable = endAccountsReceivable.add(endAmountDebit1122);
                                //							endAccountsReceivable = endAccountsReceivable.add(tBasicSubjectMessage.getEndingBalanceDebit());
                            }
                            // 年初余额  只取最下级别的借方金额总和(借方  - 贷方 大于零 为借方)
                            BigDecimal yearAmountDebit1122 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit1122.signum() > 0) {
                                initAccountsReceivable = initAccountsReceivable.add(yearAmountDebit1122);
                            }
                            //							initAccountsReceivable = initAccountsReceivable.add(tBasicSubjectMessage.getYearAmountDebit());
                        }
                    }
//						if (subCode.startsWith("2203") && lowermostCoding2203 == codeLevel)
                    if (subCode.startsWith("2203")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期初余额 只取最下级别的借方金额总和(借方  - 贷方 大于零 为借方)
                            // 期初余额 只取最下级别的借方金额总和(贷方  - 借方 小于零 为借方)
                            BigDecimal endAmountDebit2203 = endBalance(tBasicSubjectMessage);
                            if (endAmountDebit2203.signum() < 0) {
                                endAccountsReceivable = endAccountsReceivable.add(endAmountDebit2203.negate());
                                //							endAccountsReceivable = endAccountsReceivable.add(tBasicSubjectMessage.getEndingBalanceDebit());
                            }
                            // 年初余额  只取最下级别的借方金额总和(借方  - 贷方 大于零 为借方)
                            BigDecimal yearAmountDebit2203 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit2203.signum() < 0) {
                                // 借方取出来是负数  要取反
                                initAccountsReceivable = initAccountsReceivable.add(yearAmountDebit2203.negate());
                            }
                            //							initAccountsReceivable = initAccountsReceivable.add(tBasicSubjectMessage.getYearAmountDebit());
                        }
                    }
                    // 借贷方都计算
//						if (subCode.startsWith("1231") && lowermostCoding1231 == codeLevel)
                    if (subCode.startsWith("1231")) {

                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            endAccountsReceivable = endAccountsReceivable.subtract(endBalance(tBasicSubjectMessage));
                            //							endAccountsReceivable = endAccountsReceivable.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                            // 年初余额
                            initAccountsReceivable = initAccountsReceivable.subtract(initBalance(tBasicSubjectMessage));
                            //							initAccountsReceivable = initAccountsReceivable.subtract(tBasicSubjectMessage.getYearAmountDebit());
                        }
                    }

                    /** 预付帐款 特殊*/
//						if (subCode.startsWith("1123") && lowermostCoding1123 == codeLevel)
                    if (subCode.startsWith("1123")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期初余额 只取最下级别的借方金额总和(借方  - 贷方 大于零 为借方)  借方科目
                            // 期初余额 只取最下级别的借方金额总和(贷方  - 借方 小于零 为借方)  贷方科目
                            //							endAccountsPrepaid = endAccountsPrepaid.add(tBasicSubjectMessage.getEndingBalanceDebit());
                            BigDecimal endingBalanceDebit1123 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit1123.signum() > 0) {
                                endAccountsPrepaid = endAccountsPrepaid.add(endingBalanceDebit1123);
                            }

                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountDebit1123 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit1123.signum() > 0) {
                                initAccountsPrepaid = initAccountsPrepaid.add(yearAmountDebit1123);
                            }
                            //							initAccountsPrepaid = initAccountsPrepaid.add(initBalance(tBasicSubjectMessage));
                        }
                    }
                    if (subCode.startsWith("2202"))
//							if (subCode.startsWith("2202") && lowermostCoding2202 == codeLevel)
                    {
//							endAccountsPrepaid = endAccountsPrepaid.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        // 期初余额 只取最下级别的借方金额总和(借方  - 贷方 大于零 为借方)
                        // 期初余额 只取最下级别的借方金额总和(贷方  - 借方 小于零 为借方)

                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            BigDecimal endingBalanceDebit2202 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit2202.signum() < 0) {
                                // 只取借方值  公式取出为负数要取反
                                endAccountsPrepaid = endAccountsPrepaid.add(endingBalanceDebit2202.negate());
                            }

                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountDebit2202 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit2202.signum() < 0) {
                                // 借方取出来是负数  要取反
                                initAccountsPrepaid = initAccountsPrepaid.add(yearAmountDebit2202.negate());
                            }
                            //							initAccountsPrepaid = initAccountsPrepaid.add(initBalance(tBasicSubjectMessage));
                        }
                    }

                    /** 应收利息*/
                    if (subCode.equals("1132")) {
                        // 期末余额
//							BigDecimal endInterestReceivable = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endInterestReceivable = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndInterestReceivable(endInterestReceivable);

                        // 年初余额
                        BigDecimal initInterestReceivable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitInterestReceivable(initInterestReceivable);
                        continue;
                    }

                    /** 应收股利*/
                    if (subCode.equals("1131")) {
                        // 期末余额
//							BigDecimal endDividendReceivable = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endDividendReceivable = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndDividendReceivable(endDividendReceivable);

                        // 年初余额
                        BigDecimal initDividendReceivable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitDividendReceivable(initDividendReceivable);
                        continue;
                    }

                    /** 其他应收款*/
                    if (subCode.equals("1221")) {
                        // 期末余额
//							BigDecimal endOtherReceivables = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endOtherReceivables = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndOtherReceivables(endOtherReceivables);

                        // 年初余额
                        BigDecimal initOtherReceivables = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitOtherReceivables(initOtherReceivables);
                        continue;
                    }

                    /** 存货 */
                    if (subCode.equals("1321")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("2314")) {
                        // 期末余额
//							endInventories = endInventories.subtract(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.subtract(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1401")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1402")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1403")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1404")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1405")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1406")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1408")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1411")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("5001")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("5201")) {
                        // 期末余额
//							endInventories = endInventories.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1471")) {
                        // 期末余额
//							endInventories = endInventories.subtract(tBasicSubjectMessage.getEndingBalanceDebit());
                        endInventories = endInventories.subtract(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initInventories = initInventories.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 其他流动资产 特殊 */
                    if (subCode.equals("1901")) {
                        // 期末余额
//							endOtherCurrentAssets = endOtherCurrentAssets.add(tBasicSubjectMessage.getEndingBalanceDebit());
                        endOtherCurrentAssets = endOtherCurrentAssets.add(endBalance(tBasicSubjectMessage));

                        // 年初余额
                        initOtherCurrentAssets = initOtherCurrentAssets.add(initBalance(tBasicSubjectMessage));
                    }
//						if (subCode.startsWith("3101") && lowermostCoding3101 == codeLevel)
                    if (subCode.startsWith("3101")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末余额
                            //							endOtherCurrentAssets = endOtherCurrentAssets.add(tBasicSubjectMessage.getEndingBalanceDebit());
                            BigDecimal endingBalanceDebit3101 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit3101.signum() > 0) {
                                // 只取借方值
                                endOtherCurrentAssets = endOtherCurrentAssets.add(endingBalanceDebit3101);
                            }

                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountDebit3101 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit3101.signum() > 0) {
                                // 取出借方
                                initOtherCurrentAssets = initOtherCurrentAssets.add(yearAmountDebit3101);
                            }
                        }
                    }
//						if (subCode.startsWith("3201") && lowermostCoding3201 == codeLevel)
                    if (subCode.startsWith("3201")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末余额
                            BigDecimal endingBalanceDebit3201 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit3201.signum() > 0) {
                                // 只取借方值
                                endOtherCurrentAssets = endOtherCurrentAssets.add(endingBalanceDebit3201);
                            }

                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountDebit3201 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit3201.signum() > 0) {
                                // 取出借方
                                initOtherCurrentAssets = initOtherCurrentAssets.add(yearAmountDebit3201);
                            }
                        }
                    }
//						if (subCode.startsWith("3202") && lowermostCoding3202 == codeLevel)
                    if (subCode.startsWith("3202")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末余额
                            BigDecimal endingBalanceDebit3202 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit3202.signum() > 0) {
                                // 只取借方值
                                endOtherCurrentAssets = endOtherCurrentAssets.add(endingBalanceDebit3202);
                            }

                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountDebit3202 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit3202.signum() > 0) {
                                // 取出借方
                                initOtherCurrentAssets = initOtherCurrentAssets.add(yearAmountDebit3202);
                            }
                        }
                    }

                    /** 可供出售金融资产*/
                    if (subCode.equals("1503")) {
//							BigDecimal endAvailableForSaleFinancialAssets = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endAvailableForSaleFinancialAssets = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndAvailableForSaleFinancialAssets(endAvailableForSaleFinancialAssets);

                        BigDecimal initAvailableForSaleFinancialAssets = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitAvailableForSaleFinancialAssets(initAvailableForSaleFinancialAssets);
                        continue;
                    }

                    /** 持有至到期投*/
                    if (subCode.equals("1501")) {
//							endHeldToMaturityInvestmen = tBasicSubjectMessage.getEndingBalanceDebit();
                        endHeldToMaturityInvestmen = endHeldToMaturityInvestmen.add(endBalance(tBasicSubjectMessage));

                        initHeldToMaturityInvestmen = initHeldToMaturityInvestmen.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1502")) {
                        endHeldToMaturityInvestmen = endHeldToMaturityInvestmen.subtract(endBalance(tBasicSubjectMessage));
//							endHeldToMaturityInvestmen.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initHeldToMaturityInvestmen = initHeldToMaturityInvestmen.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 长期应收款*/
                    if (subCode.equals("1531")) {
                        endLongTermReceivables = endLongTermReceivables.add(endBalance(tBasicSubjectMessage));
//							endLongTermReceivables = tBasicSubjectMessage.getEndingBalanceDebit();

                        initLongTermReceivables = initLongTermReceivables.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1532")) {
                        endLongTermReceivables = endLongTermReceivables.subtract(endBalance(tBasicSubjectMessage));
//							endLongTermReceivables.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initLongTermReceivables = initLongTermReceivables.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 长期股权投资*/
                    if (subCode.equals("1511")) {
                        endLongTermEquityInvestment = endLongTermEquityInvestment.add(endBalance(tBasicSubjectMessage));
//							endLongTermEquityInvestment = tBasicSubjectMessage.getEndingBalanceDebit();

                        initLongTermEquityInvestment = initLongTermEquityInvestment.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1512")) {
                        endLongTermEquityInvestment = endLongTermEquityInvestment.subtract(endBalance(tBasicSubjectMessage));
//							endLongTermEquityInvestment.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initLongTermEquityInvestment = initLongTermEquityInvestment.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 投资性房地产*/
                    if (subCode.equals("1521")) {
//							BigDecimal endInvestmentRealEstates = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endInvestmentRealEstates = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndInvestmentRealEstates(endInvestmentRealEstates);

                        BigDecimal initInvestmentRealEstates = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitInvestmentRealEstates(initInvestmentRealEstates);
                        continue;
                    }

                    /** 固定资产*/
                    if (subCode.equals("1601")) {
                        endFixedAssets = endFixedAssets.add(endBalance(tBasicSubjectMessage));
//							endFixedAssets = tBasicSubjectMessage.getEndingBalanceDebit();

                        initFixedAssets = initFixedAssets.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1602")) {
                        endFixedAssets = endFixedAssets.subtract(endBalance(tBasicSubjectMessage));
//							endFixedAssets.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initFixedAssets = initFixedAssets.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1603")) {
                        endFixedAssets = endFixedAssets.subtract(endBalance(tBasicSubjectMessage));
//							endFixedAssets.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initFixedAssets = initFixedAssets.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 在建工程 */
                    if (subCode.equals("1604")) {
//							BigDecimal endConstructionInProgress = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endConstructionInProgress = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndConstructionInProgress(endConstructionInProgress);

                        BigDecimal initConstructionInProgress = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitConstructionInProgress(initConstructionInProgress);
                        continue;
                    }

                    /** 工程物资*/
                    if (subCode.equals("1605")) {
//							BigDecimal endConstructionSupplies = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endConstructionSupplies = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndConstructionSupplies(endConstructionSupplies);

                        BigDecimal initConstructionSupplies = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitConstructionSupplies(initConstructionSupplies);
                        continue;
                    }

                    /** 固定资产清理 */
                    if (subCode.equals("1606")) {
//							BigDecimal endFixedAssetsPendingDisposal = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endFixedAssetsPendingDisposal = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndFixedAssetsPendingDisposal(endFixedAssetsPendingDisposal);

                        BigDecimal initFixedAssetsPendingDisposal = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitFixedAssetsPendingDisposal(initFixedAssetsPendingDisposal);
                        continue;
                    }

                    /** 无形资产*/
                    if (subCode.equals("1701")) {
//							endIntangibelAssets = tBasicSubjectMessage.getEndingBalanceDebit();
                        endIntangibelAssets = endIntangibelAssets.add(endBalance(tBasicSubjectMessage));

                        initIntangibelAssets = initIntangibelAssets.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1702")) {
                        endIntangibelAssets = endIntangibelAssets.subtract(endBalance(tBasicSubjectMessage));
//							endIntangibelAssets.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initIntangibelAssets = initIntangibelAssets.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("1703")) {
                        endIntangibelAssets = endIntangibelAssets.subtract(endBalance(tBasicSubjectMessage));
//							endIntangibelAssets.subtract(tBasicSubjectMessage.getEndingBalanceDebit());

                        initIntangibelAssets = initIntangibelAssets.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 开发支出 */
                    if (subCode.equals("5301")) {
//							BigDecimal endResearchAndDevelopmentCosts = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endResearchAndDevelopmentCosts = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndResearchAndDevelopmentCosts(endResearchAndDevelopmentCosts);

                        BigDecimal initResearchAndDevelopmentCosts = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitResearchAndDevelopmentCosts(initResearchAndDevelopmentCosts);
                        continue;
                    }

                    /** 商誉 */
                    if (subCode.equals("1711")) {
//							BigDecimal endGoodwill = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endGoodwill = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndGoodwill(endGoodwill);

                        BigDecimal initGoodwill = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitGoodwill(initGoodwill);
                        continue;
                    }

                    /** 长期待摊费用 */
                    if (subCode.equals("1801")) {
//							BigDecimal endLongTermDeferredExpenses = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endLongTermDeferredExpenses = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndLongTermDeferredExpenses(endLongTermDeferredExpenses);

                        BigDecimal initLongTermDeferredExpenses = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitLongTermDeferredExpenses(initLongTermDeferredExpenses);
                        continue;
                    }

                    /** 递延所得税资产 */
                    if (subCode.equals("1811")) {
//							BigDecimal endDeferredTaxAssets = tBasicSubjectMessage.getEndingBalanceDebit();
                        BigDecimal endDeferredTaxAssets = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndDeferredTaxAssets(endDeferredTaxAssets);

                        BigDecimal initDeferredTaxAssets = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitDeferredTaxAssets(initDeferredTaxAssets);
                        continue;
                    }

                    /** 负债和所有者权益（或股东权益）*/
                    /** 短期借款 Short-term loan*/
                    if (subCode.equals("2001")) {
//							BigDecimal endShortTermLoan = tBasicSubjectMessage.getEndingBalanceCredit();
                        BigDecimal endShortTermLoan = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndShortTermLoan(endShortTermLoan);

                        BigDecimal initShortTermLoan = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitShortTermLoan(initShortTermLoan);
                        continue;
                    }

                    /** 交易性金融负债 tradable financial liabilities*/
                    if (subCode.equals("2101")) {
                        BigDecimal endTradableFinancialLiabilities = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndTradableFinancialLiabilities(endTradableFinancialLiabilities);

                        BigDecimal initTradableFinancialLiabilities = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitTradableFinancialLiabilities(initTradableFinancialLiabilities);
                        continue;
                    }

                    /** 应付票据 Notes payable */
                    if (subCode.equals("2201")) {
                        BigDecimal endNotesPayable = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndNotesPayable(endNotesPayable);

                        BigDecimal initNotesPayable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitNotesPayable(initNotesPayable);
                        continue;
                    }

                    /** 应付帐款 Accounts payable  这个特殊*/
//						if (subCode.startsWith("2202") && lowermostCoding2202 == codeLevel)
                    if (subCode.startsWith("2202")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();

                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);


                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末取2202  下面所有的贷方科目金额总和
                            // 2202为贷方科目  贷方  （贷 - 借 = 大于零的 为贷）
                            BigDecimal endingBalanceCredit2202 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceCredit2202.signum() > 0) {
                                endAccountsPayable = endAccountsPayable.add(endingBalanceCredit2202);
                            }

                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountCredit2202 = initBalance(tBasicSubjectMessage);
                            if (yearAmountCredit2202.signum() > 0) {
                                initAccountsPayable = initAccountsPayable.add(yearAmountCredit2202);
                            }
                        }
                    }
//						if (subCode.startsWith("1123") && lowermostCoding1123 == codeLevel)
                    if (subCode.startsWith("1123")) {
                        //  只取最下级别的贷方金额总和  1123借方科目 只取 贷方下面的总和
                        //  借方  （借 - 贷 = 小于零的 为贷）
                        Map<String, Object> hashMap = new HashMap<String, Object>();

                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);
                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            BigDecimal endingBalanceCredit1123 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceCredit1123.signum() < 0) {
                                // 取反再相加
                                endAccountsPayable = endAccountsPayable.add(endingBalanceCredit1123.negate());
                            }

                            // 年初余额  只取最下级别的贷方金额总和  1123借方科目 只取 贷方下面的总和
                            // 借方  （借 - 贷 = 小于零的 为贷）
                            BigDecimal yearAmountCredit1123 = initBalance(tBasicSubjectMessage);
                            if (yearAmountCredit1123.signum() < 0) {
                                // 取反再相加
                                initAccountsPayable = initAccountsPayable.add(yearAmountCredit1123.negate());
                            }
                        }
                    }


                    /** 预收款项 advance receipts 2203贷方科目  特殊*/
//					    if (subCode.startsWith("2203") && lowermostCoding2203 == codeLevel)
                    if (subCode.startsWith("2203")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末2203 下面所有的  贷方科目金额总和（贷方  - 借方  大于零  为贷方）
//						    	endAdvanceReceipts = endAdvanceReceipts.add(tBasicSubjectMessage.getEndingBalanceCredit());
                            BigDecimal endingBalanceCredit2203 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceCredit2203.signum() > 0) {
                                endAdvanceReceipts = endAdvanceReceipts.add(endingBalanceCredit2203);
                            }

                            // 年初余额  只取最下级别的贷方金额总和  2203 下面所有的  贷方科目金额总和（贷方  - 借方  大于零  为贷方）
                            BigDecimal yearAmountCredit2203 = initBalance(tBasicSubjectMessage);
                            if (yearAmountCredit2203.signum() > 0) {
                                initAdvanceReceipts = initAdvanceReceipts.add(yearAmountCredit2203);
                            }
                        }

                    }
//						if (subCode.startsWith("1122") && lowermostCoding1122 == codeLevel)
                    if (subCode.startsWith("1122")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末1122 下面所有的贷方科目金额总和（借方 - 贷方  小于零  为贷方）
                            BigDecimal endingBalanceCredit1122 = endBalance(tBasicSubjectMessage);
                            // 取出所有负数 之和  再取反
                            if (endingBalanceCredit1122.signum() < 0) {
                                endAdvanceReceipts = endAdvanceReceipts.add(endingBalanceCredit1122.negate());
                            }
                            // 年初余额  只取最下级别的借方金额总和
                            BigDecimal yearAmountCredit1122 = initBalance(tBasicSubjectMessage);
                            if (yearAmountCredit1122.signum() < 0) {
                                initAdvanceReceipts = initAdvanceReceipts.add(yearAmountCredit1122.negate());
                            }
                        }
                    }

                    /** 应付职工薪酬 accrued payroll*/
                    if (subCode.equals("2211")) {
                        BigDecimal endAccruedPayroll = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndAccruedPayroll(endAccruedPayroll);

                        BigDecimal initAccruedPayroll = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitAccruedPayroll(initAccruedPayroll);
                        continue;
                    }

                    /** 应交税费accrued tax*/
                    if (subCode.equals("2221")) {
                        BigDecimal endAccruedTax = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndAccruedTax(endAccruedTax);

                        BigDecimal initAccruedTax = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitAccruedTax(initAccruedTax);
                        continue;
                    }

                    /** 应付利息 accrued interest payable*/
                    if (subCode.equals("2231")) {
                        BigDecimal endAccruedInterestPayable = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndAccruedInterestPayable(endAccruedInterestPayable);

                        BigDecimal initAccruedInterestPayable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitAccruedInterestPayable(initAccruedInterestPayable);
                        continue;
                    }

                    /** 应付股利dividend payable*/
                    if (subCode.equals("2232")) {
                        BigDecimal endDividendPayable = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndDividendPayable(endDividendPayable);

                        BigDecimal initDividendPayable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitDividendPayable(initDividendPayable);
                        continue;
                    }

                    /** 其他应付款other payables */
                    if (subCode.equals("2241")) {
                        BigDecimal endOtherPayables = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndOtherPayables(endOtherPayables);

                        BigDecimal initOtherPayables = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitOtherPayables(initOtherPayables);
                        continue;
                    }

                    /** 其他流动负债other current liabilities  特殊*/
//						if (subCode.startsWith("3101") && lowermostCoding3101 == codeLevel)
                    if (subCode.startsWith("3101")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末余额
                            BigDecimal endingBalanceDebit3101 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit3101.signum() < 0) {
                                // 只取贷方值
                                endOtherCurrentLiabilities = endOtherCurrentLiabilities.add(endingBalanceDebit3101);
                            }

                            // 年初余额  只取最下级别的贷方金额总和
                            BigDecimal yearAmountDebit3101 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit3101.signum() < 0) {
                                // 取出贷方
                                initOtherCurrentLiabilities = initOtherCurrentLiabilities.add(yearAmountDebit3101);
                            }
                            continue;
                        }
                    }
//						if (subCode.startsWith("3201") && lowermostCoding3201 == codeLevel)
                    if (subCode.startsWith("3201")) {
                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末余额
                            BigDecimal endingBalanceDebit3201 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit3201.signum() < 0) {
                                // 只取贷方值
                                endOtherCurrentLiabilities = endOtherCurrentLiabilities.add(endingBalanceDebit3201);
                            }

                            // 年初余额  只取最下级别的贷方金额总和
                            BigDecimal yearAmountDebit3201 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit3201.signum() < 0) {
                                // 取出贷方
                                initOtherCurrentLiabilities = initOtherCurrentLiabilities.add(yearAmountDebit3201);
                            }
                            continue;
                        }
                    }
                    if (subCode.startsWith("3202")) {

                        Map<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("busDate", busDate);
                        hashMap.put("accountID", accountId);
                        hashMap.put("subCode", subCode);

                        int querySubjectByCode = tBasicSubjectMessageMapper.querySubjectByCode(hashMap);
                        if (querySubjectByCode == 1) {
                            // 期末余额
                            BigDecimal endingBalanceDebit3202 = endBalance(tBasicSubjectMessage);
                            if (endingBalanceDebit3202.signum() < 0) {
                                // 只取贷方值
                                endOtherCurrentLiabilities = endOtherCurrentLiabilities.add(endingBalanceDebit3202);
                            }

                            // 年初余额  只取最下级别的贷方金额总和
                            BigDecimal yearAmountDebit3202 = initBalance(tBasicSubjectMessage);
                            if (yearAmountDebit3202.signum() < 0) {
                                // 取出贷方
                                initOtherCurrentLiabilities = initOtherCurrentLiabilities.add(yearAmountDebit3202);
                            }
                            continue;
                        }
                    }

                    /** 长期借款long-term loan*/
                    if (subCode.equals("2501")) {
                        BigDecimal endLongTermLoan = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndLongTermLoan(endLongTermLoan);

                        BigDecimal initLongTermLoan = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitLongTermLoan(initLongTermLoan);
                        continue;
                    }

                    /** 应付债券bonds payable*/
                    if (subCode.equals("2502")) {
                        BigDecimal endBondsPayable = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndBondsPayable(endBondsPayable);

                        BigDecimal initBondsPayable = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitBondsPayable(initBondsPayable);
                        continue;
                    }

                    /** 长期应付款long-term accounts payable*/
                    if (subCode.equals("2701")) {
                        endLongTermAccountsPayable = endLongTermAccountsPayable.add(endBalance(tBasicSubjectMessage));

                        initLongTermAccountsPayable = initLongTermAccountsPayable.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("2702")) {
                        endLongTermAccountsPayable = endLongTermAccountsPayable.subtract(endBalance(tBasicSubjectMessage));

                        initLongTermAccountsPayable = initLongTermAccountsPayable.subtract(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                    /** 专项应付款accounts payable for specialised terms*/
                    if (subCode.equals("2711")) {
                        BigDecimal endAccountsPayableForSpecialisedTerms = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndAccountsPayableForSpecialisedTerms(endAccountsPayableForSpecialisedTerms);

                        BigDecimal initAccountsPayableForSpecialisedTerms = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitAccountsPayableForSpecialisedTerms(initAccountsPayableForSpecialisedTerms);
                        continue;
                    }

                    /** 预计负债provision for liabilities*/
                    if (subCode.equals("2801")) {
                        BigDecimal endProvisionForLiabilities = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndProvisionForLiabilities(endProvisionForLiabilities);

                        BigDecimal initProvisionForLiabilities = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitProvisionForLiabilities(initProvisionForLiabilities);
                        continue;
                    }

                    /** 递延所得税负债deferred income tax liabilities*/
                    if (subCode.equals("2901")) {
                        BigDecimal endDeferredIncomeTaxLiabilities = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndDeferredIncomeTaxLiabilities(endDeferredIncomeTaxLiabilities);

                        BigDecimal initDeferredIncomeTaxLiabilities = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitDeferredIncomeTaxLiabilities(initDeferredIncomeTaxLiabilities);
                        continue;
                    }

                    /** 其他非流动负债other non-current liabilities*/
                    if (subCode.equals("2401")) {
                        BigDecimal endOtherNonCurrentLiabilities = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndOtherNonCurrentLiabilities(endOtherNonCurrentLiabilities);

                        BigDecimal initOtherNonCurrentLiabilities = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitOtherNonCurrentLiabilities(initOtherNonCurrentLiabilities);
                        continue;
                    }

                    /** 所有者权益（或股东权益）： */
                    /** 实收资本capital*/
                    if (subCode.equals("4001")) {
                        BigDecimal endCapital = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndCapital(endCapital);

                        BigDecimal initCapital = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitCapital(initCapital);
                        continue;
                    }
                    /** 资本公积capital reserves*/
                    if (subCode.equals("4002")) {
                        BigDecimal entCapitalReserves = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEntCapitalReserves(entCapitalReserves);

                        BigDecimal initCapitalReserves = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitCapitalReserves(initCapitalReserves);
                        continue;
                    }

                    /** 减：库存股less: treasury stock*/
                    if (subCode.equals("4201")) {
                        BigDecimal endLessTreasuryStock = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndLessTreasuryStock(endLessTreasuryStock);

                        BigDecimal initLessTreasuryStock = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitLessTreasuryStock(initLessTreasuryStock);
                        continue;
                    }

                    /** 盈余公积earnings reserve*/
                    if (subCode.equals("4101")) {
                        BigDecimal endEarningsReserve = endBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setEndEarningsReserve(endEarningsReserve);

                        BigDecimal initEarningsReserve = initBalance(tBasicSubjectMessage);
                        tBasicBalanceSheet.setInitEarningsReserve(initEarningsReserve);
                        continue;
                    }

                    /** 未分配利润retained earnings*/
                    if (subCode.equals("4103")) {
                        endRetainedEarnings = endRetainedEarnings.add(endBalance(tBasicSubjectMessage));

                        initRetainedEarnings = initRetainedEarnings.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }
                    if (subCode.equals("4104")) {
                        endRetainedEarnings = endRetainedEarnings.add(endBalance(tBasicSubjectMessage));

                        initRetainedEarnings = initRetainedEarnings.add(initBalance(tBasicSubjectMessage));
                        continue;
                    }

                }
            }

            /** 货币资金 Cash */
            tBasicBalanceSheet.setEndCash(endCash);
            tBasicBalanceSheet.setInitCash(initCash);

            /** 应收帐款*/
            tBasicBalanceSheet.setEndAccountsReceivable(endAccountsReceivable);
            tBasicBalanceSheet.setInitAccountsReceivable(initAccountsReceivable);

            /** 预付帐款*/
            tBasicBalanceSheet.setEndAccountsPrepaid(endAccountsPrepaid);
            tBasicBalanceSheet.setInitAccountsPrepaid(initAccountsPrepaid);

            /** 存货 */
            tBasicBalanceSheet.setEndInventories(endInventories);
            tBasicBalanceSheet.setInitInventories(initInventories);

            /** 其他流动资产 */
            tBasicBalanceSheet.setEndOtherCurrentAssets(endOtherCurrentAssets);
            tBasicBalanceSheet.setInitOtherCurrentAssets(initOtherCurrentAssets);

            /** 流动资产合计 */
            // 期末
            BigDecimal endTotalCurrentAssets = new BigDecimal("0.0");
            endTotalCurrentAssets = endTotalCurrentAssets.add(endCash);
            BigDecimal endTransactionMonetaryAssets = tBasicBalanceSheet.getEndTransactionMonetaryAssets() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndTransactionMonetaryAssets();
            endTotalCurrentAssets = endTotalCurrentAssets.add(endTransactionMonetaryAssets);
            BigDecimal endNotesReceivable = tBasicBalanceSheet.getEndNotesReceivable() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndNotesReceivable();
            endTotalCurrentAssets = endTotalCurrentAssets.add(endNotesReceivable);
            endTotalCurrentAssets = endTotalCurrentAssets.add(endAccountsReceivable);
            endTotalCurrentAssets = endTotalCurrentAssets.add(endAccountsPrepaid);

            BigDecimal endInterestReceivable = tBasicBalanceSheet.getEndInterestReceivable() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndInterestReceivable();
            endTotalCurrentAssets = endTotalCurrentAssets.add(endInterestReceivable);

            BigDecimal endDividendReceivable = tBasicBalanceSheet.getEndDividendReceivable() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndDividendReceivable();
            endTotalCurrentAssets = endTotalCurrentAssets.add(endDividendReceivable);

            BigDecimal endOtherReceivables = tBasicBalanceSheet.getEndOtherReceivables() == null ? new BigDecimal(0) : tBasicBalanceSheet.getEndOtherReceivables();
            endTotalCurrentAssets = endTotalCurrentAssets.add(endOtherReceivables);
            endTotalCurrentAssets = endTotalCurrentAssets.add(endInventories);
            endTotalCurrentAssets = endTotalCurrentAssets.add(endOtherCurrentAssets);
            tBasicBalanceSheet.setEndTotalCurrentAssets(endTotalCurrentAssets);

            // 年初
            BigDecimal initTotalCurrentAssets = new BigDecimal("0.0");
            initTotalCurrentAssets = initTotalCurrentAssets.add(initCash);
            initTotalCurrentAssets = initTotalCurrentAssets.add(tBasicBalanceSheet.getInitTransactionMonetaryAssets());
            initTotalCurrentAssets = initTotalCurrentAssets.add(tBasicBalanceSheet.getInitNotesReceivable());
            initTotalCurrentAssets = initTotalCurrentAssets.add(initAccountsReceivable);
            initTotalCurrentAssets = initTotalCurrentAssets.add(initAccountsPrepaid);
            initTotalCurrentAssets = initTotalCurrentAssets.add(tBasicBalanceSheet.getInitInterestReceivable());
            initTotalCurrentAssets = initTotalCurrentAssets.add(tBasicBalanceSheet.getInitDividendReceivable());
            initTotalCurrentAssets = initTotalCurrentAssets.add(tBasicBalanceSheet.getInitOtherReceivables());
            initTotalCurrentAssets = initTotalCurrentAssets.add(initInventories);
            initTotalCurrentAssets = initTotalCurrentAssets.add(initOtherCurrentAssets);
            tBasicBalanceSheet.setInitTotalCurrentAssets(initTotalCurrentAssets);

            /** 持有至到期投*/
            tBasicBalanceSheet.setEndHeldToMaturityInvestmen(endHeldToMaturityInvestmen);
            tBasicBalanceSheet.setInitHeldToMaturityInvestmen(initHeldToMaturityInvestmen);

            /** 长期应收款*/
            tBasicBalanceSheet.setEndLongTermReceivables(endLongTermReceivables);
            tBasicBalanceSheet.setInitLongTermReceivables(initLongTermReceivables);

            /** 长期股权投资*/
            tBasicBalanceSheet.setEndHeldToMaturityInvestmen(endHeldToMaturityInvestmen);
            tBasicBalanceSheet.setInitHeldToMaturityInvestmen(initHeldToMaturityInvestmen);

            /** 持有至到期投*/
            tBasicBalanceSheet.setEndLongTermEquityInvestment(endLongTermEquityInvestment);
            tBasicBalanceSheet.setInitLongTermEquityInvestment(initLongTermEquityInvestment);

            /** 无形资产*/
            tBasicBalanceSheet.setEndIntangibelAssets(endIntangibelAssets);
            tBasicBalanceSheet.setInitIntangibelAssets(initIntangibelAssets);

            /** 固定资产*/
            tBasicBalanceSheet.setEndFixedAssets(endFixedAssets);
            tBasicBalanceSheet.setInitFixedAssets(initFixedAssets);

            /** 非流动资产合计Total of non-current asses*/
            BigDecimal endOtherNonCurrentAssets = new BigDecimal("0.0");
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndAvailableForSaleFinancialAssets());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(endHeldToMaturityInvestmen);
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(endLongTermReceivables);
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(endLongTermEquityInvestment);
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndInvestmentRealEstates());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(endFixedAssets);
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndConstructionInProgress());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndConstructionSupplies());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndFixedAssetsPendingDisposal());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(endIntangibelAssets);
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndResearchAndDevelopmentCosts());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndGoodwill());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndLongTermDeferredExpenses());
            endOtherNonCurrentAssets = endOtherNonCurrentAssets.add(tBasicBalanceSheet.getEndDeferredTaxAssets());
            tBasicBalanceSheet.setEndOtherNonCurrentAssets(endOtherNonCurrentAssets);

            /** 非流动资产合计Total of non-current asses*/
            BigDecimal initOtherNonCurrentAssets = new BigDecimal("0.0");
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitAvailableForSaleFinancialAssets());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(initHeldToMaturityInvestmen);
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(initLongTermReceivables);
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(initLongTermEquityInvestment);
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitInvestmentRealEstates());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(initFixedAssets);
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitConstructionInProgress());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitConstructionSupplies());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitFixedAssetsPendingDisposal());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(initIntangibelAssets);
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitResearchAndDevelopmentCosts());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitGoodwill());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitLongTermDeferredExpenses());
            initOtherNonCurrentAssets = initOtherNonCurrentAssets.add(tBasicBalanceSheet.getInitDeferredTaxAssets());
            tBasicBalanceSheet.setInitOtherNonCurrentAssets(initOtherNonCurrentAssets);

            /** 资产总计*/
            BigDecimal endTotalOfAssets = new BigDecimal("0.0");
            endTotalOfAssets = endTotalOfAssets.add(endTotalCurrentAssets);
            endTotalOfAssets = endTotalOfAssets.add(endOtherNonCurrentAssets);
            tBasicBalanceSheet.setEndTotalOfAssets(endTotalOfAssets);

            /** 资产总计*/
            BigDecimal initTotalOfAssets = new BigDecimal("0.0");
            initTotalOfAssets = initTotalOfAssets.add(initTotalCurrentAssets);
            initTotalOfAssets = initTotalOfAssets.add(initOtherNonCurrentAssets);
            tBasicBalanceSheet.setInitTotalOfAssets(initTotalOfAssets);

            /** 应付帐款 Accounts payable*/
            tBasicBalanceSheet.setEndAccountsPayable(endAccountsPayable);
            tBasicBalanceSheet.setInitAccountsPayable(initAccountsPayable);

            /** 预收款项 advance receipts*/
            tBasicBalanceSheet.setEndAdvanceReceipts(endAdvanceReceipts);
            tBasicBalanceSheet.setInitAdvanceReceipts(initAdvanceReceipts);

            /** 其他流动负债other current liabilities*/
            /** 取反 */
            tBasicBalanceSheet.setEndOtherCurrentLiabilities(endOtherCurrentLiabilities.negate());
            tBasicBalanceSheet.setInitOtherCurrentLiabilities(initOtherCurrentLiabilities.negate());

            /** 流动负债合计Total of current liabilities*/
            BigDecimal endTotalOfCurrentLiabilities = new BigDecimal("0.0");
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndShortTermLoan());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndTradableFinancialLiabilities());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndNotesPayable());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(endAccountsPayable);
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(endAdvanceReceipts);
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndAccruedPayroll());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndAccruedTax());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndAccruedInterestPayable());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndDividendPayable());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getEndOtherPayables());
            endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities.add(endOtherCurrentLiabilities);
            tBasicBalanceSheet.setEndTotalOfCurrentLiabilities(endTotalOfCurrentLiabilities);
            /** 流动负债合计Total of current liabilities*/
            BigDecimal initTotalOfCurrentLiabilities = new BigDecimal("0.0");
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitShortTermLoan());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitTradableFinancialLiabilities());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitNotesPayable());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(initAccountsPayable);
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(initAdvanceReceipts);
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitAccruedPayroll());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitAccruedTax());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitAccruedInterestPayable());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitDividendPayable());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(tBasicBalanceSheet.getInitOtherPayables());
            initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities.add(initOtherCurrentLiabilities);
            tBasicBalanceSheet.setInitTotalOfCurrentLiabilities(initTotalOfCurrentLiabilities);

            /** 长期应付款long-term accounts payable*/
            tBasicBalanceSheet.setEndLongTermAccountsPayable(endLongTermAccountsPayable);
            tBasicBalanceSheet.setInitLongTermAccountsPayable(initLongTermAccountsPayable);

            /** 非流动负债合计Total of non-current liabilities*/
            BigDecimal endTotalOfNonCurrentLiabilities = new BigDecimal("0.0");
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getEndLongTermLoan());
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getEndBondsPayable());
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(endLongTermAccountsPayable);
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getEndAccountsPayableForSpecialisedTerms());
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getEndProvisionForLiabilities());
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getEndDeferredIncomeTaxLiabilities());
            endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getEndOtherNonCurrentLiabilities());
            tBasicBalanceSheet.setEndTotalOfNonCurrentLiabilities(endTotalOfNonCurrentLiabilities);
            /** 非流动负债合计Total of non-current liabilities*/
            BigDecimal initTotalOfNonCurrentLiabilities = new BigDecimal("0.0");
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getInitLongTermLoan());
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getInitBondsPayable());
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(initLongTermAccountsPayable);
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getInitAccountsPayableForSpecialisedTerms());
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getInitProvisionForLiabilities());
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getInitDeferredIncomeTaxLiabilities());
            initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities.add(tBasicBalanceSheet.getInitOtherNonCurrentLiabilities());
            tBasicBalanceSheet.setInitTotalOfNonCurrentLiabilities(initTotalOfNonCurrentLiabilities);

            /** 负债合计Total of liabilities*/
            BigDecimal endTotalOfLiabilities = new BigDecimal("0.0");
            endTotalOfLiabilities = endTotalOfLiabilities.add(endTotalOfCurrentLiabilities);
            endTotalOfLiabilities = endTotalOfLiabilities.add(endTotalOfNonCurrentLiabilities);
            tBasicBalanceSheet.setEndTotalOfLiabilities(endTotalOfLiabilities);
            /** 负债合计Total of liabilities*/
            BigDecimal initTotalOfLiabilities = new BigDecimal("0.0");
            initTotalOfLiabilities = initTotalOfLiabilities.add(initTotalOfCurrentLiabilities);
            initTotalOfLiabilities = initTotalOfLiabilities.add(initTotalOfNonCurrentLiabilities);
            tBasicBalanceSheet.setInitTotalOfLiabilities(initTotalOfLiabilities);

            /** 所有者权益（或股东权益）： */
            /** 未分配利润retained earnings*/
            tBasicBalanceSheet.setEndRetainedEarnings(endRetainedEarnings);
            tBasicBalanceSheet.setInitRetainedEarnings(initRetainedEarnings);

            /** 所有者权益总计Total of owners' equity*/
            BigDecimal endTotalOfOwnersEquity = new BigDecimal("0.0");
            endTotalOfOwnersEquity = endTotalOfOwnersEquity.add(tBasicBalanceSheet.getEndCapital());
            endTotalOfOwnersEquity = endTotalOfOwnersEquity.add(tBasicBalanceSheet.getEntCapitalReserves());
            endTotalOfOwnersEquity = endTotalOfOwnersEquity.add(tBasicBalanceSheet.getEndEarningsReserve());
            endTotalOfOwnersEquity = endTotalOfOwnersEquity.add(endRetainedEarnings);
            endTotalOfOwnersEquity = endTotalOfOwnersEquity.subtract(tBasicBalanceSheet.getEndLessTreasuryStock());
            tBasicBalanceSheet.setEndTotalOfOwnersEquity(endTotalOfOwnersEquity);
            /** 所有者权益总计Total of owners' equity*/
            BigDecimal initTotalOfOwnersEquity = new BigDecimal("0.0");
            initTotalOfOwnersEquity = initTotalOfOwnersEquity.add(tBasicBalanceSheet.getInitCapital());
            initTotalOfOwnersEquity = initTotalOfOwnersEquity.add(tBasicBalanceSheet.getInitCapitalReserves());
            initTotalOfOwnersEquity = initTotalOfOwnersEquity.add(tBasicBalanceSheet.getInitEarningsReserve());
            initTotalOfOwnersEquity = initTotalOfOwnersEquity.add(initRetainedEarnings);
            initTotalOfOwnersEquity = initTotalOfOwnersEquity.subtract(tBasicBalanceSheet.getInitLessTreasuryStock());
            tBasicBalanceSheet.setInitTotalOfOwnersEquity(initTotalOfOwnersEquity);

            /** 负债和所有者权益总计Total of liabilities and owners' equity*/
            BigDecimal endTotalOfLiabilitiesAndOwnersEquity = new BigDecimal("0.0");
            endTotalOfLiabilitiesAndOwnersEquity = endTotalOfLiabilitiesAndOwnersEquity.add(endTotalOfLiabilities);
            endTotalOfLiabilitiesAndOwnersEquity = endTotalOfLiabilitiesAndOwnersEquity.add(endTotalOfOwnersEquity);
            tBasicBalanceSheet.setEndTotalOfLiabilitiesAndOwnersEquity(endTotalOfLiabilitiesAndOwnersEquity);
            /** 负债和所有者权益总计Total of liabilities and owners' equity*/
            BigDecimal initTotalOfLiabilitiesAndOwnersEquity = new BigDecimal("0.0");
            initTotalOfLiabilitiesAndOwnersEquity = initTotalOfLiabilitiesAndOwnersEquity.add(initTotalOfLiabilities);
            initTotalOfLiabilitiesAndOwnersEquity = initTotalOfLiabilitiesAndOwnersEquity.add(initTotalOfOwnersEquity);
            tBasicBalanceSheet.setInitTotalOfLiabilitiesAndOwnersEquity(initTotalOfLiabilitiesAndOwnersEquity);

            /** 类别（1.资产 2.负债 3.所有者权益（或股东权益）） */
            //		    private String type;

            /** 详细类别(1. 流动资产 2.非流动资产 3.流动负债 4.非流动负债 5.所有者权益（或股东权益）） */
            //		    private String detailType;

            /** 更新时间 */
            Date date = new Date();
            tBasicBalanceSheet.setUpdateDate(date);

            /** 时间戳 */
            String timestamp = String.valueOf(date.getTime());
            tBasicBalanceSheet.setUpdateTimestamp(timestamp);
            deleteBalanceSheet(user, account);
            int no = tBasicBalanceSheetMapper.addBalanceSheet(tBasicBalanceSheet);
            result.put("no", no);
            result.put("tBasicBalanceSheet", tBasicBalanceSheet);
            result.put("code", 1);
        }

        return result;
    }

    /**
     * @param tBasicSubjectMessage
     * @return BigDecimal    返回类型
     * @Title: endBalance 获取期末余额方法
     * @Description: 判断如果科目为 借方 科目  期末余额等于期末余额借方  减去  期末余额贷方
     * @Description: 判断如果科目为 贷方 科目  期末余额等于期末余额贷方  减去  期末余额借方
     * @date 2018年2月27日  下午2:52:55
     * @author SiLiuDong 司氏旭东
     */
    public BigDecimal endBalance(TBasicSubjectMessage tBasicSubjectMessage) {
        BigDecimal endingBalanceDebit = new BigDecimal("0");
        BigDecimal endingBalanceCredit = new BigDecimal("0");
        if (tBasicSubjectMessage.getEndingBalanceDebit() != null) {
            // 期末余额(借方)
            endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit().compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal("0.0") : tBasicSubjectMessage.getEndingBalanceDebit();
        }
        if (tBasicSubjectMessage.getEndingBalanceCredit() != null) {
            // 期末余额(贷方)
            endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit().compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal("0.0") : tBasicSubjectMessage.getEndingBalanceCredit();
        }
        String debitCreditDirection = tBasicSubjectMessage.getDebitCreditDirection();
        BigDecimal money = new BigDecimal("0.0");
        if (StringUtils.isNotBlank(debitCreditDirection)) {
            if (debitCreditDirection.equals("1")) {
                money = endingBalanceDebit.subtract(endingBalanceCredit);
            }
            if (debitCreditDirection.equals("2")) {
                money = endingBalanceCredit.subtract(endingBalanceDebit);
            }
        }
        return money;
    }

    /**
     * @param tBasicSubjectMessage
     * @return BigDecimal    返回类型
     * @Title: initBalance 获取年初余额方法
     * @Description: 判断如果科目为 借方 科目  期末借方 - 期末贷方 - 本年累计发生额(借方) + 本年累计发生额(贷方)
     * @Description: 判断如果科目为 贷方 科目  期末贷方 - 期末借方  + 本年累计发生额(借方) - 本年累计发生额(贷方)
     * @date 2018年2月27日  下午2:52:55
     * @author SiLiuDong 司氏旭东
     */
    public BigDecimal initBalance(TBasicSubjectMessage tBasicSubjectMessage) {
        // 期末余额(借方
        BigDecimal endingBalanceDebit = new BigDecimal("0");
        // 期末余额(贷方)
        BigDecimal endingBalanceCredit = new BigDecimal("0");
        // 本年累计发生额(借方)
        BigDecimal yearAmountDebit = new BigDecimal("0");
        // 本年累计发生额(贷方)
        BigDecimal yearAmountCredit = new BigDecimal("0");
        if (tBasicSubjectMessage.getEndingBalanceDebit() != null) {
            // 期末余额(借方)
            endingBalanceDebit = tBasicSubjectMessage.getEndingBalanceDebit().compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal("0.0") : tBasicSubjectMessage.getEndingBalanceDebit();
        }
        if (tBasicSubjectMessage.getEndingBalanceCredit() != null) {
            // 期末余额(贷方)
            endingBalanceCredit = tBasicSubjectMessage.getEndingBalanceCredit().compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal("0.0") : tBasicSubjectMessage.getEndingBalanceCredit();
        }
        if (tBasicSubjectMessage.getYearAmountDebit() != null) {
            // 本年累计发生额(借方)
            yearAmountDebit = tBasicSubjectMessage.getYearAmountDebit().compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal("0.0") : tBasicSubjectMessage.getYearAmountDebit();
        }
        if (tBasicSubjectMessage.getYearAmountCredit() != null) {
            // 本年累计发生额(贷方)
            yearAmountCredit = tBasicSubjectMessage.getYearAmountCredit().compareTo(new BigDecimal(0E-8)) == 0
                    ? new BigDecimal("0.0") : tBasicSubjectMessage.getYearAmountCredit();
        }
        String debitCreditDirection = tBasicSubjectMessage.getDebitCreditDirection();
        BigDecimal money = new BigDecimal("0.0");
        if (StringUtils.isNotBlank(debitCreditDirection)) {
            // 判断如果科目为 借方 科目  期末借方 - 期末贷方 - 本年累计发生额(借方) + 本年累计发生额(贷方)
            if (debitCreditDirection.equals("1")) {
                money = money.add(endingBalanceDebit);
                money = money.subtract(endingBalanceCredit);
                money = money.subtract(yearAmountDebit);
                money = money.add(yearAmountCredit);

            }

            // 判断如果科目为 贷方 科目  期末贷方 - 期末借方  + 本年累计发生额(借方) - 本年累计发生额(贷方)
            if (debitCreditDirection.equals("2")) {
                money = money.add(endingBalanceCredit);
                money = money.subtract(endingBalanceDebit);
                money = money.add(yearAmountDebit);
                money = money.subtract(yearAmountCredit);
            }
        }
        return money;
    }

    @Override
    public Map<String, Object> deleteBalanceSheet(User user, Account account) throws BusinessException {
        TBasicBalanceSheet tBasicBalanceSheet = new TBasicBalanceSheet();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取用户信息
            @SuppressWarnings("unchecked")

            String userId = user.getUserID();// 用户id
            tBasicBalanceSheet.setUserId(userId);

            String accountId = account.getAccountID();// 账套id
            tBasicBalanceSheet.setAccountId(accountId);
            String busDate = account.getUseLastPeriod();
            // 做帐期间
            tBasicBalanceSheet.setAccountPeriod(busDate);

            int no = tBasicBalanceSheetMapper.deleteBalanceSheet(tBasicBalanceSheet);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

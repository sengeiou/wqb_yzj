package com.wqb.service.report.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.report.TBasicIncomeStatementMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.*;
import com.wqb.service.report.TBasicIncomeStatementService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import com.wqb.service.voucher.VoucherService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

//@Transactional
@Service("tBasicIncomeStatementService")
public class TBasicIncomeStatementServiceImpl implements TBasicIncomeStatementService {
    @Autowired
    TBasicIncomeStatementMapper tBasicIncomeStatementMapper;
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;
    @Autowired
    VoucherService voucherService;
    @Autowired
    VatDao vatDao;
    @Autowired
    PeriodStatusDao periodStatusDao;


    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> queryProfit(Account acc, String period, User user) throws BusinessException {

        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<>();

            param.put("accountID", acc.getAccountID());
            param.put("period", period);
            int num = vatDao.queryCountSubjectMessage(param);
            if (num < 10) {
                res.put("code", "11");
                res.put("msg", "未查询到数据");
                return res;
            }
            param.clear();

            List<String> arr = getJdPeriods(period);
            param.put("arr", arr);
            param.put("accountID", acc.getAccountID());
            List<SubjectMessage> sub_list = vatDao.queryProfit(param);

            if (sub_list == null || sub_list.isEmpty()) {
                res.put("code", "1");
                res.put("msg", "sub_list==null");
                return res;
            }

            Map<String, Map<String, SubjectMessage>> map = new HashMap<>();
            for (int i = 0; i < sub_list.size(); i++) {
                SubjectMessage sub = sub_list.get(i);
                String per = sub.getAccount_period();
                String code = sub.getSub_code();
                Map<String, SubjectMessage> val_a = map.get(per);
                if (val_a == null) {
                    Map<String, SubjectMessage> map1 = new HashMap<>();
                    map1.put(code, sub);
                    map.put(per, map1);
                } else {
                    val_a.put(code, sub);
                }
            }

            // 一
            BigDecimal bq_yysr = new BigDecimal(0);    //本期营业收入
            BigDecimal bn_yysr = new BigDecimal(0);        //本年营业收入

            BigDecimal bq_yycb = new BigDecimal(0);    //本期营业成本
            BigDecimal bn_yycb = new BigDecimal(0);        //本年营业成本
            // 二
            BigDecimal bq_yylr = new BigDecimal(0);    //本期营业利润
            BigDecimal bn_yylr = new BigDecimal(0);        //本年营业利润

            // 三
            BigDecimal bq_lrze = new BigDecimal(0);    //本期利润 总额
            BigDecimal bn_lrze = new BigDecimal(0);        //本年利润 总额

            // 四
            BigDecimal bq_jlr = new BigDecimal(0);        //本期净利润
            BigDecimal bn_jlr = new BigDecimal(0);        //本年净利润

            SubjectMessage sub_6001_bq = map.get(period).get("6001");  //主营业务收入
            SubjectMessage sub_6051_bq = map.get(period).get("6051");  //其它业务收入

            //一
            SubjectMessage sub_6401_bq = map.get(period).get("6401"); //- 主营业务成本
            SubjectMessage sub_6402_bq = map.get(period).get("6402"); //- 其它业务成本

            SubjectMessage sub_6403_bq = map.get(period).get("6403"); //- 营业税金及附加
            SubjectMessage sub_6601_bq = map.get(period).get("6601"); //- 销售费用
            SubjectMessage sub_6602_bq = map.get(period).get("6602"); //- 管理费用
            SubjectMessage sub_6603_bq = map.get(period).get("6603"); //- 财务费用
            SubjectMessage sub_6701_bq = map.get(period).get("6701"); //- 资产减值损失
            SubjectMessage sub_6101_bq = map.get(period).get("6101"); //+ 加 公允价值变动收益（损失以-号填列）
            SubjectMessage sub_6111_bq = map.get(period).get("6111"); //+ 投资收益 （损失以-号填列）
            //+ 对联营企业和合营企业的投资收益

            //二 营业利润
            SubjectMessage sub_6301_bq = map.get(period).get("6301"); //+ 营业外收入
            SubjectMessage sub_6711_bq = map.get(period).get("6711"); //- 营业外支出
            //其中：非流动资产处置损失

            //三
            SubjectMessage sub_6801_bq = map.get(period).get("6801"); //- 所得税费用

            //定义利润表数据
            Profit profit = new Profit();
            String uuid = UUIDUtils.getUUID();
            profit.setPkIncomeStatementId(uuid); //主键
            profit.setUserId(user.getUserID());
            profit.setAccountId(acc.getAccountID());
            profit.setAccountPeriod(period);

            Date date = new Date();
            profit.setUpdateDate(date);
            profit.setUpdateTimestamp(String.valueOf(date.getTime()));

            bq_yysr = sub_6001_bq.getCurrent_amount_debit().add(sub_6051_bq.getCurrent_amount_debit());
            bn_yysr = sub_6001_bq.getYear_amount_debit().add(sub_6051_bq.getYear_amount_debit());

            bq_yycb = sub_6401_bq.getCurrent_amount_debit().add(sub_6402_bq.getCurrent_amount_debit());
            bn_yycb = sub_6401_bq.getYear_amount_debit().add(sub_6402_bq.getYear_amount_debit());

            profit.setCurrentSalesFromOperati(bq_yysr);        //本期营业收入
            profit.setYearSalesFromOperati(bn_yysr);            //本年营业收入

            profit.setCurrentLessOperatingCosts(bq_yycb);    //本期营业成本
            profit.setYearLessOperatingCosts(bn_yycb);    //本年营业成本


            BigDecimal bq_yysj = sub_6403_bq.getCurrent_amount_debit();
            profit.setCurrentOperatingTaxAndAdditions(sub_6403_bq.getCurrent_amount_debit()); //本期营业税金及附加
            profit.setYearOperatingTaxAndAdditions(sub_6403_bq.getYear_amount_debit()); //本年营业税金及附加

            BigDecimal bq_xxfy = sub_6601_bq.getCurrent_amount_debit();
            profit.setCurrentSellingExpenses(sub_6601_bq.getCurrent_amount_debit());  //本期销售费用
            profit.setYearSellingExpenses(sub_6601_bq.getYear_amount_debit());  //本年销售费用

            BigDecimal bq_glfy = sub_6602_bq.getCurrent_amount_debit();
            profit.setCurrentGeneralAndAdministrativeExpense(sub_6602_bq.getCurrent_amount_debit());  //本期管理费用
            profit.setYearGeneralAndAdministrativeExpense(sub_6602_bq.getYear_amount_debit()); //本年管理费用

            BigDecimal bq_cwfy = sub_6603_bq.getCurrent_amount_debit();
            profit.setCurrentFinaneiaExpense(sub_6603_bq.getCurrent_amount_debit()); //本期财务费用
            profit.setYearFinaneiaExpense(sub_6603_bq.getYear_amount_debit()); //本期财务费用

            BigDecimal bq_zzjzsx = sub_6701_bq.getCurrent_amount_debit();
            profit.setCurrentLossesOnTheAssetImpairment(sub_6701_bq.getCurrent_amount_debit());    //本期资产减值损失
            profit.setYearLossesOnTheAssetImpairment(sub_6701_bq.getYear_amount_debit());            //本年资产减值损失

            BigDecimal bq_gyjz = sub_6101_bq.getCurrent_amount_debit();
            profit.setCurrentAddProfitsOrLossesOntheChangesInFairValue(sub_6101_bq.getCurrent_amount_debit()); //本期 加公允价值变动收益（损失以-号填列）
            profit.setYearAddProfitsOrLossesOntheChangesInFairValue(sub_6101_bq.getYear_amount_debit());   //本年 加公允价值变动收益（损失以-号填列）

            BigDecimal bq_tzsy = sub_6111_bq.getCurrent_amount_debit();
            profit.setCurrentInvestmentIncome(sub_6111_bq.getCurrent_amount_debit());  //本期 投资收益
            profit.setYearInvestmentIncome(sub_6111_bq.getYear_amount_debit());  //本年 投资收益

            //profit.setYearAmongInvestmentIncomeFromAffiliatedBusiness(null);  	//本期 对联营企业和合营企业的投资收益  高经理说只有股份公司才有

            bq_yylr = js_bq_yylr(profit);
            bn_yylr = js_bn_yylr(profit);

            //二  营业利润
            profit.setCurrentOperatingIncome(bq_yylr);  //本期营业利润
            profit.setYearOperatingIncome(bn_yylr);   //本年营业利润

            profit.setCurrentAddNonOperatingIncome(sub_6301_bq.getCurrent_amount_debit());  //本期 营业外收入
            profit.setYearAddNonOperatingIncome(sub_6301_bq.getYear_amount_debit());  //本年 营业外收入

            profit.setCurrentLessNonOperatingExpense(sub_6711_bq.getCurrent_amount_debit());    //本期 营业外支出
            profit.setYearLessNonOperatingExpense(sub_6711_bq.getYear_amount_debit());    //本年 营业外支出

            profit.setCurrentIncludingLossesFromDisposalOfNonCurrentAssets(null);  //其中：非流动资产处置损失  高经理说不用管
            profit.setYearIncludingLossesFromDisposalOfNonCurrentAssets(null);  //其中：非流动资产处置损失


            bq_lrze = bq_yylr.add(sub_6301_bq.getCurrent_amount_debit()).subtract(sub_6711_bq.getCurrent_amount_debit());
            bn_lrze = bn_yylr.add(sub_6301_bq.getYear_amount_debit()).subtract(sub_6711_bq.getYear_amount_debit());

            //三 利润总额
            profit.setCurrentIncomeBeforeTax(bq_lrze); //本期利润总额
            profit.setYearIncomeBeforeTax(bn_lrze);   //本年利润总额

            profit.setCurrentLessIncomeTax(sub_6801_bq.getCurrent_amount_debit());  //本期所得税费用
            profit.setYearLessIncomeTax(sub_6801_bq.getYear_amount_debit());  //本年所得税费用

            //四 净利润
            bq_jlr = bq_lrze.subtract(sub_6801_bq.getCurrent_amount_debit());  //本期净利润
            bn_jlr = bn_lrze.subtract(sub_6801_bq.getYear_amount_debit());     //本年净利润

            profit.setCurrentEntIncome(bq_jlr);
            profit.setYearEntIncome(bn_jlr);

            //arr != hashSet
            HashSet<String> hashSet = new HashSet<>();
            for (SubjectMessage ss : sub_list) {
                hashSet.add(ss.getAccount_period());
            }
            js_jd(profit, map, hashSet);


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("busDate", period);
            hashMap.put("accountID", acc.getAccountID());
            List<StatusPeriod> stalist = periodStatusDao.queryStatus(hashMap);
            if (stalist == null || (stalist != null && stalist.get(0) == null)) {
                throw new BusinessException("期间状态异常");
            }
            Integer isCarryState = stalist.get(0).getIsCarryState();

            if (isCarryState == 1) {
                hashMap.clear();
                hashMap.put("period", period);
                hashMap.put("accountID", acc.getAccountID());
                int num2 = vatDao.queryProfitCount(hashMap);
                if (num2 == 0) {
                    TBasicIncomeStatement tb = new TBasicIncomeStatement();
                    BigDecimalConverter bd = new BigDecimalConverter(null);
                    ConvertUtils.register(bd, java.math.BigDecimal.class);
                    BeanUtils.copyProperties(tb, profit);
                    int no = tBasicIncomeStatementMapper.addIncomeStatement(tb);
                }
            }
            res.put("code", "0");
            res.put("msg", profit);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }


    }


    @SuppressWarnings("deprecation")
    private List<String> getJdPeriods(String period) {

        List<String> arr = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        set.add(period);
        Date date = DateUtil.getMonth(period);
        int month = date.getMonth();
        int num = 0;
        if (month % 3 == 1) {
            set.add(DateUtil.getNextNMonth3(date, --num));
        } else if (month % 3 == 2) {
            set.add(DateUtil.getNextNMonth3(date, --num));
            set.add(DateUtil.getNextNMonth3(date, --num));
        }
        arr.addAll(set);
        return arr;
    }


    @SuppressWarnings("unused")
    private void js_jd(Profit profit, Map<String, Map<String, SubjectMessage>> map, HashSet<String> arr) {

        // 一
        BigDecimal total_yysr = new BigDecimal(0);    //营业收入
        BigDecimal total_yycb = new BigDecimal(0);    //-营业成本

        BigDecimal total_sj = new BigDecimal(0);        //- 营业税金及附加
        BigDecimal total_xxfy = new BigDecimal(0);        //- 销售费用
        BigDecimal total_glfy = new BigDecimal(0);        //- 管理费用
        BigDecimal total_cwfy = new BigDecimal(0);        //- 财务费用
        BigDecimal total_zcjzsx = new BigDecimal(0);    //- 资产减值损失
        BigDecimal total_gyjz = new BigDecimal(0);        //+ 加 公允价值变动收益（损失以-号填列）
        BigDecimal total_tzsy = new BigDecimal(0);        //+ 投资收益

        BigDecimal total_yywsr = new BigDecimal(0);        //+ 营业外收入
        BigDecimal total_yywzc = new BigDecimal(0);    //- 营业外支出
        BigDecimal total_sdsfy = new BigDecimal(0);        //- 所得税费用

        for (String period : arr) {

            SubjectMessage sub_6001 = map.get(period).get("6001");  //主营业务收入
            SubjectMessage sub_6051 = map.get(period).get("6051");  //其它业务收入
            //一
            SubjectMessage sub_6401 = map.get(period).get("6401"); //- 主营业务成本
            SubjectMessage sub_6402 = map.get(period).get("6402"); //- 其它业务成本

            SubjectMessage sub_6403 = map.get(period).get("6403"); //- 营业税金及附加
            SubjectMessage sub_6601 = map.get(period).get("6601"); //- 销售费用
            SubjectMessage sub_6602 = map.get(period).get("6602"); //- 管理费用
            SubjectMessage sub_6603 = map.get(period).get("6603"); //- 财务费用
            SubjectMessage sub_6701 = map.get(period).get("6701"); //- 资产减值损失
            SubjectMessage sub_6101 = map.get(period).get("6101"); //+ 加 公允价值变动收益（损失以-号填列）
            SubjectMessage sub_6111 = map.get(period).get("6111"); //+ 投资收益 （损失以-号填列）
            //+ 对联营企业和合营企业的投资收益

            //二 营业利润
            SubjectMessage sub_6301 = map.get(period).get("6301"); //+ 营业外收入
            SubjectMessage sub_6711 = map.get(period).get("6711"); //- 营业外支出
            //其中：非流动资产处置损失w

            //三
            SubjectMessage sub_6801 = map.get(period).get("6801"); //- 所得税费用
            total_yysr = total_yysr.add(sub_6001.getCurrent_amount_debit()).add(sub_6051.getCurrent_amount_debit()); //	+ 营业收入
            total_yycb = total_yycb.add(sub_6401.getCurrent_amount_debit()).add(sub_6402.getCurrent_amount_debit()); // - 营业成本
            total_sj = total_sj.add(sub_6403.getCurrent_amount_debit());            //- 营业税金及附加

            total_xxfy = total_xxfy.add(sub_6601.getCurrent_amount_debit());        //- 销售费用
            total_glfy = total_glfy.add(sub_6602.getCurrent_amount_debit());        //- 管理费用
            total_cwfy = total_cwfy.add(sub_6603.getCurrent_amount_debit());        // - 财务费用
            total_zcjzsx = total_zcjzsx.add(sub_6701.getCurrent_amount_debit());    //- 资产减值损失

            total_gyjz = total_gyjz.add(sub_6101.getCurrent_amount_debit());        //+ 加 公允价值变动收益（损失以-号填列）
            total_tzsy = total_tzsy.add(sub_6111.getCurrent_amount_debit());        //+ 投资收益 （损失以-号填列）

            total_yywsr = total_yywsr.add(sub_6301.getCurrent_amount_debit());    //+ 营业外收入
            total_yywzc = total_yywzc.add(sub_6711.getCurrent_amount_debit());    //- 营业外支出

            total_sdsfy = total_sdsfy.add(sub_6801.getCurrent_amount_debit());    //- 所得税费用

        }

        profit.setJdSalesFromOperati(total_yysr);                    //+ 营业外收入
        profit.setJdLessOperatingCosts(total_yycb);                //+ 营业成本
        profit.setJdOperatingTaxAndAdditions(total_sj);            //- 营业税金及附加
        profit.setJdSellingExpenses(total_xxfy);                    //- 销售费用
        profit.setJdGeneralAndAdministrativeExpense(total_glfy);    //- 管理费用
        profit.setJdFinaneiaExpense(total_cwfy);  // - 财务费用
        profit.setJdLossesOnTheAssetImpairment(total_zcjzsx);        //- 资产减值损失
        profit.setJdAddProfitsOrLossesOntheChangesInFairValue(total_gyjz);  //+ 加 公允价值变动收益（损失以-号填列）
        //setYearAddProfitsOrLossesOntheChangesInFairValue
        profit.setJdInvestmentIncome(total_tzsy);                    //+ 投资收益 （损失以-号填列）


        BigDecimal total_yylr = total_yysr.subtract(total_yycb).subtract(total_sj).subtract(total_xxfy).subtract(total_glfy).subtract(total_cwfy).
                subtract(total_zcjzsx).add(total_gyjz).add(total_tzsy); //营业利润
        profit.setJdOperatingIncome(total_yylr);//营业利润

        profit.setJdAddNonOperatingIncome(total_yywsr); //+ 营业外收入
        profit.setJdLessNonOperatingExpense(total_yywzc); //- 营业外支出
        profit.setJdIncludingLossesFromDisposalOfNonCurrentAssets(null); //其中：非流动资产处置损失

        BigDecimal total_lrze = total_yylr.add(total_yywsr).subtract(total_yywzc); //利润总额
        profit.setJdIncomeBeforeTax(total_lrze);  //利润总额

        profit.setJdLessIncomeTax(total_sdsfy);  //所得税费用

        BigDecimal total_jlr = total_lrze.subtract(total_sdsfy);    //净利润
        profit.setJdtEntIncome(total_jlr);    //净利润
    }


    private BigDecimal js_bq_yylr(Profit profit) {
        BigDecimal yylr = profit.getCurrentSalesFromOperati().subtract(profit.getCurrentLessOperatingCosts()).subtract(profit.getCurrentOperatingTaxAndAdditions()).
                subtract(profit.getCurrentSellingExpenses()).subtract(profit.getCurrentGeneralAndAdministrativeExpense()).subtract(profit.getCurrentFinaneiaExpense()).
                subtract(profit.getCurrentLossesOnTheAssetImpairment()).add(profit.getCurrentAddProfitsOrLossesOntheChangesInFairValue()).add(profit.getCurrentInvestmentIncome());
        return yylr;
    }

    private BigDecimal js_bn_yylr(Profit profit) {
        BigDecimal yylr = profit.getYearSalesFromOperati().subtract(profit.getYearLessOperatingCosts()).subtract(profit.getYearOperatingTaxAndAdditions()).
                subtract(profit.getYearSellingExpenses()).subtract(profit.getYearGeneralAndAdministrativeExpense()).subtract(profit.getYearFinaneiaExpense()).
                subtract(profit.getYearLossesOnTheAssetImpairment()).add(profit.getYearAddProfitsOrLossesOntheChangesInFairValue()).add(profit.getYearInvestmentIncome());
        return yylr;
    }


    //2TBasicIncomeStatementController -》queryIncomeStatrment-》addIncomeStatement-》 (in to service) addIncomeStatement 添加利润表 数据到数据库
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> addIncomeStatement(User user, Account account) throws BusinessException {

        //定义返回结果集
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        //定义利润表对象 作为数据封装
        TBasicIncomeStatement tBasicIncomeStatement = new TBasicIncomeStatement();

        String busDate = account.getUseLastPeriod();
        String accountId = account.getAccountID();// 账套id
        String userId = user.getUserID();// 用户id

        tBasicIncomeStatement.setUserId(userId);
        tBasicIncomeStatement.setAccountId(accountId);
        tBasicIncomeStatement.setAccountPeriod(busDate);

        //1 先删除原来的数据
        tBasicIncomeStatementMapper.deleteIncomeStatrment(tBasicIncomeStatement);  // 删除利润表数据

        /** 利润表主键 */
        String pkIncomeStatementId = UUIDUtils.getUUID();
        tBasicIncomeStatement.setPkIncomeStatementId(pkIncomeStatementId);

        Map<String, String> parameters = new HashMap<String, String>();

        //查询科目余额表
        Map<String, Object> querySbujectBalance = tBasicSubjectMessageService.querySbujectBalance(user, account, parameters);

        // 本年余额 取本年累计借
        if (querySbujectBalance.get("subMessages") != null && !((List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages")).isEmpty()) {

            List<TBasicSubjectMessage> tBasicSubjectMessages = (List<TBasicSubjectMessage>) querySbujectBalance.get("subMessages");

            /** 本年金额  一、营业收入 Sales from operations */
            BigDecimal yearSalesFromOperati = new BigDecimal("0.0");
            /** 本期金额  一、营业收入 Sales from operations */
            BigDecimal currentSalesFromOperati = new BigDecimal("0.0");

            /** 本年金额  一、营业成本 Sales from operations */
            BigDecimal yearLessOperatingCosts = new BigDecimal("0.0");
            /** 本期金额  一、营业成本 Sales from operations */
            BigDecimal currentLessOperatingCosts = new BigDecimal("0.0");

            for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessages) {
                String subCode = tBasicSubjectMessage.getSubCode();

                if (!StringUtils.isEmpty(subCode)) {

                    BigDecimal yearAmountDebit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getYearAmountDebit());// 本年借累计发生额
                    BigDecimal currentAmountDebit = StringUtil.bigDecimalIsNull(tBasicSubjectMessage.getCurrentAmountDebit()); // 本期借发生额

                    /** 一、营业收入 Sales from operations */
                    if (subCode.equals("6001")) { //主营业务收入
                        yearSalesFromOperati = yearSalesFromOperati.add(yearAmountDebit); /** 本年金额 本年累计借方 */
                        currentSalesFromOperati = currentSalesFromOperati.add(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }
                    if (subCode.equals("6051")) { //其他业务收入
                        yearSalesFromOperati = yearSalesFromOperati.add(yearAmountDebit); /** 本年金额  本年累计借方 */
                        currentSalesFromOperati = currentSalesFromOperati.add(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 减：营业成本  Less: operating costs */
                    if (subCode.equals("6401")) { //6401 主营业务成本
                        yearLessOperatingCosts = yearLessOperatingCosts.add(yearAmountDebit); /** 本年金额  本年累计借方 */
                        currentLessOperatingCosts = currentLessOperatingCosts.add(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    if (subCode.equals("6402")) { //其他业务成本
                        yearLessOperatingCosts = yearLessOperatingCosts.add(yearAmountDebit); /** 本年金额  本年累计借方 */
                        currentLessOperatingCosts = currentLessOperatingCosts.add(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 营业税金及附加 Operating tax and additions*/
                    if (subCode.equals("6403")) {  //营业税金及附加
                        tBasicIncomeStatement.setYearOperatingTaxAndAdditions(yearAmountDebit); /** 本年金额  取科目余额表中本年累计借方 */
                        tBasicIncomeStatement.setCurrentOperatingTaxAndAdditions(currentAmountDebit); /** 本期金额  取科目余额表中本期发生额借方 */
                        continue;
                    }

                    /** 销售费用Selling expenses*/
                    if (subCode.equals("6601")) {  //销售费用
                        tBasicIncomeStatement.setYearSellingExpenses(yearAmountDebit);    /** 本年金额  本年累计借方 */
                        tBasicIncomeStatement.setCurrentSellingExpenses(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 管理费用*/
                    if (subCode.equals("6602")) {  //管理费用
                        tBasicIncomeStatement.setYearGeneralAndAdministrativeExpense(yearAmountDebit); /** 本年金额  取科目余额表中本年累计借方 */
                        tBasicIncomeStatement.setCurrentGeneralAndAdministrativeExpense(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 财务费用*/
                    if (subCode.equals("6603")) {  //财务费用
                        tBasicIncomeStatement.setYearFinaneiaExpense(yearAmountDebit);    /** 本年金额  取科目余额表中本年累计借方 */
                        tBasicIncomeStatement.setCurrentFinaneiaExpense(currentAmountDebit);    /** 本期金额  取科目余额表中本期发生额借方 */
                        continue;
                    }

                    /** 资产减值损失*/
                    if (subCode.equals("6701")) { //资产减值损失
                        tBasicIncomeStatement.setYearLossesOnTheAssetImpairment(yearAmountDebit); /** 本年金额 本年累计借方 */
                        tBasicIncomeStatement.setCurrentLossesOnTheAssetImpairment(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 加：公允价值变动收益（损失以“-”号填列）*/
                    if (subCode.equals("6101")) {  //公允价值变动损益
                        tBasicIncomeStatement.setYearAddProfitsOrLossesOntheChangesInFairValue(yearAmountDebit); /** 本年金额  本年累计借方 */
                        tBasicIncomeStatement.setCurrentAddProfitsOrLossesOntheChangesInFairValue(currentAmountDebit); /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 投资收益（损失以“-”号填列*/
                    if (subCode.equals("6111")) {  //投资收益
                        tBasicIncomeStatement.setYearInvestmentIncome(yearAmountDebit);  /** 本年金额  本年累计借方 */
                        tBasicIncomeStatement.setCurrentInvestmentIncome(currentAmountDebit);  /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 其中：对联营企业和合营企业的投资收益*/
                    /** 二、营业利润（亏损以“-”号填列*/
                    /** 加：营业外收入*/
                    if (subCode.equals("6301")) {  //营业外收入
                        tBasicIncomeStatement.setYearAddNonOperatingIncome(yearAmountDebit); /** 本年金额  本年累计借方 */
                        tBasicIncomeStatement.setCurrentAddNonOperatingIncome(currentAmountDebit);    /** 本期金额  本期发生额借方 */
                        continue;
                    }

                    /** 减：营业外支出*/
                    if (subCode.equals("6711")) {  //营业外支出
                        tBasicIncomeStatement.setYearLessNonOperatingExpense(yearAmountDebit); /** 本年金额 本年累计借方 */
                        tBasicIncomeStatement.setCurrentLessNonOperatingExpense(currentAmountDebit); /** 本期金额 本期发生额借方 */
                        continue;
                    }

                    /** 其中：非流动资产处置损失*/
                    /** 三、利润总额（亏损总额以“-”号填列）*/
                    /** 减：所得税费用    Less: Income tax*/
                    /** 6801本期发生额 减去 6901本期发生额   逻辑错误暂时放在这后期再核对**/
                    /**只取 6801 本期发生额 借方 **/
                    if (subCode.equals("6801")) {  //所得税费用
                        tBasicIncomeStatement.setYearLessIncomeTax(yearAmountDebit); /** 本年金额  取科目余额表中本年累计借方 */
                        tBasicIncomeStatement.setCurrentLessIncomeTax(currentAmountDebit); /** 本期金额  取科目余额表中本期发生额借方 */
                        continue;
                    }

                    /** 五、每股收益： EARNINGS PER SHARE (EPS)*/
                    //private BigDecimal yearEarningsPerShare;
                    /** 五、每股收益： EARNINGS PER SHARE (EPS)*/
                    // private BigDecimal currentEarningsPerShare;
                    /** （一）基本每股收益 Basic EPS*/
                    // private BigDecimal yearBasicEps;
                    /** （一）基本每股收益 Basic EPS*/
                    // private BigDecimal currentBasicEps;
                    /** （二）稀释每股收益 Diluted EPS*/
                    // private BigDecimal yearDilutedEps;
                    /** （二）稀释每股收益 Diluted EPS*/
                    //private BigDecimal currentDilutedEps;
                }
            }

            /** 本期金额  一、营业收入 Sales from operations */
            tBasicIncomeStatement.setCurrentSalesFromOperati(currentSalesFromOperati);
            /** 本年金额  一、营业收入 Sales from operations */
            tBasicIncomeStatement.setYearSalesFromOperati(yearSalesFromOperati);

            /** 本期金额 减：营业成本  Less: operating costs   */
            tBasicIncomeStatement.setCurrentLessOperatingCosts(currentLessOperatingCosts);
            /** 本年金额  减：营业成本  Less: operating costs   */
            tBasicIncomeStatement.setYearLessOperatingCosts(yearLessOperatingCosts);

            /**本期金额  二、营业利润（亏损以“-”号填列） Operating income*/
            BigDecimal currentOperatingIncome = new BigDecimal("0.0");
            BigDecimal currentSalesFromOperati2 = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentSalesFromOperati());

            currentOperatingIncome = currentOperatingIncome.add(currentSalesFromOperati2);
            BigDecimal currentLessOperatingCosts2 = tBasicIncomeStatement.getCurrentLessOperatingCosts();
            // 1 营业收入 - 营业成本
            currentOperatingIncome = currentOperatingIncome.subtract(currentLessOperatingCosts2);
            BigDecimal currentOperatingTaxAndAdditions = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentOperatingTaxAndAdditions());
            //2 营业收入 - 营业成本 - 营业税金及附加
            currentOperatingIncome = currentOperatingIncome.subtract(currentOperatingTaxAndAdditions);
            BigDecimal currentSellingExpenses = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentSellingExpenses());
            //3 营业收入 - 营业成本 - 营业税金及附加 - 销售费用
            currentOperatingIncome = currentOperatingIncome.subtract(currentSellingExpenses);
            BigDecimal currentGeneralAndAdministrativeExpense = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentGeneralAndAdministrativeExpense());
            //4 营业收入 - 营业成本 - 营业税金及附加 - 销售费用 -  管理费用
            currentOperatingIncome = currentOperatingIncome.subtract(currentGeneralAndAdministrativeExpense);
            BigDecimal currentFinaneiaExpense = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentFinaneiaExpense());
            //5 营业收入 - 营业成本 - 营业税金及附加 - 销售费用 -  管理费用 - 财务费用
            currentOperatingIncome = currentOperatingIncome.subtract(currentFinaneiaExpense);
            BigDecimal currentLossesOnTheAssetImpairment = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentLossesOnTheAssetImpairment());
            //6 营业收入 - 营业成本 - 营业税金及附加 - 销售费用 -  管理费用 - 财务费用 - 资产减值损失
            currentOperatingIncome = currentOperatingIncome.subtract(currentLossesOnTheAssetImpairment);
            BigDecimal currentAddProfitsOrLossesOntheChangesInFairValue = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentAddProfitsOrLossesOntheChangesInFairValue());
            //7 营业收入 - 营业成本 - 营业税金及附加 - 销售费用 -  管理费用 - 财务费用 - 资产减值损失 + 加：公允价值变动收益
            currentOperatingIncome = currentOperatingIncome.add(currentAddProfitsOrLossesOntheChangesInFairValue);
            BigDecimal currentInvestmentIncome = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentInvestmentIncome());
            //8  营业收入 - 营业成本 - 营业税金及附加 - 销售费用 -  管理费用 - 财务费用 - 资产减值损失 + 加：公允价值变动收益 +  投资收益（损失以“-”号填列）
            currentOperatingIncome = currentOperatingIncome.add(currentInvestmentIncome);

            /**** 本期营业利润 *****/
            tBasicIncomeStatement.setCurrentOperatingIncome(currentOperatingIncome);

            /**本年金额  二、营业利润（亏损以“-”号填列） Operating income*/
            BigDecimal yearOperatingIncome = new BigDecimal("0.0");
            BigDecimal yearSalesFromOperati2 = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearSalesFromOperati());
            yearOperatingIncome = yearOperatingIncome.add(yearSalesFromOperati2);
            BigDecimal yearLessOperatingCosts2 = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearLessOperatingCosts());
            yearOperatingIncome = yearOperatingIncome.subtract(yearLessOperatingCosts2);
            BigDecimal yearOperatingTaxAndAdditions = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearOperatingTaxAndAdditions());
            yearOperatingIncome = yearOperatingIncome.subtract(yearOperatingTaxAndAdditions);
            BigDecimal yearSellingExpenses = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearSellingExpenses());
            yearOperatingIncome = yearOperatingIncome.subtract(yearSellingExpenses);
            BigDecimal yearGeneralAndAdministrativeExpense = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearGeneralAndAdministrativeExpense());
            yearOperatingIncome = yearOperatingIncome.subtract(yearGeneralAndAdministrativeExpense);
            BigDecimal yearFinaneiaExpense = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearFinaneiaExpense());
            yearOperatingIncome = yearOperatingIncome.subtract(yearFinaneiaExpense);
            BigDecimal yearLossesOnTheAssetImpairment = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearLossesOnTheAssetImpairment());
            yearOperatingIncome = yearOperatingIncome.subtract(yearLossesOnTheAssetImpairment);
            BigDecimal yearAddProfitsOrLossesOntheChangesInFairValue = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearAddProfitsOrLossesOntheChangesInFairValue());
            yearOperatingIncome = yearOperatingIncome.add(yearAddProfitsOrLossesOntheChangesInFairValue);
            BigDecimal yearInvestmentIncome = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearInvestmentIncome());
            yearOperatingIncome = yearOperatingIncome.add(yearInvestmentIncome);

            /**** 本年营业利润 *****/
            tBasicIncomeStatement.setYearOperatingIncome(yearOperatingIncome);

            /**本期金额 三、利润总额（亏损总额以“-”号填列） Income before tax*/
            BigDecimal currentIncomeBeforeTax = new BigDecimal("0.0");
            currentIncomeBeforeTax = currentIncomeBeforeTax.add(currentOperatingIncome);
            BigDecimal currentAddNonOperatingIncome = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentAddNonOperatingIncome());

            //1 本期营业利润 + 加：营业外收入
            currentIncomeBeforeTax = currentIncomeBeforeTax.add(currentAddNonOperatingIncome);
            BigDecimal currentLessNonOperatingExpense = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentLessNonOperatingExpense());
            //2 本期营业利润 + 加：营业外收入  - 减：营业外支出
            currentIncomeBeforeTax = currentIncomeBeforeTax.subtract(currentLessNonOperatingExpense);

            /**** 本期利润总额 *****/
            tBasicIncomeStatement.setCurrentIncomeBeforeTax(currentIncomeBeforeTax);

            /**本年金额 三、利润总额（亏损总额以“-”号填列）*/
            BigDecimal yearIncomeBeforeTax = new BigDecimal("0.0");
            //1 + 本年营业利润
            yearIncomeBeforeTax = yearIncomeBeforeTax.add(yearOperatingIncome);
            BigDecimal yearAddNonOperatingIncome = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearAddNonOperatingIncome());
            //2 + 本年营业利润  + 营业外收入
            yearIncomeBeforeTax = yearIncomeBeforeTax.add(yearAddNonOperatingIncome);
            BigDecimal yearLessNonOperatingExpense = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearLessNonOperatingExpense());
            //3 利润总额  = + 本年营业利润  + 营业外收入 -营业外支出
            yearIncomeBeforeTax = yearIncomeBeforeTax.subtract(yearLessNonOperatingExpense);

            /**** 本年利润总额 *****/
            tBasicIncomeStatement.setYearIncomeBeforeTax(yearIncomeBeforeTax);

            /**本期金额 四、净利润（净亏损以“－”号填列）*/
            BigDecimal currentEntIncome = new BigDecimal("0.0");
            currentEntIncome = currentEntIncome.add(currentIncomeBeforeTax);
            if (null != tBasicIncomeStatement.getCurrentLessIncomeTax()) {
                BigDecimal currentLessIncomeTax = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getCurrentLessIncomeTax());
                //利润总额 - 减：所得税费用
                currentEntIncome = currentEntIncome.subtract(currentLessIncomeTax);
            }

            /**** 本期净利润 *****/
            tBasicIncomeStatement.setCurrentEntIncome(currentEntIncome);

            /**本年金额 四、净利润（净亏损以“－”号填列）*/
            BigDecimal yearEntIncome = new BigDecimal("0.0");
            yearEntIncome = yearEntIncome.add(yearIncomeBeforeTax);
            if (null != tBasicIncomeStatement.getYearLessIncomeTax()) {
                BigDecimal yearLessIncomeTax = StringUtil.bigDecimalIsNull(tBasicIncomeStatement.getYearLessIncomeTax());
                yearEntIncome = yearEntIncome.subtract(yearLessIncomeTax);
            }

            /**** 本年净利润 *****/
            tBasicIncomeStatement.setYearEntIncome(yearEntIncome);

            Date date = new Date();
            /** 更新时间*/
            tBasicIncomeStatement.setUpdateDate(date);

            /** 时间戳*/
            tBasicIncomeStatement.setUpdateTimestamp(String.valueOf(date.getTime()));
        }
        int no = tBasicIncomeStatementMapper.addIncomeStatement(tBasicIncomeStatement);
        result.put("no", no);
        result.put("code", 1);
        return result;
    }

    @Override
    public Map<String, Object> queryIncomeStatrment(User user, Account account) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicIncomeStatement tBasicIncomeStatement = new TBasicIncomeStatement();

        @SuppressWarnings("unchecked")

        String userId = user.getUserID();// 用户id
        tBasicIncomeStatement.setUserId(userId);

        String accountId = account.getAccountID();// 账套id
        tBasicIncomeStatement.setAccountId(accountId);
        String busDate = account.getUseLastPeriod();
        // 做帐期间
        tBasicIncomeStatement.setAccountPeriod(busDate);
        List<TBasicIncomeStatement> queryIncomeStatrment = tBasicIncomeStatementMapper.queryIncomeStatrment(tBasicIncomeStatement);
        result.put("queryIncomeStatrment", queryIncomeStatrment);
        result.put("code", 1);

        return result;
    }

    //利润表App端调用接口服务
    @Override
    public Map<String, Object> queryIncomeStatrmentAPP(Map<String, Object> param) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            TBasicIncomeStatement tBasicIncomeStatement = new TBasicIncomeStatement();

            tBasicIncomeStatement.setAccountId(param.get("accountID").toString());
            tBasicIncomeStatement.setAccountPeriod(param.get("period").toString());

            List<TBasicIncomeStatement> listSheet = tBasicIncomeStatementMapper.queryIncomeStatrment2(tBasicIncomeStatement);
            if (listSheet != null && listSheet.size() > 0) {
                result.put("sheet", listSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public Map<String, Object> deleteIncomeStatrment(User user, Account account) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicIncomeStatement tBasicIncomeStatement = new TBasicIncomeStatement();

        @SuppressWarnings("unchecked")

        String userId = user.getUserID();// 用户id
        tBasicIncomeStatement.setUserId(userId);

        String accountId = account.getAccountID();// 账套id
        tBasicIncomeStatement.setAccountId(accountId);
        String busDate = account.getUseLastPeriod();
        // 做帐期间
        tBasicIncomeStatement.setAccountPeriod(busDate);
        int no = tBasicIncomeStatementMapper.deleteIncomeStatrment(tBasicIncomeStatement);
        result.put("no", no);
        result.put("code", 1);
        return result;
    }

}

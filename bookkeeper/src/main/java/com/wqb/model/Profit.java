package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Profit implements Serializable {

    private static final long serialVersionUID = 6693707886070329612L;

    /**
     * 利润表主键
     */
    private String pkIncomeStatementId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 账套ID
     */
    private String accountId;

    /**
     * 做帐的真实期间 年 - 月(帐套启用年-月份）
     */
    private String accountPeriod;

    /**
     * 一、营业收入 Sales from operations
     */
    private BigDecimal yearSalesFromOperati;
    /**
     * 一、营业收入 Sales from operations
     */
    private BigDecimal currentSalesFromOperati;
    /**
     * 一、营业收入 Sales from operations
     */
    private BigDecimal jdSalesFromOperati;

    /**
     * 减：营业成本  Less: operating costs
     */
    private BigDecimal yearLessOperatingCosts;
    /**
     * 减：营业成本  Less: operating costs
     */
    private BigDecimal currentLessOperatingCosts;
    /**
     * 减：营业成本  Less: operating costs
     */
    private BigDecimal jdLessOperatingCosts;

    /**
     * 营业税金及附加 Operating tax and additions
     */
    private BigDecimal yearOperatingTaxAndAdditions;
    /**
     * 营业税金及附加 Operating tax and additions
     */
    private BigDecimal currentOperatingTaxAndAdditions;
    /**
     * 营业税金及附加 Operating tax and additions
     */
    private BigDecimal jdOperatingTaxAndAdditions;

    /**
     * 销售费用Selling expenses
     */
    private BigDecimal yearSellingExpenses;
    /**
     * 销售费用Selling expenses
     */
    private BigDecimal currentSellingExpenses;
    /**
     * 销售费用Selling expenses
     */
    private BigDecimal jdSellingExpenses;

    /**
     * 管理费用General and administrative expense
     */
    private BigDecimal yearGeneralAndAdministrativeExpense;
    /**
     * 管理费用General and administrative expense
     */
    private BigDecimal currentGeneralAndAdministrativeExpense;
    /**
     * 管理费用General and administrative expense
     */
    private BigDecimal jdGeneralAndAdministrativeExpense;

    /**
     * 财务费用Finaneial expense
     */
    private BigDecimal yearFinaneiaExpense;
    /**
     * 财务费用Finaneial expense
     */
    private BigDecimal currentFinaneiaExpense;
    /**
     * 财务费用Finaneial expense
     */
    private BigDecimal jdFinaneiaExpense;

    /**
     * 资产减值损失Losses on the asset impairment
     */
    private BigDecimal yearLossesOnTheAssetImpairment;
    /**
     * 资产减值损失Losses on the asset impairment
     */
    private BigDecimal currentLossesOnTheAssetImpairment;
    /**
     * 资产减值损失Losses on the asset impairment
     */
    private BigDecimal jdLossesOnTheAssetImpairment;

    /**
     * 加：公允价值变动收益（损失以“-”号填列）Add:Profits or losses onthe changes in fair value
     */
    private BigDecimal yearAddProfitsOrLossesOntheChangesInFairValue;
    /**
     * 加：公允价值变动收益（损失以“-”号填列）Add:Profits or losses onthe changes in fair value
     */
    private BigDecimal currentAddProfitsOrLossesOntheChangesInFairValue;
    /**
     * 加：公允价值变动收益（损失以“-”号填列）Add:Profits or losses onthe changes in fair value
     */
    private BigDecimal jdAddProfitsOrLossesOntheChangesInFairValue;


    /**
     * 投资收益（损失以“-”号填列）Investment income
     */
    private BigDecimal yearInvestmentIncome;
    /**
     * 投资收益（损失以“-”号填列）Investment income
     */
    private BigDecimal currentInvestmentIncome;
    /**
     * 投资收益（损失以“-”号填列）Investment income
     */
    private BigDecimal jdInvestmentIncome;

    /**
     * 其中：对联营企业和合营企业的投资收益Among: Investment income from affiliated business
     */
    private BigDecimal yearAmongInvestmentIncomeFromAffiliatedBusiness;
    /**
     * 其中：对联营企业和合营企业的投资收益Among: Investment income from affiliated business
     */
    private BigDecimal currentAmongInvestmentIncomeFromAffiliatedBusiness;
    /**
     * 其中：对联营企业和合营企业的投资收益Among: Investment income from affiliated business
     */
    private BigDecimal jdAmongInvestmentIncomeFromAffiliatedBusiness;

    /**
     * 二、营业利润（亏损以“-”号填列） Operating income
     */
    private BigDecimal yearOperatingIncome;
    /**
     * 二、营业利润（亏损以“-”号填列） Operating income
     */
    private BigDecimal currentOperatingIncome;
    /**
     * 二、营业利润（亏损以“-”号填列） Operating income
     */
    private BigDecimal jdOperatingIncome;

    /**
     * 加：营业外收入 Add: Non-operating income
     */
    private BigDecimal yearAddNonOperatingIncome;
    /**
     * 加：营业外收入 Add: Non-operating income
     */
    private BigDecimal currentAddNonOperatingIncome;
    /**
     * 加：营业外收入 Add: Non-operating income
     */
    private BigDecimal jdAddNonOperatingIncome;

    /**
     * 减：营业外支出   Less: Non-operating expense
     */
    private BigDecimal yearLessNonOperatingExpense;
    /**
     * 减：营业外支出   Less: Non-operating expense
     */
    private BigDecimal currentLessNonOperatingExpense;
    /**
     * 减：营业外支出   Less: Non-operating expense
     */
    private BigDecimal jdLessNonOperatingExpense;

    /**
     * 其中：非流动资产处置损失Including:Losses from disposal of non-current assets
     */
    private BigDecimal yearIncludingLossesFromDisposalOfNonCurrentAssets;
    /**
     * 其中：非流动资产处置损失Including:Losses from disposal of non-current assets
     */
    private BigDecimal currentIncludingLossesFromDisposalOfNonCurrentAssets;
    /**
     * 其中：非流动资产处置损失Including:Losses from disposal of non-current assets
     */
    private BigDecimal jdIncludingLossesFromDisposalOfNonCurrentAssets;

    /**
     * 三、利润总额（亏损总额以“-”号填列） Income before tax
     */
    private BigDecimal yearIncomeBeforeTax;
    /**
     * 三、利润总额（亏损总额以“-”号填列） Income before tax
     */
    private BigDecimal currentIncomeBeforeTax;
    /**
     * 三、利润总额（亏损总额以“-”号填列） Income before tax
     */
    private BigDecimal jdIncomeBeforeTax;

    /**
     * 减：所得税费用    Less: Income tax
     */
    private BigDecimal yearLessIncomeTax;
    /**
     * 减：所得税费用    Less: Income tax
     */
    private BigDecimal currentLessIncomeTax;
    /**
     * 减：所得税费用    Less: Income tax
     */
    private BigDecimal jdLessIncomeTax;

    /**
     * 四、净利润（净亏损以“－”号填列） NET PROFIT  (LOSS EXPRESSED WITH "-")
     */
    private BigDecimal yearEntIncome;
    /**
     * 四、净利润（净亏损以“－”号填列） NET PROFIT  (LOSS EXPRESSED WITH "-")
     */
    private BigDecimal currentEntIncome;
    /**
     * 四、净利润（净亏损以“－”号填列） NET PROFIT  (LOSS EXPRESSED WITH "-")
     */
    private BigDecimal jdtEntIncome;


    /**
     * 五、每股收益： EARNINGS PER SHARE (EPS)
     */
    private BigDecimal yearEarningsPerShare;
    /**
     * 五、每股收益： EARNINGS PER SHARE (EPS)
     */
    private BigDecimal currentEarningsPerShare;
    /**
     * 五、每股收益： EARNINGS PER SHARE (EPS)
     */
    private BigDecimal jdEarningsPerShare;

    /**
     * （一）基本每股收益 Basic EPS
     */
    private BigDecimal yearBasicEps;
    /**
     * （一）基本每股收益 Basic EPS
     */
    private BigDecimal currentBasicEps;
    /**
     * （一）基本每股收益 Basic EPS
     */
    private BigDecimal jdBasicEps;

    /**
     * （二）稀释每股收益 Diluted EPS
     */
    private BigDecimal yearDilutedEps;
    /**
     * （二）稀释每股收益 Diluted EPS
     */
    private BigDecimal currentDilutedEps;
    /**
     * （二）稀释每股收益 Diluted EPS
     */
    private BigDecimal jdDilutedEps;


    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 时间戳
     */
    private String updateTimestamp;

    public String getPkIncomeStatementId() {
        return pkIncomeStatementId;
    }

    public void setPkIncomeStatementId(String pkIncomeStatementId) {
        this.pkIncomeStatementId = pkIncomeStatementId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public BigDecimal getYearSalesFromOperati() {
        return yearSalesFromOperati;
    }

    public void setYearSalesFromOperati(BigDecimal yearSalesFromOperati) {
        this.yearSalesFromOperati = yearSalesFromOperati;
    }

    public BigDecimal getCurrentSalesFromOperati() {
        return currentSalesFromOperati;
    }

    public void setCurrentSalesFromOperati(BigDecimal currentSalesFromOperati) {
        this.currentSalesFromOperati = currentSalesFromOperati;
    }

    public BigDecimal getJdSalesFromOperati() {
        return jdSalesFromOperati;
    }

    public void setJdSalesFromOperati(BigDecimal jdSalesFromOperati) {
        this.jdSalesFromOperati = jdSalesFromOperati;
    }

    public BigDecimal getYearLessOperatingCosts() {
        return yearLessOperatingCosts;
    }

    public void setYearLessOperatingCosts(BigDecimal yearLessOperatingCosts) {
        this.yearLessOperatingCosts = yearLessOperatingCosts;
    }

    public BigDecimal getCurrentLessOperatingCosts() {
        return currentLessOperatingCosts;
    }

    public void setCurrentLessOperatingCosts(BigDecimal currentLessOperatingCosts) {
        this.currentLessOperatingCosts = currentLessOperatingCosts;
    }

    public BigDecimal getJdLessOperatingCosts() {
        return jdLessOperatingCosts;
    }

    public void setJdLessOperatingCosts(BigDecimal jdLessOperatingCosts) {
        this.jdLessOperatingCosts = jdLessOperatingCosts;
    }

    public BigDecimal getYearOperatingTaxAndAdditions() {
        return yearOperatingTaxAndAdditions;
    }

    public void setYearOperatingTaxAndAdditions(BigDecimal yearOperatingTaxAndAdditions) {
        this.yearOperatingTaxAndAdditions = yearOperatingTaxAndAdditions;
    }

    public BigDecimal getCurrentOperatingTaxAndAdditions() {
        return currentOperatingTaxAndAdditions;
    }

    public void setCurrentOperatingTaxAndAdditions(BigDecimal currentOperatingTaxAndAdditions) {
        this.currentOperatingTaxAndAdditions = currentOperatingTaxAndAdditions;
    }

    public BigDecimal getJdOperatingTaxAndAdditions() {
        return jdOperatingTaxAndAdditions;
    }

    public void setJdOperatingTaxAndAdditions(BigDecimal jdOperatingTaxAndAdditions) {
        this.jdOperatingTaxAndAdditions = jdOperatingTaxAndAdditions;
    }

    public BigDecimal getYearSellingExpenses() {
        return yearSellingExpenses;
    }

    public void setYearSellingExpenses(BigDecimal yearSellingExpenses) {
        this.yearSellingExpenses = yearSellingExpenses;
    }

    public BigDecimal getCurrentSellingExpenses() {
        return currentSellingExpenses;
    }

    public void setCurrentSellingExpenses(BigDecimal currentSellingExpenses) {
        this.currentSellingExpenses = currentSellingExpenses;
    }

    public BigDecimal getJdSellingExpenses() {
        return jdSellingExpenses;
    }

    public void setJdSellingExpenses(BigDecimal jdSellingExpenses) {
        this.jdSellingExpenses = jdSellingExpenses;
    }

    public BigDecimal getYearGeneralAndAdministrativeExpense() {
        return yearGeneralAndAdministrativeExpense;
    }

    public void setYearGeneralAndAdministrativeExpense(BigDecimal yearGeneralAndAdministrativeExpense) {
        this.yearGeneralAndAdministrativeExpense = yearGeneralAndAdministrativeExpense;
    }

    public BigDecimal getCurrentGeneralAndAdministrativeExpense() {
        return currentGeneralAndAdministrativeExpense;
    }

    public void setCurrentGeneralAndAdministrativeExpense(BigDecimal currentGeneralAndAdministrativeExpense) {
        this.currentGeneralAndAdministrativeExpense = currentGeneralAndAdministrativeExpense;
    }

    public BigDecimal getJdGeneralAndAdministrativeExpense() {
        return jdGeneralAndAdministrativeExpense;
    }

    public void setJdGeneralAndAdministrativeExpense(BigDecimal jdGeneralAndAdministrativeExpense) {
        this.jdGeneralAndAdministrativeExpense = jdGeneralAndAdministrativeExpense;
    }

    public BigDecimal getYearFinaneiaExpense() {
        return yearFinaneiaExpense;
    }

    public void setYearFinaneiaExpense(BigDecimal yearFinaneiaExpense) {
        this.yearFinaneiaExpense = yearFinaneiaExpense;
    }

    public BigDecimal getCurrentFinaneiaExpense() {
        return currentFinaneiaExpense;
    }

    public void setCurrentFinaneiaExpense(BigDecimal currentFinaneiaExpense) {
        this.currentFinaneiaExpense = currentFinaneiaExpense;
    }

    public BigDecimal getJdFinaneiaExpense() {
        return jdFinaneiaExpense;
    }

    public void setJdFinaneiaExpense(BigDecimal jdFinaneiaExpense) {
        this.jdFinaneiaExpense = jdFinaneiaExpense;
    }

    public BigDecimal getYearLossesOnTheAssetImpairment() {
        return yearLossesOnTheAssetImpairment;
    }

    public void setYearLossesOnTheAssetImpairment(BigDecimal yearLossesOnTheAssetImpairment) {
        this.yearLossesOnTheAssetImpairment = yearLossesOnTheAssetImpairment;
    }

    public BigDecimal getCurrentLossesOnTheAssetImpairment() {
        return currentLossesOnTheAssetImpairment;
    }

    public void setCurrentLossesOnTheAssetImpairment(BigDecimal currentLossesOnTheAssetImpairment) {
        this.currentLossesOnTheAssetImpairment = currentLossesOnTheAssetImpairment;
    }

    public BigDecimal getJdLossesOnTheAssetImpairment() {
        return jdLossesOnTheAssetImpairment;
    }

    public void setJdLossesOnTheAssetImpairment(BigDecimal jdLossesOnTheAssetImpairment) {
        this.jdLossesOnTheAssetImpairment = jdLossesOnTheAssetImpairment;
    }

    public BigDecimal getYearAddProfitsOrLossesOntheChangesInFairValue() {
        return yearAddProfitsOrLossesOntheChangesInFairValue;
    }

    public void setYearAddProfitsOrLossesOntheChangesInFairValue(BigDecimal yearAddProfitsOrLossesOntheChangesInFairValue) {
        this.yearAddProfitsOrLossesOntheChangesInFairValue = yearAddProfitsOrLossesOntheChangesInFairValue;
    }

    public BigDecimal getCurrentAddProfitsOrLossesOntheChangesInFairValue() {
        return currentAddProfitsOrLossesOntheChangesInFairValue;
    }

    public void setCurrentAddProfitsOrLossesOntheChangesInFairValue(
            BigDecimal currentAddProfitsOrLossesOntheChangesInFairValue) {
        this.currentAddProfitsOrLossesOntheChangesInFairValue = currentAddProfitsOrLossesOntheChangesInFairValue;
    }

    public BigDecimal getJdAddProfitsOrLossesOntheChangesInFairValue() {
        return jdAddProfitsOrLossesOntheChangesInFairValue;
    }

    public void setJdAddProfitsOrLossesOntheChangesInFairValue(BigDecimal jdAddProfitsOrLossesOntheChangesInFairValue) {
        this.jdAddProfitsOrLossesOntheChangesInFairValue = jdAddProfitsOrLossesOntheChangesInFairValue;
    }

    public BigDecimal getYearInvestmentIncome() {
        return yearInvestmentIncome;
    }

    public void setYearInvestmentIncome(BigDecimal yearInvestmentIncome) {
        this.yearInvestmentIncome = yearInvestmentIncome;
    }

    public BigDecimal getCurrentInvestmentIncome() {
        return currentInvestmentIncome;
    }

    public void setCurrentInvestmentIncome(BigDecimal currentInvestmentIncome) {
        this.currentInvestmentIncome = currentInvestmentIncome;
    }

    public BigDecimal getJdInvestmentIncome() {
        return jdInvestmentIncome;
    }

    public void setJdInvestmentIncome(BigDecimal jdInvestmentIncome) {
        this.jdInvestmentIncome = jdInvestmentIncome;
    }

    public BigDecimal getYearAmongInvestmentIncomeFromAffiliatedBusiness() {
        return yearAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public void setYearAmongInvestmentIncomeFromAffiliatedBusiness(
            BigDecimal yearAmongInvestmentIncomeFromAffiliatedBusiness) {
        this.yearAmongInvestmentIncomeFromAffiliatedBusiness = yearAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public BigDecimal getCurrentAmongInvestmentIncomeFromAffiliatedBusiness() {
        return currentAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public void setCurrentAmongInvestmentIncomeFromAffiliatedBusiness(
            BigDecimal currentAmongInvestmentIncomeFromAffiliatedBusiness) {
        this.currentAmongInvestmentIncomeFromAffiliatedBusiness = currentAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public BigDecimal getJdAmongInvestmentIncomeFromAffiliatedBusiness() {
        return jdAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public void setJdAmongInvestmentIncomeFromAffiliatedBusiness(BigDecimal jdAmongInvestmentIncomeFromAffiliatedBusiness) {
        this.jdAmongInvestmentIncomeFromAffiliatedBusiness = jdAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public BigDecimal getYearOperatingIncome() {
        return yearOperatingIncome;
    }

    public void setYearOperatingIncome(BigDecimal yearOperatingIncome) {
        this.yearOperatingIncome = yearOperatingIncome;
    }

    public BigDecimal getCurrentOperatingIncome() {
        return currentOperatingIncome;
    }

    public void setCurrentOperatingIncome(BigDecimal currentOperatingIncome) {
        this.currentOperatingIncome = currentOperatingIncome;
    }

    public BigDecimal getJdOperatingIncome() {
        return jdOperatingIncome;
    }

    public void setJdOperatingIncome(BigDecimal jdOperatingIncome) {
        this.jdOperatingIncome = jdOperatingIncome;
    }

    public BigDecimal getYearAddNonOperatingIncome() {
        return yearAddNonOperatingIncome;
    }

    public void setYearAddNonOperatingIncome(BigDecimal yearAddNonOperatingIncome) {
        this.yearAddNonOperatingIncome = yearAddNonOperatingIncome;
    }

    public BigDecimal getCurrentAddNonOperatingIncome() {
        return currentAddNonOperatingIncome;
    }

    public void setCurrentAddNonOperatingIncome(BigDecimal currentAddNonOperatingIncome) {
        this.currentAddNonOperatingIncome = currentAddNonOperatingIncome;
    }

    public BigDecimal getJdAddNonOperatingIncome() {
        return jdAddNonOperatingIncome;
    }

    public void setJdAddNonOperatingIncome(BigDecimal jdAddNonOperatingIncome) {
        this.jdAddNonOperatingIncome = jdAddNonOperatingIncome;
    }

    public BigDecimal getYearLessNonOperatingExpense() {
        return yearLessNonOperatingExpense;
    }

    public void setYearLessNonOperatingExpense(BigDecimal yearLessNonOperatingExpense) {
        this.yearLessNonOperatingExpense = yearLessNonOperatingExpense;
    }

    public BigDecimal getCurrentLessNonOperatingExpense() {
        return currentLessNonOperatingExpense;
    }

    public void setCurrentLessNonOperatingExpense(BigDecimal currentLessNonOperatingExpense) {
        this.currentLessNonOperatingExpense = currentLessNonOperatingExpense;
    }

    public BigDecimal getJdLessNonOperatingExpense() {
        return jdLessNonOperatingExpense;
    }

    public void setJdLessNonOperatingExpense(BigDecimal jdLessNonOperatingExpense) {
        this.jdLessNonOperatingExpense = jdLessNonOperatingExpense;
    }

    public BigDecimal getYearIncludingLossesFromDisposalOfNonCurrentAssets() {
        return yearIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public void setYearIncludingLossesFromDisposalOfNonCurrentAssets(
            BigDecimal yearIncludingLossesFromDisposalOfNonCurrentAssets) {
        this.yearIncludingLossesFromDisposalOfNonCurrentAssets = yearIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public BigDecimal getCurrentIncludingLossesFromDisposalOfNonCurrentAssets() {
        return currentIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public void setCurrentIncludingLossesFromDisposalOfNonCurrentAssets(
            BigDecimal currentIncludingLossesFromDisposalOfNonCurrentAssets) {
        this.currentIncludingLossesFromDisposalOfNonCurrentAssets = currentIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public BigDecimal getJdIncludingLossesFromDisposalOfNonCurrentAssets() {
        return jdIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public void setJdIncludingLossesFromDisposalOfNonCurrentAssets(
            BigDecimal jdIncludingLossesFromDisposalOfNonCurrentAssets) {
        this.jdIncludingLossesFromDisposalOfNonCurrentAssets = jdIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public BigDecimal getYearIncomeBeforeTax() {
        return yearIncomeBeforeTax;
    }

    public void setYearIncomeBeforeTax(BigDecimal yearIncomeBeforeTax) {
        this.yearIncomeBeforeTax = yearIncomeBeforeTax;
    }

    public BigDecimal getCurrentIncomeBeforeTax() {
        return currentIncomeBeforeTax;
    }

    public void setCurrentIncomeBeforeTax(BigDecimal currentIncomeBeforeTax) {
        this.currentIncomeBeforeTax = currentIncomeBeforeTax;
    }

    public BigDecimal getJdIncomeBeforeTax() {
        return jdIncomeBeforeTax;
    }

    public void setJdIncomeBeforeTax(BigDecimal jdIncomeBeforeTax) {
        this.jdIncomeBeforeTax = jdIncomeBeforeTax;
    }

    public BigDecimal getYearLessIncomeTax() {
        return yearLessIncomeTax;
    }

    public void setYearLessIncomeTax(BigDecimal yearLessIncomeTax) {
        this.yearLessIncomeTax = yearLessIncomeTax;
    }

    public BigDecimal getCurrentLessIncomeTax() {
        return currentLessIncomeTax;
    }

    public void setCurrentLessIncomeTax(BigDecimal currentLessIncomeTax) {
        this.currentLessIncomeTax = currentLessIncomeTax;
    }

    public BigDecimal getJdLessIncomeTax() {
        return jdLessIncomeTax;
    }

    public void setJdLessIncomeTax(BigDecimal jdLessIncomeTax) {
        this.jdLessIncomeTax = jdLessIncomeTax;
    }

    public BigDecimal getYearEntIncome() {
        return yearEntIncome;
    }

    public void setYearEntIncome(BigDecimal yearEntIncome) {
        this.yearEntIncome = yearEntIncome;
    }

    public BigDecimal getCurrentEntIncome() {
        return currentEntIncome;
    }

    public void setCurrentEntIncome(BigDecimal currentEntIncome) {
        this.currentEntIncome = currentEntIncome;
    }

    public BigDecimal getJdtEntIncome() {
        return jdtEntIncome;
    }

    public void setJdtEntIncome(BigDecimal jdtEntIncome) {
        this.jdtEntIncome = jdtEntIncome;
    }

    public BigDecimal getYearEarningsPerShare() {
        return yearEarningsPerShare;
    }

    public void setYearEarningsPerShare(BigDecimal yearEarningsPerShare) {
        this.yearEarningsPerShare = yearEarningsPerShare;
    }

    public BigDecimal getCurrentEarningsPerShare() {
        return currentEarningsPerShare;
    }

    public void setCurrentEarningsPerShare(BigDecimal currentEarningsPerShare) {
        this.currentEarningsPerShare = currentEarningsPerShare;
    }

    public BigDecimal getJdEarningsPerShare() {
        return jdEarningsPerShare;
    }

    public void setJdEarningsPerShare(BigDecimal jdEarningsPerShare) {
        this.jdEarningsPerShare = jdEarningsPerShare;
    }

    public BigDecimal getYearBasicEps() {
        return yearBasicEps;
    }

    public void setYearBasicEps(BigDecimal yearBasicEps) {
        this.yearBasicEps = yearBasicEps;
    }

    public BigDecimal getCurrentBasicEps() {
        return currentBasicEps;
    }

    public void setCurrentBasicEps(BigDecimal currentBasicEps) {
        this.currentBasicEps = currentBasicEps;
    }

    public BigDecimal getJdBasicEps() {
        return jdBasicEps;
    }

    public void setJdBasicEps(BigDecimal jdBasicEps) {
        this.jdBasicEps = jdBasicEps;
    }

    public BigDecimal getYearDilutedEps() {
        return yearDilutedEps;
    }

    public void setYearDilutedEps(BigDecimal yearDilutedEps) {
        this.yearDilutedEps = yearDilutedEps;
    }

    public BigDecimal getCurrentDilutedEps() {
        return currentDilutedEps;
    }

    public void setCurrentDilutedEps(BigDecimal currentDilutedEps) {
        this.currentDilutedEps = currentDilutedEps;
    }

    public BigDecimal getJdDilutedEps() {
        return jdDilutedEps;
    }

    public void setJdDilutedEps(BigDecimal jdDilutedEps) {
        this.jdDilutedEps = jdDilutedEps;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        return "Profit [pkIncomeStatementId=" + pkIncomeStatementId + ", userId=" + userId + ", accountId=" + accountId
                + ", accountPeriod=" + accountPeriod + ", yearSalesFromOperati=" + yearSalesFromOperati
                + ", currentSalesFromOperati=" + currentSalesFromOperati + ", jdSalesFromOperati=" + jdSalesFromOperati
                + ", yearLessOperatingCosts=" + yearLessOperatingCosts + ", currentLessOperatingCosts="
                + currentLessOperatingCosts + ", jdLessOperatingCosts=" + jdLessOperatingCosts
                + ", yearOperatingTaxAndAdditions=" + yearOperatingTaxAndAdditions
                + ", currentOperatingTaxAndAdditions=" + currentOperatingTaxAndAdditions
                + ", jdOperatingTaxAndAdditions=" + jdOperatingTaxAndAdditions + ", yearSellingExpenses="
                + yearSellingExpenses + ", currentSellingExpenses=" + currentSellingExpenses + ", jdSellingExpenses="
                + jdSellingExpenses + ", yearGeneralAndAdministrativeExpense=" + yearGeneralAndAdministrativeExpense
                + ", currentGeneralAndAdministrativeExpense=" + currentGeneralAndAdministrativeExpense
                + ", jdGeneralAndAdministrativeExpense=" + jdGeneralAndAdministrativeExpense + ", yearFinaneiaExpense="
                + yearFinaneiaExpense + ", currentFinaneiaExpense=" + currentFinaneiaExpense + ", jdFinaneiaExpense="
                + jdFinaneiaExpense + ", yearLossesOnTheAssetImpairment=" + yearLossesOnTheAssetImpairment
                + ", currentLossesOnTheAssetImpairment=" + currentLossesOnTheAssetImpairment
                + ", jdLossesOnTheAssetImpairment=" + jdLossesOnTheAssetImpairment
                + ", yearAddProfitsOrLossesOntheChangesInFairValue=" + yearAddProfitsOrLossesOntheChangesInFairValue
                + ", currentAddProfitsOrLossesOntheChangesInFairValue="
                + currentAddProfitsOrLossesOntheChangesInFairValue + ", jdAddProfitsOrLossesOntheChangesInFairValue="
                + jdAddProfitsOrLossesOntheChangesInFairValue + ", yearInvestmentIncome=" + yearInvestmentIncome
                + ", currentInvestmentIncome=" + currentInvestmentIncome + ", jdInvestmentIncome=" + jdInvestmentIncome
                + ", yearAmongInvestmentIncomeFromAffiliatedBusiness=" + yearAmongInvestmentIncomeFromAffiliatedBusiness
                + ", currentAmongInvestmentIncomeFromAffiliatedBusiness="
                + currentAmongInvestmentIncomeFromAffiliatedBusiness
                + ", jdAmongInvestmentIncomeFromAffiliatedBusiness=" + jdAmongInvestmentIncomeFromAffiliatedBusiness
                + ", yearOperatingIncome=" + yearOperatingIncome + ", currentOperatingIncome=" + currentOperatingIncome
                + ", jdOperatingIncome=" + jdOperatingIncome + ", yearAddNonOperatingIncome="
                + yearAddNonOperatingIncome + ", currentAddNonOperatingIncome=" + currentAddNonOperatingIncome
                + ", jdAddNonOperatingIncome=" + jdAddNonOperatingIncome + ", yearLessNonOperatingExpense="
                + yearLessNonOperatingExpense + ", currentLessNonOperatingExpense=" + currentLessNonOperatingExpense
                + ", jdLessNonOperatingExpense=" + jdLessNonOperatingExpense
                + ", yearIncludingLossesFromDisposalOfNonCurrentAssets="
                + yearIncludingLossesFromDisposalOfNonCurrentAssets
                + ", currentIncludingLossesFromDisposalOfNonCurrentAssets="
                + currentIncludingLossesFromDisposalOfNonCurrentAssets
                + ", jdIncludingLossesFromDisposalOfNonCurrentAssets=" + jdIncludingLossesFromDisposalOfNonCurrentAssets
                + ", yearIncomeBeforeTax=" + yearIncomeBeforeTax + ", currentIncomeBeforeTax=" + currentIncomeBeforeTax
                + ", jdIncomeBeforeTax=" + jdIncomeBeforeTax + ", yearLessIncomeTax=" + yearLessIncomeTax
                + ", currentLessIncomeTax=" + currentLessIncomeTax + ", jdLessIncomeTax=" + jdLessIncomeTax
                + ", yearEntIncome=" + yearEntIncome + ", currentEntIncome=" + currentEntIncome + ", jdtEntIncome="
                + jdtEntIncome + ", yearEarningsPerShare=" + yearEarningsPerShare + ", currentEarningsPerShare="
                + currentEarningsPerShare + ", jdEarningsPerShare=" + jdEarningsPerShare + ", yearBasicEps="
                + yearBasicEps + ", currentBasicEps=" + currentBasicEps + ", jdBasicEps=" + jdBasicEps
                + ", yearDilutedEps=" + yearDilutedEps + ", currentDilutedEps=" + currentDilutedEps + ", jdDilutedEps="
                + jdDilutedEps + "]";
    }


}

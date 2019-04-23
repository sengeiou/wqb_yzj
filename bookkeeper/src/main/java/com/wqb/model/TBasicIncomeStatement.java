package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TBasicIncomeStatement implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6754812484093762276L;

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
     * 减：营业成本  Less: operating costs
     */
    private BigDecimal yearLessOperatingCosts;

    /**
     * 减：营业成本  Less: operating costs
     */
    private BigDecimal currentLessOperatingCosts;

    /**
     * 营业税金及附加 Operating tax and additions
     */
    private BigDecimal yearOperatingTaxAndAdditions;

    /**
     * 营业税金及附加 Operating tax and additions
     */
    private BigDecimal currentOperatingTaxAndAdditions;

    /**
     * 销售费用Selling expenses
     */
    private BigDecimal yearSellingExpenses;

    /**
     * 销售费用Selling expenses
     */
    private BigDecimal currentSellingExpenses;

    /**
     * 管理费用General and administrative expense
     */
    private BigDecimal yearGeneralAndAdministrativeExpense;

    /**
     * 管理费用General and administrative expense
     */
    private BigDecimal currentGeneralAndAdministrativeExpense;

    /**
     * 财务费用Finaneial expense
     */
    private BigDecimal yearFinaneiaExpense;

    /**
     * 财务费用Finaneial expense
     */
    private BigDecimal currentFinaneiaExpense;

    /**
     * 资产减值损失Losses on the asset impairment
     */
    private BigDecimal yearLossesOnTheAssetImpairment;

    /**
     * 资产减值损失Losses on the asset impairment
     */
    private BigDecimal currentLossesOnTheAssetImpairment;

    /**
     * 加：公允价值变动收益（损失以“-”号填列）Add:Profits or losses onthe changes in fair value
     */
    private BigDecimal yearAddProfitsOrLossesOntheChangesInFairValue;

    /**
     * 加：公允价值变动收益（损失以“-”号填列）Add:Profits or losses onthe changes in fair value
     */
    private BigDecimal currentAddProfitsOrLossesOntheChangesInFairValue;

    /**
     * 投资收益（损失以“-”号填列）Investment income
     */
    private BigDecimal yearInvestmentIncome;

    /**
     * 投资收益（损失以“-”号填列）Investment income
     */
    private BigDecimal currentInvestmentIncome;

    /**
     * 其中：对联营企业和合营企业的投资收益Among: Investment income from affiliated business
     */
    private BigDecimal yearAmongInvestmentIncomeFromAffiliatedBusiness;

    /**
     * 其中：对联营企业和合营企业的投资收益Among: Investment income from affiliated business
     */
    private BigDecimal currentAmongInvestmentIncomeFromAffiliatedBusiness;

    /**
     * 二、营业利润（亏损以“-”号填列） Operating income
     */
    private BigDecimal yearOperatingIncome;

    /**
     * 二、营业利润（亏损以“-”号填列） Operating income
     */
    private BigDecimal currentOperatingIncome;

    /**
     * 加：营业外收入 Add: Non-operating income
     */
    private BigDecimal yearAddNonOperatingIncome;

    /**
     * 加：营业外收入 Add: Non-operating income
     */
    private BigDecimal currentAddNonOperatingIncome;

    /**
     * 减：营业外支出   Less: Non-operating expense
     */
    private BigDecimal yearLessNonOperatingExpense;

    /**
     * 减：营业外支出   Less: Non-operating expense
     */
    private BigDecimal currentLessNonOperatingExpense;

    /**
     * 其中：非流动资产处置损失Including:Losses from disposal of non-current assets
     */
    private BigDecimal yearIncludingLossesFromDisposalOfNonCurrentAssets;

    /**
     * 其中：非流动资产处置损失Including:Losses from disposal of non-current assets
     */
    private BigDecimal currentIncludingLossesFromDisposalOfNonCurrentAssets;

    /**
     * 三、利润总额（亏损总额以“-”号填列） Income before tax
     */
    private BigDecimal yearIncomeBeforeTax;

    /**
     * 三、利润总额（亏损总额以“-”号填列） Income before tax
     */
    private BigDecimal currentIncomeBeforeTax;

    /**
     * 减：所得税费用    Less: Income tax
     */
    private BigDecimal yearLessIncomeTax;

    /**
     * 减：所得税费用    Less: Income tax
     */
    private BigDecimal currentLessIncomeTax;

    /**
     * 四、净利润（净亏损以“－”号填列） NET PROFIT  (LOSS EXPRESSED WITH "-")
     */
    private BigDecimal yearEntIncome;

    /**
     * 四、净利润（净亏损以“－”号填列） NET PROFIT  (LOSS EXPRESSED WITH "-")
     */
    private BigDecimal currentEntIncome;

    /**
     * 五、每股收益： EARNINGS PER SHARE (EPS)
     */
    private BigDecimal yearEarningsPerShare;

    /**
     * 五、每股收益： EARNINGS PER SHARE (EPS)
     */
    private BigDecimal currentEarningsPerShare;

    /**
     * （一）基本每股收益 Basic EPS
     */
    private BigDecimal yearBasicEps;

    /**
     * （一）基本每股收益 Basic EPS
     */
    private BigDecimal currentBasicEps;

    /**
     * （二）稀释每股收益 Diluted EPS
     */
    private BigDecimal yearDilutedEps;

    /**
     * （二）稀释每股收益 Diluted EPS
     */
    private BigDecimal currentDilutedEps;

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
        this.pkIncomeStatementId = pkIncomeStatementId == null ? null : pkIncomeStatementId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod == null ? null : accountPeriod.trim();
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

    public BigDecimal getYearAddProfitsOrLossesOntheChangesInFairValue() {
        return yearAddProfitsOrLossesOntheChangesInFairValue;
    }

    public void setYearAddProfitsOrLossesOntheChangesInFairValue(BigDecimal yearAddProfitsOrLossesOntheChangesInFairValue) {
        this.yearAddProfitsOrLossesOntheChangesInFairValue = yearAddProfitsOrLossesOntheChangesInFairValue;
    }

    public BigDecimal getCurrentAddProfitsOrLossesOntheChangesInFairValue() {
        return currentAddProfitsOrLossesOntheChangesInFairValue;
    }

    public void setCurrentAddProfitsOrLossesOntheChangesInFairValue(BigDecimal currentAddProfitsOrLossesOntheChangesInFairValue) {
        this.currentAddProfitsOrLossesOntheChangesInFairValue = currentAddProfitsOrLossesOntheChangesInFairValue;
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

    public BigDecimal getYearAmongInvestmentIncomeFromAffiliatedBusiness() {
        return yearAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public void setYearAmongInvestmentIncomeFromAffiliatedBusiness(BigDecimal yearAmongInvestmentIncomeFromAffiliatedBusiness) {
        this.yearAmongInvestmentIncomeFromAffiliatedBusiness = yearAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public BigDecimal getCurrentAmongInvestmentIncomeFromAffiliatedBusiness() {
        return currentAmongInvestmentIncomeFromAffiliatedBusiness;
    }

    public void setCurrentAmongInvestmentIncomeFromAffiliatedBusiness(BigDecimal currentAmongInvestmentIncomeFromAffiliatedBusiness) {
        this.currentAmongInvestmentIncomeFromAffiliatedBusiness = currentAmongInvestmentIncomeFromAffiliatedBusiness;
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

    public BigDecimal getYearIncludingLossesFromDisposalOfNonCurrentAssets() {
        return yearIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public void setYearIncludingLossesFromDisposalOfNonCurrentAssets(BigDecimal yearIncludingLossesFromDisposalOfNonCurrentAssets) {
        this.yearIncludingLossesFromDisposalOfNonCurrentAssets = yearIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public BigDecimal getCurrentIncludingLossesFromDisposalOfNonCurrentAssets() {
        return currentIncludingLossesFromDisposalOfNonCurrentAssets;
    }

    public void setCurrentIncludingLossesFromDisposalOfNonCurrentAssets(BigDecimal currentIncludingLossesFromDisposalOfNonCurrentAssets) {
        this.currentIncludingLossesFromDisposalOfNonCurrentAssets = currentIncludingLossesFromDisposalOfNonCurrentAssets;
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
        this.updateTimestamp = updateTimestamp == null ? null : updateTimestamp.trim();
    }
}

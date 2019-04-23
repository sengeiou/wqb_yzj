package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TBasicBalanceSheet implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8360838197029116184L;

    /**
     * 资产负债表主键
     */
    private String pkBalanceSheetId;

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
     * 货币资金 Cash
     */
    private BigDecimal initCash;

    /**
     * 货币资金 Cash
     */
    private BigDecimal endCash;

    /**
     * 交易性金融资产
     */
    private BigDecimal initTransactionMonetaryAssets;

    /**
     * 交易性金融资产
     */
    private BigDecimal endTransactionMonetaryAssets;

    /**
     * 应收票据
     */
    private BigDecimal initNotesReceivable;

    /**
     * 应收票据
     */
    private BigDecimal endNotesReceivable;

    /**
     * 应收帐款
     */
    private BigDecimal initAccountsReceivable;

    /**
     * 应收帐款
     */
    private BigDecimal endAccountsReceivable;

    /**
     * 预付帐款
     */
    private BigDecimal initAccountsPrepaid;

    /**
     * 预付帐款
     */
    private BigDecimal endAccountsPrepaid;

    /**
     * 应收利息
     */
    private BigDecimal initInterestReceivable;

    /**
     * 应收利息
     */
    private BigDecimal endInterestReceivable;

    /**
     * 应收股利
     */
    private BigDecimal initDividendReceivable;

    /**
     * 应收股利
     */
    private BigDecimal endDividendReceivable;

    /**
     * 其他应收款
     */
    private BigDecimal initOtherReceivables;

    /**
     * 其他应收款
     */
    private BigDecimal endOtherReceivables;

    /**
     * 存货
     */
    private BigDecimal initInventories;

    /**
     * 存货
     */
    private BigDecimal endInventories;

    /**
     * 一年内到期的非流动资产
     */
    private BigDecimal initNonCurrentAssetsDueInOneYear;

    /**
     * 一年内到期的非流动资产
     */
    private BigDecimal endNonCurrentAssetsDueInOneYear;

    /**
     * 其他流动资产
     */
    private BigDecimal initOtherCurrentAssets;

    /**
     * 其他流动资产
     */
    private BigDecimal endOtherCurrentAssets;

    /**
     * 流动资产合计
     */
    private BigDecimal initTotalCurrentAssets;

    /**
     * 流动资产合计
     */
    private BigDecimal endTotalCurrentAssets;

    /**
     * 可供出售金融资产
     */
    private BigDecimal initAvailableForSaleFinancialAssets;

    /**
     * 可供出售金融资产
     */
    private BigDecimal endAvailableForSaleFinancialAssets;

    /**
     * 持有至到期投
     */
    private BigDecimal initHeldToMaturityInvestmen;

    /**
     * 持有至到期投
     */
    private BigDecimal endHeldToMaturityInvestmen;

    /**
     * 长期应收款
     */
    private BigDecimal initLongTermReceivables;

    /**
     * 长期应收款
     */
    private BigDecimal endLongTermReceivables;

    /**
     * 长期股权投资
     */
    private BigDecimal initLongTermEquityInvestment;

    /**
     * 长期股权投资
     */
    private BigDecimal endLongTermEquityInvestment;

    /**
     * 投资性房地产
     */
    private BigDecimal initInvestmentRealEstates;

    /**
     * 投资性房地产
     */
    private BigDecimal endInvestmentRealEstates;

    /**
     * 固定资产
     */
    private BigDecimal initFixedAssets;

    /**
     * 固定资产
     */
    private BigDecimal endFixedAssets;

    /**
     * 在建工程
     */
    private BigDecimal initConstructionInProgress;

    /**
     * 在建工程
     */
    private BigDecimal endConstructionInProgress;

    /**
     * 工程物资
     */
    private BigDecimal initConstructionSupplies;

    /**
     * 工程物资
     */
    private BigDecimal endConstructionSupplies;

    /**
     * 固定资产清理
     */
    private BigDecimal initFixedAssetsPendingDisposal;

    /**
     * 固定资产清理
     */
    private BigDecimal endFixedAssetsPendingDisposal;

    /**
     * 生产性生物资产
     */
    private BigDecimal initBearerBiologicalAssets;

    /**
     * 生产性生物资产
     */
    private BigDecimal endBearerBiologicalAssets;

    /**
     * 油气资产
     */
    private BigDecimal initOilAndNaturalGasAssets;

    /**
     * 油气资产
     */
    private BigDecimal endOilAndNaturalGasAssets;

    /**
     * 无形资产
     */
    private BigDecimal initIntangibelAssets;

    /**
     * 无形资产
     */
    private BigDecimal endIntangibelAssets;

    /**
     * 开发支出
     */
    private BigDecimal initResearchAndDevelopmentCosts;

    /**
     * 开发支出
     */
    private BigDecimal endResearchAndDevelopmentCosts;

    /**
     * 商誉
     */
    private BigDecimal initGoodwill;

    /**
     * 商誉
     */
    private BigDecimal endGoodwill;

    /**
     * 长期待摊费用
     */
    private BigDecimal initLongTermDeferredExpenses;

    /**
     * 长期待摊费用
     */
    private BigDecimal endLongTermDeferredExpenses;

    /**
     * 递延所得税资产
     */
    private BigDecimal initDeferredTaxAssets;

    /**
     * 递延所得税资产
     */
    private BigDecimal endDeferredTaxAssets;

    /**
     * 其他非流动资产other non-current assets
     */
    private BigDecimal initOtherNonCurrentAssets;

    /**
     * 其他非流动资产other non-current assets
     */
    private BigDecimal endOtherNonCurrentAssets;

    /**
     * 非流动资产合计Total of non-current asses
     */
    private BigDecimal initTotalOfNonCurrentAsses;

    /**
     * 非流动资产合计Total of non-current asses
     */
    private BigDecimal endTotalOfNonCurrentAsses;

    /**
     * 资产总计Total of Assets
     */
    private BigDecimal initTotalOfAssets;

    /**
     * 资产总计Total of Assets
     */
    private BigDecimal endTotalOfAssets;

    /**
     * 短期借款 Short-term loan
     */
    private BigDecimal initShortTermLoan;

    /**
     * 短期借款 Short-term loan
     */
    private BigDecimal endShortTermLoan;

    /**
     * 交易性金融负债 tradable financial liabilities
     */
    private BigDecimal initTradableFinancialLiabilities;

    /**
     * 交易性金融负债 tradable financial liabilities
     */
    private BigDecimal endTradableFinancialLiabilities;

    /**
     * 应付票据 Notes payable
     */
    private BigDecimal initNotesPayable;

    /**
     * 应付票据 Notes payable
     */
    private BigDecimal endNotesPayable;

    /**
     * 应付帐款 Accounts payable
     */
    private BigDecimal initAccountsPayable;

    /**
     * 应付帐款 Accounts payable
     */
    private BigDecimal endAccountsPayable;

    /**
     * 预收款项 advance receipts
     */
    private BigDecimal initAdvanceReceipts;

    /**
     * 预收款项 advance receipts
     */
    private BigDecimal endAdvanceReceipts;

    /**
     * 应付职工薪酬 accrued payroll
     */
    private BigDecimal initAccruedPayroll;

    /**
     * 应付职工薪酬 accrued payroll
     */
    private BigDecimal endAccruedPayroll;

    /**
     * 应交税费accrued tax
     */
    private BigDecimal initAccruedTax;

    /**
     * 应交税费accrued tax
     */
    private BigDecimal endAccruedTax;

    /**
     * 应付利息 accrued interest payable
     */
    private BigDecimal initAccruedInterestPayable;

    /**
     * 应付利息 accrued interest payable
     */
    private BigDecimal endAccruedInterestPayable;

    /**
     * 应付股利dividend payable
     */
    private BigDecimal initDividendPayable;

    /**
     * 应付股利dividend payable
     */
    private BigDecimal endDividendPayable;

    /**
     * 其他应付款other payables
     */
    private BigDecimal initOtherPayables;

    /**
     * 其他应付款other payables
     */
    private BigDecimal endOtherPayables;

    /**
     * 一年内到期的非流动负债current liailities falling due within one year
     */
    private BigDecimal initCurrentLiailitiesFallingDueWithinOneYear;

    /**
     * 一年内到期的非流动负债current liailities falling due within one year
     */
    private BigDecimal endCurrentLiailitiesFallingDueWithinOneYear;

    /**
     * 其他流动负债other current liabilities
     */
    private BigDecimal initOtherCurrentLiabilities;

    /**
     * 其他流动负债other current liabilities
     */
    private BigDecimal endOtherCurrentLiabilities;

    /**
     * 流动负债合计Total of current liabilities
     */
    private BigDecimal initTotalOfCurrentLiabilities;

    /**
     * 流动负债合计Total of current liabilities
     */
    private BigDecimal endTotalOfCurrentLiabilities;

    /**
     * 长期借款long-term loan
     */
    private BigDecimal initLongTermLoan;

    /**
     * 长期借款long-term loan
     */
    private BigDecimal endLongTermLoan;

    /**
     * 应付债券bonds payable
     */
    private BigDecimal initBondsPayable;

    /**
     * 应付债券bonds payable
     */
    private BigDecimal endBondsPayable;

    /**
     * 长期应付款long-term accounts payable
     */
    private BigDecimal initLongTermAccountsPayable;

    /**
     * 长期应付款long-term accounts payable
     */
    private BigDecimal endLongTermAccountsPayable;

    /**
     * 专项应付款accounts payable for specialised terms
     */
    private BigDecimal initAccountsPayableForSpecialisedTerms;

    /**
     * 专项应付款accounts payable for specialised terms
     */
    private BigDecimal endAccountsPayableForSpecialisedTerms;

    /**
     * 预计负债provision for liabilities
     */
    private BigDecimal initProvisionForLiabilities;

    /**
     * 预计负债provision for liabilities
     */
    private BigDecimal endProvisionForLiabilities;

    /**
     * 递延所得税负债deferred income tax liabilities
     */
    private BigDecimal initDeferredIncomeTaxLiabilities;

    /**
     * 递延所得税负债deferred income tax liabilities
     */
    private BigDecimal endDeferredIncomeTaxLiabilities;

    /**
     * 其他非流动负债other non-current liabilities
     */
    private BigDecimal initOtherNonCurrentLiabilities;

    /**
     * 其他非流动负债other non-current liabilities
     */
    private BigDecimal endOtherNonCurrentLiabilities;

    /**
     * 非流动负债合计Total of non-current liabilities
     */
    private BigDecimal initTotalOfNonCurrentLiabilities;

    /**
     * 非流动负债合计Total of non-current liabilities
     */
    private BigDecimal endTotalOfNonCurrentLiabilities;

    /**
     * 负债合计Total of liabilities
     */
    private BigDecimal initTotalOfLiabilities;

    /**
     * 负债合计Total of liabilities
     */
    private BigDecimal endTotalOfLiabilities;

    /**
     * 实收资本capital
     */
    private BigDecimal initCapital;

    /**
     * 实收资本capital
     */
    private BigDecimal endCapital;

    /**
     * 资本公积capital reserves
     */
    private BigDecimal initCapitalReserves;

    /**
     * 资本公积capital reserves
     */
    private BigDecimal entCapitalReserves;

    /**
     * 减：库存股less: treasury stock
     */
    private BigDecimal initLessTreasuryStock;

    /**
     * 减：库存股less: treasury stock
     */
    private BigDecimal endLessTreasuryStock;

    /**
     * 盈余公积earnings reserve
     */
    private BigDecimal initEarningsReserve;

    /**
     * 盈余公积earnings reserve
     */
    private BigDecimal endEarningsReserve;

    /**
     * 未分配利润retained earnings
     */
    private BigDecimal initRetainedEarnings;

    /**
     * 未分配利润retained earnings
     */
    private BigDecimal endRetainedEarnings;

    /**
     * 所有者权益总计Total of owners' equity
     */
    private BigDecimal initTotalOfOwnersEquity;

    /**
     * 所有者权益总计Total of owners' equity
     */
    private BigDecimal endTotalOfOwnersEquity;

    /**
     * 负债和所有者权益总计Total of liabilities and owners' equity
     */
    private BigDecimal initTotalOfLiabilitiesAndOwnersEquity;

    /**
     * 负债和所有者权益总计Total of liabilities and owners' equity
     */
    private BigDecimal endTotalOfLiabilitiesAndOwnersEquity;

    /**
     * 类别（1.资产 2.负债 3.所有者权益（或股东权益））
     */
    private String type;

    /**
     * 详细类别(1. 流动资产 2.非流动资产 3.流动负债 4.非流动负债  5.所有者权益（或股东权益））
     */
    private String detailType;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 时间戳
     */
    private String updateTimestamp;

    public String getPkBalanceSheetId() {
        return pkBalanceSheetId;
    }

    public void setPkBalanceSheetId(String pkBalanceSheetId) {
        this.pkBalanceSheetId = pkBalanceSheetId == null ? null : pkBalanceSheetId.trim();
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

    public BigDecimal getInitCash() {
        return initCash;
    }

    public void setInitCash(BigDecimal initCash) {
        this.initCash = initCash;
    }

    public BigDecimal getEndCash() {
        return endCash;
    }

    public void setEndCash(BigDecimal endCash) {
        this.endCash = endCash;
    }

    public BigDecimal getInitTransactionMonetaryAssets() {
        return initTransactionMonetaryAssets;
    }

    public void setInitTransactionMonetaryAssets(BigDecimal initTransactionMonetaryAssets) {
        this.initTransactionMonetaryAssets = initTransactionMonetaryAssets;
    }

    public BigDecimal getEndTransactionMonetaryAssets() {
        return endTransactionMonetaryAssets;
    }

    public void setEndTransactionMonetaryAssets(BigDecimal endTransactionMonetaryAssets) {
        this.endTransactionMonetaryAssets = endTransactionMonetaryAssets;
    }

    public BigDecimal getInitNotesReceivable() {
        return initNotesReceivable;
    }

    public void setInitNotesReceivable(BigDecimal initNotesReceivable) {
        this.initNotesReceivable = initNotesReceivable;
    }

    public BigDecimal getEndNotesReceivable() {
        return endNotesReceivable;
    }

    public void setEndNotesReceivable(BigDecimal endNotesReceivable) {
        this.endNotesReceivable = endNotesReceivable;
    }

    public BigDecimal getInitAccountsReceivable() {
        return initAccountsReceivable;
    }

    public void setInitAccountsReceivable(BigDecimal initAccountsReceivable) {
        this.initAccountsReceivable = initAccountsReceivable;
    }

    public BigDecimal getEndAccountsReceivable() {
        return endAccountsReceivable;
    }

    public void setEndAccountsReceivable(BigDecimal endAccountsReceivable) {
        this.endAccountsReceivable = endAccountsReceivable;
    }

    public BigDecimal getInitAccountsPrepaid() {
        return initAccountsPrepaid;
    }

    public void setInitAccountsPrepaid(BigDecimal initAccountsPrepaid) {
        this.initAccountsPrepaid = initAccountsPrepaid;
    }

    public BigDecimal getEndAccountsPrepaid() {
        return endAccountsPrepaid;
    }

    public void setEndAccountsPrepaid(BigDecimal endAccountsPrepaid) {
        this.endAccountsPrepaid = endAccountsPrepaid;
    }

    public BigDecimal getInitInterestReceivable() {
        return initInterestReceivable;
    }

    public void setInitInterestReceivable(BigDecimal initInterestReceivable) {
        this.initInterestReceivable = initInterestReceivable;
    }

    public BigDecimal getEndInterestReceivable() {
        return endInterestReceivable;
    }

    public void setEndInterestReceivable(BigDecimal endInterestReceivable) {
        this.endInterestReceivable = endInterestReceivable;
    }

    public BigDecimal getInitDividendReceivable() {
        return initDividendReceivable;
    }

    public void setInitDividendReceivable(BigDecimal initDividendReceivable) {
        this.initDividendReceivable = initDividendReceivable;
    }

    public BigDecimal getEndDividendReceivable() {
        return endDividendReceivable;
    }

    public void setEndDividendReceivable(BigDecimal endDividendReceivable) {
        this.endDividendReceivable = endDividendReceivable;
    }

    public BigDecimal getInitOtherReceivables() {
        return initOtherReceivables;
    }

    public void setInitOtherReceivables(BigDecimal initOtherReceivables) {
        this.initOtherReceivables = initOtherReceivables;
    }

    public BigDecimal getEndOtherReceivables() {
        return endOtherReceivables;
    }

    public void setEndOtherReceivables(BigDecimal endOtherReceivables) {
        this.endOtherReceivables = endOtherReceivables;
    }

    public BigDecimal getInitInventories() {
        return initInventories;
    }

    public void setInitInventories(BigDecimal initInventories) {
        this.initInventories = initInventories;
    }

    public BigDecimal getEndInventories() {
        return endInventories;
    }

    public void setEndInventories(BigDecimal endInventories) {
        this.endInventories = endInventories;
    }

    public BigDecimal getInitNonCurrentAssetsDueInOneYear() {
        return initNonCurrentAssetsDueInOneYear;
    }

    public void setInitNonCurrentAssetsDueInOneYear(BigDecimal initNonCurrentAssetsDueInOneYear) {
        this.initNonCurrentAssetsDueInOneYear = initNonCurrentAssetsDueInOneYear;
    }

    public BigDecimal getEndNonCurrentAssetsDueInOneYear() {
        return endNonCurrentAssetsDueInOneYear;
    }

    public void setEndNonCurrentAssetsDueInOneYear(BigDecimal endNonCurrentAssetsDueInOneYear) {
        this.endNonCurrentAssetsDueInOneYear = endNonCurrentAssetsDueInOneYear;
    }

    public BigDecimal getInitOtherCurrentAssets() {
        return initOtherCurrentAssets;
    }

    public void setInitOtherCurrentAssets(BigDecimal initOtherCurrentAssets) {
        this.initOtherCurrentAssets = initOtherCurrentAssets;
    }

    public BigDecimal getEndOtherCurrentAssets() {
        return endOtherCurrentAssets;
    }

    public void setEndOtherCurrentAssets(BigDecimal endOtherCurrentAssets) {
        this.endOtherCurrentAssets = endOtherCurrentAssets;
    }

    public BigDecimal getInitTotalCurrentAssets() {
        return initTotalCurrentAssets;
    }

    public void setInitTotalCurrentAssets(BigDecimal initTotalCurrentAssets) {
        this.initTotalCurrentAssets = initTotalCurrentAssets;
    }

    public BigDecimal getEndTotalCurrentAssets() {
        return endTotalCurrentAssets;
    }

    public void setEndTotalCurrentAssets(BigDecimal endTotalCurrentAssets) {
        this.endTotalCurrentAssets = endTotalCurrentAssets;
    }

    public BigDecimal getInitAvailableForSaleFinancialAssets() {
        return initAvailableForSaleFinancialAssets;
    }

    public void setInitAvailableForSaleFinancialAssets(BigDecimal initAvailableForSaleFinancialAssets) {
        this.initAvailableForSaleFinancialAssets = initAvailableForSaleFinancialAssets;
    }

    public BigDecimal getEndAvailableForSaleFinancialAssets() {
        return endAvailableForSaleFinancialAssets;
    }

    public void setEndAvailableForSaleFinancialAssets(BigDecimal endAvailableForSaleFinancialAssets) {
        this.endAvailableForSaleFinancialAssets = endAvailableForSaleFinancialAssets;
    }

    public BigDecimal getInitHeldToMaturityInvestmen() {
        return initHeldToMaturityInvestmen;
    }

    public void setInitHeldToMaturityInvestmen(BigDecimal initHeldToMaturityInvestmen) {
        this.initHeldToMaturityInvestmen = initHeldToMaturityInvestmen;
    }

    public BigDecimal getEndHeldToMaturityInvestmen() {
        return endHeldToMaturityInvestmen;
    }

    public void setEndHeldToMaturityInvestmen(BigDecimal endHeldToMaturityInvestmen) {
        this.endHeldToMaturityInvestmen = endHeldToMaturityInvestmen;
    }

    public BigDecimal getInitLongTermReceivables() {
        return initLongTermReceivables;
    }

    public void setInitLongTermReceivables(BigDecimal initLongTermReceivables) {
        this.initLongTermReceivables = initLongTermReceivables;
    }

    public BigDecimal getEndLongTermReceivables() {
        return endLongTermReceivables;
    }

    public void setEndLongTermReceivables(BigDecimal endLongTermReceivables) {
        this.endLongTermReceivables = endLongTermReceivables;
    }

    public BigDecimal getInitLongTermEquityInvestment() {
        return initLongTermEquityInvestment;
    }

    public void setInitLongTermEquityInvestment(BigDecimal initLongTermEquityInvestment) {
        this.initLongTermEquityInvestment = initLongTermEquityInvestment;
    }

    public BigDecimal getEndLongTermEquityInvestment() {
        return endLongTermEquityInvestment;
    }

    public void setEndLongTermEquityInvestment(BigDecimal endLongTermEquityInvestment) {
        this.endLongTermEquityInvestment = endLongTermEquityInvestment;
    }

    public BigDecimal getInitInvestmentRealEstates() {
        return initInvestmentRealEstates;
    }

    public void setInitInvestmentRealEstates(BigDecimal initInvestmentRealEstates) {
        this.initInvestmentRealEstates = initInvestmentRealEstates;
    }

    public BigDecimal getEndInvestmentRealEstates() {
        return endInvestmentRealEstates;
    }

    public void setEndInvestmentRealEstates(BigDecimal endInvestmentRealEstates) {
        this.endInvestmentRealEstates = endInvestmentRealEstates;
    }

    public BigDecimal getInitFixedAssets() {
        return initFixedAssets;
    }

    public void setInitFixedAssets(BigDecimal initFixedAssets) {
        this.initFixedAssets = initFixedAssets;
    }

    public BigDecimal getEndFixedAssets() {
        return endFixedAssets;
    }

    public void setEndFixedAssets(BigDecimal endFixedAssets) {
        this.endFixedAssets = endFixedAssets;
    }

    public BigDecimal getInitConstructionInProgress() {
        return initConstructionInProgress;
    }

    public void setInitConstructionInProgress(BigDecimal initConstructionInProgress) {
        this.initConstructionInProgress = initConstructionInProgress;
    }

    public BigDecimal getEndConstructionInProgress() {
        return endConstructionInProgress;
    }

    public void setEndConstructionInProgress(BigDecimal endConstructionInProgress) {
        this.endConstructionInProgress = endConstructionInProgress;
    }

    public BigDecimal getInitConstructionSupplies() {
        return initConstructionSupplies;
    }

    public void setInitConstructionSupplies(BigDecimal initConstructionSupplies) {
        this.initConstructionSupplies = initConstructionSupplies;
    }

    public BigDecimal getEndConstructionSupplies() {
        return endConstructionSupplies;
    }

    public void setEndConstructionSupplies(BigDecimal endConstructionSupplies) {
        this.endConstructionSupplies = endConstructionSupplies;
    }

    public BigDecimal getInitFixedAssetsPendingDisposal() {
        return initFixedAssetsPendingDisposal;
    }

    public void setInitFixedAssetsPendingDisposal(BigDecimal initFixedAssetsPendingDisposal) {
        this.initFixedAssetsPendingDisposal = initFixedAssetsPendingDisposal;
    }

    public BigDecimal getEndFixedAssetsPendingDisposal() {
        return endFixedAssetsPendingDisposal;
    }

    public void setEndFixedAssetsPendingDisposal(BigDecimal endFixedAssetsPendingDisposal) {
        this.endFixedAssetsPendingDisposal = endFixedAssetsPendingDisposal;
    }

    public BigDecimal getInitBearerBiologicalAssets() {
        return initBearerBiologicalAssets;
    }

    public void setInitBearerBiologicalAssets(BigDecimal initBearerBiologicalAssets) {
        this.initBearerBiologicalAssets = initBearerBiologicalAssets;
    }

    public BigDecimal getEndBearerBiologicalAssets() {
        return endBearerBiologicalAssets;
    }

    public void setEndBearerBiologicalAssets(BigDecimal endBearerBiologicalAssets) {
        this.endBearerBiologicalAssets = endBearerBiologicalAssets;
    }

    public BigDecimal getInitOilAndNaturalGasAssets() {
        return initOilAndNaturalGasAssets;
    }

    public void setInitOilAndNaturalGasAssets(BigDecimal initOilAndNaturalGasAssets) {
        this.initOilAndNaturalGasAssets = initOilAndNaturalGasAssets;
    }

    public BigDecimal getEndOilAndNaturalGasAssets() {
        return endOilAndNaturalGasAssets;
    }

    public void setEndOilAndNaturalGasAssets(BigDecimal endOilAndNaturalGasAssets) {
        this.endOilAndNaturalGasAssets = endOilAndNaturalGasAssets;
    }

    public BigDecimal getInitIntangibelAssets() {
        return initIntangibelAssets;
    }

    public void setInitIntangibelAssets(BigDecimal initIntangibelAssets) {
        this.initIntangibelAssets = initIntangibelAssets;
    }

    public BigDecimal getEndIntangibelAssets() {
        return endIntangibelAssets;
    }

    public void setEndIntangibelAssets(BigDecimal endIntangibelAssets) {
        this.endIntangibelAssets = endIntangibelAssets;
    }

    public BigDecimal getInitResearchAndDevelopmentCosts() {
        return initResearchAndDevelopmentCosts;
    }

    public void setInitResearchAndDevelopmentCosts(BigDecimal initResearchAndDevelopmentCosts) {
        this.initResearchAndDevelopmentCosts = initResearchAndDevelopmentCosts;
    }

    public BigDecimal getEndResearchAndDevelopmentCosts() {
        return endResearchAndDevelopmentCosts;
    }

    public void setEndResearchAndDevelopmentCosts(BigDecimal endResearchAndDevelopmentCosts) {
        this.endResearchAndDevelopmentCosts = endResearchAndDevelopmentCosts;
    }

    public BigDecimal getInitGoodwill() {
        return initGoodwill;
    }

    public void setInitGoodwill(BigDecimal initGoodwill) {
        this.initGoodwill = initGoodwill;
    }

    public BigDecimal getEndGoodwill() {
        return endGoodwill;
    }

    public void setEndGoodwill(BigDecimal endGoodwill) {
        this.endGoodwill = endGoodwill;
    }

    public BigDecimal getInitLongTermDeferredExpenses() {
        return initLongTermDeferredExpenses;
    }

    public void setInitLongTermDeferredExpenses(BigDecimal initLongTermDeferredExpenses) {
        this.initLongTermDeferredExpenses = initLongTermDeferredExpenses;
    }

    public BigDecimal getEndLongTermDeferredExpenses() {
        return endLongTermDeferredExpenses;
    }

    public void setEndLongTermDeferredExpenses(BigDecimal endLongTermDeferredExpenses) {
        this.endLongTermDeferredExpenses = endLongTermDeferredExpenses;
    }

    public BigDecimal getInitDeferredTaxAssets() {
        return initDeferredTaxAssets;
    }

    public void setInitDeferredTaxAssets(BigDecimal initDeferredTaxAssets) {
        this.initDeferredTaxAssets = initDeferredTaxAssets;
    }

    public BigDecimal getEndDeferredTaxAssets() {
        return endDeferredTaxAssets;
    }

    public void setEndDeferredTaxAssets(BigDecimal endDeferredTaxAssets) {
        this.endDeferredTaxAssets = endDeferredTaxAssets;
    }

    public BigDecimal getInitOtherNonCurrentAssets() {
        return initOtherNonCurrentAssets;
    }

    public void setInitOtherNonCurrentAssets(BigDecimal initOtherNonCurrentAssets) {
        this.initOtherNonCurrentAssets = initOtherNonCurrentAssets;
    }

    public BigDecimal getEndOtherNonCurrentAssets() {
        return endOtherNonCurrentAssets;
    }

    public void setEndOtherNonCurrentAssets(BigDecimal endOtherNonCurrentAssets) {
        this.endOtherNonCurrentAssets = endOtherNonCurrentAssets;
    }

    public BigDecimal getInitTotalOfNonCurrentAsses() {
        return initTotalOfNonCurrentAsses;
    }

    public void setInitTotalOfNonCurrentAsses(BigDecimal initTotalOfNonCurrentAsses) {
        this.initTotalOfNonCurrentAsses = initTotalOfNonCurrentAsses;
    }

    public BigDecimal getEndTotalOfNonCurrentAsses() {
        return endTotalOfNonCurrentAsses;
    }

    public void setEndTotalOfNonCurrentAsses(BigDecimal endTotalOfNonCurrentAsses) {
        this.endTotalOfNonCurrentAsses = endTotalOfNonCurrentAsses;
    }

    public BigDecimal getInitTotalOfAssets() {
        return initTotalOfAssets;
    }

    public void setInitTotalOfAssets(BigDecimal initTotalOfAssets) {
        this.initTotalOfAssets = initTotalOfAssets;
    }

    public BigDecimal getEndTotalOfAssets() {
        return endTotalOfAssets;
    }

    public void setEndTotalOfAssets(BigDecimal endTotalOfAssets) {
        this.endTotalOfAssets = endTotalOfAssets;
    }

    public BigDecimal getInitShortTermLoan() {
        return initShortTermLoan;
    }

    public void setInitShortTermLoan(BigDecimal initShortTermLoan) {
        this.initShortTermLoan = initShortTermLoan;
    }

    public BigDecimal getEndShortTermLoan() {
        return endShortTermLoan;
    }

    public void setEndShortTermLoan(BigDecimal endShortTermLoan) {
        this.endShortTermLoan = endShortTermLoan;
    }

    public BigDecimal getInitTradableFinancialLiabilities() {
        return initTradableFinancialLiabilities;
    }

    public void setInitTradableFinancialLiabilities(BigDecimal initTradableFinancialLiabilities) {
        this.initTradableFinancialLiabilities = initTradableFinancialLiabilities;
    }

    public BigDecimal getEndTradableFinancialLiabilities() {
        return endTradableFinancialLiabilities;
    }

    public void setEndTradableFinancialLiabilities(BigDecimal endTradableFinancialLiabilities) {
        this.endTradableFinancialLiabilities = endTradableFinancialLiabilities;
    }

    public BigDecimal getInitNotesPayable() {
        return initNotesPayable;
    }

    public void setInitNotesPayable(BigDecimal initNotesPayable) {
        this.initNotesPayable = initNotesPayable;
    }

    public BigDecimal getEndNotesPayable() {
        return endNotesPayable;
    }

    public void setEndNotesPayable(BigDecimal endNotesPayable) {
        this.endNotesPayable = endNotesPayable;
    }

    public BigDecimal getInitAccountsPayable() {
        return initAccountsPayable;
    }

    public void setInitAccountsPayable(BigDecimal initAccountsPayable) {
        this.initAccountsPayable = initAccountsPayable;
    }

    public BigDecimal getEndAccountsPayable() {
        return endAccountsPayable;
    }

    public void setEndAccountsPayable(BigDecimal endAccountsPayable) {
        this.endAccountsPayable = endAccountsPayable;
    }

    public BigDecimal getInitAdvanceReceipts() {
        return initAdvanceReceipts;
    }

    public void setInitAdvanceReceipts(BigDecimal initAdvanceReceipts) {
        this.initAdvanceReceipts = initAdvanceReceipts;
    }

    public BigDecimal getEndAdvanceReceipts() {
        return endAdvanceReceipts;
    }

    public void setEndAdvanceReceipts(BigDecimal endAdvanceReceipts) {
        this.endAdvanceReceipts = endAdvanceReceipts;
    }

    public BigDecimal getInitAccruedPayroll() {
        return initAccruedPayroll;
    }

    public void setInitAccruedPayroll(BigDecimal initAccruedPayroll) {
        this.initAccruedPayroll = initAccruedPayroll;
    }

    public BigDecimal getEndAccruedPayroll() {
        return endAccruedPayroll;
    }

    public void setEndAccruedPayroll(BigDecimal endAccruedPayroll) {
        this.endAccruedPayroll = endAccruedPayroll;
    }

    public BigDecimal getInitAccruedTax() {
        return initAccruedTax;
    }

    public void setInitAccruedTax(BigDecimal initAccruedTax) {
        this.initAccruedTax = initAccruedTax;
    }

    public BigDecimal getEndAccruedTax() {
        return endAccruedTax;
    }

    public void setEndAccruedTax(BigDecimal endAccruedTax) {
        this.endAccruedTax = endAccruedTax;
    }

    public BigDecimal getInitAccruedInterestPayable() {
        return initAccruedInterestPayable;
    }

    public void setInitAccruedInterestPayable(BigDecimal initAccruedInterestPayable) {
        this.initAccruedInterestPayable = initAccruedInterestPayable;
    }

    public BigDecimal getEndAccruedInterestPayable() {
        return endAccruedInterestPayable;
    }

    public void setEndAccruedInterestPayable(BigDecimal endAccruedInterestPayable) {
        this.endAccruedInterestPayable = endAccruedInterestPayable;
    }

    public BigDecimal getInitDividendPayable() {
        return initDividendPayable;
    }

    public void setInitDividendPayable(BigDecimal initDividendPayable) {
        this.initDividendPayable = initDividendPayable;
    }

    public BigDecimal getEndDividendPayable() {
        return endDividendPayable;
    }

    public void setEndDividendPayable(BigDecimal endDividendPayable) {
        this.endDividendPayable = endDividendPayable;
    }

    public BigDecimal getInitOtherPayables() {
        return initOtherPayables;
    }

    public void setInitOtherPayables(BigDecimal initOtherPayables) {
        this.initOtherPayables = initOtherPayables;
    }

    public BigDecimal getEndOtherPayables() {
        return endOtherPayables;
    }

    public void setEndOtherPayables(BigDecimal endOtherPayables) {
        this.endOtherPayables = endOtherPayables;
    }

    public BigDecimal getInitCurrentLiailitiesFallingDueWithinOneYear() {
        return initCurrentLiailitiesFallingDueWithinOneYear;
    }

    public void setInitCurrentLiailitiesFallingDueWithinOneYear(BigDecimal initCurrentLiailitiesFallingDueWithinOneYear) {
        this.initCurrentLiailitiesFallingDueWithinOneYear = initCurrentLiailitiesFallingDueWithinOneYear;
    }

    public BigDecimal getEndCurrentLiailitiesFallingDueWithinOneYear() {
        return endCurrentLiailitiesFallingDueWithinOneYear;
    }

    public void setEndCurrentLiailitiesFallingDueWithinOneYear(BigDecimal endCurrentLiailitiesFallingDueWithinOneYear) {
        this.endCurrentLiailitiesFallingDueWithinOneYear = endCurrentLiailitiesFallingDueWithinOneYear;
    }

    public BigDecimal getInitOtherCurrentLiabilities() {
        return initOtherCurrentLiabilities;
    }

    public void setInitOtherCurrentLiabilities(BigDecimal initOtherCurrentLiabilities) {
        this.initOtherCurrentLiabilities = initOtherCurrentLiabilities;
    }

    public BigDecimal getEndOtherCurrentLiabilities() {
        return endOtherCurrentLiabilities;
    }

    public void setEndOtherCurrentLiabilities(BigDecimal endOtherCurrentLiabilities) {
        this.endOtherCurrentLiabilities = endOtherCurrentLiabilities;
    }

    public BigDecimal getInitTotalOfCurrentLiabilities() {
        return initTotalOfCurrentLiabilities;
    }

    public void setInitTotalOfCurrentLiabilities(BigDecimal initTotalOfCurrentLiabilities) {
        this.initTotalOfCurrentLiabilities = initTotalOfCurrentLiabilities;
    }

    public BigDecimal getEndTotalOfCurrentLiabilities() {
        return endTotalOfCurrentLiabilities;
    }

    public void setEndTotalOfCurrentLiabilities(BigDecimal endTotalOfCurrentLiabilities) {
        this.endTotalOfCurrentLiabilities = endTotalOfCurrentLiabilities;
    }

    public BigDecimal getInitLongTermLoan() {
        return initLongTermLoan;
    }

    public void setInitLongTermLoan(BigDecimal initLongTermLoan) {
        this.initLongTermLoan = initLongTermLoan;
    }

    public BigDecimal getEndLongTermLoan() {
        return endLongTermLoan;
    }

    public void setEndLongTermLoan(BigDecimal endLongTermLoan) {
        this.endLongTermLoan = endLongTermLoan;
    }

    public BigDecimal getInitBondsPayable() {
        return initBondsPayable;
    }

    public void setInitBondsPayable(BigDecimal initBondsPayable) {
        this.initBondsPayable = initBondsPayable;
    }

    public BigDecimal getEndBondsPayable() {
        return endBondsPayable;
    }

    public void setEndBondsPayable(BigDecimal endBondsPayable) {
        this.endBondsPayable = endBondsPayable;
    }

    public BigDecimal getInitLongTermAccountsPayable() {
        return initLongTermAccountsPayable;
    }

    public void setInitLongTermAccountsPayable(BigDecimal initLongTermAccountsPayable) {
        this.initLongTermAccountsPayable = initLongTermAccountsPayable;
    }

    public BigDecimal getEndLongTermAccountsPayable() {
        return endLongTermAccountsPayable;
    }

    public void setEndLongTermAccountsPayable(BigDecimal endLongTermAccountsPayable) {
        this.endLongTermAccountsPayable = endLongTermAccountsPayable;
    }

    public BigDecimal getInitAccountsPayableForSpecialisedTerms() {
        return initAccountsPayableForSpecialisedTerms;
    }

    public void setInitAccountsPayableForSpecialisedTerms(BigDecimal initAccountsPayableForSpecialisedTerms) {
        this.initAccountsPayableForSpecialisedTerms = initAccountsPayableForSpecialisedTerms;
    }

    public BigDecimal getEndAccountsPayableForSpecialisedTerms() {
        return endAccountsPayableForSpecialisedTerms;
    }

    public void setEndAccountsPayableForSpecialisedTerms(BigDecimal endAccountsPayableForSpecialisedTerms) {
        this.endAccountsPayableForSpecialisedTerms = endAccountsPayableForSpecialisedTerms;
    }

    public BigDecimal getInitProvisionForLiabilities() {
        return initProvisionForLiabilities;
    }

    public void setInitProvisionForLiabilities(BigDecimal initProvisionForLiabilities) {
        this.initProvisionForLiabilities = initProvisionForLiabilities;
    }

    public BigDecimal getEndProvisionForLiabilities() {
        return endProvisionForLiabilities;
    }

    public void setEndProvisionForLiabilities(BigDecimal endProvisionForLiabilities) {
        this.endProvisionForLiabilities = endProvisionForLiabilities;
    }

    public BigDecimal getInitDeferredIncomeTaxLiabilities() {
        return initDeferredIncomeTaxLiabilities;
    }

    public void setInitDeferredIncomeTaxLiabilities(BigDecimal initDeferredIncomeTaxLiabilities) {
        this.initDeferredIncomeTaxLiabilities = initDeferredIncomeTaxLiabilities;
    }

    public BigDecimal getEndDeferredIncomeTaxLiabilities() {
        return endDeferredIncomeTaxLiabilities;
    }

    public void setEndDeferredIncomeTaxLiabilities(BigDecimal endDeferredIncomeTaxLiabilities) {
        this.endDeferredIncomeTaxLiabilities = endDeferredIncomeTaxLiabilities;
    }

    public BigDecimal getInitOtherNonCurrentLiabilities() {
        return initOtherNonCurrentLiabilities;
    }

    public void setInitOtherNonCurrentLiabilities(BigDecimal initOtherNonCurrentLiabilities) {
        this.initOtherNonCurrentLiabilities = initOtherNonCurrentLiabilities;
    }

    public BigDecimal getEndOtherNonCurrentLiabilities() {
        return endOtherNonCurrentLiabilities;
    }

    public void setEndOtherNonCurrentLiabilities(BigDecimal endOtherNonCurrentLiabilities) {
        this.endOtherNonCurrentLiabilities = endOtherNonCurrentLiabilities;
    }

    public BigDecimal getInitTotalOfNonCurrentLiabilities() {
        return initTotalOfNonCurrentLiabilities;
    }

    public void setInitTotalOfNonCurrentLiabilities(BigDecimal initTotalOfNonCurrentLiabilities) {
        this.initTotalOfNonCurrentLiabilities = initTotalOfNonCurrentLiabilities;
    }

    public BigDecimal getEndTotalOfNonCurrentLiabilities() {
        return endTotalOfNonCurrentLiabilities;
    }

    public void setEndTotalOfNonCurrentLiabilities(BigDecimal endTotalOfNonCurrentLiabilities) {
        this.endTotalOfNonCurrentLiabilities = endTotalOfNonCurrentLiabilities;
    }

    public BigDecimal getInitTotalOfLiabilities() {
        return initTotalOfLiabilities;
    }

    public void setInitTotalOfLiabilities(BigDecimal initTotalOfLiabilities) {
        this.initTotalOfLiabilities = initTotalOfLiabilities;
    }

    public BigDecimal getEndTotalOfLiabilities() {
        return endTotalOfLiabilities;
    }

    public void setEndTotalOfLiabilities(BigDecimal endTotalOfLiabilities) {
        this.endTotalOfLiabilities = endTotalOfLiabilities;
    }

    public BigDecimal getInitCapital() {
        return initCapital;
    }

    public void setInitCapital(BigDecimal initCapital) {
        this.initCapital = initCapital;
    }

    public BigDecimal getEndCapital() {
        return endCapital;
    }

    public void setEndCapital(BigDecimal endCapital) {
        this.endCapital = endCapital;
    }

    public BigDecimal getInitCapitalReserves() {
        return initCapitalReserves;
    }

    public void setInitCapitalReserves(BigDecimal initCapitalReserves) {
        this.initCapitalReserves = initCapitalReserves;
    }

    public BigDecimal getEntCapitalReserves() {
        return entCapitalReserves;
    }

    public void setEntCapitalReserves(BigDecimal entCapitalReserves) {
        this.entCapitalReserves = entCapitalReserves;
    }

    public BigDecimal getInitLessTreasuryStock() {
        return initLessTreasuryStock;
    }

    public void setInitLessTreasuryStock(BigDecimal initLessTreasuryStock) {
        this.initLessTreasuryStock = initLessTreasuryStock;
    }

    public BigDecimal getEndLessTreasuryStock() {
        return endLessTreasuryStock;
    }

    public void setEndLessTreasuryStock(BigDecimal endLessTreasuryStock) {
        this.endLessTreasuryStock = endLessTreasuryStock;
    }

    public BigDecimal getInitEarningsReserve() {
        return initEarningsReserve;
    }

    public void setInitEarningsReserve(BigDecimal initEarningsReserve) {
        this.initEarningsReserve = initEarningsReserve;
    }

    public BigDecimal getEndEarningsReserve() {
        return endEarningsReserve;
    }

    public void setEndEarningsReserve(BigDecimal endEarningsReserve) {
        this.endEarningsReserve = endEarningsReserve;
    }

    public BigDecimal getInitRetainedEarnings() {
        return initRetainedEarnings;
    }

    public void setInitRetainedEarnings(BigDecimal initRetainedEarnings) {
        this.initRetainedEarnings = initRetainedEarnings;
    }

    public BigDecimal getEndRetainedEarnings() {
        return endRetainedEarnings;
    }

    public void setEndRetainedEarnings(BigDecimal endRetainedEarnings) {
        this.endRetainedEarnings = endRetainedEarnings;
    }

    public BigDecimal getInitTotalOfOwnersEquity() {
        return initTotalOfOwnersEquity;
    }

    public void setInitTotalOfOwnersEquity(BigDecimal initTotalOfOwnersEquity) {
        this.initTotalOfOwnersEquity = initTotalOfOwnersEquity;
    }

    public BigDecimal getEndTotalOfOwnersEquity() {
        return endTotalOfOwnersEquity;
    }

    public void setEndTotalOfOwnersEquity(BigDecimal endTotalOfOwnersEquity) {
        this.endTotalOfOwnersEquity = endTotalOfOwnersEquity;
    }

    public BigDecimal getInitTotalOfLiabilitiesAndOwnersEquity() {
        return initTotalOfLiabilitiesAndOwnersEquity;
    }

    public void setInitTotalOfLiabilitiesAndOwnersEquity(BigDecimal initTotalOfLiabilitiesAndOwnersEquity) {
        this.initTotalOfLiabilitiesAndOwnersEquity = initTotalOfLiabilitiesAndOwnersEquity;
    }

    public BigDecimal getEndTotalOfLiabilitiesAndOwnersEquity() {
        return endTotalOfLiabilitiesAndOwnersEquity;
    }

    public void setEndTotalOfLiabilitiesAndOwnersEquity(BigDecimal endTotalOfLiabilitiesAndOwnersEquity) {
        this.endTotalOfLiabilitiesAndOwnersEquity = endTotalOfLiabilitiesAndOwnersEquity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType == null ? null : detailType.trim();
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

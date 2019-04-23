package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class VoucherHead implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3281495646581063882L;
    // 主键
    private String vouchID;
    // 日期
    private Date vcDate;
    // 币别ID
    private String currencyID;
    // 币别
    private String currency;
    // 备注
    private String des;

    // 修改人ID
    private String updatePsnID;
    // 修改人
    private String updatePsn;
    // 修改时间
    private Date updatedate;
    // 创建人ID
    private String createPsnID;
    // 创建人
    private String createpsn;
    // 创建日期
    private long createDate;
    // 账套ID
    private String accountID;
    // 登录用户ID
    private String userID;
    // 审核日期
    private Date checkedDate;
    // 期间
    private String period;
    // 审核人
    private String checker;
    // 审核状态(0:未审核1:审核)
    private Integer auditStatus;
    // 来源0:发票1:银行2：固定资产3:工资4:结转损益
    private Integer source;
    // 凭证贷方金额合计
    private Double totalCredit;
    // 凭证借方金额合计
    private Double totalDbit;
    // 0:非模凭证1:模板凭证
    private Integer vouchFlag;
    // 是否有问题
    private String isproblem;
    // 修改凭证次数
    //private Integer modify;
    // 凭证头
    private Integer voucherNO;
    // 附件名称
    private String attachID;
    private Integer voucherType;

    public String getAttachID() {
        return attachID;
    }

    public void setAttachID(String attachID) {
        this.attachID = attachID;
    }

    public Integer getVoucherNO() {
        return voucherNO;
    }

    public String getIsproblem() {
        return isproblem;
    }

    public void setIsproblem(String isproblem) {
        this.isproblem = isproblem;
    }

    public Date getVcDate() {
        return vcDate;
    }

    public void setVcDate(Date vcDate) {
        this.vcDate = vcDate;
    }

    public void setVoucherNO(Integer voucherNO) {
        this.voucherNO = voucherNO;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUpdatePsnID() {
        return updatePsnID;
    }

    public void setUpdatePsnID(String updatePsnID) {
        this.updatePsnID = updatePsnID;
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }

    public String getCreatepsn() {
        return createpsn;
    }

    public void setCreatepsn(String createpsn) {
        this.createpsn = createpsn;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Double getTotalDbit() {
        return totalDbit;
    }

    public void setTotalDbit(Double totalDbit) {
        this.totalDbit = totalDbit;
    }

    public Integer getVouchFlag() {
        return vouchFlag;
    }

    public void setVouchFlag(Integer vouchFlag) {
        this.vouchFlag = vouchFlag;
    }


    public Integer getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(Integer voucherType) {
        this.voucherType = voucherType;
    }

    @Override
    public String toString() {
        return "VoucherHead [vouchID=" + vouchID + ", currencyID=" + currencyID + ", des=" + des + ", accountID="
                + accountID + ", userID=" + userID + ", checkedDate=" + checkedDate + ", period=" + period
                + ", checker=" + checker + ", auditStatus=" + auditStatus + ", source=" + source + ", totalCredit="
                + totalCredit + ", totalDbit=" + totalDbit + ", isproblem=" + isproblem + ", voucherNO=" + voucherNO
                + "]";
    }

}

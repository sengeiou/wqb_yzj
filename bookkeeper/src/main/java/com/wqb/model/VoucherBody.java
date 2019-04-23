package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class VoucherBody implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7946055714945326842L;
    // 主键
    private String vouchAID;
    // 凭证主表主键
    private String vouchID;
    // 分录号
    private String rowIndex;
    // 摘要
    private String vcabstact;
    // 科目名称
    private String vcsubject;
    // 计量单位ID
    private String vcunitID;
    // 计量单位
    private String vcunit;
    // 期间
    private String period;
    // 借方金额
    private Double debitAmount;
    // 贷方金额
    private Double creditAmount;
    // 账套ID
    private String accountID;
    // 租户ID
    private String userID;
    // 方向(1:借2:贷)
    private String direction;
    // 科目主键
    private String subjectID;
    // 修改人ID
    private String updatePsnID;
    // 修改人
    private String updatePsn;
    // 修改时间
    private Date updatedate;
    //数量
    private Double number;
    //单价
    private Double price;
    //是否有问题
    private String isproblem;
    //备注
    private String des;

    public String getIsproblem() {
        return isproblem;
    }

    public void setIsproblem(String isproblem) {
        this.isproblem = isproblem;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getVouchAID() {
        return vouchAID;
    }

    public void setVouchAID(String vouchAID) {
        this.vouchAID = vouchAID;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getVcabstact() {
        return vcabstact;
    }

    public void setVcabstact(String vcabstact) {
        this.vcabstact = vcabstact;
    }

    public String getVcsubject() {
        return vcsubject;
    }

    public void setVcsubject(String vcsubject) {
        this.vcsubject = vcsubject;
    }

    public String getVcunitID() {
        return vcunitID;
    }

    public void setVcunitID(String vcunitID) {
        this.vcunitID = vcunitID;
    }

    public String getVcunit() {
        return vcunit;
    }

    public void setVcunit(String vcunit) {
        this.vcunit = vcunit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
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

    @Override
    public String toString() {
        return "VoucherBody [vouchAID=" + vouchAID + ", vouchID=" + vouchID + ", rowIndex=" + rowIndex + ", vcabstact="
                + vcabstact + ", vcsubject=" + vcsubject + ", period=" + period + ", debitAmount=" + debitAmount
                + ", creditAmount=" + creditAmount + ", direction=" + direction + ", subjectID=" + subjectID
                + ", number=" + number + ", price=" + price + ", isproblem=" + isproblem + ", des=" + des + "]";
    }


}

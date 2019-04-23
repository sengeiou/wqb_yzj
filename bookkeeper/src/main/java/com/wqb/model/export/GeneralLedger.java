package com.wqb.model.export;

import java.io.Serializable;
import java.math.BigDecimal;

public class GeneralLedger implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1392349551609924553L;
    // 科目编码
    private String subCode;
    // 科目名称
    private String subName;
    // 期间
    private String period;
    // 摘要
    private String des;
    // 借方金额
    private BigDecimal debitAmount;
    // 贷方金额
    private BigDecimal creditAmount;
    // 方向
    private String direction;
    // 余额
    private BigDecimal balanceAmount;

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

}

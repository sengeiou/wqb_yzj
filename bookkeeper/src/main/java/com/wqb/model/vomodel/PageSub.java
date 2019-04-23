package com.wqb.model.vomodel;

import java.io.Serializable;
import java.math.BigDecimal;

//页面查询全部科目
public class PageSub implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8301905795284430663L;
    private String pkSubId;
    private String subCode;
    // private String accountId;
    private String subName;
    private String fullName;
    private BigDecimal endingBalanceDebit;
    private BigDecimal endingBalanceCredit;
    private String dir;

    public String getPkSubId() {
        return pkSubId;
    }

    public void setPkSubId(String pkSubId) {
        this.pkSubId = pkSubId;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getEndingBalanceDebit() {
        return endingBalanceDebit;
    }

    public void setEndingBalanceDebit(BigDecimal endingBalanceDebit) {
        this.endingBalanceDebit = endingBalanceDebit;
    }

    public BigDecimal getEndingBalanceCredit() {
        return endingBalanceCredit;
    }

    public void setEndingBalanceCredit(BigDecimal endingBalanceCredit) {
        this.endingBalanceCredit = endingBalanceCredit;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "PageSub [subCode=" + subCode + ", subName=" + subName + ", fullName=" + fullName
                + ", endingBalanceDebit=" + endingBalanceDebit + ", endingBalanceCredit=" + endingBalanceCredit
                + ", dir=" + dir + "]";
    }


}

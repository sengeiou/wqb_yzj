package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProfitTrendVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2674415417124151826L;
    private String id;
    private String period;
    private String accountID;
    private BigDecimal curr_yysr;
    private BigDecimal curr_yycb;
    private BigDecimal curr_yysjjfj;
    private BigDecimal curr_xxfy;
    private BigDecimal curr_glfy;
    private BigDecimal curr_cwfy;
    private BigDecimal curr_zcjzss;
    private BigDecimal curr_bdsy;
    private BigDecimal curr_tzsy;
    private BigDecimal curr_lyhytzsy;
    private BigDecimal curr_yylr;
    private BigDecimal curr_jlr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public BigDecimal getCurr_yysr() {
        return curr_yysr;
    }

    public void setCurr_yysr(BigDecimal curr_yysr) {
        this.curr_yysr = curr_yysr;
    }

    public BigDecimal getCurr_yycb() {
        return curr_yycb;
    }

    public void setCurr_yycb(BigDecimal curr_yycb) {
        this.curr_yycb = curr_yycb;
    }

    public BigDecimal getCurr_yysjjfj() {
        return curr_yysjjfj;
    }

    public void setCurr_yysjjfj(BigDecimal curr_yysjjfj) {
        this.curr_yysjjfj = curr_yysjjfj;
    }

    public BigDecimal getCurr_xxfy() {
        return curr_xxfy;
    }

    public void setCurr_xxfy(BigDecimal curr_xxfy) {
        this.curr_xxfy = curr_xxfy;
    }

    public BigDecimal getCurr_glfy() {
        return curr_glfy;
    }

    public void setCurr_glfy(BigDecimal curr_glfy) {
        this.curr_glfy = curr_glfy;
    }

    public BigDecimal getCurr_cwfy() {
        return curr_cwfy;
    }

    public void setCurr_cwfy(BigDecimal curr_cwfy) {
        this.curr_cwfy = curr_cwfy;
    }

    public BigDecimal getCurr_zcjzss() {
        return curr_zcjzss;
    }

    public void setCurr_zcjzss(BigDecimal curr_zcjzss) {
        this.curr_zcjzss = curr_zcjzss;
    }

    public BigDecimal getCurr_bdsy() {
        return curr_bdsy;
    }

    public void setCurr_bdsy(BigDecimal curr_bdsy) {
        this.curr_bdsy = curr_bdsy;
    }

    public BigDecimal getCurr_tzsy() {
        return curr_tzsy;
    }

    public void setCurr_tzsy(BigDecimal curr_tzsy) {
        this.curr_tzsy = curr_tzsy;
    }

    public BigDecimal getCurr_lyhytzsy() {
        return curr_lyhytzsy;
    }

    public void setCurr_lyhytzsy(BigDecimal curr_lyhytzsy) {
        this.curr_lyhytzsy = curr_lyhytzsy;
    }

    public BigDecimal getCurr_yylr() {
        return curr_yylr;
    }

    public void setCurr_yylr(BigDecimal curr_yylr) {
        this.curr_yylr = curr_yylr;
    }

    public BigDecimal getCurr_jlr() {
        return curr_jlr;
    }

    public void setCurr_jlr(BigDecimal curr_jlr) {
        this.curr_jlr = curr_jlr;
    }

    @Override
    public String toString() {
        return "ProfitTrendVo [id=" + id + ", period=" + period + ", accountID=" + accountID + ", curr_yysr="
                + curr_yysr + ", curr_yycb=" + curr_yycb + ", curr_yysjjfj=" + curr_yysjjfj + ", curr_xxfy=" + curr_xxfy
                + ", curr_glfy=" + curr_glfy + ", curr_cwfy=" + curr_cwfy + ", curr_zcjzss=" + curr_zcjzss
                + ", curr_bdsy=" + curr_bdsy + ", curr_tzsy=" + curr_tzsy + ", curr_lyhytzsy=" + curr_lyhytzsy
                + ", curr_yylr=" + curr_yylr + ", curr_jlr=" + curr_jlr + "]";
    }


}

package com.wqb.model.vomodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SubBookMessageVo implements Serializable {
    private static final long serialVersionUID = 7102140357628322836L;

    private Integer sub_bk_id;    //主键
    private String accountID;    //账套
    private String period;    //期间
    private String vouchID;    //凭证主键
    private String vouchAID;    //凭证体主键
    private Integer vouchNum;    //凭证号
    private String vcabstact;    //摘要
    private String sub_code;    //科目编码
    private String sub_name;    //科目名称
    private BigDecimal debitAmount;    //金额
    private BigDecimal creditAmount;    //金额
    private BigDecimal blanceAmount;    //金额
    private String direction;    //科目方向
    private Date updateDate;    //更新时间
    private Long up_date;    //时间戳

    private String pk_sub_id;
    private String account_id;// 账套ID
    private String account_period;// 做帐的真实期间 年 - 月(帐套启用年-月份）
    private String s_subCode;// 科目代码
    private String s_subName;// 科目名称
    private String full_name;// 科目完整名称
    private Integer code_level;// 编码级别
    private Integer debit_credit_direction;// 科目方向
    private BigDecimal init_debit_balance;// 期初余额(借方)
    private BigDecimal init_credit_balance;// 期初余额(贷方)
    private BigDecimal current_amount_debit;// 本期发生额(借方)
    private BigDecimal current_amount_credit;// 本期发生额(贷方)
    private BigDecimal year_amount_debit;// 本年累计发生额(借方)
    private BigDecimal year_amount_credit;// 本年累计发生额(贷方)
    private BigDecimal ending_balance_debit;// 期末余额(借方)
    private BigDecimal ending_balance_credit;// 期末余额(贷方)

    public Integer getSub_bk_id() {
        return sub_bk_id;
    }

    public void setSub_bk_id(Integer sub_bk_id) {
        this.sub_bk_id = sub_bk_id;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }

    public String getVouchAID() {
        return vouchAID;
    }

    public void setVouchAID(String vouchAID) {
        this.vouchAID = vouchAID;
    }

    public Integer getVouchNum() {
        return vouchNum;
    }

    public void setVouchNum(Integer vouchNum) {
        this.vouchNum = vouchNum;
    }

    public String getVcabstact() {
        return vcabstact;
    }

    public void setVcabstact(String vcabstact) {
        this.vcabstact = vcabstact;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
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

    public BigDecimal getBlanceAmount() {
        return blanceAmount;
    }

    public void setBlanceAmount(BigDecimal blanceAmount) {
        this.blanceAmount = blanceAmount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUp_date() {
        return up_date;
    }

    public void setUp_date(Long up_date) {
        this.up_date = up_date;
    }

    public String getPk_sub_id() {
        return pk_sub_id;
    }

    public void setPk_sub_id(String pk_sub_id) {
        this.pk_sub_id = pk_sub_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_period() {
        return account_period;
    }

    public void setAccount_period(String account_period) {
        this.account_period = account_period;
    }

    public String getS_subCode() {
        return s_subCode;
    }

    public void setS_subCode(String s_subCode) {
        this.s_subCode = s_subCode;
    }

    public String getS_subName() {
        return s_subName;
    }

    public void setS_subName(String s_subName) {
        this.s_subName = s_subName;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Integer getCode_level() {
        return code_level;
    }

    public void setCode_level(Integer code_level) {
        this.code_level = code_level;
    }

    public Integer getDebit_credit_direction() {
        return debit_credit_direction;
    }

    public void setDebit_credit_direction(Integer debit_credit_direction) {
        this.debit_credit_direction = debit_credit_direction;
    }

    public BigDecimal getInit_debit_balance() {
        return init_debit_balance;
    }

    public void setInit_debit_balance(BigDecimal init_debit_balance) {
        this.init_debit_balance = init_debit_balance;
    }

    public BigDecimal getInit_credit_balance() {
        return init_credit_balance;
    }

    public void setInit_credit_balance(BigDecimal init_credit_balance) {
        this.init_credit_balance = init_credit_balance;
    }

    public BigDecimal getCurrent_amount_debit() {
        return current_amount_debit;
    }

    public void setCurrent_amount_debit(BigDecimal current_amount_debit) {
        this.current_amount_debit = current_amount_debit;
    }

    public BigDecimal getCurrent_amount_credit() {
        return current_amount_credit;
    }

    public void setCurrent_amount_credit(BigDecimal current_amount_credit) {
        this.current_amount_credit = current_amount_credit;
    }

    public BigDecimal getYear_amount_debit() {
        return year_amount_debit;
    }

    public void setYear_amount_debit(BigDecimal year_amount_debit) {
        this.year_amount_debit = year_amount_debit;
    }

    public BigDecimal getYear_amount_credit() {
        return year_amount_credit;
    }

    public void setYear_amount_credit(BigDecimal year_amount_credit) {
        this.year_amount_credit = year_amount_credit;
    }

    public BigDecimal getEnding_balance_debit() {
        return ending_balance_debit;
    }

    public void setEnding_balance_debit(BigDecimal ending_balance_debit) {
        this.ending_balance_debit = ending_balance_debit;
    }

    public BigDecimal getEnding_balance_credit() {
        return ending_balance_credit;
    }

    public void setEnding_balance_credit(BigDecimal ending_balance_credit) {
        this.ending_balance_credit = ending_balance_credit;
    }

    @Override
    public String toString() {
        return "SubBookMessageVo [sub_bk_id=" + sub_bk_id + ", accountID=" + accountID + ", period=" + period
                + ", vouchID=" + vouchID + ", vouchAID=" + vouchAID + ", vouchNum=" + vouchNum + ", vcabstact="
                + vcabstact + ", sub_code=" + sub_code + ", sub_name=" + sub_name + ", debitAmount=" + debitAmount
                + ", creditAmount=" + creditAmount + ", blanceAmount=" + blanceAmount + ", direction=" + direction
                + ", updateDate=" + updateDate + ", up_date=" + up_date + ", pk_sub_id=" + pk_sub_id + ", account_id="
                + account_id + ", account_period=" + account_period + ", s_subCode=" + s_subCode + ", s_subName="
                + s_subName + ", full_name=" + full_name + ", code_level=" + code_level + ", debit_credit_direction="
                + debit_credit_direction + ", init_debit_balance=" + init_debit_balance + ", init_credit_balance="
                + init_credit_balance + ", current_amount_debit=" + current_amount_debit + ", current_amount_credit="
                + current_amount_credit + ", year_amount_debit=" + year_amount_debit + ", year_amount_credit="
                + year_amount_credit + ", ending_balance_debit=" + ending_balance_debit + ", ending_balance_credit="
                + ending_balance_credit + "]";
    }


}

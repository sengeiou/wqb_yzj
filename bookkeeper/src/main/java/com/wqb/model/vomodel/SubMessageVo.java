package com.wqb.model.vomodel;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubMessageVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5552768781338500731L;
    private String pk_sub_id;
    private String account_id;//账套ID
    private String account_period;//做帐的真实期间 年 - 月(帐套启用年-月份）
    private String sub_code;//科目代码
    private String sub_name;// 科目名称
    private BigDecimal init_debit_balance;// 期初余额(借方)
    private BigDecimal init_credit_balance;//期初余额(贷方)
    private BigDecimal current_amount_debit;//本期发生额(借方)
    private BigDecimal current_amount_credit;//本期发生额(贷方)
    private BigDecimal year_amount_debit;//本年累计发生额(借方)
    private BigDecimal year_amount_credit;//本年累计发生额(贷方)
    private BigDecimal ending_balance_debit;//期末余额(借方)
    private BigDecimal ending_balance_credit;//期末余额(贷方)

    private String full_name;//科目完整名称
    private String superior_coding;    //上级编码
    private String category;// 1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类
    private BigDecimal amount;// 金额=数量*金额
    private Integer code_level;//编码级别
    private Integer debit_credit_direction;//科目方向

    private String zhaiYao;//摘要
    private String fx_jd;//方向

    private int index;//索引

    private BigDecimal jf_amount;//借方余额(贷方)
    private BigDecimal df_amount;//贷方余额(贷方)


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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getZhaiYao() {
        return zhaiYao;
    }

    public void setZhaiYao(String zhaiYao) {
        this.zhaiYao = zhaiYao;
    }

    public String getFx_jd() {
        return fx_jd;
    }

    public void setFx_jd(String fx_jd) {
        this.fx_jd = fx_jd;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public BigDecimal getJf_amount() {
        return jf_amount;
    }

    public void setJf_amount(BigDecimal jf_amount) {
        this.jf_amount = jf_amount;
    }

    public BigDecimal getDf_amount() {
        return df_amount;
    }

    public void setDf_amount(BigDecimal df_amount) {
        this.df_amount = df_amount;
    }

    public String getSuperior_coding() {
        return superior_coding;
    }

    public void setSuperior_coding(String superior_coding) {
        this.superior_coding = superior_coding;
    }

    @Override
    public String toString() {
        return "SubMessageVo [pk_sub_id=" + pk_sub_id + ", account_id=" + account_id + ", account_period="
                + account_period + ", sub_code=" + sub_code + ", sub_name=" + sub_name + ", init_debit_balance="
                + init_debit_balance + ", init_credit_balance=" + init_credit_balance + ", current_amount_debit="
                + current_amount_debit + ", current_amount_credit=" + current_amount_credit + ", year_amount_debit="
                + year_amount_debit + ", year_amount_credit=" + year_amount_credit + ", ending_balance_debit="
                + ending_balance_debit + ", ending_balance_credit=" + ending_balance_credit + ", full_name=" + full_name
                + ", category=" + category + ", amount=" + amount + ", code_level=" + code_level
                + ", debit_credit_direction=" + debit_credit_direction + ", zhaiYao=" + zhaiYao + ", fx_jd=" + fx_jd
                + ", index=" + index + ", jf_amount=" + jf_amount + ", df_amount=" + df_amount + "]";
    }


}

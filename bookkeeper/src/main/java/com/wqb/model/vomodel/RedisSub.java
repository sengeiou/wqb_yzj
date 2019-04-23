package com.wqb.model.vomodel;

import java.io.Serializable;
import java.math.BigDecimal;

public class RedisSub implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4298556860246670622L;
    private String pk_sub_id;                        // 主键
    private String account_id;                    // 账套ID
    private String account_period;                //做帐期间
    private String sub_code;                        //科目代码
    private String sub_name;                        //科目名称
    private String full_name;                        //科目完整名称
    private BigDecimal init_debit_balance;        //期初余额(借方)
    private BigDecimal init_credit_balance;        //期初余额(贷方)
    private BigDecimal current_amount_debit;        //本期发生额(借方)
    private BigDecimal current_amount_credit;    //本期发生额(贷方)
    private BigDecimal year_amount_debit;        //本年累计发生额(借方)
    private BigDecimal year_amount_credit;        //本年累计发生额(贷方)
    private BigDecimal ending_balance_debit;        //期末余额(借方)
    private BigDecimal ending_balance_credit;    //期末余额(贷方)

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

    /*public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }*/
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account_period == null) ? 0 : account_period.hashCode());
        result = prime * result + ((sub_code == null) ? 0 : sub_code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RedisSub other = (RedisSub) obj;
        if (account_period == null) {
            if (other.account_period != null)
                return false;
        } else if (!account_period.equals(other.account_period))
            return false;
        if (sub_code == null) {
            if (other.sub_code != null)
                return false;
        } else if (!sub_code.equals(other.sub_code))
            return false;
        return true;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "RedisSub [pk_sub_id=" + pk_sub_id + ", account_id=" + account_id + ", sub_code=" + sub_code
                + ", sub_name=" + sub_name + ", init_debit_balance=" + init_debit_balance + ", init_credit_balance="
                + init_credit_balance + ", current_amount_debit=" + current_amount_debit + ", current_amount_credit="
                + current_amount_credit + ", year_amount_debit=" + year_amount_debit + ", year_amount_credit="
                + year_amount_credit + ", ending_balance_debit=" + ending_balance_debit + ", ending_balance_credit="
                + ending_balance_credit + "]";
    }


}

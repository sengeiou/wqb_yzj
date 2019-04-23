package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SubjectMessage implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4683718120690250786L;
    private String pk_sub_id;        // '主键',
    private String user_id;//` varchar(32) NOT NULL COMMENT '用户ID',
    private String account_id;//` varchar(32) NOT NULL COMMENT '账套ID',
    private String account_period;//做帐的真实期间 年 - 月(帐套启用年-月份）
    private String excel_import_period;//导入的期间只有月份
    private String sub_code;//` varchar(50) DEFAULT NULL COMMENT '科目代码',
    private String sub_name;//` varchar(100) DEFAULT NULL COMMENT '科目名称',
    private String type_of_currency;//` varchar(20) DEFAULT NULL COMMENT '币别',
    private BigDecimal init_debit_balance;//` decimal(10,2) DEFAULT NULL COMMENT '期初余额(借方)',
    private BigDecimal init_credit_balance;//` decimal(10,2) DEFAULT NULL COMMENT '期初余额(贷方)',
    private BigDecimal current_amount_debit;//` decimal(10,2) DEFAULT NULL COMMENT '本期发生额(借方)',
    private BigDecimal current_amount_credit;//` decimal(10,2) DEFAULT NULL COMMENT '本期发生额(贷方)',
    private BigDecimal year_amount_debit;//` decimal(10,2) DEFAULT NULL COMMENT '本年累计发生额(借方)',
    private BigDecimal year_amount_credit;//` decimal(10,2) DEFAULT NULL COMMENT '本年累计发生额(贷方)',
    private BigDecimal ending_balance_debit;//` decimal(10,2) DEFAULT NULL COMMENT '期末余额(借方)',
    private String excel_import_code;//excel导入的编码
    private BigDecimal ending_balance_credit;//` decimal(10,2) DEFAULT NULL COMMENT '期末余额(贷方)',
    private String is_multiple_siblings;//` varchar(2) DEFAULT '0' COMMENT '是否多个同级(0无，1有)',
    private String siblings_coding;//同级编码(一个银行多个外币时用到)
    private String superior_coding;// 上级编码(1级为0，二级取前4位）
    private String full_name;// varchar(255) DEFAULT NULL COMMENT '科目完整名称',
    private Date update_date; // datetime  NULL COMMENT '更新时间',
    private String update_timestamp; // char(20) DEFAULT NULL COMMENT '时间戳',
    private String category;// varchar(255) DEFAULT NULL COMMENT '类别(1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类)类别(1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类)',
    private String sub_source;// varchar(255) DEFAULT '' COMMENT '科目来源（导入）',
    private String unit;// varchar(255) DEFAULT NULL COMMENT '计量单位',
    private BigDecimal unit_id;// decimal(11,0) DEFAULT NULL COMMENT '计量单位ID',
    private BigDecimal price;// decimal(10,0) DEFAULT NULL COMMENT '单价(国际单位)',
    private BigDecimal number;// decimal(11,0) DEFAULT NULL COMMENT '数量',
    private BigDecimal amount;// decimal(10,0) DEFAULT NULL COMMENT '金额=数量*金额',
    private String state;// 启用状态',
    private String mender;//修改者',
    private String fk_t_basic_measure_id; //数量单位主键
    private Integer measure_state;// 数量单位核算状态(0关闭，1开启）
    private String fk_exchange_rate_id;// 汇率设置主键
    private Integer exchange_rate__state;// 外币设置状态(0关闭，1开启）
    private Integer code_level;//编码级别
    private Integer debit_credit_direction;//科目方向

    public String getPk_sub_id() {
        return pk_sub_id;
    }

    public void setPk_sub_id(String pk_sub_id) {
        this.pk_sub_id = pk_sub_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getExcel_import_period() {
        return excel_import_period;
    }

    public void setExcel_import_period(String excel_import_period) {
        this.excel_import_period = excel_import_period;
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

    public String getType_of_currency() {
        return type_of_currency;
    }

    public void setType_of_currency(String type_of_currency) {
        this.type_of_currency = type_of_currency;
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

    public String getExcel_import_code() {
        return excel_import_code;
    }

    public void setExcel_import_code(String excel_import_code) {
        this.excel_import_code = excel_import_code;
    }

    public BigDecimal getEnding_balance_credit() {
        return ending_balance_credit;
    }

    public void setEnding_balance_credit(BigDecimal ending_balance_credit) {
        this.ending_balance_credit = ending_balance_credit;
    }

    public String getIs_multiple_siblings() {
        return is_multiple_siblings;
    }

    public void setIs_multiple_siblings(String is_multiple_siblings) {
        this.is_multiple_siblings = is_multiple_siblings;
    }

    public String getSiblings_coding() {
        return siblings_coding;
    }

    public void setSiblings_coding(String siblings_coding) {
        this.siblings_coding = siblings_coding;
    }

    public String getSuperior_coding() {
        return superior_coding;
    }

    public void setSuperior_coding(String superior_coding) {
        this.superior_coding = superior_coding;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public String getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(String update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_source() {
        return sub_source;
    }

    public void setSub_source(String sub_source) {
        this.sub_source = sub_source;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(BigDecimal unit_id) {
        this.unit_id = unit_id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public String getFk_t_basic_measure_id() {
        return fk_t_basic_measure_id;
    }

    public void setFk_t_basic_measure_id(String fk_t_basic_measure_id) {
        this.fk_t_basic_measure_id = fk_t_basic_measure_id;
    }

    public Integer getMeasure_state() {
        return measure_state;
    }

    public void setMeasure_state(Integer measure_state) {
        this.measure_state = measure_state;
    }

    public String getFk_exchange_rate_id() {
        return fk_exchange_rate_id;
    }

    public void setFk_exchange_rate_id(String fk_exchange_rate_id) {
        this.fk_exchange_rate_id = fk_exchange_rate_id;
    }

    public Integer getExchange_rate__state() {
        return exchange_rate__state;
    }

    public void setExchange_rate__state(Integer exchange_rate__state) {
        this.exchange_rate__state = exchange_rate__state;
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

    @Override
    public String toString() {
        return "SubjectMessage [pk_sub_id=" + pk_sub_id + ", user_id=" + user_id + ", account_id=" + account_id
                + ", account_period=" + account_period + ", excel_import_period=" + excel_import_period + ", sub_code="
                + sub_code + ", sub_name=" + sub_name + ", type_of_currency=" + type_of_currency
                + ", init_debit_balance=" + init_debit_balance + ", init_credit_balance=" + init_credit_balance
                + ", current_amount_debit=" + current_amount_debit + ", current_amount_credit=" + current_amount_credit
                + ", year_amount_debit=" + year_amount_debit + ", year_amount_credit=" + year_amount_credit
                + ", ending_balance_debit=" + ending_balance_debit + ", excel_import_code=" + excel_import_code
                + ", ending_balance_credit=" + ending_balance_credit + ", is_multiple_siblings=" + is_multiple_siblings
                + ", siblings_coding=" + siblings_coding + ", superior_coding=" + superior_coding + ", full_name="
                + full_name + ", update_date=" + update_date + ", update_timestamp=" + update_timestamp + ", category="
                + category + ", sub_source=" + sub_source + ", unit=" + unit + ", unit_id=" + unit_id + ", price="
                + price + ", number=" + number + ", amount=" + amount + ", state=" + state + ", mender=" + mender
                + ", fk_t_basic_measure_id=" + fk_t_basic_measure_id + ", measure_state=" + measure_state
                + ", fk_exchange_rate_id=" + fk_exchange_rate_id + ", exchange_rate__state=" + exchange_rate__state
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account_id == null) ? 0 : account_id.hashCode());
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
        SubjectMessage other = (SubjectMessage) obj;
        if (account_id == null) {
            if (other.account_id != null)
                return false;
        } else if (!account_id.equals(other.account_id))
            return false;
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


}

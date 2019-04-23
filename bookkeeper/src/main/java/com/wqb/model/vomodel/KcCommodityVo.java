package com.wqb.model.vomodel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * KcCommodity 实体类  库存商品表
 * 2018-02-26 lch
 */


public class KcCommodityVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3486646063032111103L;
    private String comID;    //主键
    private String accountID;    //账套ID
    private String period;    //期间
    private String sub_code;    //科目编码
    private String sub_comName;    //科目名称

    private Double qm_balanceNum;    //期末结存数量
    private BigDecimal qm_balancePrice;    //期末结存单价
    private BigDecimal qm_balanceAmount;    //期末结存金额

    public String getComID() {
        return comID;
    }

    public void setComID(String comID) {
        this.comID = comID;
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

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_comName() {
        return sub_comName;
    }

    public void setSub_comName(String sub_comName) {
        this.sub_comName = sub_comName;
    }

    public Double getQm_balanceNum() {
        return qm_balanceNum;
    }

    public void setQm_balanceNum(Double qm_balanceNum) {
        this.qm_balanceNum = qm_balanceNum;
    }

    public BigDecimal getQm_balancePrice() {
        return qm_balancePrice;
    }

    public void setQm_balancePrice(BigDecimal qm_balancePrice) {
        this.qm_balancePrice = qm_balancePrice;
    }

    public BigDecimal getQm_balanceAmount() {
        return qm_balanceAmount;
    }

    public void setQm_balanceAmount(BigDecimal qm_balanceAmount) {
        this.qm_balanceAmount = qm_balanceAmount;
    }

    @Override
    public String toString() {
        return "KcCommodityVo [accountID=" + accountID + ", period=" + period + ", sub_code=" + sub_code
                + ", sub_comName=" + sub_comName + ", qm_balanceNum=" + qm_balanceNum + ", qm_balanceAmount="
                + qm_balanceAmount + "]";
    }


}


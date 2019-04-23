package com.wqb.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * KcCommodity 实体类  库存商品表
 * 2018-02-26 lch
 */


public class KcCommodity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7753273636643464250L;
    private String comID;    //主键
    private String accountID;    //账套ID
    private String period;    //期间
    private String startPeriod;    //起始期间(第一次导入期间)
    private String endPeriod;    //截止期间(第一次导入期间)
    private String importSubcode;    //导入的科目编码
    private String sub_comName;    //科目名称
    private String comNameSpec;    //商品名称
    private String direction;    //借贷方向
    private Double qc_balanceNum;    //期初结存数量
    private BigDecimal qc_balancePrice;    //期初结存单价
    private BigDecimal qc_balanceAmount;    //期初结存金额
    private Double bq_incomeNum;    //本期收入数量
    private BigDecimal bq_incomeAmount;    //本期收入金额
    private BigDecimal bq_incomePrice;    //本期收入价格
    private Double bq_issueNum;    //本期发出数量
    private BigDecimal bq_issueAmount;    //本期发出金额
    private BigDecimal bq_issuePrice;    //本期发出价格
    private Double total_incomeNum;    //本年累计收入数量
    private BigDecimal total_incomeAmount;    //本年累计收入金额
    private Double total_issueNum;    //本年累计发出数量
    private BigDecimal total_issueAmount;    //本年累计发出金额
    private String balance_direction;    //余额借贷方向
    private Double qm_balanceNum;    //期末结存数量
    private BigDecimal qm_balancePrice;    //期末结存单价
    private BigDecimal qm_balanceAmount;    //期末结存金额
    private String updatePsnID;    //修改人ID
    private String updatePsn;    //修改人
    private Date updatedate;    //修改时间
    private String createPsnID;    //创建人ID
    private Date createDate;    //创建时间
    private String createPsn;    //创建人
    private String des;    //说明备注
    private Date importDate;    //库存商品导入时间
    private Date balanceDate;    //结转时间
    private String vcunit;    //计量单位
    private String sub_code;    //科目编码
    private String comName;    //商品名称
    private String spec;    //规格

    public void setComID(String comID) {
        this.comID = comID;
    }

    public String getComID() {
        return comID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setImportSubcode(String importSubcode) {
        this.importSubcode = importSubcode;
    }

    public String getImportSubcode() {
        return importSubcode;
    }

    public void setSub_comName(String sub_comName) {
        this.sub_comName = sub_comName;
    }

    public String getSub_comName() {
        return sub_comName;
    }

    public String getComNameSpec() {
        return comNameSpec;
    }

    public void setComNameSpec(String comNameSpec) {
        this.comNameSpec = comNameSpec;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setQc_balanceNum(Double qc_balanceNum) {
        this.qc_balanceNum = qc_balanceNum;
    }

    public Double getQc_balanceNum() {
        return qc_balanceNum;
    }

    public void setQc_balancePrice(BigDecimal qc_balancePrice) {
        this.qc_balancePrice = qc_balancePrice;
    }

    public BigDecimal getQc_balancePrice() {
        return qc_balancePrice;
    }

    public void setQc_balanceAmount(BigDecimal qc_balanceAmount) {
        this.qc_balanceAmount = qc_balanceAmount;
    }

    public BigDecimal getQc_balanceAmount() {
        return qc_balanceAmount;
    }

    public void setBq_incomeNum(Double bq_incomeNum) {
        this.bq_incomeNum = bq_incomeNum;
    }

    public Double getBq_incomeNum() {
        return bq_incomeNum;
    }

    public void setBq_incomeAmount(BigDecimal bq_incomeAmount) {
        this.bq_incomeAmount = bq_incomeAmount;
    }

    public BigDecimal getBq_incomeAmount() {
        return bq_incomeAmount;
    }

    public void setBq_issueNum(Double bq_issueNum) {
        this.bq_issueNum = bq_issueNum;
    }

    public Double getBq_issueNum() {
        return bq_issueNum;
    }

    public void setBq_issueAmount(BigDecimal bq_issueAmount) {
        this.bq_issueAmount = bq_issueAmount;
    }

    public BigDecimal getBq_issueAmount() {
        return bq_issueAmount;
    }

    public void setTotal_incomeNum(Double total_incomeNum) {
        this.total_incomeNum = total_incomeNum;
    }

    public Double getTotal_incomeNum() {
        return total_incomeNum;
    }

    public void setTotal_incomeAmount(BigDecimal total_incomeAmount) {
        this.total_incomeAmount = total_incomeAmount;
    }

    public BigDecimal getTotal_incomeAmount() {
        return total_incomeAmount;
    }

    public void setTotal_issueNum(Double total_issueNum) {
        this.total_issueNum = total_issueNum;
    }

    public Double getTotal_issueNum() {
        return total_issueNum;
    }

    public void setTotal_issueAmount(BigDecimal total_issueAmount) {
        this.total_issueAmount = total_issueAmount;
    }

    public BigDecimal getTotal_issueAmount() {
        return total_issueAmount;
    }

    public void setBalance_direction(String balance_direction) {
        this.balance_direction = balance_direction;
    }

    public String getBalance_direction() {
        return balance_direction;
    }

    public void setQm_balanceNum(Double qm_balanceNum) {
        this.qm_balanceNum = qm_balanceNum;
    }

    public Double getQm_balanceNum() {
        return qm_balanceNum;
    }

    public void setQm_balancePrice(BigDecimal qm_balancePrice) {
        this.qm_balancePrice = qm_balancePrice;
    }

    public BigDecimal getQm_balancePrice() {
        return qm_balancePrice;
    }

    public void setQm_balanceAmount(BigDecimal qm_balanceAmount) {
        this.qm_balanceAmount = qm_balanceAmount;
    }

    public BigDecimal getQm_balanceAmount() {
        return qm_balanceAmount;
    }

    public void setUpdatePsnID(String updatePsnID) {
        this.updatePsnID = updatePsnID;
    }

    public String getUpdatePsnID() {
        return updatePsnID;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn;
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }

    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn;
    }

    public String getCreatePsn() {
        return createPsn;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Date getImportDate() {
        return importDate;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public String getVcunit() {
        return vcunit;
    }

    public void setVcunit(String vcunit) {
        this.vcunit = vcunit;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getBq_incomePrice() {
        return bq_incomePrice;
    }

    public void setBq_incomePrice(BigDecimal bq_incomePrice) {
        this.bq_incomePrice = bq_incomePrice;
    }

    public BigDecimal getBq_issuePrice() {
        return bq_issuePrice;
    }

    public void setBq_issuePrice(BigDecimal bq_issuePrice) {
        this.bq_issuePrice = bq_issuePrice;
    }

    @Override
    public String toString() {
        return "KcCommodity [comID=" + comID + ", period=" + period + ",sub_comName="
                + sub_comName + ", comNameSpec=" + comNameSpec + ", qc_balanceNum="
                + qc_balanceNum + ", qc_balancePrice=" + qc_balancePrice + ", qc_balanceAmount=" + qc_balanceAmount
                + ", bq_incomeNum=" + bq_incomeNum + ", bq_incomeAmount=" + bq_incomeAmount + ", bq_incomePrice="
                + bq_incomePrice + ", bq_issueNum=" + bq_issueNum + ", bq_issueAmount=" + bq_issueAmount
                + ", bq_issuePrice=" + bq_issuePrice + ", total_incomeNum=" + total_incomeNum + ", total_incomeAmount="
                + total_incomeAmount + ", total_issueNum=" + total_issueNum + ", total_issueAmount=" + total_issueAmount
                + ", balance_direction=" + balance_direction + ", qm_balanceNum=" + qm_balanceNum + ", qm_balancePrice="
                + qm_balancePrice + ", qm_balanceAmount=" + qm_balanceAmount + ", sub_code=" + sub_code
                + ", comName=" + comName + ", spec=" + spec + "]";
    }


}


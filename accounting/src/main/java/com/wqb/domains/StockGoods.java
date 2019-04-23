package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 库存商品表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
@TableName("t_kc_commodity")
public class StockGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "comID", type = IdType.UUID)
    private String comID;

    /**
     * 科目编码
     */
    private String subCode;

    /**
     * 导入的科目编码
     */
    @TableField("importSubcode")
    private String importSubcode;

    /**
     * 科目名称
     */
    @TableField("sub_comName")
    private String subComname;

    /**
     * 商品名称规格
     */
    @TableField("comNameSpec")
    private String comNameSpec;

    /**
     * 商品名称
     */
    @TableField("comName")
    private String comName;

    /**
     * 规格
     */
    private String spec;

    /**
     * 余额借贷方向
     */
    private String balanceDirection;

    /**
     * 期间
     */
    private String period;

    /**
     * 期初结存数量
     */
    @TableField("qc_balanceNum")
    private Double qcBalancenum;

    /**
     * 期初结存单价
     */
    @TableField("qc_balancePrice")
    private BigDecimal qcBalanceprice;

    /**
     * 期初结存金额
     */
    @TableField("qc_balanceAmount")
    private BigDecimal qcBalanceamount;

    /**
     * 本期收入数量
     */
    @TableField("bq_incomeNum")
    private Double bqIncomenum;

    /**
     * 本期收入金额
     */
    @TableField("bq_incomeAmount")
    private BigDecimal bqIncomeamount;

    /**
     * 本期收入价格
     */
    @TableField("bq_incomePrice")
    private BigDecimal bqIncomeprice;

    /**
     * 本期发出数量
     */
    @TableField("bq_issueNum")
    private Double bqIssuenum;

    /**
     * 本期发出金额
     */
    @TableField("bq_issueAmount")
    private BigDecimal bqIssueamount;

    /**
     * 本期发出价格
     */
    @TableField("bq_issuePrice")
    private BigDecimal bqIssueprice;

    /**
     * 本年累计收入数量
     */
    @TableField("total_incomeNum")
    private Double totalIncomenum;

    /**
     * 本年累计收入金额
     */
    @TableField("total_incomeAmount")
    private BigDecimal totalIncomeamount;

    /**
     * 本年累计发出数量
     */
    @TableField("total_issueNum")
    private Double totalIssuenum;

    /**
     * 本年累计发出金额
     */
    @TableField("total_issueAmount")
    private BigDecimal totalIssueamount;

    /**
     * 期末结存数量
     */
    @TableField("qm_balanceNum")
    private Double qmBalancenum;

    /**
     * 期末结存单价
     */
    @TableField("qm_balancePrice")
    private BigDecimal qmBalanceprice;

    /**
     * 期末结存金额
     */
    @TableField("qm_balanceAmount")
    private BigDecimal qmBalanceamount;

    /**
     * 借贷方向
     */
    private String direction;

    /**
     * 账套ID
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 起始期间(第一次导入期间)
     */
    @TableField("startPeriod")
    private String startPeriod;

    /**
     * 截止期间(第一次导入期间)
     */
    @TableField("endPeriod")
    private String endPeriod;

    /**
     * 修改人ID
     */
    @TableField("updatePsnID")
    private String updatePsnID;

    /**
     * 修改人
     */
    @TableField("updatePsn")
    private String updatePsn;

    /**
     * 修改时间
     */
    private Date updatedate;

    /**
     * 创建人ID
     */
    @TableField("createPsnID")
    private String createPsnID;

    /**
     * 创建时间
     */
    @TableField("createDate")
    private Date createDate;

    /**
     * 创建人
     */
    @TableField("createPsn")
    private String createPsn;

    /**
     * 说明备注
     */
    private String des;

    /**
     * 库存商品导入时间
     */
    @TableField("importDate")
    private Date importDate;

    /**
     * 结存时间
     */
    @TableField("balanceDate")
    private Date balanceDate;

    /**
     * 计量单位
     */
    private String vcunit;

    public String getComID() {
        return comID;
    }

    public void setComID(String comID) {
        this.comID = comID;
    }
    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
    public String getImportSubcode() {
        return importSubcode;
    }

    public void setImportSubcode(String importSubcode) {
        this.importSubcode = importSubcode;
    }
    public String getSubComname() {
        return subComname;
    }

    public void setSubComname(String subComname) {
        this.subComname = subComname;
    }
    public String getComNameSpec() {
        return comNameSpec;
    }

    public void setComNameSpec(String comNameSpec) {
        this.comNameSpec = comNameSpec;
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
    public String getBalanceDirection() {
        return balanceDirection;
    }

    public void setBalanceDirection(String balanceDirection) {
        this.balanceDirection = balanceDirection;
    }
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    public Double getQcBalancenum() {
        return qcBalancenum;
    }

    public void setQcBalancenum(Double qcBalancenum) {
        this.qcBalancenum = qcBalancenum;
    }
    public BigDecimal getQcBalanceprice() {
        return qcBalanceprice;
    }

    public void setQcBalanceprice(BigDecimal qcBalanceprice) {
        this.qcBalanceprice = qcBalanceprice;
    }
    public BigDecimal getQcBalanceamount() {
        return qcBalanceamount;
    }

    public void setQcBalanceamount(BigDecimal qcBalanceamount) {
        this.qcBalanceamount = qcBalanceamount;
    }
    public Double getBqIncomenum() {
        return bqIncomenum;
    }

    public void setBqIncomenum(Double bqIncomenum) {
        this.bqIncomenum = bqIncomenum;
    }
    public BigDecimal getBqIncomeamount() {
        return bqIncomeamount;
    }

    public void setBqIncomeamount(BigDecimal bqIncomeamount) {
        this.bqIncomeamount = bqIncomeamount;
    }
    public BigDecimal getBqIncomeprice() {
        return bqIncomeprice;
    }

    public void setBqIncomeprice(BigDecimal bqIncomeprice) {
        this.bqIncomeprice = bqIncomeprice;
    }
    public Double getBqIssuenum() {
        return bqIssuenum;
    }

    public void setBqIssuenum(Double bqIssuenum) {
        this.bqIssuenum = bqIssuenum;
    }
    public BigDecimal getBqIssueamount() {
        return bqIssueamount;
    }

    public void setBqIssueamount(BigDecimal bqIssueamount) {
        this.bqIssueamount = bqIssueamount;
    }
    public BigDecimal getBqIssueprice() {
        return bqIssueprice;
    }

    public void setBqIssueprice(BigDecimal bqIssueprice) {
        this.bqIssueprice = bqIssueprice;
    }
    public Double getTotalIncomenum() {
        return totalIncomenum;
    }

    public void setTotalIncomenum(Double totalIncomenum) {
        this.totalIncomenum = totalIncomenum;
    }
    public BigDecimal getTotalIncomeamount() {
        return totalIncomeamount;
    }

    public void setTotalIncomeamount(BigDecimal totalIncomeamount) {
        this.totalIncomeamount = totalIncomeamount;
    }
    public Double getTotalIssuenum() {
        return totalIssuenum;
    }

    public void setTotalIssuenum(Double totalIssuenum) {
        this.totalIssuenum = totalIssuenum;
    }
    public BigDecimal getTotalIssueamount() {
        return totalIssueamount;
    }

    public void setTotalIssueamount(BigDecimal totalIssueamount) {
        this.totalIssueamount = totalIssueamount;
    }
    public Double getQmBalancenum() {
        return qmBalancenum;
    }

    public void setQmBalancenum(Double qmBalancenum) {
        this.qmBalancenum = qmBalancenum;
    }
    public BigDecimal getQmBalanceprice() {
        return qmBalanceprice;
    }

    public void setQmBalanceprice(BigDecimal qmBalanceprice) {
        this.qmBalanceprice = qmBalanceprice;
    }
    public BigDecimal getQmBalanceamount() {
        return qmBalanceamount;
    }

    public void setQmBalanceamount(BigDecimal qmBalanceamount) {
        this.qmBalanceamount = qmBalanceamount;
    }
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }
    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
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
    public String getCreatePsnID() {
        return createPsnID;
    }

    public void setCreatePsnID(String createPsnID) {
        this.createPsnID = createPsnID;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreatePsn() {
        return createPsn;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn;
    }
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
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

    @Override
    public String toString() {
        return "StockGoodsDirection{" +
        "comID=" + comID +
        ", subCode=" + subCode +
        ", importSubcode=" + importSubcode +
        ", subComname=" + subComname +
        ", comNameSpec=" + comNameSpec +
        ", comName=" + comName +
        ", spec=" + spec +
        ", balanceDirection=" + balanceDirection +
        ", period=" + period +
        ", qcBalancenum=" + qcBalancenum +
        ", qcBalanceprice=" + qcBalanceprice +
        ", qcBalanceamount=" + qcBalanceamount +
        ", bqIncomenum=" + bqIncomenum +
        ", bqIncomeamount=" + bqIncomeamount +
        ", bqIncomeprice=" + bqIncomeprice +
        ", bqIssuenum=" + bqIssuenum +
        ", bqIssueamount=" + bqIssueamount +
        ", bqIssueprice=" + bqIssueprice +
        ", totalIncomenum=" + totalIncomenum +
        ", totalIncomeamount=" + totalIncomeamount +
        ", totalIssuenum=" + totalIssuenum +
        ", totalIssueamount=" + totalIssueamount +
        ", qmBalancenum=" + qmBalancenum +
        ", qmBalanceprice=" + qmBalanceprice +
        ", qmBalanceamount=" + qmBalanceamount +
        ", direction=" + direction +
        ", accountID=" + accountID +
        ", startPeriod=" + startPeriod +
        ", endPeriod=" + endPeriod +
        ", updatePsnID=" + updatePsnID +
        ", updatePsn=" + updatePsn +
        ", updatedate=" + updatedate +
        ", createPsnID=" + createPsnID +
        ", createDate=" + createDate +
        ", createPsn=" + createPsn +
        ", des=" + des +
        ", importDate=" + importDate +
        ", balanceDate=" + balanceDate +
        ", vcunit=" + vcunit +
        "}";
    }
}

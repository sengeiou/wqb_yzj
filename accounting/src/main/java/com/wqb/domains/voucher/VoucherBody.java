package com.wqb.domains.voucher;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 凭证子表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_vouch_b")
public class VoucherBody implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "vouchAID", type = IdType.UUID)
    private String vouchAID;

    /**
     * 分录号
     */
    @TableField("rowIndex")
    private String rowIndex;

    /**
     * 科目编码
     */
    @TableField("subjectID")
    private String subjectID;

    /**
     * 借方金额
     */
    @TableField("debitAmount")
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    @TableField("creditAmount")
    private BigDecimal creditAmount;

    /**
     * 凭证主表主键
     */
    @TableField("vouchID")
    private String vouchID;

    /**
     * 科目名称
     */
    private String vcsubject;

    /**
     * 数量
     */
    private BigDecimal number;

    /**
     * 1有问题2没问题
     */
    private String isproblem;

    /**
     * 备注
     */
    private String des;

    /**
     * 方向(1:借2:贷)
     */
    private String direction;

    /**
     * 摘要
     */
    private String vcabstact;

    /**
     * 期间
     */
    private String period;

    /**
     * 账套ID
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 计量单位
     */
    private String vcunit;

    /**
     * 计量单位ID
     */
    @TableField("vcunitID")
    private String vcunitID;

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
     * 单价(国际单位)
     */
    private BigDecimal price;

    public String getVouchAID() {
        return vouchAID;
    }

    public void setVouchAID(String vouchAID) {
        this.vouchAID = vouchAID;
    }
    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
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
    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }
    public String getVcsubject() {
        return vcsubject;
    }

    public void setVcsubject(String vcsubject) {
        this.vcsubject = vcsubject;
    }
    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }
    public String getIsproblem() {
        return isproblem;
    }

    public void setIsproblem(String isproblem) {
        this.isproblem = isproblem;
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
    public String getVcabstact() {
        return vcabstact;
    }

    public void setVcabstact(String vcabstact) {
        this.vcabstact = vcabstact;
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
    public String getVcunit() {
        return vcunit;
    }

    public void setVcunit(String vcunit) {
        this.vcunit = vcunit;
    }
    public String getVcunitID() {
        return vcunitID;
    }

    public void setVcunitID(String vcunitID) {
        this.vcunitID = vcunitID;
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
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "VoucherBody{" +
        "vouchAID=" + vouchAID +
        ", rowIndex=" + rowIndex +
        ", subjectID=" + subjectID +
        ", debitAmount=" + debitAmount +
        ", creditAmount=" + creditAmount +
        ", vouchID=" + vouchID +
        ", vcsubject=" + vcsubject +
        ", number=" + number +
        ", isproblem=" + isproblem +
        ", des=" + des +
        ", direction=" + direction +
        ", vcabstact=" + vcabstact +
        ", period=" + period +
        ", accountID=" + accountID +
        ", vcunit=" + vcunit +
        ", vcunitID=" + vcunitID +
        ", updatePsnID=" + updatePsnID +
        ", updatePsn=" + updatePsn +
        ", updatedate=" + updatedate +
        ", price=" + price +
        "}";
    }
}

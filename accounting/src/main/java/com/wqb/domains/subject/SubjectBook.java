package com.wqb.domains.subject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
@TableName("t_basic_subject_book")
public class SubjectBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer subBkId;

    /**
     * 账套
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 期间
     */
    private String period;

    /**
     * 凭证主键
     */
    @TableField("vouchID")
    private String vouchID;

    /**
     * 凭证体主键
     */
    @TableField("vouchAID")
    private String vouchAID;

    /**
     * 凭证号
     */
    @TableField("vouchNum")
    private Integer vouchNum;

    /**
     * 摘要
     */
    private String vcabstact;

    /**
     * 科目编码
     */
    private String subCode;

    /**
     * 科目名称
     */
    private String subName;

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
     * 余额
     */
    @TableField("blanceAmount")
    private BigDecimal blanceAmount;

    /**
     * 科目方向
     */
    private String direction;

    /**
     * 更新时间
     */
    @TableField("updateDate")
    private Date updateDate;

    /**
     * 时间戳
     */
    private Long upDate;

    /**
     * 是否为更新末级科目 0 否1是
     */
    @TableField("isEndSubCode")
    private Integer isEndSubCode;

    public Integer getSubBkId() {
        return subBkId;
    }

    public void setSubBkId(Integer subBkId) {
        this.subBkId = subBkId;
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
    public Long getUpDate() {
        return upDate;
    }

    public void setUpDate(Long upDate) {
        this.upDate = upDate;
    }
    public Integer getIsEndSubCode() {
        return isEndSubCode;
    }

    public void setIsEndSubCode(Integer isEndSubCode) {
        this.isEndSubCode = isEndSubCode;
    }

    @Override
    public String toString() {
        return "SubjectBook{" +
        "subBkId=" + subBkId +
        ", accountID=" + accountID +
        ", period=" + period +
        ", vouchID=" + vouchID +
        ", vouchAID=" + vouchAID +
        ", vouchNum=" + vouchNum +
        ", vcabstact=" + vcabstact +
        ", subCode=" + subCode +
        ", subName=" + subName +
        ", debitAmount=" + debitAmount +
        ", creditAmount=" + creditAmount +
        ", blanceAmount=" + blanceAmount +
        ", direction=" + direction +
        ", updateDate=" + updateDate +
        ", upDate=" + upDate +
        ", isEndSubCode=" + isEndSubCode +
        "}";
    }
}

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
 * 凭证主表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_vouch_h")
public class VoucherHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "vouchID", type = IdType.UUID)
    private String vouchID;

    /**
     * 凭证号
     */
    @TableField("voucherNo")
    private Integer voucherNo;

    /**
     * 凭证贷方金额合计
     */
    @TableField("totalCredit")
    private BigDecimal totalCredit;

    /**
     * 凭证借方金额合计
     */
    @TableField("totalDbit")
    private BigDecimal totalDbit;

    /**
     * 1有问题2没问题
     */
    private String isproblem;

    /**
     * 来源0:进项凭证1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据 7.结转成本 9销项凭证 10结转全年净利润 11导入序时薄凭证 13结转增值税 131结转留底税 14 结转附赠税 15结转企业所得税
     */
    private Integer source;

    /**
     * 备注
     */
    private String des;

    /**
     * 账套ID
     */
    @TableField("accountID")
    private String accountID;

    /**
     * 期间
     */
    private String period;

    /**
     * 生成日期
     */
    @TableField("vcDate")
    private Date vcDate;

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
     * 创建人
     */
    private String createpsn;

    /**
     * 创建日期
     */
    @TableField("createDate")
    private Long createDate;

    /**
     * 审核日期
     */
    @TableField("checkedDate")
    private Date checkedDate;

    /**
     * 审核人
     */
    private String checker;

    /**
     * 审核状态(0:未审核1:审核)
     */
    @TableField("auditStatus")
    private Integer auditStatus;

    /**
     * 币别
     */
    private String currency;

    /**
     * 币别ID
     */
    @TableField("currencyID")
    private String currencyID;

    /**
     * 0:非模凭证1:模板凭证
     */
    @TableField("vouchFlag")
    private Integer vouchFlag;

    /**
     * 附件ID
     */
    @TableField("attachID")
    private String attachID;

    /**
     * 凭证类型2 标记手工添加的结转成本
     */
    @TableField("voucherType")
    private Integer voucherType;

    public String getVouchID() {
        return vouchID;
    }

    public void setVouchID(String vouchID) {
        this.vouchID = vouchID;
    }
    public Integer getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(Integer voucherNo) {
        this.voucherNo = voucherNo;
    }
    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }
    public BigDecimal getTotalDbit() {
        return totalDbit;
    }

    public void setTotalDbit(BigDecimal totalDbit) {
        this.totalDbit = totalDbit;
    }
    public String getIsproblem() {
        return isproblem;
    }

    public void setIsproblem(String isproblem) {
        this.isproblem = isproblem;
    }
    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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
    public Date getVcDate() {
        return vcDate;
    }

    public void setVcDate(Date vcDate) {
        this.vcDate = vcDate;
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
    public String getCreatepsn() {
        return createpsn;
    }

    public void setCreatepsn(String createpsn) {
        this.createpsn = createpsn;
    }
    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
    public Date getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }
    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }
    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }
    public Integer getVouchFlag() {
        return vouchFlag;
    }

    public void setVouchFlag(Integer vouchFlag) {
        this.vouchFlag = vouchFlag;
    }
    public String getAttachID() {
        return attachID;
    }

    public void setAttachID(String attachID) {
        this.attachID = attachID;
    }
    public Integer getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(Integer voucherType) {
        this.voucherType = voucherType;
    }

    @Override
    public String toString() {
        return "VoucherHeader{" +
        "vouchID=" + vouchID +
        ", voucherNo=" + voucherNo +
        ", totalCredit=" + totalCredit +
        ", totalDbit=" + totalDbit +
        ", isproblem=" + isproblem +
        ", source=" + source +
        ", des=" + des +
        ", accountID=" + accountID +
        ", period=" + period +
        ", vcDate=" + vcDate +
        ", updatePsnID=" + updatePsnID +
        ", updatePsn=" + updatePsn +
        ", updatedate=" + updatedate +
        ", createPsnID=" + createPsnID +
        ", createpsn=" + createpsn +
        ", createDate=" + createDate +
        ", checkedDate=" + checkedDate +
        ", checker=" + checker +
        ", auditStatus=" + auditStatus +
        ", currency=" + currency +
        ", currencyID=" + currencyID +
        ", vouchFlag=" + vouchFlag +
        ", attachID=" + attachID +
        ", voucherType=" + voucherType +
        "}";
    }
}

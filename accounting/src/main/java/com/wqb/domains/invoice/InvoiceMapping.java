package com.wqb.domains.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 发票映射科目记录表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-20
 */
@TableName("t_fa_invoice_mappingrecord")
public class InvoiceMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String mrId;

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
     * 1进项2销项
     */
    @TableField("invoiceType")
    private String invoiceType;

    /**
     * 导入是否保存1保存0未保存
     */
    private String isUploadSave;

    /**
     * 保持累次次数
     */
    private Integer saveNum;

    /**
     * 保存时间
     */
    @TableField("saveDate")
    private Date saveDate;

    public String getMrId() {
        return mrId;
    }

    public void setMrId(String mrId) {
        this.mrId = mrId;
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
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
    public String getIsUploadSave() {
        return isUploadSave;
    }

    public void setIsUploadSave(String isUploadSave) {
        this.isUploadSave = isUploadSave;
    }
    public Integer getSaveNum() {
        return saveNum;
    }

    public void setSaveNum(Integer saveNum) {
        this.saveNum = saveNum;
    }
    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    @Override
    public String toString() {
        return "InvoiceMapping{" +
        "mrId=" + mrId +
        ", accountID=" + accountID +
        ", period=" + period +
        ", invoiceType=" + invoiceType +
        ", isUploadSave=" + isUploadSave +
        ", saveNum=" + saveNum +
        ", saveDate=" + saveDate +
        "}";
    }
}

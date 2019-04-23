package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * InvoiceMappingrecord 实体类
 * 2018-11-09 lch
 */


public class InvoiceMappingrecord implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1141976229201026132L;
    private String mr_id;    //主键
    private String accountID;    //账套ID
    private String period;    //期间
    private String invoiceType;    //1进项2销项
    private String is_upload_save;    //导入是否保存
    private Integer save_num;    //保持累次次数
    private Date saveDate;    //保存时间

    public void setMr_id(String mr_id) {
        this.mr_id = mr_id;
    }

    public String getMr_id() {
        return mr_id;
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

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setIs_upload_save(String is_upload_save) {
        this.is_upload_save = is_upload_save;
    }

    public String getIs_upload_save() {
        return is_upload_save;
    }

    public Integer getSave_num() {
        return save_num;
    }

    public void setSave_num(Integer save_num) {
        this.save_num = save_num;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    @Override
    public String toString() {
        return "InvoiceMappingrecord [mr_id=" + mr_id + ", accountID=" + accountID + ", period=" + period
                + ", invoiceType=" + invoiceType + ", is_upload_save=" + is_upload_save + ", save_num=" + save_num + ", saveDate=" + saveDate + "]";
    }

}


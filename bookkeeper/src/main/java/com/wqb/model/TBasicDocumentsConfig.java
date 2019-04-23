package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class TBasicDocumentsConfig implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7582767817307785936L;

    /**
     * 单据配置表主键
     */
    private String pkDocumentsConfigId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 借方科目主键
     */
    private String pkSubIdDebit;

    /**
     * 借方科目父类代码
     */
    private String subCodeParentDebit;

    /**
     * 借方科目名称
     */
    private String subNameDebit;

    /**
     * 借方科目完整名称
     */
    private String fullNameDebit;

    /**
     * 贷方科目主键
     */
    private String pkSubIdCtedit;

    /**
     * 贷方科目父类代码
     */
    private String subCodeParentCredit;

    /**
     * 贷方科目名称
     */
    private String subNameCredit;

    /**
     * 贷方科目完整名称
     */
    private String fullNameCredit;

    /**
     * 单据类别(1. 采购单据 2.销售单据 3.银行单据 4.费用单据)
     */
    private String documentsType;

    /**
     * 创建人
     */
    private String createPsn;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updatePsn;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新时间戳
     */
    private String updateTimestamp;

    public String getPkDocumentsConfigId() {
        return pkDocumentsConfigId;
    }

    public void setPkDocumentsConfigId(String pkDocumentsConfigId) {
        this.pkDocumentsConfigId = pkDocumentsConfigId == null ? null : pkDocumentsConfigId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getPkSubIdDebit() {
        return pkSubIdDebit;
    }

    public void setPkSubIdDebit(String pkSubIdDebit) {
        this.pkSubIdDebit = pkSubIdDebit == null ? null : pkSubIdDebit.trim();
    }

    public String getSubCodeParentDebit() {
        return subCodeParentDebit;
    }

    public void setSubCodeParentDebit(String subCodeParentDebit) {
        this.subCodeParentDebit = subCodeParentDebit == null ? null : subCodeParentDebit.trim();
    }

    public String getSubNameDebit() {
        return subNameDebit;
    }

    public void setSubNameDebit(String subNameDebit) {
        this.subNameDebit = subNameDebit == null ? null : subNameDebit.trim();
    }

    public String getFullNameDebit() {
        return fullNameDebit;
    }

    public void setFullNameDebit(String fullNameDebit) {
        this.fullNameDebit = fullNameDebit == null ? null : fullNameDebit.trim();
    }

    public String getPkSubIdCtedit() {
        return pkSubIdCtedit;
    }

    public void setPkSubIdCtedit(String pkSubIdCtedit) {
        this.pkSubIdCtedit = pkSubIdCtedit == null ? null : pkSubIdCtedit.trim();
    }

    public String getSubCodeParentCredit() {
        return subCodeParentCredit;
    }

    public void setSubCodeParentCredit(String subCodeParentCredit) {
        this.subCodeParentCredit = subCodeParentCredit == null ? null : subCodeParentCredit.trim();
    }

    public String getSubNameCredit() {
        return subNameCredit;
    }

    public void setSubNameCredit(String subNameCredit) {
        this.subNameCredit = subNameCredit == null ? null : subNameCredit.trim();
    }

    public String getFullNameCredit() {
        return fullNameCredit;
    }

    public void setFullNameCredit(String fullNameCredit) {
        this.fullNameCredit = fullNameCredit == null ? null : fullNameCredit.trim();
    }

    public String getDocumentsType() {
        return documentsType;
    }

    public void setDocumentsType(String documentsType) {
        this.documentsType = documentsType == null ? null : documentsType.trim();
    }

    public String getCreatePsn() {
        return createPsn;
    }

    public void setCreatePsn(String createPsn) {
        this.createPsn = createPsn == null ? null : createPsn.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdatePsn() {
        return updatePsn;
    }

    public void setUpdatePsn(String updatePsn) {
        this.updatePsn = updatePsn == null ? null : updatePsn.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp == null ? null : updateTimestamp.trim();
    }
}

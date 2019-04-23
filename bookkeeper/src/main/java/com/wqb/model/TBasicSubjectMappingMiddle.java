package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

public class TBasicSubjectMappingMiddle implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -20101727814502659L;

    /**
     * 科目关系映射中间表主键
     */
    private Integer pkSubMappingMiddleId;

    /**
     * 帐套id
     */
    private String accountId;

    /**
     * 映射科目编码
     */
    private String subMappingCode;

    /**
     * 映射科目名称
     */
    private String subMappingName;

    /**
     * 系统科目编码
     */
    private String subMessageCode;

    /**
     * 系统科目名称
     */
    private String subMessageName;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新时间戳
     */
    private String updateTimestamp;

    /**
     * 来源
     */
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPkSubMappingMiddleId() {
        return pkSubMappingMiddleId;
    }

    public void setPkSubMappingMiddleId(Integer pkSubMappingMiddleId) {
        this.pkSubMappingMiddleId = pkSubMappingMiddleId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getSubMappingCode() {
        return subMappingCode;
    }

    public void setSubMappingCode(String subMappingCode) {
        this.subMappingCode = subMappingCode == null ? null : subMappingCode.trim();
    }

    public String getSubMappingName() {
        return subMappingName;
    }

    public void setSubMappingName(String subMappingName) {
        this.subMappingName = subMappingName == null ? null : subMappingName.trim();
    }

    public String getSubMessageCode() {
        return subMessageCode;
    }

    public void setSubMessageCode(String subMessageCode) {
        this.subMessageCode = subMessageCode == null ? null : subMessageCode.trim();
    }

    public String getSubMessageName() {
        return subMessageName;
    }

    public void setSubMessageName(String subMessageName) {
        this.subMessageName = subMessageName == null ? null : subMessageName.trim();
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
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

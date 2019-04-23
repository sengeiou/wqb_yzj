package com.wqb.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectParent
 * @Description: 系统科目表（新会计准则）
 * @date 2017年12月25日 上午11:06:07
 */
public class TBasicSubjectParent implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8362665124877681014L;

    /**
     * 主键
     */
    private String pkSubPareenId;

    /**
     * 科目代码
     */
    private String subCode;

    /**
     * 科目名称
     */
    private String subName;

    /**
     * 修改者
     */
    private String mender;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 时间戳
     */
    private String updateTimestamp;

    /**
     * 类别(一、资产类二、负债类　三、共同类　四、所有者权益类五、成本类六、损益类)
     */
    private String category;

    /**
     * 借贷方向（1.debit借方， 2.credit贷方）
     */
    private String debitCreditDirection;

    /**
     * 会计准则 例： 新会计准则
     */
    private String accountingStandard;

    /**
     * 企业类型(0. 通用  11：一般纳税人_生产型 12：一般纳税人_贸易型 13：一般纳税人_服务型 14：一般纳税人_进出口 15：一般纳税人_高新 21：小规模_生产型 22：小规模_贸易型 23：小规模_服务型 24：小规模_进出口 26：小规模_高新)
     */
    private Integer companyType;

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public String getPkSubPareenId() {
        return pkSubPareenId;
    }

    public void setPkSubPareenId(String pkSubPareenId) {
        this.pkSubPareenId = pkSubPareenId == null ? null : pkSubPareenId.trim();
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode == null ? null : subCode.trim();
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName == null ? null : subName.trim();
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender == null ? null : mender.trim();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getDebitCreditDirection() {
        return debitCreditDirection;
    }

    public void setDebitCreditDirection(String debitCreditDirection) {
        this.debitCreditDirection = debitCreditDirection == null ? null : debitCreditDirection.trim();
    }

    public String getAccountingStandard() {
        return accountingStandard;
    }

    public void setAccountingStandard(String accountingStandard) {
        this.accountingStandard = accountingStandard == null ? null : accountingStandard.trim();
    }
}

package com.wqb.domains.subject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * EXCEL科目档案表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_basic_subject_parent")
public class SubjectParent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String pId;

    /**
     * 科目代码
     */
    private String subCode;

    /**
     * 科目名称
     */
    private String subName;

    /**
     * 科目全名
     */
    @TableField("sub_fullName")
    private String subFullname;

    /**
     * 借贷方向（1.debit借方， 2.credit贷方）
     */
    private Integer dir;

    /**
     * 会计准则(3:新会计准则,1小企业会计准则2政府民间非营利组织4村集体会计准则)
     */
    private Integer accstandards;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 类别(1、资产2、负债　3、共同4、权益5、成本6、损益  7净资产 8收入费用)
     */
    private String category;

    /**
     * 科目分组名称
     */
    @TableField("categoryName")
    private String categoryName;

    /**
     * 更新时间
     */
    @TableField("creatorDate")
    private Date creatorDate;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
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
    public String getSubFullname() {
        return subFullname;
    }

    public void setSubFullname(String subFullname) {
        this.subFullname = subFullname;
    }
    public Integer getDir() {
        return dir;
    }

    public void setDir(Integer dir) {
        this.dir = dir;
    }
    public Integer getAccstandards() {
        return accstandards;
    }

    public void setAccstandards(Integer accstandards) {
        this.accstandards = accstandards;
    }
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public Date getCreatorDate() {
        return creatorDate;
    }

    public void setCreatorDate(Date creatorDate) {
        this.creatorDate = creatorDate;
    }

    @Override
    public String toString() {
        return "SubjectParent{" +
        "pId=" + pId +
        ", subCode=" + subCode +
        ", subName=" + subName +
        ", subFullname=" + subFullname +
        ", dir=" + dir +
        ", accstandards=" + accstandards +
        ", level=" + level +
        ", category=" + category +
        ", categoryName=" + categoryName +
        ", creatorDate=" + creatorDate +
        "}";
    }
}

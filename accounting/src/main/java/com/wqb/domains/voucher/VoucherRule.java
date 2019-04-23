package com.wqb.domains.voucher;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 项目与科目关系规则表  生成凭证用
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_simple_voucher_rule")
public class VoucherRule implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_VALID = 1;

    public static final int STATUS_INVALID = 0;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 项目id
     */
    private Integer itemId;

    /**
     * 付款方式，一般都有多个 当只有一个时为 0
     * 1现金 2银行（公账）3应收 4应付 5预收 6预付
     */
    private Integer type;

    /**
     * 科目id
     */
    private Integer subjectId;

    /**
     * 科目借贷方向 1借 2贷
     */
    private Integer subjectDeriction;

    /**
     * 0 禁用 1启用
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getSubjectDeriction() {
        return subjectDeriction;
    }

    public void setSubjectDeriction(Integer subjectDeriction) {
        this.subjectDeriction = subjectDeriction;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VoucherRule{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", subjectId=" + subjectId +
                ", subjectDeriction=" + subjectDeriction +
                ", status=" + status +
                "}";
    }
}

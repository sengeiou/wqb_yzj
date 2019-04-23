package com.wqb.domains.subject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 记账科目表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_simple_subject")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int EXTENDABLE = 1;

    public static final String EXTEND_GOODS = "goods";

    public static final String EXTEND_BANK = "bank";

    public static final String EXTEND_CUSTOMER = "customer";

    public static final int DIRECTION_DEBIT = 1;

    public static final int DIRECTION_CREDIT = 2;

    public static final int STATUS_VALID = 1;

    public static final int STATUS_INVALID = 0;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 科目编码
     */
    private String code;

    /**
     * 父级编码  (1级为0，二级取前4位）
     */
    private String parentCode;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目全民
     */
    private String fullName;

    /**
     * 类别(1、资产类2、负债类　3、共同类4、所有者权益类5、成本类6、损益类)
     */
    private Integer category;

    /**
     * 借贷方向（1.debit借方， 2.credit贷方）
     */
    private Integer direction;

    /**
     *  启用状态(0禁用，1启用）
     */
    private Integer status;

    /**
     *  可扩展(0不能，1能）银行存款、其他应收款等需要带对方信息
     */
    private Integer extend;

    /**
     * 扩展标示：bank 银行号码类，name 名称类
     */
    private String extendKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getExtend() {
        return extend;
    }

    public void setExtend(Integer extend) {
        this.extend = extend;
    }
    public String getExtendKey() {
        return extendKey;
    }

    public void setExtendKey(String extendKey) {
        this.extendKey = extendKey;
    }

    @Override
    public String toString() {
        return "SubjectItem{" +
        "id=" + id +
        ", code=" + code +
        ", parentCode=" + parentCode +
        ", name=" + name +
        ", fullName=" + fullName +
        ", category=" + category +
        ", direction=" + direction +
        ", status=" + status +
        ", extend=" + extend +
        ", extendKey=" + extendKey +
        "}";
    }
}

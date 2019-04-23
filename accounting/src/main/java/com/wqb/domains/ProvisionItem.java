package com.wqb.domains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 计提凭证项目表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_simple_provision_item")
public class ProvisionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 启用状态(0禁用，1启用）
     */
    private Integer status;

    /**
     * 涉及费用是否有现金或银行分类 0没有 1有
     */
    private Integer hasAmountType;

    /**
     * 是否有子节点 0没有 1有
     */
    private Integer hasChildren;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getHasAmountType() {
        return hasAmountType;
    }

    public void setHasAmountType(Integer hasAmountType) {
        this.hasAmountType = hasAmountType;
    }
    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Override
    public String toString() {
        return "ProvisionItem{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", name=" + name +
        ", status=" + status +
        ", hasAmountType=" + hasAmountType +
        ", hasChildren=" + hasChildren +
        "}";
    }
}

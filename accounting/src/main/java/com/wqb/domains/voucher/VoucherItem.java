package com.wqb.domains.voucher;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 生成凭证项目表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@TableName("t_simple_voucher_item")
public class VoucherItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int ADD_SALARY_ITEM_ID = 19;

    public static final int STATUS_VALID = 1;

    public static final int STATUS_INVALID = 0;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 启用状态(0禁用，1启用）
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VoucherItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", status=" + status +
                '}';
    }
}

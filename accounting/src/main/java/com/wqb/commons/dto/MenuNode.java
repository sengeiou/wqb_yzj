package com.wqb.commons.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shoven
 * @since 2019-04-08 13:47
 */
public interface MenuNode extends Serializable {
    /**
     * 获取所有孩子节点
     *
     * @return
     */
    List<? extends MenuNode> getChildren();


    /**
     * 设置一组孩子节点
     *
     * @param children
     */
    void setChildren(List<? extends MenuNode> children);
}

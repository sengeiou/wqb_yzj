package com.wqb.commons.dto;

/**
 * @author Shoven
 * @since 2019-04-08 16:09
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生成凭证项目表
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public class Menu implements Serializable, MenuNode {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String fullName;

    private Map<Integer, Payment> payments;

    private List<MenuNode> children = new ArrayList<>();

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<Integer, Payment> getPayments() {
        return payments;
    }

    public void setPayments(Map<Integer, Payment> payments) {
        this.payments = payments;
    }

    @Override
    public List<? extends MenuNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<? extends MenuNode> children) {
        this.children = (List<MenuNode>)children;
    }

    public static class Payment implements Serializable {

        /**
         * 付款名称
         */
        private String name;

        /**
         * 必须有银行
         */
        private boolean mustBank;

        /**
         * 必须有商品
         */
        private boolean mustGoods;

        /**
         * 必须有顾客
         */
        private boolean mustCustomer;

        /**
         * 支持发票
         */
        private boolean supportInvoice;

        /**
         * 支持收据
         */
        private boolean supportReceipt;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isMustBank() {
            return mustBank;
        }

        public void setMustBank(boolean mustBank) {
            this.mustBank = mustBank;
        }

        public boolean isMustGoods() {
            return mustGoods;
        }

        public void setMustGoods(boolean mustGoods) {
            this.mustGoods = mustGoods;
        }

        public boolean isMustCustomer() {
            return mustCustomer;
        }

        public void setMustCustomer(boolean mustCustomer) {
            this.mustCustomer = mustCustomer;
        }

        public boolean isSupportInvoice() {
            return supportInvoice;
        }

        public void setSupportInvoice(boolean supportInvoice) {
            this.supportInvoice = supportInvoice;
        }

        public boolean isSupportReceipt() {
            return supportReceipt;
        }

        public void setSupportReceipt(boolean supportReceipt) {
            this.supportReceipt = supportReceipt;
        }
    }
}

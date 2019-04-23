package com.wqb.model;

import java.io.Serializable;
import java.util.List;

public class Voucher implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1815508111749804999L;
    private VoucherHead voucherHead;
    private List<VoucherBody> voucherBodyList;

    public VoucherHead getVoucherHead() {
        return voucherHead;
    }

    public void setVoucherHead(VoucherHead voucherHead) {
        this.voucherHead = voucherHead;
    }

    public List<VoucherBody> getVoucherBodyList() {
        return voucherBodyList;
    }

    public void setVoucherBodyList(List<VoucherBody> voucherBodyList) {
        this.voucherBodyList = voucherBodyList;
    }
}

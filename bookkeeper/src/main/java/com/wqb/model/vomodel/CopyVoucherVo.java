package com.wqb.model.vomodel;

import com.wqb.model.VoucherBody;

import java.util.List;

public class CopyVoucherVo {

    private List<VoucherBody> vbList;
    private String voucherNo;
    private String changeDate;

    public List<VoucherBody> getVbList() {
        return vbList;
    }

    public void setVbList(List<VoucherBody> vbList) {
        this.vbList = vbList;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public String toString() {
        return "CopyVoucherVo [vbList=" + vbList + ", voucherNo=" + voucherNo + ", changeDate=" + changeDate + "]";
    }


}

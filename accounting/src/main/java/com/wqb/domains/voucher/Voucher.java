package com.wqb.domains.voucher;

import java.util.List;

/**
 * @author Shoven
 * @since 2019-03-19 14:43
 */
public class Voucher {

    public static final int AUDIT_STATUS_AUDITED = 1;

    public static final int AUDIT_STATUS_UNAUDITED = 0;

    public static final int VOUCH_FLAG_NON_TEMPLATE = 1;

    public static final int VOUCH_FLAG_TEMPLATE= 0;

    public static final String IS_PROBLEM_PROBLEM = "1";

    public static final String IS_PROBLEM_NO_PROBLEM = "2";

    public static final String DIRECTION_DEBIT = "1";

    public static final String DIRECTION_CREDIT = "2";

    private VoucherHeader voucherHeader;

    private List<VoucherBody> voucherBodies;

    public Voucher() {
    }

    public Voucher(VoucherHeader voucherHeader, List<VoucherBody> voucherBodies) {
        this.voucherHeader = voucherHeader;
        this.voucherBodies = voucherBodies;
    }

    public VoucherHeader getVoucherHeader() {
        return voucherHeader;
    }

    public void setVoucherHeader(VoucherHeader voucherHeader) {
        this.voucherHeader = voucherHeader;
    }

    public List<VoucherBody> getVoucherBodies() {
        return voucherBodies;
    }

    public void setVoucherBodies(List<VoucherBody> voucherBodies) {
        this.voucherBodies = voucherBodies;
    }
}

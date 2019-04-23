package com.wqb.services;

import com.wqb.domains.Receipt;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.base.BaseService;

import java.util.List;

/**
 * <p>
 * 收据表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-04-15
 */
public interface ReceiptService extends BaseService<Receipt> {

    void changeReceiptForCreateVoucher(String receiptId, Voucher voucher, List<VoucherRule> voucherRules);

    void changeReceiptForDeleteVoucher(Voucher oldVoucher);
}

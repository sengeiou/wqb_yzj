package com.wqb.services.invoice;

import com.wqb.domains.invoice.Invoice;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherRule;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author WQB
 * @since 2019-03-19 14:39
 */
public interface InvoiceService  {

    /**
     * 通过凭证ID集合查找
     *
     * @param ids
     * @return
     */
    List<Invoice> selectListByVoucherIds(List<String> ids);

    /**
     * 通过
     *
     * @param invoiceId
     * @return
     */
    Invoice selectById(String invoiceId);

    /**
     * 设置发票上的凭证引用
     *  @param oldInvoiceId
     * @param newVoucher
     * @param voucherRules
     */
    void changeInvoiceForCreateVoucher(String oldInvoiceId, Voucher newVoucher, List<VoucherRule> voucherRules);

    /**
     * 删除发票上的凭证引用
     * @param voucher
     *
     *
     */
    void changeInvoiceForDeleteVoucher(Voucher voucher);

    BigDecimal getPositiveTotalTaxAmount(Invoice invoice);

    BigDecimal getNegativeTotalTaxAmount(Invoice invoice);

    BigDecimal getPositiveTotalNamount(Invoice invoice);

    BigDecimal getNegativeTotalNamount(Invoice invoice);
}

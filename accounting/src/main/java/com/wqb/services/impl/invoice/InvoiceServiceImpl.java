package com.wqb.services.impl.invoice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wqb.commons.bo.SubjectMatcher;
import com.wqb.commons.emun.SubjectItem;
import com.wqb.domains.StockGoods;
import com.wqb.domains.invoice.Invoice;
import com.wqb.domains.invoice.InvoiceBody;
import com.wqb.domains.invoice.InvoiceHeader;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.StockGoodsService;
import com.wqb.services.impl.subject.SubjectBalanceServiceImpl;
import com.wqb.services.invoice.InvoiceBodyService;
import com.wqb.services.invoice.InvoiceHeaderService;
import com.wqb.services.invoice.InvoiceService;
import com.wqb.services.subject.SubjectService;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.wqb.commons.emun.SubjectItem.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author WQB
 * @since 2019-03-19 14:41
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceBodyService invoiceBodyService;

    @Autowired
    private InvoiceHeaderService invoiceHeaderService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StockGoodsService stockGoodsService;

    @Autowired
    private SubjectBalanceServiceImpl subjectBalanceService;

    @Override
    public List<Invoice> selectListByVoucherIds(List<String> ids) {
        checkArgument(CollectionUtils.isNotEmpty(ids), "ids 不能为空");
        LambdaQueryWrapper<InvoiceHeader> headerQueryWrapper = Wrappers.<InvoiceHeader>lambdaQuery()
                .in(InvoiceHeader::getVouchID, ids);

        List<InvoiceHeader> invoiceHeaders = invoiceHeaderService.list(headerQueryWrapper);

        if (invoiceHeaders != null) {
            List<String> invoiceIds = invoiceHeaders.stream()
                    .map(InvoiceHeader::getInvoiceHID)
                    .collect(toList());

            LambdaQueryWrapper<InvoiceBody> bodyQueryWrapper = Wrappers.<InvoiceBody>lambdaQuery()
                    .in(InvoiceBody::getInvoiceHID, invoiceIds);

            List<InvoiceBody> voucherBodies = invoiceBodyService.list(bodyQueryWrapper);

            Map<String, List<InvoiceBody>> bodiesGroupedByVoucherId
                    = voucherBodies.stream().collect(groupingBy(InvoiceBody::getInvoiceHID));


            List<Invoice> invoices = new ArrayList<>(invoiceHeaders.size());
            invoiceHeaders.forEach(invoiceHeader -> {
                invoices.add(new Invoice(invoiceHeader, bodiesGroupedByVoucherId.get(invoiceHeader.getInvoiceHID())));
            });

            return invoices;
        }
        return null;
    }

    @Override
    public Invoice selectById(String invoiceId) {
        checkArgument(StringUtils.isNotBlank(invoiceId), "invoiceId 不能为空");

        InvoiceHeader invoiceHeader = invoiceHeaderService.getById(invoiceId);

        if (invoiceHeader != null) {
            InvoiceBody condition = new InvoiceBody();
            condition.setInvoiceHID(invoiceHeader.getInvoiceHID());
            List<InvoiceBody> invoiceBodies = invoiceBodyService.list(condition);
            return new Invoice(invoiceHeader, invoiceBodies);
        }
        return null;
    }

    @Override
    public void changeInvoiceForCreateVoucher(String oldInvoiceId, Voucher newVoucher, List<VoucherRule> voucherRules) {
        checkArgument(oldInvoiceId != null, "invoiceId 不能为空");
        checkArgument(newVoucher != null, "voucher 不能为空");

        Invoice invoice = selectById(oldInvoiceId);

        String vouchID = newVoucher.getVoucherHeader().getVouchID();
        Integer invoiceType = getInvoiceType(voucherRules);

        boolean b1 = invoiceHeaderService.lambdaUpdate()
                .set(InvoiceHeader::getVouchID, vouchID)
                .set(InvoiceHeader::getInvoiceType, invoiceType)
                .eq(InvoiceHeader::getInvoiceHID, oldInvoiceId)
                .update();
        List<InvoiceBody> updatedInvoiceBodies = getUpdatedInvoiceBodies(newVoucher, invoice);
        boolean b2 = invoiceBodyService.updateBatchById(updatedInvoiceBodies);
        if (!b1 || !b2) {
            throw new WqbException("更新发票上的凭证失败");
        }
    }

    private Integer getInvoiceType(List<VoucherRule> voucherRules) {
        List<Subject> subjects = subjectService.selectListByVoucherRules(voucherRules);
        SubjectMatcher subjectMatcher = new SubjectMatcher(subjects, voucherRules);
        return subjectMatcher.match((SubjectItem) null, ZYYWSR) ? 2 : 1;
    }

    private List<InvoiceBody> getUpdatedInvoiceBodies(Voucher voucher, Invoice invoice) {
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String account = voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();

        Function<InvoiceBody, InvoiceBody> mapper = body -> {
            StockGoods condition = new StockGoods();
            condition.setComNameSpec(body.getComNameSpec());
            condition.setAccountID(account);
            condition.setPeriod(period);

            StockGoods correlatedStockGoods = stockGoodsService.getOne(condition);
            checkNotNull(correlatedStockGoods, "根据库存名称：%s 找不到库存", body.getComNameSpec());

            InvoiceBody updatedBody = new InvoiceBody();
            updatedBody.setInvoiceBID(body.getInvoiceBID());
            updatedBody.setComName(correlatedStockGoods.getComName());
            updatedBody.setSpec(correlatedStockGoods.getSpec());
            updatedBody.setSubCode(correlatedStockGoods.getSubCode());
            updatedBody.setSubFullName(correlatedStockGoods.getSubComname());
            updatedBody.setUpdatedate(new Date());
            return updatedBody;
        };

        return invoice.getInvoiceBodies().stream().map(mapper).collect(toList());
    }

    @Override
    public void changeInvoiceForDeleteVoucher(Voucher voucher) {
        checkArgument(voucher != null, "voucher 不能为空");
        String vouchID = voucher.getVoucherHeader().getVouchID();

        boolean b = invoiceHeaderService.lambdaUpdate()
                .set(InvoiceHeader::getVouchID, "")
                .eq(InvoiceHeader::getVouchID, vouchID)
                .update();
        if (!b) {
            throw new WqbException("置空发票上的凭证失败");
        }
    }

    @Override
    public BigDecimal getPositiveTotalTaxAmount(Invoice invoice) {
        return invoice.getInvoiceBodies().stream()
                .map(o -> BigDecimals.of(o.getTaxAmount()))
                .filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getNegativeTotalTaxAmount(Invoice invoice) {
        return invoice.getInvoiceBodies().stream()
                .map(o -> BigDecimals.of(o.getTaxAmount()))
                .filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getPositiveTotalNamount(Invoice invoice) {
        return invoice.getInvoiceBodies().stream()
                .map(o -> BigDecimals.of(o.getNamount()))
                .filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getNegativeTotalNamount(Invoice invoice) {
        return invoice.getInvoiceBodies().stream()
                .map(o -> BigDecimals.of(o.getNamount()))
                .filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}

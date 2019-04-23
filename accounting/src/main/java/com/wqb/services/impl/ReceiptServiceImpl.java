package com.wqb.services.impl;

import com.wqb.commons.bo.SubjectMatcher;
import com.wqb.commons.emun.SubjectItem;
import com.wqb.domains.Receipt;
import com.wqb.domains.StockGoods;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.ReceiptService;
import com.wqb.services.StockGoodsService;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.services.subject.SubjectService;
import com.wqb.supports.exceptions.WqbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.wqb.commons.emun.SubjectItem.*;

/**
 * <p>
 * 收据表 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-04-15
 */
@Service
public class ReceiptServiceImpl extends BaseServiceImpl<Receipt> implements ReceiptService {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StockGoodsService stockGoodsService;

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    @Override
    public void changeReceiptForCreateVoucher(String oldReceiptId, Voucher newVoucher, List<VoucherRule> voucherRules) {
        checkArgument(oldReceiptId != null, "receiptId 不能为空");
        checkArgument(newVoucher != null, "voucher 不能为空");

        Receipt receipt = getById(oldReceiptId);

        String voucherId = newVoucher.getVoucherHeader().getVouchID();
        // 收据类型
        Integer receiptType = getReceiptType(voucherRules);
        SubjectBalance subjectBalance = getCorrelatedSubjectBalance(newVoucher, receipt, receiptType);
        boolean b = lambdaUpdate()
                .set(Receipt::getVoucherId, voucherId)
                .set(Receipt::getType, receiptType)
                .set(Receipt::getSubjectName, subjectBalance.getSubName())
                .set(Receipt::getSubjectCode, subjectBalance.getSubCode())
                .set(Receipt::getSubjectFullName, subjectBalance.getFullName())
                .set(Receipt::getUpdateTime, new Date())
                .eq(Receipt::getId, oldReceiptId)
                .update();
        if (!b) {
            throw new WqbException("更新票据失败");
        }
    }

    @Override
    public void changeReceiptForDeleteVoucher(Voucher voucher) {
        checkArgument(voucher != null, "voucher 不能为空");
        String vouchID = voucher.getVoucherHeader().getVouchID();

        boolean b = lambdaUpdate()
                .set(Receipt::getVoucherId, "")
                .eq(Receipt::getId, vouchID)
                .update();
        if (!b) {
            throw new WqbException("置空票据与凭证关联失败");
        }
    }

    private Integer getReceiptType(List<VoucherRule> voucherRules) {
        List<Subject> subjects = subjectService.selectListByVoucherRules(voucherRules);
        SubjectMatcher subjectMatcher = new SubjectMatcher(subjects, voucherRules);

        // 销售收据
        if (subjectMatcher.match((SubjectItem)null, ZYYWSR)) {
            return 2;
        }

        // 采购收据
        if (subjectMatcher.match(KCSP, (SubjectItem)null) || subjectMatcher.match(YCL, (SubjectItem)null)) {
            return 1;
        }

        // 费用收据
        if (subjectMatcher.match(GLFY,  (SubjectItem)null)) {
            return 4;
        }

        // 银行单收据
        if (subjectMatcher.match(YHCK, YSZK) || subjectMatcher.match(YFZK, YHCK)) {
            return 3;
        }

        // 其他单据
        return 5;
    }

    private SubjectBalance getCorrelatedSubjectBalance(Voucher voucher, Receipt receipt, Integer receiptType) {
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String account = voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();
        String subjectCode;

        // 采购和销售
        if (receiptType == 2 || receiptType == 1) {
            StockGoods condition = new StockGoods();
            condition.setComNameSpec(receipt.getSubjectName());
            condition.setAccountID(account);
            condition.setPeriod(period);
            StockGoods correlatedStockGoods = stockGoodsService.getOne(condition);
            checkNotNull(correlatedStockGoods, "根据库存名称：%s 找不到库存", receipt.getSubjectName());
            subjectCode = correlatedStockGoods.getSubCode();
        } else {
            List<VoucherBody> voucherBodies = voucher.getVoucherBodies();
            Predicate<VoucherBody> voucherfilter;
            // 管理费用
            if (receiptType == 4) {
                voucherfilter = vb -> vb.getSubjectID().startsWith(GLFY.getCode());
            // 银行
            } else if (receiptType == 3) {
                voucherfilter = vb -> vb.getSubjectID().startsWith(YHCK.getCode());
            } else {
                voucherfilter = vb -> vb.getSubjectID().startsWith(QTYFK.getCode())
                            || vb.getSubjectID().startsWith(QTYSK.getCode());
            }
            VoucherBody voucherBody = voucherBodies.stream().filter(voucherfilter).findFirst().orElseThrow(null);
            checkNotNull(voucherBody, "票据通过票据类型找不到对应的凭证体");
            subjectCode = voucherBody.getSubjectID();
        }

        SubjectBalance subjectBalance = subjectBalanceService.selectCurrentOneFormCache(account, period, subjectCode);
        return checkNotNull(subjectBalance, "票据根据科目代码：%s 找不到对应的科目余额", subjectCode);
    }
}

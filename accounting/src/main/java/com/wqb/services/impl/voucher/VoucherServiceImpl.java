package com.wqb.services.impl.voucher;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wqb.commons.bo.SubjectMatcher;
import com.wqb.commons.bo.VoucherBuilder;
import com.wqb.commons.toolkit.SubjectCodeUtils;
import com.wqb.commons.toolkit.VoucherHelpers;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.mappers.voucher.VoucherHeaderMapper;
import com.wqb.services.ReceiptService;
import com.wqb.services.StockGoodsService;
import com.wqb.services.invoice.InvoiceService;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.services.subject.SubjectBookService;
import com.wqb.services.subject.SubjectService;
import com.wqb.services.voucher.VoucherBodyService;
import com.wqb.services.voucher.VoucherHeaderService;
import com.wqb.services.voucher.VoucherService;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author WQB
 * @since 2019-03-19 14:41
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherHeaderService voucherHeaderService;

    @Autowired
    private VoucherBodyService voucherBodyService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    @Autowired
    private SubjectBookService subjectBookService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StockGoodsService stockGoodsService;

    @Autowired
    private VoucherHeaderMapper voucherHeaderMapper;

    @Autowired
    private ReceiptService receiptService;

    /**
     * 生成普通凭证
     *
     * @param journalItem
     * @param voucherRules
     * @param user
     * @param account
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateVoucher(JournalItem journalItem, List<VoucherRule> voucherRules, User user, Account account) {
        // 提前读取科目余额表到缓存，生成凭证时分配科目代码、更新科目余额表都需要用到
        subjectBalanceService.selectCurrentListFormCache(account.getAccountID(), account.getUseLastPeriod());

        // 生成凭证
        Voucher voucher = internalGenerateVoucher(journalItem, voucherRules, user, account);
        // 金额为零无需生成凭证
        if (voucher == null) {
            return;
        }

        // 保存凭证
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        if (!voucherHeaderService.save(voucherHeader)) {
            throw new WqbException("保存凭证头信息失败");
        }
        if (!voucherBodyService.saveBatch(voucher.getVoucherBodies())) {
            throw new WqbException("保存凭证体信息失败");
        }

        // 创建或更新科目余额表
        subjectBalanceService.changeSubjectBalanceForCreateVoucher(voucher, journalItem);

        // 创建或更新科目明细表
        subjectBookService.changeSubjectBookForCreateVoucher(voucher);

        // 创建或更新库存
        stockGoodsService.changeStockGoodsForCreateVoucher(voucher);

        // 假如有发票关联修改发票引用
        if (journalItem.supportInvoice()) {
            invoiceService.changeInvoiceForCreateVoucher(journalItem.getInvoiceId(), voucher, voucherRules);
        } else {
            receiptService.changeReceiptForCreateVoucher(journalItem.getReceiptId(), voucher, voucherRules);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyVoucher(JournalItem journalItem, List<VoucherRule> voucherRules, User user, Account account) {
        Voucher oldVoucher = selectById(journalItem.getVoucherId());
        checkNotNull(oldVoucher, "参数有误，找不到对应的信息");
        checkOwner(user, account, oldVoucher);

        // 生成凭证
        Voucher newVoucher = internalGenerateVoucher(journalItem, voucherRules, user, account);
        if (newVoucher == null || !needChange(oldVoucher, newVoucher)) {
            return;
        }

        // 创建、删除或更新科目余额
        subjectBalanceService.changeSubjectBalanceForUpdatedVoucher(oldVoucher, newVoucher, journalItem);

        // 创建、更新科目明细
        subjectBookService.changeSubjectBookForUpdateVoucher(oldVoucher, newVoucher);

        // 创建、删除或更新库存
        stockGoodsService.changeStockGoodsForUpdateVoucher(oldVoucher, newVoucher);

        // 假如有发票关联修改发票引用
        if (journalItem.supportInvoice()) {
            invoiceService.changeInvoiceForCreateVoucher(journalItem.getInvoiceId(), newVoucher, voucherRules);
        } else {
            receiptService.changeReceiptForCreateVoucher(journalItem.getReceiptId(), newVoucher, voucherRules);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void destroyVoucher(JournalItem journalItem,  List<VoucherRule> voucherRules, User user, Account account) {
        Voucher oldVoucher = selectById(journalItem.getVoucherId());
        checkNotNull(oldVoucher, "参数有误，找不到对应的信息");

        checkOwner(user, account, oldVoucher);

        // 生成凭证
        Voucher newVoucher = internalGenerateVoucher(journalItem, voucherRules, user, account);
        if (newVoucher == null || !needChange(oldVoucher, newVoucher)) {
            return;
        }

        // 删除或更新科目余额
        subjectBalanceService.changeSubjectBalanceForDeleteVoucher(oldVoucher, journalItem);

        // 删除或更新科目明细
        subjectBookService.changeSubjectBookForDeleteVoucher(oldVoucher, newVoucher);

        // 删除或更新库存
        stockGoodsService.changeStockGoodsForDeleteVoucher(oldVoucher);

        if (journalItem.supportInvoice()) {
            // 删除发票引用
            invoiceService.changeInvoiceForDeleteVoucher(oldVoucher);
        } else {
            // 删除票据引用
            receiptService.changeReceiptForDeleteVoucher(oldVoucher);
        }
    }

    /**
     * 生成凭证的内部方法
     *
     * @param journalItem
     * @param voucherRules
     * @param user
     * @param account
     * @return
     */
    private Voucher internalGenerateVoucher(JournalItem journalItem, List<VoucherRule> voucherRules,
                                           User user, Account account) {
        BigDecimal taxAmount = BigDecimals.of(journalItem.getTaxAmount());
        BigDecimal amount = BigDecimals.of(journalItem.getAmount());
        // 获取凭证规则对应的会计科目
        List<Subject> subjects = subjectService.selectListByVoucherRules(voucherRules);
        checkArgument(CollectionUtils.isNotEmpty(subjects), "凭证规则没有配置对应会计科目");
        SubjectMatcher matcher = new SubjectMatcher(subjects, voucherRules);
        checkArgument(matcher.getDebitSubjects().size() > 0 && matcher.getDebitSubjects().size() > 0,
                "生成凭证检查：缺少借或贷科目");

        int maxVoucherNo = getMaxVoucherNo(account.getAccountID(), account.getUseLastPeriod());

        VoucherBuilder voucherBuilder = new VoucherBuilder(voucherRules)
                .setUser(user)
                .setAccount(account)
                .setStartVoucherNo(maxVoucherNo)
                .setReferencedSubjects(subjects)
                .setJournalItem(journalItem)
                // 配置凭证头
                .configureHeader(voucherHeaderHolder -> {
                    // 金额
                    voucherHeaderHolder.setHeaderAmountProvider(() -> BigDecimals.safeAdd(amount, taxAmount));
                    // 来源
                    voucherHeaderHolder.setSourceProvider(() -> VoucherHelpers.getSource(journalItem, matcher));
                    // 摘要
                    voucherHeaderHolder.setSummaryProvider(() ->  VoucherHelpers.getSummary(journalItem, matcher));
                })
                // 配置凭证体
                .configureBody(voucherBodyHolder -> {
                    // 金额
                    voucherBodyHolder.setBodyAmountProvider(() ->  VoucherHelpers.chooseVoucherBodyAmount(
                            voucherBodyHolder.getReferencedSubject(),
                            voucherBodyHolder.getVoucherRule(),
                            amount, taxAmount, matcher));
                    // 科目代码
                    voucherBodyHolder.setSubjectCodeProvider(
                            () -> allocateSubjectCode(journalItem,
                                    account, voucherBodyHolder.getReferencedSubject()));
                    // 科目名称
                    voucherBodyHolder.setSubjectNameProvider(
                            () -> VoucherHelpers.obtainExtendedSubjectFullNameIfExtendable(journalItem,
                                    voucherBodyHolder.getReferencedSubject()));
                    voucherBodyHolder.setNumberProvider(
                            () -> VoucherHelpers.getStockGoodsNumber(journalItem,
                                    voucherBodyHolder.getReferencedSubject()));
                });

        return voucherBuilder.build();
    }

    private void checkOwner(User user, Account account, Voucher oldVoucher) {
        VoucherHeader voucherHeader = oldVoucher.getVoucherHeader();
        if (Objects.equals(user.getUserID(), voucherHeader.getUpdatePsnID())) {
            throw new BizException("非法操作，非当前用户");
        }
        if (Objects.equals(voucherHeader.getAccountID(), account.getAccountID())
                || Objects.equals(voucherHeader.getPeriod(), account.getUseLastPeriod())) {
            throw new BizException("只能对当前账套操作");
        }
    }

    /**
     * 是否需要改变
     *
     * @param oldVoucher
     * @param newVoucher
     * @return
     */
    private boolean needChange(Voucher oldVoucher, Voucher newVoucher) {
        VoucherHeader oldVoucherHeader = oldVoucher.getVoucherHeader();
        VoucherHeader newVoucherHeader = newVoucher.getVoucherHeader();

        if (!BigDecimals.equals(oldVoucherHeader.getTotalDbit(), newVoucherHeader.getTotalDbit())
                || !BigDecimals.equals(oldVoucherHeader.getTotalCredit(), newVoucherHeader.getTotalCredit())) {
            return true;
        }

        List<VoucherBody> oldVoucherBodies = oldVoucher.getVoucherBodies();
        List<VoucherBody> newVoucherBodies = newVoucher.getVoucherBodies();
        for (int i = 0; i < oldVoucherBodies.size(); i++) {
            VoucherBody oldBody = oldVoucherBodies.get(i);
            VoucherBody newBody = newVoucherBodies.get(i);

            if (!Objects.equals(oldBody.getVcabstact(), newBody.getVcabstact())
                    || !Objects.equals(oldBody.getVcsubject(), newBody.getVcsubject())
                    || !Objects.equals(oldBody.getCreditAmount(), newBody.getCreditAmount())
                    || !Objects.equals(oldBody.getDebitAmount(), newBody.getDebitAmount())
                    || !Objects.equals(oldBody.getNumber(), newBody.getNumber())) {
                return false;
            }
        }

        if (!Objects.equals(oldVoucherHeader.getDes(), newVoucherHeader.getDes())) {
            VoucherHeader voucherHeader = new VoucherHeader();
            voucherHeader.setVouchID(oldVoucherHeader.getVouchID());
            voucherHeader.setVouchID(newVoucherHeader.getDes());
            voucherHeaderService.updateById(voucherHeader);
        }

        return false;
    }

    @Override
    public Voucher selectById(String voucherId) {
        checkArgument(voucherId != null, "voucherId 不能为null ");
        VoucherHeader voucherHeader = voucherHeaderService.getById(voucherId);

        if (voucherHeader != null) {
            LambdaQueryWrapper<VoucherBody> bodyQueryWrapper = Wrappers.<VoucherBody>lambdaQuery()
                    .eq(VoucherBody::getVouchID, voucherHeader.getVouchID());
            List<VoucherBody> voucherBodies = voucherBodyService.list(bodyQueryWrapper);

            return new Voucher(voucherHeader, voucherBodies);
        }
        return null;
    }

    @Override
    public int getMaxVoucherNo(String accountId, String period) {
        if (StringUtils.isAnyBlank(accountId, period)) {
            throw new WqbException(String.format("参数错误: accountId = %s, period = %s", accountId, period));
        }

        return voucherHeaderMapper.getMaxVoucherNo(accountId, period);
    }

    @Override
    public IPage<Voucher> selectPagingList(Page<Voucher> page, String accountId, String period) {
        LambdaQueryWrapper<VoucherHeader> headerQueryWrapper = Wrappers.<VoucherHeader>lambdaQuery()
                .eq(VoucherHeader::getAccountID, accountId)
                .eq(VoucherHeader::getPeriod, period)
                .orderByDesc(VoucherHeader::getVcDate);

        Page<VoucherHeader> voucherHeaderPage = new Page<>(page.getCurrent(), page.getSize());
        IPage<VoucherHeader> headerIPage = voucherHeaderService.page(voucherHeaderPage, headerQueryWrapper);


        if (headerIPage.getTotal() > 0L) {
            List<VoucherHeader> voucherHeaders = headerIPage.getRecords();
            List<String> voucherIds = voucherHeaders.stream()
                    .map(VoucherHeader::getVouchID)
                    .collect(Collectors.toList());

            LambdaQueryWrapper<VoucherBody> bodyQueryWrapper = Wrappers.<VoucherBody>lambdaQuery()
                    .in(VoucherBody::getVouchID, voucherIds);
            List<VoucherBody> voucherBodies = voucherBodyService.list(bodyQueryWrapper);

            Map<String, List<VoucherBody>> bodiesGroupedByVoucherId
                    = voucherBodies.stream().collect(groupingBy(VoucherBody::getVouchID));

            List<Voucher> vouchers = new ArrayList<>(voucherHeaders.size());
            voucherHeaders.forEach(voucherHeader -> {
                vouchers.add(new Voucher(voucherHeader, bodiesGroupedByVoucherId.get(voucherHeader.getVouchID())));
            });

            page.setRecords(vouchers);
            page.setSize(headerIPage.getSize());
            page.setTotal(headerIPage.getTotal());
        }

        return page;
    }

    /**
     * 分配科目代码
     *
     * @param journalItem
     * @param account
     * @param referencedSubject
     * @return
     */
    private String allocateSubjectCode(JournalItem journalItem, Account account, Subject referencedSubject) {
        // 需要扩展的科目，分配的科目代码是在涉及的科目代码基础上扩展的
        // 例如涉及的库存商品1405，分配的科目代码是1405XXX
        if (Objects.equals(referencedSubject.getExtend(), Subject.EXTENDABLE)) {
            String parentCode = referencedSubject.getCode();
            // 获取扩展名（只包含扩展部分）或者科目名（非科目全名）
            String subName =
                    VoucherHelpers.obtainSubjectNameOrExtendedNameIfExtendable(journalItem, referencedSubject);

            // 查找科目余额信息
            List<SubjectBalance> subjectBalances = subjectBalanceService.selectCurrentListFormCache(
                    account.getAccountID(), account.getUseLastPeriod());

            // 科目代码和科目名称过滤
            Optional<SubjectBalance> foundOptional = subjectBalances.stream()
                    .filter(sb -> Objects.equals(sb.getSuperiorCoding(), parentCode))
                    .filter(sb -> Objects.equals(sb.getSubName(), subName))
                    .findFirst();

            // 已经存在直接返回
            if (foundOptional.isPresent()) {
                return foundOptional.get().getSubCode();

                // 不存在就取最大的 +1 返回
            } else {
                // 取最大的分配的科目代码1405XXX
                Optional<SubjectBalance> maxOptional = subjectBalances.stream()
                        .filter(subjectBalance -> Objects.equals(subjectBalance.getSuperiorCoding(), parentCode))
                        .max(Comparator.comparing(SubjectBalance::getSubCode));

                // 存在已分配的那么就自增1
                if (maxOptional.isPresent()) {
                    String code = SubjectCodeUtils.getSubjectCodeByParentSubjectCode(
                            parentCode, maxOptional.get().getSubCode());
                    return String.valueOf(Long.valueOf(code) + 1);
                }

                // 一个也没有就手动分配第一号
                return SubjectCodeUtils.getSubjectCodeByParentSubjectCode(parentCode);
            }
        }

        // 不需要扩展的直接返回
        return referencedSubject.getCode();
    }
}

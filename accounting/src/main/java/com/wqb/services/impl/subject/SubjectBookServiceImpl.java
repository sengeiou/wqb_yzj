package com.wqb.services.impl.subject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wqb.commons.toolkit.SubjectCodeUtils;
import com.wqb.commons.toolkit.VoucherHelpers;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.subject.SubjectBook;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.impl.voucher.VoucherServiceImpl;
import com.wqb.services.subject.SubjectBookService;
import com.wqb.services.subject.SubjectService;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
@Service
public class SubjectBookServiceImpl extends BaseServiceImpl<SubjectBook> implements SubjectBookService {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectBalanceServiceImpl subjectBalanceService;


    @Override
    public List<SubjectBook> selectListByVoucherIds(List<String> voucherIds) {
        checkArgument(CollectionUtils.isNotEmpty(voucherIds), "凭证id集合为空");
        LambdaQueryWrapper<SubjectBook> queryWrapper = Wrappers.<SubjectBook>lambdaQuery()
                .in(SubjectBook::getVouchID, voucherIds);
        return list(queryWrapper);
    }

    @Override
    public List<SubjectBook> selectThePreviousListByVoucherNo(Voucher voucher) {
        checkArgument(voucher != null, "凭证条件条件为null");
        VoucherHeader voucherHeader = voucher.getVoucherHeader();

        Set<String> allCodes = new HashSet<>();
        voucher.getVoucherBodies().stream().map(VoucherBody::getSubjectID).forEach(s -> {
            CollectionUtils.addAll(allCodes, SubjectCodeUtils.splitSubjectCode(s));
        });

        LambdaQueryWrapper<SubjectBook> queryWrapper = Wrappers.<SubjectBook>lambdaQuery()
                .eq(SubjectBook::getAccountID,  voucherHeader.getAccountID())
                .eq(SubjectBook::getPeriod, voucherHeader.getPeriod())
                .in(SubjectBook::getSubCode, allCodes)
                .le(SubjectBook::getVouchNum, voucherHeader.getVoucherNo());
        return list(queryWrapper);
    }


    @Override
    public void changeSubjectBookForCreateVoucher(Voucher voucher) {
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String accountId =  voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();
        // 获取凭证的摘要
        String summary = VoucherHelpers.getVoucherFirstBody(voucher).getVcabstact();

        List<SubjectBook> createdList = new ArrayList<>();
        VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
            SubjectBalance subjectBalance = subjectBalanceService.selectCurrentOneFormCache(
                    accountId, period, code);
            // 新建凭证，建立一条全新的科目明细
            createdList.add(newBuildSubjectBookOfVoucher(voucherHeader,
                    voucherBody, subjectBalance, code, summary));
        });

        // 批量创建科目明细
        if (!saveBatch(createdList)) {
            List<Integer> ids = createdList.stream()
                    .map(SubjectBook::getSubBkId)
                    .collect(toList());
            throw new WqbException("创建凭证时，新建科目明细失败失败：" + ids);
        }
    }

    @Override
    public void changeSubjectBookForUpdateVoucher(Voucher oldVoucher, Voucher voucher) {
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String accountId =  voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();

        // 获取凭证的摘要
        String summary = VoucherHelpers.getVoucherFirstBody(voucher).getVcabstact();
        // 获取小于等于当前凭证号科目明细
        List<SubjectBook> beforeSubjectBooks = selectThePreviousListByVoucherNo(voucher);

        List<SubjectBook> updateList = new ArrayList<>();
        VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
            SubjectBalance subjectBalance = subjectBalanceService.selectCurrentOneFormCache(
                    accountId, period, code);
            // 更新凭证产生的科目明细
            List<SubjectBook> list = beforeSubjectBooks.stream()
                    .filter(s -> Objects.equals(code, s.getSubCode()))
                    .sorted((o1, o2) -> {
                        int a = o2.getVouchNum().compareTo(o1.getVouchNum());
                        int b = o2.getSubBkId().compareTo(o1.getSubBkId());
                        return a == 0 ? b : a;
                    })
                    .collect(toList());

            SubjectBook original;
            // 修改凭证， 更新从最近的一条作为基础数据
            if (list.size() > 1) {
                original = list.get(0);
            // 修改凭证，以当前这条作为基础数据
            } else {
                original = new SubjectBook();
                // 没有历史明细数据的 余额使用科目余额表的期初数据
                Subject subject = subjectService.selectOneByCodeFormCache(voucherBody.getSubjectID());
                BigDecimal balanceAmount =
                        Objects.equals(Subject.DIRECTION_DEBIT , subject.getDirection())
                        ? BigDecimals.safeSubtract(
                                subjectBalance.getInitDebitBalance(), subjectBalance.getInitCreditBalance())
                        : BigDecimals.safeSubtract(
                                subjectBalance.getInitCreditBalance(), subjectBalance.getInitDebitBalance());
                original.setBlanceAmount(balanceAmount);
            }
            SubjectBook subjectBook = rebuildFromBaseToUpdate(original, voucherHeader, voucherBody,
                    subjectBalance, code, summary);
            updateList.add(subjectBook);
        });

        // 批量更新科目明细
        if (!updateBatchById(updateList)) {
            List<Integer> ids = updateList.stream()
                    .map(SubjectBook::getSubBkId)
                    .collect(toList());
            throw new WqbException("更新凭证时，新建科目明细失败失败：" + ids);
        }

        // 假如更新的科目明细后面还有科目明细，金额变动后，后面的科目明细的金额也要跟着变动
        List<SubjectBook> afterSubjectBooks = getTheFollowingList(voucher);

        List<SubjectBook> updateAfterList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(afterSubjectBooks)) {
            VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
                // 假如更新的科目明细后面还有科目明细，金额变动后，后面的科目明细的金额也要跟着变动
                Subject subject = subjectService.selectOneByCodeFormCache(voucherBody.getSubjectID());
                BigDecimal changedAmount = Objects.equals(Subject.DIRECTION_DEBIT , subject.getDirection())
                        ? BigDecimals.safeSubtract(voucherBody.getDebitAmount(), voucherBody.getCreditAmount())
                        : BigDecimals.safeSubtract(voucherBody.getCreditAmount(), voucherBody.getDebitAmount());

                updateAfterList.addAll(filterFollowingListAndAddBalanceAmount(afterSubjectBooks,
                        voucherBody.getSubjectID(), changedAmount));
            });

            if (!updateBatchById(updateAfterList)) {
                List<Integer> ids = updateAfterList.stream()
                        .map(SubjectBook::getSubBkId)
                        .collect(toList());
                throw new WqbException("更新凭证时，更新对应的科目明细后，级联更新后续时失败：" + ids);
            }
        }
    }

    @Override
    public void changeSubjectBookForDeleteVoucher(Voucher oldVoucher, Voucher voucher) {
        List<Serializable> deletedIds = new ArrayList<>();
        VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
            List<SubjectBook> subjectBooks = selectListByVoucherIds(Lists.newArrayList(voucherBody.getVouchID()));
            deletedIds.addAll(subjectBooks.stream().map(SubjectBook::getSubBkId).collect(toList()));
        });

        // 批量创建科目明细
        if (!removeByIds(deletedIds)) {
            throw new WqbException("删除凭证时，删除科目明细失败：" + deletedIds);
        }

        // 获取所有凭证体对应的明细
        List<SubjectBook> allFollowingSubjectBooks = getTheFollowingList(voucher);
        if (CollectionUtils.isNotEmpty(allFollowingSubjectBooks)) {
            List<SubjectBook> updateAfterList = new ArrayList<>();
            VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
                // 假如更新的科目明细后面还有科目明细，金额变动后，后面的科目明细的金额也要跟着变动
                Subject subject = subjectService.selectOneByCodeFormCache(voucherBody.getSubjectID());
                BigDecimal changedAmount = Objects.equals(Subject.DIRECTION_DEBIT , subject.getDirection())
                        ? BigDecimals.safeSubtract(voucherBody.getDebitAmount(), voucherBody.getCreditAmount())
                        : BigDecimals.safeSubtract(voucherBody.getCreditAmount(), voucherBody.getDebitAmount());

                // 因为是删除明细，所有后序明细都要减去该明细的金额
                changedAmount = changedAmount.negate();

                // 从后置明细中过滤出当前科目代码对应的明细，该方法默认为加金额，所以取反
                updateAfterList.addAll(filterFollowingListAndAddBalanceAmount(allFollowingSubjectBooks,
                        voucherBody.getSubjectID(), changedAmount));
            });

            if (!updateBatchById(updateAfterList)) {
                List<Integer> ids = updateAfterList.stream()
                        .map(SubjectBook::getSubBkId)
                        .collect(toList());
                throw new WqbException("删除凭证时，更新对应的科目明细后，级联更新后续时失败：" + ids);
            }
        }
    }

    private List<SubjectBook> filterFollowingListAndAddBalanceAmount(List<SubjectBook> list, String fullCode,
                                                                     BigDecimal changedAmount) {
        list.stream()
                .filter(subjectBook -> Objects.equals(subjectBook.getSubCode(), fullCode))
                .findFirst()
                .ifPresent(subjectBook -> {
                    subjectBook.setBlanceAmount(
                            BigDecimals.safeAdd(subjectBook.getBlanceAmount(), changedAmount));
                });
        return list;
    }

    private List<SubjectBook> getTheFollowingList(Voucher voucher) {
        VoucherHeader voucherHeader = voucher.getVoucherHeader();

        Set<String> codes = voucher.getVoucherBodies().stream()
                .map(VoucherBody::getSubjectID)
                .collect(toSet());

        LambdaQueryWrapper<SubjectBook> queryWrapper = Wrappers.<SubjectBook>lambdaQuery()
                .select(SubjectBook::getSubBkId, SubjectBook::getBlanceAmount)
                .eq(SubjectBook::getAccountID,  voucherHeader.getAccountID())
                .eq(SubjectBook::getPeriod, voucherHeader.getPeriod())
                .in(SubjectBook::getSubCode, codes)
                .gt(SubjectBook::getVouchNum, voucherHeader.getVoucherNo());

        return list(queryWrapper);
    }

    /**
     *  更新凭证时，最近的一条作为基础数据去更新
     *
     * @param base
     * @param voucherHeader
     * @param voucherBody
     * @param subjectBalance
     * @param code
     * @param summary
     * @return
     */
    private SubjectBook rebuildFromBaseToUpdate(SubjectBook base, VoucherHeader voucherHeader,
                                                VoucherBody voucherBody, SubjectBalance subjectBalance,
                                                String code, String summary) {
        SubjectBook subjectBook = buildNewSubjectBook(voucherBody, subjectBalance,
                voucherHeader.getVoucherNo(), code, summary, subjectBalance.getSubName());
        subjectBook.setSubBkId(base.getSubBkId());
        // 附带上前一条科目明细的余额
        subjectBook.setBlanceAmount(BigDecimals.safeAdd(base.getBlanceAmount(), subjectBook.getBlanceAmount()));
        return subjectBook;
    }

    /**
     * 创建凭证时，保存新建科目明细
     *
     * @param voucherHeader
     * @param voucherBody
     * @param subjectBalance
     * @param code
     * @param summary
     * @return
     */
    private SubjectBook newBuildSubjectBookOfVoucher(VoucherHeader voucherHeader, VoucherBody voucherBody,
                                                     SubjectBalance subjectBalance, String code, String summary) {
        return buildNewSubjectBook(voucherBody, subjectBalance,
                voucherHeader.getVoucherNo(), code, summary, subjectBalance.getSubName());
    }


    /**
     * 建立的科目明细
     *
     * @param voucherBody
     * @param subjectBalance
     * @param voucherNum
     * @param subjectCode
     * @param voucherSummary
     * @param subjectName
     * @return
     */
    private SubjectBook buildNewSubjectBook(VoucherBody voucherBody, SubjectBalance subjectBalance,
                                            Integer voucherNum, String subjectCode, String voucherSummary,
                                            String subjectName) {
        SubjectBook subjectBook = new SubjectBook();
        subjectBook.setAccountID(voucherBody.getAccountID());
        subjectBook.setPeriod(voucherBody.getPeriod());
        subjectBook.setVouchID(voucherBody.getVouchID());
        subjectBook.setVouchAID(voucherBody.getVouchAID());
        subjectBook.setVouchNum(voucherNum);
        subjectBook.setVcabstact(voucherSummary);
        subjectBook.setSubCode(subjectCode);
        subjectBook.setSubName(subjectName);
        subjectBook.setUpdateDate(new Date());
        subjectBook.setUpDate(System.currentTimeMillis());
        subjectBook.setIsEndSubCode(subjectCode.equals(voucherBody.getSubjectID()) ? 1 : 0);

        String topCode = SubjectCodeUtils.getTopCodeBySubjectCode(subjectCode);
        Subject subject = subjectService.selectOneByCodeFormCache(topCode);
        BigDecimal changedAmount;

        if (Subject.DIRECTION_DEBIT == subject.getDirection()) {
            subjectBook.setDebitAmount(voucherBody.getDebitAmount());
            subjectBook.setCreditAmount(BigDecimal.ZERO);
            changedAmount = BigDecimals.safeSubtract(
                    subjectBalance.getEndingBalanceDebit(), subjectBalance.getEndingBalanceCredit());
        } else {
            subjectBook.setDebitAmount(BigDecimal.ZERO);
            subjectBook.setCreditAmount(voucherBody.getCreditAmount());
            changedAmount = BigDecimals.safeSubtract(
                    subjectBalance.getEndingBalanceCredit(), subjectBalance.getEndingBalanceDebit());
        }

        subjectBook.setDirection(String.valueOf(subject.getDirection()));
        // 明细账期末余额 。记录每次科目更新之后的期末余额 ， 有正负大小之分的期末余额
        subjectBook.setBlanceAmount(changedAmount);
        subjectBook.setSubName(subjectBalance.getSubName());
        return subjectBook;
    }
}

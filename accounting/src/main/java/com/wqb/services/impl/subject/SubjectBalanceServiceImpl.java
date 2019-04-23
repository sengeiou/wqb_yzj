package com.wqb.services.impl.subject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wqb.commons.constant.CacheNames;
import com.wqb.commons.toolkit.SubjectCodeUtils;
import com.wqb.commons.toolkit.VoucherHelpers;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.services.AccountService;
import com.wqb.services.UserService;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.services.subject.SubjectService;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * EXCEL科目档案表 服务实现类
 * </p>
 *
 * @author Shove
 * @since 2019-03-16
 */
@Service
@CacheConfig(cacheNames = CacheNames.SUBJECTS_BALANCES)
public class SubjectBalanceServiceImpl extends BaseServiceImpl<SubjectBalance> implements SubjectBalanceService {

    @Autowired
    private SubjectService subjectService;

    @Override
    public void changeSubjectBalanceForCreateVoucher(Voucher voucher, JournalItem journalItem) {
        try {
            updateSubjectBalancesForCreateThisVoucher(voucher, journalItem);
        } catch (Exception e) {
            throw new WqbException("创建凭证时，" + e.getMessage());
        }
    }


    @Override
    public void changeSubjectBalanceForUpdatedVoucher(Voucher oldVoucher, Voucher newVoucher, JournalItem journalItem) {
        try {
            deleteSubjectBalanceForDeleteThisVoucher(oldVoucher);
        } catch (Exception e) {
            throw new WqbException("更新凭证时，" + e.getMessage());
        }

        try {
            updateSubjectBalancesForCreateThisVoucher(newVoucher, journalItem);
        } catch (Exception e) {
            throw new WqbException("更新凭证证时，" + e.getMessage());
        }
    }

    @Override
    public void changeSubjectBalanceForDeleteVoucher(Voucher oldVoucher, JournalItem journalItem) {
        try {
            deleteSubjectBalanceForDeleteThisVoucher(oldVoucher);
        } catch (Exception e) {
            throw new WqbException("删除凭证证时，" + e.getMessage());
        }
    }

    /**
     * 根据当前凭证更新科目余额
     *
     * @param voucher
     */
    private void updateSubjectBalancesForCreateThisVoucher(Voucher voucher, JournalItem journalItem) {
        // 检查和处理科目余额表，如果有缺少的科目余额信息需要提前创建好
        // 科目余额中没有这个科目，就需要添加，类似库存商品、银行存款、应收、应付等
        VoucherHeader voucherHeader = voucher.getVoucherHeader();
        String accountId =  voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();

        List<SubjectBalance> createdList = new ArrayList<>();
        for (VoucherBody voucherBody : voucher.getVoucherBodies()) {
            SubjectBalance createdObj = buildSubjectBalanceIfAbsent(voucherBody, journalItem);
            if (createdObj != null) {
                createdList.add(createdObj);
            }
        }

        if (!createdList.isEmpty()) {
            if (!saveBatch(createdList)) {
                List<String> ids = createdList.stream()
                        .map(SubjectBalance::getPkSubId)
                        .collect(toList());
                throw new WqbException("创建科目余额失败：" + ids);
            }
            // 清空缓存
            ((SubjectBalanceService) AopContext.currentProxy()).deleteCachedCurrentList(accountId, period);
        }

        List<SubjectBalance> updatedList = new ArrayList<>();
        VoucherHelpers.processBody(voucher, (voucherBody, code) -> {
            SubjectBalance original = selectCurrentOneFormCache(accountId, period, code);
            BigDecimal changeAmount = VoucherHelpers.getVoucherBodyAmountByDirection(voucherBody);
            updatedList.add(rebuildFromBaseToUpdate(voucherBody.getDirection(), changeAmount, original));
        });

        if (!updatedList.isEmpty() && !updateBatchById(updatedList)) {
            List<String> ids = updatedList.stream()
                    .map(SubjectBalance::getPkSubId)
                    .collect(toList());
            throw new WqbException("更新科目余额失败：" + ids);
        }
        // 清空缓存
        ((SubjectBalanceService) AopContext.currentProxy()).deleteCachedCurrentList(accountId, period);
    }

    /**
     * 根据当前凭证删除科目余额
     *
     * @param oldVoucher
     */
    private void deleteSubjectBalanceForDeleteThisVoucher(Voucher oldVoucher) {
        VoucherHeader voucherHeader = oldVoucher.getVoucherHeader();
        String accountId = voucherHeader.getAccountID();
        String period = voucherHeader.getPeriod();

        List<Serializable> deletedIds = new ArrayList<>();
        List<SubjectBalance> updatedList = new ArrayList<>();

        // 先处理旧凭证对应的科目余额
        VoucherHelpers.processBody(oldVoucher, (voucherBody, code) -> {
            SubjectBalance subjectBalance = selectCurrentOneFormCache(accountId, period, code);

            boolean notUpdated = Objects.equals(subjectBalance.getCreateTime(), subjectBalance.getUpdateDate());
            // 未更新直接删除
            if (notUpdated) {
                deletedIds.add(subjectBalance.getPkSubId());

                // 已更新，将金额取反来消除增加的金额
            } else {
                BigDecimal changeAmount = VoucherHelpers.getVoucherBodyAmountByDirection(voucherBody);
                updatedList.add(
                        rebuildFromBaseToUpdate(voucherBody.getDirection(), changeAmount.negate(), subjectBalance));
            }
        });

        if (!deletedIds.isEmpty() && !removeByIds(deletedIds)) {
            throw new WqbException("删除科目余额表失败：" + deletedIds);
        }

        if (!updatedList.isEmpty() && !updateBatchById(updatedList)) {
            List<String> ids = updatedList.stream()
                    .map(SubjectBalance::getPkSubId)
                    .collect(toList());
            throw new WqbException("更新科目余额表失败：" + ids);
        }
    }

    /**
     * 如果没有凭证科目代码对应的科目余额记录就建立新科目余额对象
     *
     * @param voucherBody
     * @param journalItem
     * @return
     */
    private SubjectBalance buildSubjectBalanceIfAbsent(VoucherBody voucherBody, JournalItem journalItem) {
        String accountId = voucherBody.getAccountID();
        String period = voucherBody.getPeriod();

        // 这个id是生成凭证时分配的，当时分配的情况有：1、科目已经存在直接返回科目代码 2、科目不存在分配一个科目代码
        // 所有只要根据该科目代码去查找，若没有科目余额信息，即需要创建该科目余额
        String subjectCode = voucherBody.getSubjectID();

        try {
            // 存在科目明细就什么也不做，不存在会抛异常接着创建科目余额信息
            SubjectBalance exist = selectCurrentOneFormCache(accountId, period, subjectCode);
            if (exist != null) {
                return null;
            }
        } catch (WqbException ignored) {}

        // 根据规则取出父级科目代码
        String parentSubjectCode = SubjectCodeUtils.getParentSubjectCodeBySubjectCode(subjectCode);
        // 取出父科目
        Subject parentSubject = subjectService.selectOneByCodeFormCache(parentSubjectCode);
        // 父级科目余额
        SubjectBalance parentSubjectBalance = selectCurrentOneFormCache(accountId, period, parentSubject.getCode());

        SubjectBalance subjectBalance = new SubjectBalance();
        subjectBalance.setSubCode(subjectCode);

        String extendedSubjectName = voucherBody.getVcsubject();
        String extendedName = VoucherHelpers.trimSubjectNameOfExtendedSubjectName(
                extendedSubjectName, parentSubject.getFullName());
        subjectBalance.setSubName(extendedName);
        subjectBalance.setFullName(extendedSubjectName);

        subjectBalance.setInitCreditBalance(parentSubjectBalance.getInitCreditBalance());
        subjectBalance.setInitDebitBalance(parentSubjectBalance.getInitDebitBalance());
        subjectBalance.setCurrentAmountCredit(parentSubjectBalance.getCurrentAmountCredit());
        subjectBalance.setCurrentAmountDebit(parentSubjectBalance.getCurrentAmountDebit());
        subjectBalance.setYearAmountCredit(parentSubjectBalance.getYearAmountCredit());
        subjectBalance.setYearAmountDebit(parentSubjectBalance.getYearAmountDebit());
        subjectBalance.setEndingBalanceCredit(parentSubjectBalance.getEndingBalanceCredit());
        subjectBalance.setEndingBalanceDebit(parentSubjectBalance.getEndingBalanceDebit());

        subjectBalance.setDebitCreditDirection(parentSubject.getDirection() + "");
        subjectBalance.setCodeLevel(SubjectCodeUtils.getLevelBySubjectCode(parentSubjectCode));
        subjectBalance.setCategory(parentSubjectBalance.getCategory());
        subjectBalance.setUnit(parentSubjectBalance.getUnit());
        subjectBalance.setUnitId(parentSubjectBalance.getUnitId());
        Date now = new Date();
        subjectBalance.setCreateTime(now);
        subjectBalance.setUpdateDate(now);
        subjectBalance.setUpdateTimestamp(System.currentTimeMillis() + "");
        subjectBalance.setSubSource("手动新增" + (DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss")));

        subjectBalance.setSuperiorCoding(parentSubjectBalance.getSubCode());
        subjectBalance.setUserId(parentSubjectBalance.getUserId());
        subjectBalance.setAccountId(parentSubjectBalance.getAccountId());
        subjectBalance.setAccountPeriod(parentSubjectBalance.getAccountPeriod());
        subjectBalance.setExchangeRateState(1);

        return subjectBalance;
    }


    /**
     * 获取即将更新的科目余额表
     *
     * @param direction
     * @param changeAmount
     * @param base
     * @return
     */
    private SubjectBalance rebuildFromBaseToUpdate(String direction, BigDecimal changeAmount, SubjectBalance base) {
        SubjectBalance updated = new SubjectBalance();
        updated.setPkSubId(base.getPkSubId());

        if (Objects.equals(direction, Voucher.DIRECTION_DEBIT)) {
            updated.setCurrentAmountDebit(
                    BigDecimals.safeAdd(base.getCurrentAmountDebit(), changeAmount));
            updated.setEndingBalanceDebit(
                    BigDecimals.safeAdd(base.getEndingBalanceDebit(), changeAmount));
            updated.setYearAmountDebit(
                    BigDecimals.safeAdd(base.getYearAmountDebit(), changeAmount));
        } else {
            updated.setCurrentAmountCredit(
                    BigDecimals.safeAdd(base.getCurrentAmountCredit(), changeAmount));
            updated.setEndingBalanceCredit(
                    BigDecimals.safeAdd(base.getEndingBalanceCredit(), changeAmount));
            updated.setYearAmountCredit(
                    BigDecimals.safeAdd(base.getYearAmountCredit(), changeAmount));
        }

        BigDecimal diffValue = BigDecimals.safeSubtract(
                updated.getEndingBalanceDebit(), updated.getEndingBalanceCredit());
        if (diffValue.compareTo(BigDecimal.ZERO) > 0) {
            updated.setEndingBalanceDebit(diffValue);
            updated.setEndingBalanceCredit(BigDecimal.ZERO);
        } else {
            updated.setEndingBalanceDebit(BigDecimal.ZERO);
            updated.setEndingBalanceCredit(diffValue.abs());
        }

        updated.setUpdateDate(new Date());
        updated.setUpdateTimestamp(System.currentTimeMillis() + "");

        return updated;
    }


    @Override
    public List<SubjectBalance> selectCurrentListByCodes(String accountId, String period, List<String> codes) {
        LambdaQueryWrapper<SubjectBalance> queryWrapper = Wrappers.<SubjectBalance>lambdaQuery()
                .eq(SubjectBalance::getAccountId, accountId)
                .eq(SubjectBalance::getAccountPeriod, period)
                .in(SubjectBalance::getSubCode, codes);
        return list(queryWrapper);
    }

    @Override
    public SubjectBalance selectCurrentOneFormCache(String accountId, String period, String code) {
        // 获取当前套账的科目余额表
        List<SubjectBalance> list =
                ((SubjectBalanceService) AopContext.currentProxy()).selectCurrentListFormCache(accountId, period);
        return list.stream()
                .filter(s -> Objects.equals(s.getSubCode(), code))
                .findFirst()
                .orElseThrow(() -> {
                    String message = String.format("未找到科目余额信息，accountId：%s, period：%s, subjectCode：%s",
                            accountId, period, code);
                    return new WqbException(message);
                });
    }

    @Override
    @Cacheable(key = "#p0 + '_' + #p1")
    public List<SubjectBalance> selectCurrentListFormCache(String accountId, String period) {
        SubjectBalance condition = new SubjectBalance();
        condition.setAccountId(accountId);
        condition.setAccountPeriod(period);
        return list(condition);
    }

    @Override
    @CacheEvict(key = "#p0 + '_' + #p1")
    public void deleteCachedCurrentList(String accountId, String period) {
    }


}

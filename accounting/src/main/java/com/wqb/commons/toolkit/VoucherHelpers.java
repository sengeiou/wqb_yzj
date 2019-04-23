package com.wqb.commons.toolkit;

import com.wqb.commons.bo.SubjectMatcher;
import com.wqb.commons.emun.SubjectItem;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.BankAccount;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.wqb.commons.emun.SubjectItem.*;

/**
 * @author XW
 * @since 2019-04-07 11:13
 */
public class VoucherHelpers {

    public static void processBody(Voucher voucher, BiConsumer<VoucherBody, String> consumer) {
        checkArgument(voucher != null, "凭证不能为空");
        checkArgument(consumer != null, "consumer不能为空");

        List<VoucherBody> voucherBodies = voucher.getVoucherBodies();

        // 遍历凭证体（凭证对应的科目项）
        for (VoucherBody voucherBody : voucherBodies) {
            String subjectCode = voucherBody.getSubjectID();
            // 分解科目代码，遍历每一项
            for (String code : SubjectCodeUtils.splitSubjectCode(subjectCode)) {
                consumer.accept(voucherBody, code);
            }
        }
    }

    public static BigDecimal getVoucherBodyAmountByDirection(VoucherBody voucherBody) {
        String direction = voucherBody.getDirection();
        if (Objects.equals(voucherBody.getDirection(), Voucher.DIRECTION_DEBIT)) {
            return voucherBody.getDebitAmount();
        } else if (Objects.equals(voucherBody.getDirection(), Voucher.DIRECTION_CREDIT)) {
            return voucherBody.getCreditAmount();
        }

        throw new WqbException("借贷方向异常: " + direction);
    }

    /**
     * 选择凭证体的金额
     *
     * @param subject
     * @param voucherRule
     * @param amount
     * @param matcher
     * @return
     */
    public static BigDecimal chooseVoucherBodyAmount(Subject subject, VoucherRule voucherRule, BigDecimal amount,
                                               BigDecimal taxAmount, SubjectMatcher matcher) {
        // 科目代码是应交税费2221开头
        if (StringUtils.startsWith(subject.getCode(), YJSF.getCode())) {
            return taxAmount;
        }

        // 获取借贷科目的数量
        int debitSubjectSize = matcher.getDebitSubjects().size();
        int creditSubjectSize = matcher.getCreditSubjects().size();

        // 价税合计
        BigDecimal totalAmount = BigDecimals.safeAdd(amount, taxAmount);

        // 借贷科目都只有一个 返回价税合计
        if (debitSubjectSize == creditSubjectSize && debitSubjectSize == 1) {
            return totalAmount;
        }

        int direction = voucherRule.getSubjectDeriction();
        boolean isDebit = Objects.equals(direction, Subject.DIRECTION_DEBIT);
        boolean isCredit = Objects.equals(direction, Subject.DIRECTION_CREDIT);

        // 当前科目属于多的一方 返回金额
        if ((isDebit && debitSubjectSize > 1) || (isCredit && creditSubjectSize > 1)) {
            return amount;
        }

        // 当前科目属于一的一方 返回价税合计
        if ((isDebit && debitSubjectSize == 1) || (isCredit && creditSubjectSize == 1)) {
            return totalAmount;
        }

        throw new WqbException("无法确定凭证分录科目的金额情况");
    }

    /**
     * 如果可扩展的话获得扩展的科目全名，否则获取科目名（非全名）
     *
     * @param journalItem
     * @param subject
     * @return
     */
    public static String obtainExtendedSubjectFullNameIfExtendable(JournalItem journalItem, Subject subject) {
        String extendName = obtainExtendedNameIfExtendable(journalItem, subject);
        return subject.getFullName() + (extendName != null ? "_" + extendName : "");
    }

    /**
     * 如果可扩展的话获得扩展名，否则获取科目全民
     *
     * @param journalItem
     * @param subject
     * @return
     */
    public static String obtainSubjectNameOrExtendedNameIfExtendable(JournalItem journalItem, Subject subject) {
        String extendName = obtainExtendedNameIfExtendable(journalItem, subject);
        return extendName != null ? extendName : subject.getName();
    }

    /**
     * 去除扩展科目名中的科目名
     *
     * @param subjectWithExtendedName
     * @param subjectName
     * @return
     */
    public static String trimSubjectNameOfExtendedSubjectName(String subjectWithExtendedName, String subjectName) {
        return StringUtils.substringAfter(subjectWithExtendedName, subjectName + "_");
    }

    /**
     * 如果可扩展的话获得扩展名，否则null
     *
     * @param journalItem
     * @param subject
     * @return
     */
    public static String obtainExtendedNameIfExtendable(JournalItem journalItem, Subject subject) {
        if (Objects.equals(subject.getExtend(), Subject.EXTENDABLE)) {
            switch (subject.getExtendKey()) {
                case Subject.EXTEND_BANK:
                    return getSubjectNameOfBank(journalItem.getBankAccount());
                case Subject.EXTEND_GOODS:
                case Subject.EXTEND_CUSTOMER:
                    return journalItem.getTarget();
                default:
            }
        }
        return null;
    }

    /**
     * 获取有关银行的科目名
     *
     * @param bankAccount
     * @return
     */
    public static String getSubjectNameOfBank(BankAccount bankAccount) {
        return bankAccount.getBankType() + "-" + bankAccount.getBankAccount();
    }


    /**
     * 获取商品数量
     *
     * @param journalItem
     * @param subject
     * @return
     */
    public static BigDecimal getStockGoodsNumber(JournalItem journalItem, Subject subject) {
        if (Objects.equals(subject.getCode(), KCSP.getCode())
                || Objects.equals(subject.getCode(), YCL.getCode())) {
            return journalItem.getNumber();
        }
        return BigDecimal.ZERO;
    }

    public static VoucherBody getVoucherFirstBody(Voucher voucher) {
        return voucher.getVoucherBodies().stream()
                .filter(voucherBody -> Objects.equals(voucherBody.getRowIndex(), "1"))
                .findFirst()
                .orElseThrow(() -> new WqbException("生成的凭证有误，没有rowIndex为1的凭证体"));
    }


    /**
     * 来源 0:进项凭证 1.银行 2.固定资产 3.工资 4.结转损益 5.手工凭证  6.单据  7.结转成本 9销项凭证
     * 10结转全年净利润 11导入序时薄凭证 13结转增值税 131结转留底税 14 结转附赠税 15结转企业所得税
     *
     * @param journalItem
     * @param matcher
     * @return
     */
    public static int getSource(JournalItem journalItem, SubjectMatcher matcher) {
        // 暂且定手工凭证
        if (true) {
            return 5;
        }
        // 进项
        if (matcher.match(KCSP, YFZK)) {
            return 0;
        }
        // 销项
        if (matcher.match(YSZK, new SubjectItem[]{ZYYWSR, YJSF_WJZZS})) {
            return 9;
        }
        // 单据
        if (matcher.match(KCSP, KCXJ) || matcher.match(KCSP, YHCK) || matcher.match(KCSP, YFZK)) {
            return 6;
        }

        // 银行
        if (matcher.match(YHCK, QTYSK) || matcher.match(YHCK, QTYFK)
                || matcher.match(KCSP, YHCK) || matcher.match(QTYSK, YHCK)
                || matcher.match(KCXJ, YHCK) || matcher.match(YFZGXC, YHCK)
                || matcher.match(
                s -> StringUtils.startsWith(s.getFullName(), GLFY.getName()),
                s -> Objects.equals(s.getName(), YHCK.getName()))) {
            return 1;
        }

        throw new WqbException("找不到的凭证来源");
    }


    /**
     * 获取凭证摘要
     *
     * @param journalItem
     * @param matcher
     * @return
     */
    public static String getSummary(JournalItem journalItem, SubjectMatcher matcher) {
        if (matcher.match(KCXJ, ZYYWSR)) {
            return "现金收入";
        }
        if (matcher.match(YHCK, ZYYWSR)) {
            return "银行收入";
        }
        if (matcher.match(KCSP, KCXJ)) {
            return "现金购商品";
        }
        if (matcher.match(KCSP, YHCK)) {
            return "银行购商品";
        }
        if (matcher.match(YSZK, ZYYWSR)) {
            return "收入（未收款）";
        }
        if (matcher.match(KCSP, YFZK)) {
            return "采购商品（未付款）";
        }
        if (matcher.match(KCXJ, QTYSK)) {
            return "收回借款（现金）";
        }
        if (matcher.match(YHCK, QTYSK)) {
            return "收回借款（银行）";
        }
        if (matcher.match(QTYSK, KCXJ)) {
            return "借出现金";
        }
        if (matcher.match(QTYSK, YHCK)) {
            return "借出银行存款";
        }
        if (matcher.match(KCXJ, QTYFK)) {
            return "借备用金";
        }
        if (matcher.match(KCXJ, YHCK)) {
            return "取现";
        }
        if (matcher.match(YHCK, QTYFK)) {
            return "找股东借款";
        }

        // 管理费用
        boolean useKCXGPayGLFY = matcher.match(
                s -> StringUtils.startsWith(s.getFullName(), GLFY.getName()),
                s -> Objects.equals(s.getName(), KCXJ.getName()));
        boolean useYHCKPayGLFY = matcher.match(
                s -> StringUtils.startsWith(s.getFullName(), GLFY.getName()),
                s -> Objects.equals(s.getName(), YHCK.getName()));
        if (useKCXGPayGLFY || useYHCKPayGLFY) {
            if (matcher.getDebitSubjects().size() == 1) {
                return "支付" + matcher.getDebitSubjects().get(0).getName();
            }
            return "支付" + GLFY.getName();
        }

        if (matcher.match(
                s -> StringUtils.startsWith(s.getFullName(), GLFY.getName()),
                s -> Objects.equals(s.getName(), YFZGXC.getName()))) {
            return "计提本月工资";
        }
        if (matcher.match(YFZGXC, KCXJ)) {
            return "发放工资（现金）";
        }
        if (matcher.match(YFZGXC, YHCK)) {
            return "发放工资（银行）";
        }

        // 第一个借方科目名称
        return matcher.getDebitSubjects().get(0).getName() + "(" + journalItem.getTarget() + ")";
    }

}

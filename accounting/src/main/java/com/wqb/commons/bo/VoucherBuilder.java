package com.wqb.commons.bo;

import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherBody;
import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.wqb.commons.toolkit.MyCollectors.singleResultGroupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author Shoven
 * @since 2019-03-30 11:16
 */
public class VoucherBuilder {

    private List<VoucherRule> voucherRules;

    private List<Subject> referencedSubjects;

    private JournalItem journalItem;

    private VoucherHeaderHolder voucherHeaderHolder;

    private List<VoucherBodyHolder> voucherBodyHolders;

    private User user;

    private Account account;

    private int startVoucherNo;

    private String voucherId;

    public VoucherBuilder(List<VoucherRule> voucherRules) {
        this.voucherRules = voucherRules;
        voucherId = generateVouchId();
    }

    /**
     * 生成凭证
     *
     * @return
     */
    public Voucher build() {
        // 检查参数
        checkParams();

        // 初始化凭证体持有者对象
        initVoucherHoldersAndFillData();

        // 凭证头
        VoucherHeader voucherHeader = buildVoucherHeader();
        // 凭证体
        List<VoucherBody> voucherBodies = buildVoucherBodies();

        // 设置凭证信息
        return new Voucher(voucherHeader, voucherBodies);
    }

    /**
     * 参数检查
     */
    private void checkParams() {
        checkArgument(user != null, "生成凭证检查：用户不能为空");
        checkArgument(account != null, "生成凭证检查：账套不能为空");
        checkArgument(startVoucherNo >= 1, "生成凭证检查：凭证号 %s 错误", startVoucherNo);
        checkArgument(journalItem != null, "生成凭证检查：没有收支信息");
        checkArgument(CollectionUtils.isNotEmpty(referencedSubjects), "生成凭证检查：科目规则不能为空");
        checkArgument(CollectionUtils.isNotEmpty(voucherRules), "生成凭证检查：科目规则不能为空");
        checkArgument(ObjectUtils.allNotNull(
                voucherHeaderHolder.getHeaderAmountProvider(),
                voucherHeaderHolder.getSourceProvider(),
                voucherHeaderHolder.getSummaryProvider()), "生成凭证检查：科目规则不能为空");
        VoucherBodyHolder voucherBodyHolder = voucherBodyHolders.get(0);
        checkArgument(ObjectUtils.allNotNull(
                voucherBodyHolder.getBodyAmountProvider(),
                voucherBodyHolder.getSubjectCodeProvider(),
                voucherBodyHolder.getSubjectNameProvider()), "生成凭证检查：凭证体配置缺失");
    }

    /**
     * 初始化凭证体持有者，并填充数据
     */
    private void initVoucherHoldersAndFillData() {
        voucherHeaderHolder.useProviders();

        for (int i = 0; i < voucherRules.size(); ++ i) {
            VoucherRule voucherRule = voucherRules.get(i);
            VoucherBodyHolder bodyHolder = voucherBodyHolders.get(i);
            bodyHolder.voucherRule = voucherRule;
            bodyHolder.referencedSubject = getSubjectByRule(referencedSubjects, voucherRule);
            bodyHolder.rowIndex = (i + 1);
        }

        voucherBodyHolders.forEach(VoucherBodyHolder::useProviders);
    }

    /**
     * 生成凭证头
     *
     * @return
     */
    private VoucherHeader buildVoucherHeader() {
        VoucherHeader voucherHeader = new VoucherHeader();
        // 手动设置主键，凭证体就不需要依赖凭证头的id回写功能才插入凭证体
        voucherHeader.setVouchID(voucherId);
        voucherHeader.setCreatePsnID(user.getUserID());
        voucherHeader.setCreateDate(System.currentTimeMillis());
        voucherHeader.setCreatepsn(user.getUserName());
        voucherHeader.setVoucherNo(startVoucherNo);
        voucherHeader.setAccountID(account.getAccountID());
        voucherHeader.setVcDate(new Date());
        voucherHeader.setDes(journalItem.getRemark());
        voucherHeader.setSource(voucherHeaderHolder.getSource());
        voucherHeader.setPeriod(account.getUseLastPeriod());
        voucherHeader.setAuditStatus(Voucher.AUDIT_STATUS_UNAUDITED);
        voucherHeader.setVouchFlag(Voucher.VOUCH_FLAG_NON_TEMPLATE);
        voucherHeader.setTotalCredit(voucherHeaderHolder.getHeaderAmount());
        voucherHeader.setTotalDbit(voucherHeaderHolder.getHeaderAmount());
        voucherHeader.setIsproblem(Voucher.IS_PROBLEM_NO_PROBLEM);
        return voucherHeader;
    }

    /**
     * 生成凭证体集合
     *
     * @return
     */
    private List<VoucherBody> buildVoucherBodies() {
        List<VoucherBody> voucherBodies = new ArrayList<>(voucherRules.size());

        voucherBodyHolders.forEach(voucherBodyHolder -> {
            VoucherRule voucherRule = voucherBodyHolder.getVoucherRule();
            if (voucherBodyHolder.getBodyAmount().equals(BigDecimal.ZERO)) {
                return;
            }

            // 借方向
            if (Objects.equals(voucherRule.getSubjectDeriction(), Subject.DIRECTION_DEBIT)) {
                VoucherBody debitVoucherBody = getDebitVoucherBody(voucherBodyHolder);
                voucherBodies.add(debitVoucherBody);
            }

            // 贷方向
            if (Objects.equals(voucherRule.getSubjectDeriction(), Subject.DIRECTION_CREDIT)) {
                VoucherBody creditVoucherBody = getCreditVoucherBody(voucherBodyHolder);
                voucherBodies.add(creditVoucherBody);
            }
        });

        return voucherBodies;
    }

    /**
     * 获取借方凭证
     *
     * @param voucherBodyHolder
     * @return
     */
    private VoucherBody getDebitVoucherBody(VoucherBodyHolder voucherBodyHolder) {
        VoucherBody debitVoucherBody = getBaseVoucherBody(voucherBodyHolder);
        debitVoucherBody.setDirection(Voucher.DIRECTION_DEBIT);
        debitVoucherBody.setCreditAmount(BigDecimal.ZERO);
        debitVoucherBody.setDebitAmount(voucherBodyHolder.getBodyAmount());
        return debitVoucherBody;
    }

    /**
     * 获取贷方凭证
     *
     * @param voucherBodyHolder
     * @return
     */
    private VoucherBody getCreditVoucherBody(VoucherBodyHolder voucherBodyHolder) {
        VoucherBody creditVoucherBody = getBaseVoucherBody(voucherBodyHolder);
        creditVoucherBody.setDirection(Voucher.DIRECTION_CREDIT);
        creditVoucherBody.setCreditAmount(voucherBodyHolder.getBodyAmount());
        creditVoucherBody.setDebitAmount(BigDecimal.ZERO);
        return creditVoucherBody;
    }

    /**
     * 获取基础凭证体
     *
     * @param voucherBodyHolder
     * @return
     */
    private VoucherBody getBaseVoucherBody(VoucherBodyHolder voucherBodyHolder) {
        int rowIndex = voucherBodyHolder.getRowIndex();

        VoucherBody voucherBody = new VoucherBody();
        voucherBody.setVouchID(voucherId);
        voucherBody.setRowIndex(rowIndex + "");

        if (rowIndex == 1) {
            voucherBody.setVcabstact(voucherHeaderHolder.getSummary());
        }
        voucherBody.setVcsubject(voucherBodyHolder.getSubjectName());
        voucherBody.setSubjectID(voucherBodyHolder.getSubjectCode());
        voucherBody.setNumber(voucherBodyHolder.getNumber());

        voucherBody.setAccountID(account.getAccountID());
        voucherBody.setPeriod(account.getUseLastPeriod());
        voucherBody.setUpdatePsnID(user.getUserID());
        voucherBody.setUpdatePsn(user.getUserName());
        voucherBody.setUpdatedate(new Date());
        voucherBody.setIsproblem(Voucher.IS_PROBLEM_NO_PROBLEM);

        return voucherBody;
    }

    /**
     * 根据规则获取科目
     *
     * @param subjects
     * @param rule
     * @return
     */
    private Subject getSubjectByRule(Collection<Subject> subjects, VoucherRule rule) {
        List<Subject> subjectsByFilter = subjects.stream()
                .filter(s -> Objects.equals(s.getId(), rule.getSubjectId()))
                .collect(toList());

        if (CollectionUtils.isEmpty(subjectsByFilter)) {
            throw new WqbException("找不到对应的会计科目");
        }
        return subjectsByFilter.get(0);
    }


    public VoucherBuilder configureHeader(Consumer<VoucherHeaderHolder> headerConfig) {
        VoucherHeaderHolder voucherHeaderHolder = new VoucherHeaderHolder();
        headerConfig.accept(voucherHeaderHolder);
        this.voucherHeaderHolder = voucherHeaderHolder;
        return this;
    }

    public VoucherBuilder configureBody(Consumer<VoucherBodyHolder> bodyConfig) {
        voucherBodyHolders = new ArrayList<>(voucherRules.size());
        voucherRules.forEach(voucherRule -> {
            VoucherBodyHolder voucherBodyHolder = new VoucherBodyHolder();
            bodyConfig.accept(voucherBodyHolder);
            voucherBodyHolders.add(voucherBodyHolder);
        });
        return this;
    }

    public Map<Integer, VoucherBodyHolder> toMapGroupByRuleId() {
        return voucherBodyHolders.stream()
                .collect(singleResultGroupingBy(voucherBodyHolder -> voucherBodyHolder.getVoucherRule().getId()));
    }

    public String generateVouchId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public List<VoucherRule> getVoucherRules() {
        return voucherRules;
    }

    public List<Subject> getReferencedSubjects() {
        return referencedSubjects;
    }

    public VoucherBuilder setReferencedSubjects(List<Subject> referencedSubjects) {
        this.referencedSubjects = referencedSubjects;
        return this;
    }

    public User getUser() {
        return user;
    }

    public VoucherBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public VoucherBuilder setAccount(Account account) {
        this.account = account;
        return this;
    }

    public JournalItem getJournalItem() {
        return journalItem;
    }

    public VoucherBuilder setJournalItem(JournalItem journalItem) {
        this.journalItem = journalItem;
        return this;
    }

    public int getStartVoucherNo() {
        return startVoucherNo;
    }

    public VoucherBuilder setStartVoucherNo(int startVoucherNo) {
        this.startVoucherNo = startVoucherNo;
        return this;
    }

    public class VoucherHeaderHolder {

        private String summary;

        private BigDecimal headerAmount;

        private Integer source;

        private Supplier<String> summaryProvider;

        private Supplier<Integer> sourceProvider;

        private Supplier<BigDecimal> headerAmountProvider;

        private void useProviders() {
            headerAmount = headerAmountProvider.get();
            // 赋予凭证摘要
            summary = summaryProvider.get();
            source = sourceProvider.get();
        }

        public Supplier<String> getSummaryProvider() {
            return summaryProvider;
        }

        public void setSummaryProvider(Supplier<String> summaryProvider) {
            this.summaryProvider = summaryProvider;
        }

        public Supplier<Integer> getSourceProvider() {
            return sourceProvider;
        }

        public void setSourceProvider(Supplier<Integer> sourceProvider) {
            this.sourceProvider = sourceProvider;
        }

        public Supplier<BigDecimal> getHeaderAmountProvider() {
            return headerAmountProvider;
        }

        public void setHeaderAmountProvider(Supplier<BigDecimal> headerAmountProvider) {
            this.headerAmountProvider = headerAmountProvider;
        }

        public String getSummary() {
            return summary;
        }

        public BigDecimal getHeaderAmount() {
            return headerAmount;
        }

        public Integer getSource() {
            return source;
        }
    }

    public class VoucherBodyHolder {
        private Subject referencedSubject;

        private VoucherRule voucherRule;

        private String subjectName;

        private String subjectCode;

        private BigDecimal bodyAmount;

        private BigDecimal number;

        private int rowIndex;

        private Supplier<String> subjectCodeProvider;

        private Supplier<String> subjectNameProvider;

        private Supplier<BigDecimal> bodyAmountProvider;

        private Supplier<BigDecimal> numberProvider;

        private void useProviders() {
            bodyAmount = bodyAmountProvider.get();
            subjectName = subjectNameProvider.get();
            subjectCode = subjectCodeProvider.get();
            number = numberProvider.get();
        }

        public Account getAccount() {
            return account;
        }

        public Subject getReferencedSubject() {
            return referencedSubject;
        }

        public VoucherRule getVoucherRule() {
            return voucherRule;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public Supplier<String> getSubjectCodeProvider() {
            return subjectCodeProvider;
        }

        public void setSubjectCodeProvider(Supplier<String> subjectCodeProvider) {
            this.subjectCodeProvider = subjectCodeProvider;
        }

        public Supplier<String> getSubjectNameProvider() {
            return subjectNameProvider;
        }

        public void setSubjectNameProvider(Supplier<String> subjectNameProvider) {
            this.subjectNameProvider = subjectNameProvider;
        }

        public Supplier<BigDecimal> getBodyAmountProvider() {
            return bodyAmountProvider;
        }

        public void setBodyAmountProvider(Supplier<BigDecimal> bodyAmountProvider) {
            this.bodyAmountProvider = bodyAmountProvider;
        }

        public Supplier<BigDecimal> getNumberProvider() {
            return numberProvider;
        }

        public void setNumberProvider(Supplier<BigDecimal> numberProvider) {
            this.numberProvider = numberProvider;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public BigDecimal getBodyAmount() {
            return bodyAmount;
        }

        public BigDecimal getNumber() {
            return number;
        }
    }
}

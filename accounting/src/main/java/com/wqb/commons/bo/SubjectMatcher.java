package com.wqb.commons.bo;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.wqb.commons.emun.SubjectItem;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.VoucherRule;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;


/**
 * 科目匹配器，用一组科目和一组凭证规则做初始化，可以检查是否在借贷两个方向上存在对应的借贷科目
 *
 * @author Shoven
 * @since 2019-03-21 11:43
 */
public class SubjectMatcher {

    private List<Subject> subjects;
    private List<Subject> debitSubjects;
    private List<Subject> creditSubjects;

    public SubjectMatcher(Collection<Subject> subjects, List<VoucherRule> voucherRules) {
        this.subjects = new ArrayList<>(subjects);
        this.debitSubjects = extractDebitSubjects(voucherRules);
        this.creditSubjects = extractCreditSubjects(voucherRules);
    }

    /**
     * 一借一贷枚举匹配
     *
     * @param debitCondition
     * @param creditCondition
     * @return
     */
    public boolean match(SubjectItem debitCondition, SubjectItem creditCondition) {
        return match(new SubjectItem[]{debitCondition}, new SubjectItem[]{creditCondition});
    }

    /**
     * 一借多贷枚举匹配
     *
     * @param debitCondition
     * @param creditCondition
     * @return
     */
    public boolean match(SubjectItem debitCondition, SubjectItem[] creditCondition) {
        return match(new SubjectItem[]{debitCondition}, creditCondition);
    }

    /**
     * 多借一贷匹配
     *
     * @param debitCondition
     * @param creditCondition
     * @return
     */
    public boolean match(SubjectItem[] debitCondition, SubjectItem creditCondition) {
        return match(debitCondition, new SubjectItem[]{creditCondition});
    }

    /**
     * 按枚举数组同时借贷科目
     *
     * @param debitConditions
     * @param creditConditions
     * @return
     */
    public boolean match(SubjectItem[] debitConditions, SubjectItem[] creditConditions) {
        return match(buildFilters(debitConditions), buildFilters(creditConditions));
    }

    /**
     * 按单个过滤函数同时匹配借贷科目
     *
     * @param debitMatcher
     * @param creditMatcher
     * @return
     */
    public boolean match(Predicate<Subject> debitMatcher, Predicate<Subject> creditMatcher) {
        return match(Lists.newArrayList(debitMatcher), Lists.newArrayList(creditMatcher));
    }

    /**
     * 按一组过滤函数同时匹配借贷科目，假如条件为空 == 没有条件 即默认匹配成功
     *
     * @param debitMatchers
     * @param creditMatchers
     * @return
     */
    public boolean match(Collection<Predicate<Subject>> debitMatchers,
                         Collection<Predicate<Subject>> creditMatchers) {

        boolean findDebit = true;
        boolean findCredit = true;

        if (CollectionUtils.isNotEmpty(debitMatchers)) {
            // 直到匹配失败
            for (Predicate<Subject> debitMatcher : debitMatchers) {
                if (debitSubjects.stream().noneMatch(debitMatcher)) {
                    findDebit = false;
                    break;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(creditMatchers)) {
            // 直到匹配失败
            for (Predicate<Subject> creditMatcher : creditMatchers) {
                if (creditSubjects.stream().noneMatch(creditMatcher)) {
                    findCredit = false;
                    break;
                }
            }
        }

        return findDebit && findCredit;
    }

    /**
     *
     *
     * @param creditConditions
     * @return
     */
    private List<Predicate<Subject>> buildFilters(SubjectItem[] creditConditions) {
        List<Predicate<Subject>> creditMatchers = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(creditConditions)) {
            for (SubjectItem creditCondition : creditConditions) {
                if (creditCondition != null) {
                    creditMatchers.add(s -> StringUtils.startsWith(s.getCode(), creditCondition.getCode()));
                }
            }
        }
        return creditMatchers;
    }

    /**
     * 提取借科目
     *
     * @param voucherRules
     * @return
     */
    private List<Subject> extractDebitSubjects(List<VoucherRule> voucherRules) {
        return getSubjectsByFilter(subject -> {
                    Optional<VoucherRule> optional = voucherRules.stream()
                            .filter(voucherRule -> Objects.equals(voucherRule.getSubjectId(), subject.getId()))
                            .findFirst();
                    return optional.isPresent()
                            && Objects.equals(optional.get().getSubjectDeriction(), Subject.DIRECTION_DEBIT);
                }
        );
    }

    /**
     * 提取贷科目
     *
     * @param voucherRules
     * @return
     */
    private List<Subject> extractCreditSubjects(List<VoucherRule> voucherRules) {
        return getSubjectsByFilter(subject -> {
                    Optional<VoucherRule> optional = voucherRules.stream()
                            .filter(voucherRule -> Objects.equals(voucherRule.getSubjectId(), subject.getId()))
                            .findFirst();
                    return optional.isPresent()
                            && Objects.equals(optional.get().getSubjectDeriction(), Subject.DIRECTION_CREDIT);
                }
        );
    }

    private List<Subject> getSubjectsByFilter(Predicate<Subject> filter) {
        return subjects.stream().filter(filter).collect(toList());
    }


    public List<Subject> getDebitSubjects() {
        return debitSubjects;
    }

    public List<Subject> getCreditSubjects() {
        return creditSubjects;
    }
}

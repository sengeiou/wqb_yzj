package com.wqb.services.impl.voucher;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wqb.commons.constant.CacheNames;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.subject.SubjectService;
import com.wqb.services.voucher.VoucherRuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 项目与科目关系规则表  生成凭证用 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@Service
@CacheConfig(cacheNames = CacheNames.VOUCHER_RULES)
public class VoucherRuleServiceImpl extends BaseServiceImpl<VoucherRule> implements VoucherRuleService {

    @Autowired
    private SubjectService subjectService;

    @Override
    public List<VoucherRule> selectListByItemIdFormCache(Integer id) {
        checkArgument(id != null, "id 不能为null");
        Predicate<VoucherRule> predicate = voucherRule -> Objects.equals(voucherRule.getItemId(), id);
        return selectListByFilterFormCache(predicate);
    }

    @Override
    public List<VoucherRule> selectListByItemIdAndTypeFormCache(Integer id, Integer type) {
        checkArgument(id != null, "id 不能为null");
        return selectListByItemIdsAndTypeFormCache(Lists.newArrayList(id), type);
    }

    @Override
    public List<VoucherRule> selectListByItemIdsAndTypeFormCache(List<Integer> ids, Integer type) {
        checkArgument(CollectionUtils.isNotEmpty(ids), "ids 不能为空");
        checkArgument(type != null, "type 不能为null");

        Predicate<VoucherRule> predicate =
                voucherRule -> ids.contains(voucherRule.getItemId()) && Objects.equals(voucherRule.getType(), type);
        return selectListByFilterFormCache(predicate);
    }

    private List<VoucherRule> selectListByFilterFormCache(Predicate<VoucherRule> predicate) {
        List<VoucherRule> voucherRules = ((VoucherRuleService) AopContext.currentProxy()).selectAllFormCache();
        if (CollectionUtils.isEmpty(voucherRules)) {
            return Collections.emptyList();
        }
        return voucherRules.stream().filter(predicate).collect(toList());
    }

    @Override
    @Cacheable(key = "'list'")
    public List<VoucherRule> selectAllFormCache() {
        LambdaQueryWrapper<VoucherRule> queryWrapper = Wrappers.<VoucherRule>lambdaQuery()
                .eq(VoucherRule::getStatus, VoucherRule.STATUS_VALID);
        List<VoucherRule> voucherRules = list(queryWrapper);
        return checkNotNull(voucherRules, "未配置凭证项目对应规则表");
    }

    @Override
    @CacheEvict(key = "'list'")
    public void deleteListCache() {}

    @Override
    public boolean subjectsBelongsToRules(List<VoucherRule> voucherRules, List<Subject> subjects) {
        checkArgument(CollectionUtils.isNotEmpty(voucherRules), "voucherRules 不能为空");
        checkArgument(CollectionUtils.isNotEmpty(subjects), "subjects 不能为空");
        List<Integer> subjectIds = subjects.stream().map(Subject::getId).collect(toList());
        return  voucherRules.stream().allMatch(voucherRule -> subjectIds.contains(voucherRule.getSubjectId()));
    }
}

package com.wqb.services.impl.subject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wqb.commons.constant.CacheNames;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.subject.SubjectService;
import com.wqb.supports.exceptions.WqbException;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * <p>
 * 记账科目表 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@Service
@CacheConfig(cacheNames = CacheNames.SUBJECTS)
public class SubjectServiceImpl extends BaseServiceImpl<Subject> implements SubjectService {

    @Override
    public Subject selectOneByCodeFormCache(String subjectCode) {
        return ((SubjectService)AopContext.currentProxy()).selectAllFormCache().stream()
                .filter(s -> Objects.equals(s.getCode(), subjectCode))
                .findFirst()
                .orElseThrow(() -> {
                    String message = String.format("未找到科目信息， subjectCode：%s", subjectCode);
                    return new WqbException(message);
                });
    }

    @Override
    public Subject selectOneByIdFormCache(Integer id) {
        return ((SubjectService)AopContext.currentProxy()).selectAllFormCache().stream()
                .filter(s -> Objects.equals(s.getId(), id))
                .findFirst()
                .orElseThrow(() -> {
                    String message = String.format("未找到科目信息， id：%s", id);
                    return new WqbException(message);
                });
    }

    @Override
    @Cacheable(key = "'list'")
    public List<Subject> selectAllFormCache() {
        LambdaQueryWrapper<Subject> queryWrapper =
                Wrappers.<Subject>lambdaQuery().eq(Subject::getStatus, Subject.STATUS_VALID);
        List<Subject> subjects = list(queryWrapper);
        return checkNotNull(subjects, "未配置科目表");
    }

    @Override
    @CacheEvict(key = "'list'")
    public void deleteListCache(){}

    @Override
    public List<Subject> selectListByVoucherRules(List<VoucherRule> voucherRules) {
        checkArgument(CollectionUtils.isNotEmpty(voucherRules), "规则不能为空");
        Set<Integer> subjectIds = voucherRules.stream().map(VoucherRule::getSubjectId).collect(toSet());
        return ((SubjectService)AopContext.currentProxy()).selectAllFormCache().stream()
                .filter(s -> subjectIds.contains(s.getId()))
                .collect(toList());
    }

}

package com.wqb.services.impl.voucher;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wqb.commons.bo.SubjectMatcher;
import com.wqb.commons.constant.CacheNames;
import com.wqb.commons.dto.Menu;
import com.wqb.commons.dto.MenuTree;
import com.wqb.commons.emun.PaymentType;
import com.wqb.commons.emun.SubjectItem;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.VoucherItem;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.services.subject.SubjectService;
import com.wqb.services.voucher.VoucherItemService;
import com.wqb.services.voucher.VoucherRuleService;
import com.wqb.supports.exceptions.WqbException;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.wqb.commons.emun.SubjectItem.*;
import static com.wqb.commons.emun.SubjectItem.YHCK;
import static java.util.stream.Collectors.groupingBy;

/**
 * <p>
 * 生成凭证项目表  服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@Service
@CacheConfig(cacheNames = CacheNames.VOUCHER_ITEMS)
public class VoucherItemServiceImpl extends BaseServiceImpl<VoucherItem> implements VoucherItemService {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private VoucherRuleService voucherRuleService;

    @Override
    @Cacheable(key = "'menus'")
    public List<Menu> prepareMenuTreeFormCache() {
        List<VoucherItem> voucherItems = ((VoucherItemService) AopContext.currentProxy()).selectAllFormCache();
        return new MenuTree<VoucherItem, Menu>(voucherItems)
                // 顶级父节点元素选择器 parentId == 0
                .setParentSelector(item -> Objects.equals(item.getParentId(), 0))
                // 孩子节点元素选择器 父id == 子parentId
                .setChildSelector((parent, child) -> Objects.equals(parent.getId(), child.getParentId()))
                // 设置需映射转换的节点对象
                .setNodeMapper((voucherItem, parent) -> {
                    Menu menu = new Menu();
                    menu.setId(voucherItem.getId());
                    menu.setName(voucherItem.getName());
                    menu.setFullName(parent != null
                            ? parent.getFullName() + "_" + voucherItem.getName()
                            : voucherItem.getName());

                    // 获取当前项的规则
                    List<VoucherRule> rules =
                            voucherRuleService.selectListByItemIdFormCache(voucherItem.getId());

                    // 根据规则获取支付方式
                    if (!rules.isEmpty()) {
                        // 规则根据支付方式类型分组
                        Map<Integer, List<VoucherRule>> voucherRulesOfGroupedByType =
                                rules.stream().collect(groupingBy(VoucherRule::getType));

                        HashMap<Integer, Menu.Payment> payments = new HashMap<>();
                        // 遍历分组设置支付方式
                        voucherRulesOfGroupedByType.forEach((key, value) -> {
                            Menu.Payment payment = getPayment(value);
                            payment.setName(PaymentType.typeOf(key).getName());
                            payments.put(key, payment);
                        });
                        menu.setPayments(payments);
                    }
                    return menu;
                })
                .generate();
    }

    /**
     * 获取支付方式
     *
     * @param rules
     * @return
     */
    private Menu.Payment getPayment(List<VoucherRule> rules) {
        Menu.Payment payment = new Menu.Payment();
        rules.stream()
                .map(voucherRule -> subjectService.selectOneByIdFormCache(voucherRule.getSubjectId()))
                .filter(subject -> Objects.equals(Subject.EXTENDABLE, subject.getExtend()))
                .forEach(subject -> {
                    switch (subject.getExtendKey()) {
                        case Subject.EXTEND_BANK:
                            payment.setMustBank(true);
                            break;
                        case Subject.EXTEND_GOODS:
                            payment.setMustGoods(true);
                            break;
                        case Subject.EXTEND_CUSTOMER:
                            payment.setMustCustomer(true);
                            break;
                        default:
                    }
                });

        // 设置发票和收据支持
        List<Subject> subjects = subjectService.selectListByVoucherRules(rules);
        SubjectMatcher subjectMatcher = new SubjectMatcher(subjects, rules);

        // 销售、采购、管理费用、银行单 支持收据
        if (subjectMatcher.match((SubjectItem)null, ZYYWSR)
                || (subjectMatcher.match(KCSP, (SubjectItem)null) || subjectMatcher.match(YCL, (SubjectItem)null))) {
            payment.setSupportInvoice(true);
        }
        payment.setSupportReceipt(true);
        return payment;
    }

    @Override
    @CacheEvict(key = "'menus'")
    public void deleteMenusCache(){}

    @Override
    @Cacheable(key = "'list'")
    public List<VoucherItem> selectAllFormCache() {
        List<VoucherItem> voucherItems =
                list(Wrappers.<VoucherItem>lambdaQuery().eq(VoucherItem::getStatus, VoucherItem.STATUS_VALID));
        return checkNotNull(voucherItems, "未配置凭证项目表");
    }

    @Override
    @CacheEvict(key = "'list'")
    public void deleteListCache(){}

    @Override
    public VoucherItem selectOneByIdFormCache(Integer id) {
        checkArgument(id != null, "id 不能为空");
        List<VoucherItem> voucherItems = ((VoucherItemService) AopContext.currentProxy()).selectAllFormCache();
        return voucherItems.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
    }
}

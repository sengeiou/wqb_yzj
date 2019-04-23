package com.wqb.services.voucher;

import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.base.BaseService;

import java.util.List;

/**
 * <p>
 * 项目与科目关系规则表  服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface VoucherRuleService extends BaseService<VoucherRule> {

    /**
     * 根据id查找规则集合
     *
     * @param id
     * @return
     */
    List<VoucherRule> selectListByItemIdFormCache(Integer id);

    /**
     * 根据id和type查找规则集合
     *
     * @param id
     * @param type
     * @return
     */
    List<VoucherRule> selectListByItemIdAndTypeFormCache(Integer id, Integer type);

    /**
     * 根据id批量查找规则集合
     *
     * @param ids
     * @return
     */
    List<VoucherRule> selectListByItemIdsAndTypeFormCache(List<Integer> ids, Integer type);

    /**
     * 从缓存中取所有数据
     *
     * @return
     */
    List<VoucherRule> selectAllFormCache();

    void deleteListCache();

    /**
     * 判断这组科目属于这组规则吗
     *
     * @param voucherRules
     * @param subjects
     * @return
     */
    boolean subjectsBelongsToRules(List<VoucherRule> voucherRules, List<Subject> subjects);
}

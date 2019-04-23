package com.wqb.services.subject;

import com.wqb.domains.subject.Subject;
import com.wqb.domains.voucher.VoucherRule;
import com.wqb.services.base.BaseService;

import java.util.List;

/**
 * <p>
 * 记账科目表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface SubjectService extends BaseService<Subject> {

    /**
     * 根据科目代码得到借贷方向
     *
     * @param subjectCode
     * @return
     */
    Subject selectOneByCodeFormCache(String subjectCode);

    /**
     * 根据科目代码得到借贷方向
     *
     * @param id
     * @return
     */
    Subject selectOneByIdFormCache(Integer id);

    /**
     * 从缓存中取集合
     *
             * @return
             */
    List<Subject> selectAllFormCache();

    void deleteListCache();

    /**
     * 根据凭证规则获取科目
     *
     * @param voucherRules
     * @return
     */
    List<Subject> selectListByVoucherRules(List<VoucherRule> voucherRules);
}

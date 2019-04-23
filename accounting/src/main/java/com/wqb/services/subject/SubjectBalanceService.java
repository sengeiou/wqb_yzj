package com.wqb.services.subject;

import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.voucher.Voucher;
import com.wqb.services.base.BaseService;

import java.util.List;

/**
 * <p>
 * EXCEL科目档案表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface SubjectBalanceService extends BaseService<SubjectBalance> {

    /**
     * 根据科目代码获取当前套账的科目余额表
     *
     * @param accountId
     * @param period
     * @param code
     * @return
     */
    SubjectBalance selectCurrentOneFormCache(String accountId, String period, String code);

    /**
     * 根据科目代码即可取当前套账的科目余额信息
     *
     * @param codes
     * @param accountId
     * @param period
     * @return
     */
    List<SubjectBalance> selectCurrentListByCodes(String accountId, String period, List<String> codes);

    /**
     * 取当前套账的科目余额信息
     *
     * @param accountId
     * @param period
     * @return
     */
    List<SubjectBalance> selectCurrentListFormCache(String accountId, String period);

    /**
     * 删除缓存的科目余额信息
     *
     * @param accountId
     * @param period
     */
    void deleteCachedCurrentList(String accountId, String period);


    /**
     * 为创建凭证变更科目余额（创建、更新）
     *
     * @param voucher
     * @param journalItem
     */
    void changeSubjectBalanceForCreateVoucher(Voucher voucher, JournalItem journalItem);


    /**
     * 为更新凭证变更科目余额（创建、更新或删除）
     *
     * @param oldVoucher
     * @param newVoucher
     * @param journalItem
     */
    void changeSubjectBalanceForUpdatedVoucher(Voucher oldVoucher, Voucher newVoucher, JournalItem journalItem);


    /**
     * 为删除凭证变更科目余额（更新、删除）
     *
     * @param oldVoucher
     * @param journalItem
     */
    void changeSubjectBalanceForDeleteVoucher(Voucher oldVoucher, JournalItem journalItem);

}

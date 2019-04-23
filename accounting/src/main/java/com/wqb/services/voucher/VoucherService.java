package com.wqb.services.voucher;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.voucher.Voucher;
import com.wqb.domains.voucher.VoucherRule;

import java.util.List;

/**
 * @author WQB
 * @since 2019-03-19 14:39
 */
public interface VoucherService {

    /**
     * 生成通用的凭证
     * @param journalItem
     * @param voucherRules
     * @param user
     * @param account
     */
    void generateVoucher(JournalItem journalItem, List<VoucherRule> voucherRules, User user, Account account);

    /**
     * 修改凭证
     *
     * @param journalItem
     * @param voucherRules
     * @param user
     * @param account
     */
    void modifyVoucher(JournalItem journalItem, List<VoucherRule> voucherRules, User user, Account account);


    /**
     * 销毁凭证
     *
     * @param journalItem
     * @param voucherRules
     * @param user
     * @param account
     */
    void destroyVoucher(JournalItem journalItem, List<VoucherRule> voucherRules, User user, Account account);

    /**
     * id插座凭证
     *
     * @param voucherId
     * @return
     */
    Voucher selectById(String voucherId);

    /**
     * 取当前套账最大的凭证号
     *
     * @param accountId
     * @param period
     * @return
     */
    int getMaxVoucherNo(String accountId, String period);

    /**
     * 获取分页的当前账套凭证
     *
     * @param page
     * @param accountId
     * @param period
     * @return
     */
    IPage<Voucher> selectPagingList(Page<Voucher> page, String accountId, String period);
}

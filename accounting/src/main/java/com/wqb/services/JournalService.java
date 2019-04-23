package com.wqb.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wqb.commons.vo.Journal;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.Account;
import com.wqb.domains.User;

import java.util.Map;

/**
 * 收支服务类
 *
 * @author WQB
 * @since 2019-03-18 16:26
 */

public interface JournalService {

    /**
     * 获取可用的收支项目菜单
     *
     * @return
     */
    Map<String, Object> getMenus();

    /**
     * 创建收支
     *
     * @param journalItem
     * @param user
     * @param account
     */
    void createJournal(JournalItem journalItem, User user, Account account);

    /**
     * 更新收支
     *
     * @param journalItem
     * @param user
     * @param account
     */
    void updateJournal(JournalItem journalItem, User user, Account account);


    /**
     * 保存收支
     *
     * @param journalItem
     * @param user
     * @param account
     */
    void deleteJournal(JournalItem journalItem, User user, Account account);

    /**
     * 从缓存中获取收支列表
     *
     * @param page
     * @param accountId
     * @param useLastPeriod
     * @return
     */
    IPage<Journal> getJournals(Page<Journal> page, String accountId, String useLastPeriod);

    /**
     * 清空缓存
     *
     * @param accountId
     * @param period
     */
    void deleteCachedJournals(String accountId, String period);
}

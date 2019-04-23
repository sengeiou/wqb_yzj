package com.wqb.controllers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wqb.commons.vo.Journal;
import com.wqb.commons.vo.JournalItem;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.AccountService;
import com.wqb.services.JournalService;
import com.wqb.services.UserService;
import com.wqb.supports.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 收支控制器
 *
 * @author Shoven
 * @since 2019-03-19 10:48
 */
@RequestMapping("journals")
@RestController
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    /**
     * 获取收支项目
     *
     * @return
     */
    @GetMapping("/menus")
    public ResponseEntity getJournalMenus() {
        Map<String, Object> menus = journalService.getMenus();
        return ResponseUtils.ok(menus);
    }

    /**
     * 首页收支流水账
     *
     * @return
     */
    @GetMapping
    public ResponseEntity getJournals(Page<Journal> page) {
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);
        IPage<Journal> journals = journalService.getJournals(
                page, account.getAccountID(), account.getUseLastPeriod());
        return ResponseUtils.ok(journals);
    }


    /**
     * 创建收支流水
     *
     * @param journalItem
     * @return
     */
    @PostMapping
    public ResponseEntity createJournalItem(@Valid JournalItem journalItem) {
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);
        journalService.createJournal(journalItem, user, account);
        return ResponseUtils.ok(journalItem);
    }

    /**
     * 更新收支流水
     *
     * @param journalItem
     * @return
     */
    @PutMapping
    public ResponseEntity updateJournalItem(@Valid JournalItem journalItem) {
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);
        journalService.updateJournal(journalItem, user, account);
        return ResponseUtils.ok(journalItem);
    }

    /**
     * 删除收支流水
     *
     * @param journalItem
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteJournalItem(@Valid JournalItem journalItem) {
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);
        journalService.deleteJournal(journalItem, user, account);
        return ResponseUtils.ok(journalItem);
    }
}

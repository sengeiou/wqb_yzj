package com.wqb.controllers;

import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.AccountService;
import com.wqb.services.UserService;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.services.subject.SubjectService;
import com.wqb.services.voucher.VoucherItemService;
import com.wqb.services.voucher.VoucherRuleService;
import com.wqb.supports.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Shoven
 * @since 2019-03-21 16:03
 */
@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private VoucherItemService voucherItemService;

    @Autowired
    private VoucherRuleService voucherRuleService;

    @GetMapping
    public void index() {}

    /**
     * 清除所有的缓存
     *
     * @return
     */
    @DeleteMapping("/caches")
    public ResponseEntity deleteAllCaches() {
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);

        voucherRuleService.deleteListCache();
        voucherItemService.deleteMenusCache();
        voucherItemService.deleteListCache();
        subjectService.deleteListCache();
        subjectBalanceService.deleteCachedCurrentList(account.getAccountID(), account.getUseLastPeriod());
        return ResponseUtils.ok("清除缓存成功");
    }
}

package com.wqb.controllers;

import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.AccountService;
import com.wqb.services.InitService;
import com.wqb.services.UserService;
import com.wqb.supports.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Shoven
 * @since 2019-04-09 10:13
 */
@RestController
@RequestMapping("initiator")
public class InitController {

    @Autowired
    private InitService initService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


//    /**
//     * 初始化科目表（数据已修改不能再次生成）
//     *
//     * @return
//     */
//    @PostMapping("subjects")
//    public ResponseEntity initSubjects() {
//        URL url = this.getClass().getClassLoader().getResource("subjects.xls");
//        initService.initSubjectsFromExcel(url.getFile(), 1, -1);
//        return ResponseUtils.ok();
//    }

    @PostMapping("subjectBalances")
    public ResponseEntity initSubjectBalance() {
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);
        initService.initSubjectBalanceFromSubjects(user, account);
        return ResponseUtils.ok();
    }
}

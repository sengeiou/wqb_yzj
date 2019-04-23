package com.wqb.controllers;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.AccountService;
import com.wqb.services.UserService;
import com.wqb.supports.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账套信息表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/account")
public class AccountController{

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/me")
    public Object getCurrentUser()  {
        User currentUser = userService.getCurrentUser();
        Account currentAccount = accountService.getCurrentAccount(currentUser);
        return ResponseUtils.ok("获取账套信息成功", currentAccount);
    }
}

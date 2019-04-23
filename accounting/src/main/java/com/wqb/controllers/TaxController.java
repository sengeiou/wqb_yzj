package com.wqb.controllers;

import com.wqb.commons.vo.TaxVO;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.AccountService;
import com.wqb.services.UserService;
import com.wqb.services.impl.tax.TaxProvider;
import com.wqb.supports.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @Author: Ben
* @Date: 2019-04-01
* @Description: 报税api
*/
@RequestMapping("/tax")
@RestController
public class TaxController {

    @Autowired
    private TaxProvider taxProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/getTaxInfo")
    public ResponseEntity getTaxInfo(TaxVO taxVO){
        User user = userService.getCurrentUser();
        Account account = accountService.getCurrentAccount(user);
        Object o = taxProvider.getTaxInfo(taxVO,user,account);
        return ResponseUtils.ok(o);
    }
}

package com.wqb.controllers;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.Account;
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
public class AccountController extends BaseController<Account> {

}

package com.wqb.controllers;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.BankAccount;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 银行账户和科目映射表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-20
 */
@RestController
@RequestMapping("/bankAccount")
public class BankAccountController extends BaseController<BankAccount> {

}

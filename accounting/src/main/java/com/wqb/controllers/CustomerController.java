package com.wqb.controllers;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.Customer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 客户信息表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController<Customer> {

}

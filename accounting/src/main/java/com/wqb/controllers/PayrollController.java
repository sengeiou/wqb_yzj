package com.wqb.controllers;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.Payroll;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 人员薪资档案表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/payroll")
public class PayrollController extends BaseController<Payroll> {

}

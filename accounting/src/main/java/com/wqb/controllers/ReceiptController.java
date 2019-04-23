package com.wqb.controllers;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.wqb.controllers.base.BaseController;
import com.wqb.domains.Receipt;

/**
 * <p>
 * 收据表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-04-15
 */
@RestController
@RequestMapping("/receipt")
public class ReceiptController extends BaseController<Receipt> {

}

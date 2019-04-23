package com.wqb.controllers.invoice;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.invoice.InvoiceHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 发票主表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/invoiceHeader")
public class InvoiceHeaderController extends BaseController<InvoiceHeader> {

}

package com.wqb.controllers.invoice;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.invoice.InvoiceMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 发票映射科目记录表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-20
 */
@RestController
@RequestMapping("/invoiceMapping")
public class InvoiceMappingController extends BaseController<InvoiceMapping> {

}

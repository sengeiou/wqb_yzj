package com.wqb.controllers.voucher;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.voucher.VoucherItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 生成凭证项目表  前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/voucherItem")
public class VoucherItemController extends BaseController<VoucherItem> {

}

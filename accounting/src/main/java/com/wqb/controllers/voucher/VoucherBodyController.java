package com.wqb.controllers.voucher;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.voucher.VoucherBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 凭证子表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/voucherBody")
public class VoucherBodyController extends BaseController<VoucherBody> {

}

package com.wqb.controllers.voucher;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.voucher.VoucherRule;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 项目与科目关系规则表  生成凭证用 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/voucherRule")
public class VoucherRuleController extends BaseController<VoucherRule> {

}

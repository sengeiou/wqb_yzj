package com.wqb.controllers;


import com.wqb.controllers.base.BaseController;
import com.wqb.domains.ProvisionItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 计提凭证项目表 前端控制器
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@RestController
@RequestMapping("/provisionItem")
public class ProvisionItemController extends BaseController<ProvisionItem> {

}

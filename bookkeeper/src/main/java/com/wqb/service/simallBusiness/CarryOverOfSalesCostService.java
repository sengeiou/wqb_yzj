package com.wqb.service.simallBusiness;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.Voucher;

import javax.servlet.http.HttpSession;

/**
 * @author 司氏旭东
 * @ClassName: CarryOverOfSalesCostService
 * @Description: 结转销售成本
 * @date 2018年4月12日 下午4:23:33
 */
public interface CarryOverOfSalesCostService {
    Voucher CarryOverOfSalesCostVoucher(User user, Account account) throws BusinessException;
}

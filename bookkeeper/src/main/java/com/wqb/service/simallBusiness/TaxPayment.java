package com.wqb.service.simallBusiness;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.Voucher;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface TaxPayment {

    Map<String, Object> TaxPaymentVouch(User user, Account account) throws BusinessException;

    Voucher sobretaxaVouch(HttpSession session) throws BusinessException;


}

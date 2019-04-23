package com.wqb.service.jzcb;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.Voucher;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface JzcbService {
    Map<String, Object> doJzcb(User user, Account account, Voucher sfVoucher, Voucher zjVouch) throws BusinessException;
}

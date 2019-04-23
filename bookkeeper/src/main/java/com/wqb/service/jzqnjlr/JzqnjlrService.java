package com.wqb.service.jzqnjlr;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.Voucher;

import javax.servlet.http.HttpSession;

public interface JzqnjlrService {
    Voucher doJzqnjlr(User user, Account account) throws BusinessException;
}

package com.wqb.service.jzsy;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.Voucher;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface JzsyService {
    List<Voucher> doJzsy(User user, Account account) throws BusinessException;
}

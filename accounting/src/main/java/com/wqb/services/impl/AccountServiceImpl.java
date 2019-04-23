package com.wqb.services.impl;

import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.AccountService;
import com.wqb.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账套信息表 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService {

    @Override
    public Account getCurrentAccount(User user) {
        Account account = new Account();
        account.setUserID(user.getUserID());
        return getOne(account);
    }
}

package com.wqb.services;

import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.services.base.BaseService;

/**
 * <p>
 * 账套信息表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface AccountService extends BaseService<Account> {

    /**
     * 获取当前套账
     *
     * @return
     * @param user
     */
    Account getCurrentAccount(User user);
}

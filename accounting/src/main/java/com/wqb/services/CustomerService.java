package com.wqb.services;

import com.wqb.domains.Account;
import com.wqb.domains.Customer;
import com.wqb.domains.User;
import com.wqb.services.base.BaseService;

/**
 * <p>
 * 客户信息表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface CustomerService extends BaseService<Customer> {

    /**
     * 获取当前客户信息（自己）
     *
     * @param account
     * @return
     */
    Customer getCustomerByAccount(Account account);
}

package com.wqb.services.impl;

import com.wqb.domains.Account;
import com.wqb.domains.Customer;
import com.wqb.domains.User;
import com.wqb.services.CustomerService;
import com.wqb.services.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户信息表 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService {

    @Override
    public Customer getCustomerByAccount(Account account) {
        Customer condition = new Customer();
        condition.setAccountID(account.getAccountID());
        return getOne(condition);
    }
}

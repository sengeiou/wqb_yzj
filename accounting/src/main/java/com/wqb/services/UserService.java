package com.wqb.services;

import com.wqb.domains.Account;
import com.wqb.domains.Customer;
import com.wqb.domains.User;
import com.wqb.services.base.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户登录信息表 服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface UserService extends BaseService<User> {

    /**
     * 注册用户
     *
     * @param user
     * @param account
     * @param customer
     * @return
     */
    User register(User user, Account account, Customer customer);

    /**
     * 注册并绑定互联（第三方登录）
     *
     * @param request
     * @param user
     * @param account
     * @param customer
     * @return
     */
    User registerWithConnection(HttpServletRequest request, User user, Account account, Customer customer);

    /**
     * 获取当前用户信息
     *
     * @return
     */
    User getCurrentUser();
}

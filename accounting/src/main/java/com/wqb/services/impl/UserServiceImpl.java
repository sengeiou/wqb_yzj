package com.wqb.services.impl;

import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.security.app.social.AppSingUpUtils;
import com.wqb.services.AccountService;
import com.wqb.services.UserService;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.supports.exceptions.WqbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户登录信息表 服务实现类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppSingUpUtils appSingUpUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(User user, Account account) {
        if (!save(user)) {
            throw new WqbException("注册用户时，保存用户信息失败");
        }

        account.setUserID(user.getUserID());
        if (accountService.save(account)) {
            throw new WqbException("注册用户时，保存账套信息失败");
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUserWithConnection(HttpServletRequest request, User user, Account account) {
        User newUser = registerUser(user, account);
        try {
            appSingUpUtils.doPostSignUp(new ServletWebRequest(request), newUser.getUserID());
        } catch (Exception e) {
            throw new WqbException("注册用户时，保存第三方信息失败");
        }
        return newUser;
    }

    @Override
    public User getCurrentUser() {
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User condition = new User();
        condition.setLoginUser(username);
        return getOne(condition);
    }

}

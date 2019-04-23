package com.wqb.services.impl;

import com.wqb.domains.Account;
import com.wqb.domains.Customer;
import com.wqb.domains.User;
import com.wqb.security.app.AppSecretException;
import com.wqb.security.app.social.AppSingUpUtils;
import com.wqb.services.AccountService;
import com.wqb.services.CustomerService;
import com.wqb.services.UserService;
import com.wqb.services.base.BaseServiceImpl;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

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
    private CustomerService customerService;

    @Autowired
    private AppSingUpUtils appSingUpUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(User user, Account account, Customer customer) {
        String uid = getUUID();
        String accountId = getUUID();
        String customerId = getUUID();
        String userName = user.getUserName();
        int companyType = 3;

        Date now = new Date();
        user.setAbleDate(now);
        user.setDisableDate(now);
        user.setCompanyType("1");
        user.setCreatePsn(uid);
        user.setCreateDate(now);
        user.setUpdatePsn(uid);
        user.setUpdateDate(now);
        user.setUserType(6);
        user.setState(0);
        if (!save(user)) {
            throw new WqbException("注册用户时，保存用户信息失败");
        }

        account.setAccountID(accountId);
        account.setCustomID(customerId);
        account.setUserID(uid);
        account.setAccstandards(3);
        account.setCalculate("新会计准则");
        account.setUpdatepsnID(uid);
        account.setUpdatepsn(userName);
        account.setUpdatedate(now);
        account.setCreateDate(now);
        account.setCreatepsn(userName);
        account.setCreatepsnID(uid);
        account.setLastTime(now);
        account.setUseLastPeriod(DateFormatUtils.format(account.getPeriod(), "yyyy-MM"));
        account.setCompanyType(companyType);
        account.setGsmm(customer.getStateTaxCode());
        account.setTyxydm(customer.getComTyxydm());
        account.setJzbwb(1);
        account.setStatu(0);
        account.setInitialStates(1);
        account.setChgStatuTime(now);
        account.setMappingStates(1);
        if (!accountService.save(account)) {
            throw new WqbException("注册用户时，保存账套信息失败");
        }

        customer.setCustomID(customerId);
        customer.setCusPhone(user.getPhone());
        customer.setCusName(account.getCompanyName());
        customer.setBelongPersonID(uid);
        customer.setBelongPerName(userName);
        customer.setCreatePerName(userName);
        customer.setCreateDate(now);
        customer.setAccstandards(3);
        customer.setThreeAndOne(0);
        customer.setComGxjsqy(0);
        customer.setComKjxzxqy(0);
        customer.setComJsrgynssx(0);
        customer.setSsType(1);
        customer.setCompanyType(companyType);
        customer.setJzbwb(1);
        customer.setAccountID(accountId);
        if (!customerService.save(customer)) {
            throw new WqbException("注册用户时，保存客户信息失败");
        }

        return user;
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerWithConnection(HttpServletRequest request, User user, Account account, Customer customer) {
        User newUser = register(user, account, customer);
        try {
            appSingUpUtils.doPostSignUp(new ServletWebRequest(request), newUser.getUserID());
        }catch (AppSecretException e) {
            throw new BizException(e.getMessage());
        } catch (Exception e) {
            throw new WqbException("注册用户时，保存第三方信息失败" + e.getMessage());
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

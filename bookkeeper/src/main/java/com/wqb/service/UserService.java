package com.wqb.service;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.user.UserDao;
import com.wqb.model.Account;
import com.wqb.model.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author Shoven
 * @since 2019-03-28 18:22
 */
@Component
public class UserService {

    Log4jLogger logger = Log4jLogger.getLogger(UserService.class);

    ThreadLocal<User> threadLocalUser = new ThreadLocal<>();
    ThreadLocal<Account> threadLocalAccount = new ThreadLocal<>();


    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    public User getCurrentUser() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginUser", "18684844593");

        User user = threadLocalUser.get();
        if (user != null) {
            return user;
        }

        try {
            List<User> users = userDao.queryUserByCondition(map);
            User currentUser = users.get(0);
            threadLocalUser.set(currentUser);
            return currentUser;
        } catch (BusinessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account getCurrentAccount(User user) {
        Account account = threadLocalAccount.get();
        if (account != null) {
            return account;
        }

        HashMap<String, Object> map = new HashMap<>();
        Integer userType = user.getUserType();
        String userID = user.getUserID();

        map.put("statu", 1);
        map.put("lastTime", 1);
        if (userType == 2 || userType == 3) {
            map.put("source", userID);
        } else {
            map.put("userID", userID);
        }
        map.put("limitLogin", "1");

        try {
            List<Account> accList = accountDao.queryAccByCondition(map);
            Account currentAccount = accList.get(0);
            threadLocalAccount.set(currentAccount);
            return currentAccount;
        } catch (BusinessException e) {
            e.printStackTrace();
        }

        return null;
    }
}

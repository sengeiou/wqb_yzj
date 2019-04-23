package com.wqb.service.account.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.account.AccountDao;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Component
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountDao accountDao;

    @Override
    public Integer chgAccLastTime(Date lastTime, String accountID) throws BusinessException {
        return accountDao.chgAccLastTime(lastTime, accountID);
    }

    /**
     * @param session
     * @return
     * @throws BusinessException
     * @Title: chgAccInitialStates
     * @Description: 更改初始化状态
     * @see com.wqb.service.account.AccountService#chgAccInitialStates(javax.servlet.http.HttpSession)
     */
    @Override
    public Map<String, Object> chgAccInitialStates(HttpSession session) throws BusinessException {
        Account account = new Account();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) userDate.get("user");
            account = (Account) userDate.get("account");

            // 查询映射状态 映射未完成不能初始化
            Integer mappingStates = account.getMappingStates();
            if (mappingStates != 1) {
                result.put("msg", "请把 《科目映射》 完成再进行初始化！！！");
                return result;
            }
            account.setUserID(user.getUserID());
            account.setUpdatepsn(user.getUserName());
            account.setUpdatedate(new Date());
            account.setUpdatepsnID(user.getUserID());
            account.getAccountID();
            account.setInitialStates(1);
            Integer no = accountDao.chgAccInitialStates(account);
            result.put("no", no);
            result.put("msg", "科目初始化 已完成！");
            result.put("code", 1);

        } catch (Exception e) {
            result.put("msg", "AccountService.chgAccInitialStates出错啦");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param userID
     * @return
     * @throws BusinessException List<Account> 返回类型
     * @Title: queryByUserID
     * @Description: 根据用户id查询此用户下面所有的帐套
     * @date 2017年12月29日 下午4:06:24
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<Account> queryByUserID(String userID) throws BusinessException {
        List<Account> queryByUserIDList = accountDao.queryByUserID(userID);
        List<Account> accountList = new ArrayList<Account>();

        for (Account account : accountList)

//		for (Account account : queryByUserID)
//		{
//			Integer statu = account.getStatu();
//			if (!"2".equals(statu))
//			{
//				queryByUserID.add(account);
//			}
//		}

        //		for (Account account : queryByUserID)
        //		{
        //			Integer statu = account.getStatu();
        //			if (!"2".equals(statu))
        //			{
        //				queryByUserID.add(account);
        //			}
        //		}

        {
            Integer statu = account.getStatu();
            if (!"2".equals(statu)) {
                //queryByUserID.add(account);
            }
        }

        //		queryByUserIDList.getStatu() //'账套状态（0:新生成1:启用2:禁用）',
        return queryByUserIDList;
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示
     * 开发时间：2018-07-23
     * 开发人：tangsheng
     */
    @Override
    public Account queryPeriodId(Map<String, Object> pam) throws BusinessException {
        Account account = accountDao.queryPeriodId(pam);
        return account;
    }

    @Override
    public Account queryAccByID(String accountID) throws BusinessException {

        return accountDao.queryAccByID(accountID);
    }

    /**
     * @param session
     * @return
     * @Title: mappingStates
     * @Description: 更改映射状态
     * @see com.wqb.service.account.AccountService#mappingStates(javax.servlet.http.HttpSession)
     */
    @Override
    public Map<String, Object> mappingStates(HttpSession session) {
        Account account = new Account();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) userDate.get("user");
            account = (Account) userDate.get("account");

            account.setUserID(user.getUserID());
            account.setUpdatepsn(user.getUserName());
            account.setUpdatedate(new Date());
            account.setUpdatepsnID(user.getUserID());
            account.getAccountID();
            account.setMappingStates(1);
            Integer no = accountDao.mappingStates(account);
            result.put("no", no);
            result.put("code", 1);
        } catch (Exception e) {
            result.put("message", "AccountService.mappingStates出错啦");
            e.printStackTrace();
        }
        return result;
    }
}

package com.wqb.service.app;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.Test01;
import com.wqb.model.User;

import java.util.List;
import java.util.Map;

public interface AccountAppService {

    //根据用户id查询账套
    Account queryAccByUserID(Map<String, Object> param) throws BusinessException;

    //根据公司名称查询用户下面的所有账套
    List<Account> queryAllAccByUserIDAndComName(Map<String, Object> param) throws BusinessException;

    //根据账套id查询查询账套
    Account queryAccByID(Map<String, Object> param) throws BusinessException;

    //根据管理员查询员工
    List<User> queryUserByUserID(Map<String, Object> param) throws BusinessException;

    List<String> queryUserByUserID2(Map<String, Object> param) throws BusinessException;


    //根据员工查询账套
    List<Account> queryAllByID(Map<String, Object> param) throws BusinessException;

    List<Account> queryAllByID2(Map<String, Object> param) throws BusinessException;

    //根据用户查询权限
    User queryAcc(Map<String, Object> param) throws BusinessException;

    //测试方法可以删除
    void insertUser(Test01 test01) throws BusinessException;


}

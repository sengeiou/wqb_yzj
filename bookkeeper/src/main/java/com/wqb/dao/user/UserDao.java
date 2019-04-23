package com.wqb.dao.user;

import com.wqb.common.BusinessException;
import com.wqb.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    // 用户登录接口
    User login(String userName, String password) throws BusinessException;

    // 超级管理员查看自己名下管理员 管理员查看自己名下员工
    List<User> queryUserByParentID(String userID) throws BusinessException;

    // 根据平台ID查询用户信息
    User queryUserByLineID(String id) throws BusinessException;

    // 添加财税用户
    int addUser(User user) throws BusinessException;

    // 修改试用户转正状态
    int upUserByUid(User user) throws BusinessException;

    List<User> queryUserByID(String userID) throws BusinessException;

    // 根据平台ID查询用户信息
    User queryUserById(String userID) throws BusinessException;

    String queryUserName(String id) throws BusinessException;

    List<User> queryDzAdmin(String userType) throws BusinessException;

    List<User> queryUserBath(List<String> list) throws BusinessException;

    List<User> queryAllUser() throws BusinessException;

    void updCsID(Map<String, Object> param) throws BusinessException;

    void updCsMobile(Map<String, Object> param) throws BusinessException;

    void updSessionIDbyID(Map<String, Object> param) throws BusinessException;

    List<User> queryUserByCondition(Map<String, Object> param) throws BusinessException;

    int upUserByUid2(Map<String, Object> param) throws BusinessException;

    int updUserByLoginUser(Map<String, Object> param) throws BusinessException;


}

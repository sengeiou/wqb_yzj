package com.wqb.service.login;

import com.wqb.common.BusinessException;
import com.wqb.model.AccountOrder;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface LoginService {
    Map<String, Object> login(String userName, String password) throws BusinessException;

    // 财税系统单独登录(查询商城库)
    Map<String, Object> login1(String userName, String password, HttpSession session) throws BusinessException;

    Map<String, Object> synchronizeUserToAccoun(@RequestBody AccountOrder accountOrder) throws BusinessException;

    Map<String, Object> savePassword(String loginUser, String oldPassword, String newPassword) throws BusinessException;

    Map<String, Object> testUserCheckPermission(String phone, String userId) throws BusinessException;

    Map<String, Object> testUserLoing(String phone, String userId, HttpSession session, String ip_address) throws BusinessException;

    Map<String, Object> userToCheckPermission(String phone, String userId) throws BusinessException;
}

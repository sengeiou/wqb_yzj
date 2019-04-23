package com.wqb.service.qmjz;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface QmjzService {
    // 期末结转
    Map<String, Object> qmjz(Map<String, Object> param, User user, Account account) throws BusinessException;

    // 反结转功能
    Map<String, Object> unQmjz(User user, Account account) throws BusinessException;
}

package com.wqb.service.jzcl;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface JzclService {
    Map<String, Object> doJzcl(HttpSession session) throws BusinessException;

    Map<String, Object> unJzcl(User user, Account account, boolean isUnQmjz) throws BusinessException;
}

package com.wqb.service.report;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface TBasicIncomeStatementService {
    public Map<String, Object> addIncomeStatement(User user, Account account) throws BusinessException;

    public Map<String, Object> queryIncomeStatrment(User user, Account account) throws BusinessException;

    public Map<String, Object> deleteIncomeStatrment(User user, Account account) throws BusinessException;

    public Map<String, Object> queryIncomeStatrmentAPP(Map<String, Object> param) throws BusinessException;

    public Map<String, Object> queryProfit(Account acc, String period, User user) throws BusinessException;

}

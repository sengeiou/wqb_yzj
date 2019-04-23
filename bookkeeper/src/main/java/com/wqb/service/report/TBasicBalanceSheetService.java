package com.wqb.service.report;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface TBasicBalanceSheetService {

    /**
     * @return Map<String, Object>    返回类型
     * @Title: queryBalanceSheet
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @date 2018年2月8日  下午6:35:50
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> queryBalanceSheet(User user, Account account) throws BusinessException;

    Map<String, Object> addBalanceSheet(User user, Account account) throws BusinessException;

    Map<String, Object> queryBalanceSheetMap(HttpSession session) throws BusinessException;

    Map<String, Object> deleteBalanceSheet(User user, Account account) throws BusinessException;

    Map<String, Object> queryBalanceSheetApp(Map<String, Object> param) throws BusinessException;

}

package com.wqb.service.account;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;

import java.util.Map;

public interface AcccountInitializationService {

    Map<String, Object> initSub(Map<String, Object> map) throws BusinessException;


    Map<String, Object> accountReinitialization(Account acc, String period) throws BusinessException;

}

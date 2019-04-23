package com.wqb.dao.init;

import com.wqb.common.BusinessException;
import com.wqb.model.SljeInit;

import java.util.List;

public interface SljeInitDao {
    void insertSljeInit(SljeInit sljeInit) throws BusinessException;

    List<SljeInit> querySlje(String accountID) throws BusinessException;

    List<SljeInit> querySljeBySubNumber(String accountID, String subNumber) throws BusinessException;
}

package com.wqb.dao.pay;

import com.wqb.common.BusinessException;
import com.wqb.model.JzAmount;

import java.util.List;
import java.util.Map;

public interface JzAmountDao {
    List<JzAmount> queryJzAmount(Map<String, Object> param) throws BusinessException;

    void addJzAmount(JzAmount jzAmount) throws BusinessException;

    void updJzAmount(Map<String, Object> param) throws BusinessException;
}

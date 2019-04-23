package com.wqb.dao.pay;

import com.wqb.common.BusinessException;
import com.wqb.model.DzPay;

import java.util.List;
import java.util.Map;

public interface DzPayDao {
    public List<DzPay> queryDzPay(Map<String, Object> param) throws BusinessException;

    public void addDzPay(DzPay dzPay) throws BusinessException;

    public List<DzPay> queryRecord(Map<String, Object> param) throws BusinessException;

    public List<DzPay> querySeePeriod(Map<String, Object> param) throws BusinessException;

    public void updPayResult(Map<String, Object> param) throws BusinessException;

    public Object queryMonthJzCount(Map<String, Object> param) throws BusinessException;
}

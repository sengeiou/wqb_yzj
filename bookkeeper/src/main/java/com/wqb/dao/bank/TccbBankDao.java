package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.TccbBank;

import java.util.List;
import java.util.Map;

public interface TccbBankDao {
    void insertTccb(TccbBank tccbBank) throws BusinessException;

    List<TccbBank> querySame(Map<String, Object> param) throws BusinessException;

    void deleteByID(Map<String, Object> param) throws BusinessException;

    List<TccbBank> queryBankBill(Map<String, Object> param) throws BusinessException;

    //凭证添加 查询全部数据
    List<TccbBank> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;

}

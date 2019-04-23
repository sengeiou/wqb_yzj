package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.SzrcbBank;

import java.util.List;
import java.util.Map;

public interface SzrcbBankBillDao {

    void insertSzrcb(SzrcbBank szrcbBank) throws BusinessException;

    List<SzrcbBank> querySame(Map<String, Object> param) throws BusinessException;

    void deleteByID(Map<String, Object> param) throws BusinessException;

    List<SzrcbBank> queryBankBill(Map<String, Object> param) throws BusinessException;

    // 凭证添加 查询全部数据
    List<SzrcbBank> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;
}

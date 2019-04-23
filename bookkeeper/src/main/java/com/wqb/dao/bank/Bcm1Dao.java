package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.BcmBank1;

import java.util.List;
import java.util.Map;

public interface Bcm1Dao {
    void insertBcm(BcmBank1 bcmBank1) throws BusinessException;

    void deleteByID(Map<String, Object> param) throws BusinessException;

    List<BcmBank1> queryBankBill(Map<String, Object> param) throws BusinessException;

    List<BcmBank1> querySame(Map<String, Object> param) throws BusinessException;

    // 凭证添加 查询全部数据
    List<BcmBank1> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;
}

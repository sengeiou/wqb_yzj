package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.NyBank;
import com.wqb.model.bank.NyBankNew;

import java.util.List;
import java.util.Map;

public interface NyBankDao {
    // 添加建设银行
    void insertNyBank(NyBank nyBank) throws BusinessException;

    // 导入数据重复检验
    List<NyBank> querySame(Map<String, Object> param);

    void deleteByID(Map<String, Object> param) throws BusinessException;

    List<NyBank> queryBankBill(Map<String, Object> param) throws BusinessException;

    List<NyBankNew> queryBankBillNew(Map<String, Object> param) throws BusinessException;

    // 凭证添加 查询全部数据
    List<NyBank> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    // param1：accountID param2：period
    List<String> querytranID(Map<String, Object> param) throws BusinessException;

    void insertNyBankNew(NyBankNew nyBankNew) throws BusinessException;

    void deleteNewByID(Map<String, Object> param) throws BusinessException;

    List<NyBankNew> queryNewAll(Map<String, String> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;

    void updByIDNew(Map<String, Object> param) throws BusinessException;

}

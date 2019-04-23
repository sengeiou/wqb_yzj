package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.PaBank;

import java.util.List;
import java.util.Map;

//平安银行
public interface PaBankDao {
    // 添加建设银行
    void insertPaBank(PaBank paBank) throws BusinessException;

    // 导入数据重复检验
    List<PaBank> querySame(Map<String, Object> param) throws BusinessException;

    void deleteByID(Map<String, Object> param) throws BusinessException;

    // 分页查询
    List<PaBank> queryBankBill(Map<String, Object> param) throws BusinessException;

    // 凭证添加 查询全部数据
    List<PaBank> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;

}

package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.JsBank;

import java.util.List;
import java.util.Map;

public interface JsBankDao {
    // 添加建设银行
    void insertJsBank(JsBank jsBank) throws BusinessException;

    // 导入数据重复检验
    List<JsBank> querySame(Map<String, Object> param);

    void deleteByID(Map<String, Object> param) throws BusinessException;

    List<JsBank> queryBankBill(Map<String, Object> param) throws BusinessException;

    // 凭证添加 查询全部数据
    List<JsBank> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;
}

package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.ZsBank;

import java.util.List;
import java.util.Map;

public interface ZsBankDao {
    // 添加招商银行
    void insertZsBank(ZsBank zsBank) throws BusinessException;

    // 导入数据重复检验
    List<ZsBank> querySame(Map<String, Object> param);

    void deleteByID(Map<String, Object> param) throws BusinessException;

    // 分页查询
    List<ZsBank> queryBankBill(Map<String, Object> param) throws BusinessException;

    // 凭证添加 查询全部数据
    List<ZsBank> queryAll(Map<String, String> param) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;
}

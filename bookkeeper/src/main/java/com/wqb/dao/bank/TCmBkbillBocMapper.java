package com.wqb.dao.bank;

import com.wqb.common.BusinessException;
import com.wqb.model.bank.TCmBkbillBoc;

import java.util.List;
import java.util.Map;

public interface TCmBkbillBocMapper {
    //
//    int deleteByPrimaryKey(String pkBkbillBoc);
//
    int insert(TCmBkbillBoc record);
//
//    int insertSelective(TCmBkbillBoc record);
//
//    TCmBkbillBoc selectByPrimaryKey(String pkBkbillBoc);
//
//    int updateByPrimaryKeySelective(TCmBkbillBoc record);
//
//    int updateByPrimaryKey(TCmBkbillBoc record);
//
//	void uploadTCmBillBoc(MultipartFile bkillBocExcel);

    int uploadTCmBillBocList(List<TCmBkbillBoc> tCmBkbillBocList);

    List<TCmBkbillBoc> queryTCmBkbillBocList(TCmBkbillBoc tCmBkbillBoc);

    int deleteTCmbkbillBocAll(TCmBkbillBoc tCmBkbillBoc);

    int deleteTCmBkbillBocByBkbillBoc(String pkBkbillBoc);

    void deleteByID(Map<String, Object> param) throws BusinessException;

    //分页查询
    List<TCmBkbillBoc> queryBankBill(Map<String, Object> param) throws BusinessException;

    //凭证添加 查询全部数据
    List<TCmBkbillBoc> queryAll(Map<String, String> param) throws BusinessException;

    List<TCmBkbillBoc> queryBkbillBocByReferenceNumber(List<TCmBkbillBoc> tCmBkbillBocList) throws BusinessException;

    // 解除银行对账单对凭证的引用
    void delVouchID(String vouchID) throws BusinessException;

    void addVouchID(Map<String, Object> param) throws BusinessException;

    void updByID(Map<String, Object> param) throws BusinessException;
}

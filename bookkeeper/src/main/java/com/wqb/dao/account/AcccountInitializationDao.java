package com.wqb.dao.account;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface AcccountInitializationDao {

    int delSubBook(Map<String, Object> param) throws BusinessException;

    int delSub(Map<String, Object> param) throws BusinessException;

    int delSubMiddle(Map<String, Object> param) throws BusinessException;

    int delSubExcel(Map<String, Object> param) throws BusinessException;

    int delComm(Map<String, Object> param) throws BusinessException;

    int delAssets(Map<String, Object> param) throws BusinessException;

    int delAssetsExcel(Map<String, Object> param) throws BusinessException;

    int delAssetsRecord(Map<String, Object> param) throws BusinessException;

    //int delBasicAction(Map<String, Object> param) throws BusinessException;
    int delBalanceSheet(Map<String, Object> param) throws BusinessException;

    int delBankaccount2subject(Map<String, Object> param) throws BusinessException;

    int delDocuments(Map<String, Object> param) throws BusinessException;

    int delBasicExchangeRate(Map<String, Object> param) throws BusinessException;

    int delBasicFj(Map<String, Object> param) throws BusinessException;

    int delIncomeStatement(Map<String, Object> param) throws BusinessException;

    int delBasicMeasure(Map<String, Object> param) throws BusinessException;

    int delBasicProgress(Map<String, Object> param) throws BusinessException;

    int delReporttax(Map<String, Object> param) throws BusinessException;

    int delBasicSubject(Map<String, Object> param) throws BusinessException;

    //int delCmBkbill(Map<String, Object> param) throws BusinessException;
    int delCmBkbillAbc(Map<String, Object> param) throws BusinessException;

    int delCmBkbillBcm(Map<String, Object> param) throws BusinessException;

    int delCmBkbillBcm1(Map<String, Object> param) throws BusinessException;

    int delCmBkbillBoc(Map<String, Object> param) throws BusinessException;

    int delCmBkbillCcb(Map<String, Object> param) throws BusinessException;

    int delCmBkbillCeb(Map<String, Object> param) throws BusinessException;

    int delCmBkbillCib(Map<String, Object> param) throws BusinessException;

    int delCmBkbillCmb(Map<String, Object> param) throws BusinessException;

    int delCmBkbillCmbc(Map<String, Object> param) throws BusinessException;

    int delCmBkbillIcbc(Map<String, Object> param) throws BusinessException;

    int delCmBkbillJs(Map<String, Object> param) throws BusinessException;

    int detCmBkbillNy(Map<String, Object> param) throws BusinessException;

    int deltCmBkbillNyNew(Map<String, Object> param) throws BusinessException;

    int delCmBkbillPa(Map<String, Object> param) throws BusinessException;

    int delCmBkbillSzrcb(Map<String, Object> param) throws BusinessException;

    int delCmBkbillZs(Map<String, Object> param) throws BusinessException;

    int delInvoiceB(Map<String, Object> param) throws BusinessException;

    int delInvoiceH(Map<String, Object> param) throws BusinessException;

    int delInvoiceMappingrecord(Map<String, Object> param) throws BusinessException;

    int deltQmjzSetting(Map<String, Object> param) throws BusinessException;

    int delStatusPeriod(Map<String, Object> param) throws BusinessException;

    int delSysOperlog(Map<String, Object> param) throws BusinessException;

    int delTempType(Map<String, Object> param) throws BusinessException;

    int delUserAcc(Map<String, Object> param) throws BusinessException;

    int delVouchB(Map<String, Object> param) throws BusinessException;

    int delVouchH(Map<String, Object> param) throws BusinessException;

    int delVouchmdB(Map<String, Object> param) throws BusinessException;

    int delWaArch(Map<String, Object> param) throws BusinessException;

    int delWaExtra(Map<String, Object> param) throws BusinessException;

    int delZwPay(Map<String, Object> param) throws BusinessException;

    int delTblProCfg(Map<String, Object> param) throws BusinessException;

    int upAccInit(Map<String, Object> param) throws BusinessException;


}

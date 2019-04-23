package com.wqb.service.account.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.account.AcccountInitializationDao;
import com.wqb.dao.account.AccountDao;
import com.wqb.model.Account;
import com.wqb.service.account.AcccountInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Component
@Service("acccountInitializationService")
public class AcccountInitializationServiceImpl implements AcccountInitializationService {

    @Autowired
    AcccountInitializationDao acccountInitDao;
    @Autowired
    AccountDao accountDao;

    //初始化科目
    @Override
    public Map<String, Object> initSub(Map<String, Object> map) throws BusinessException {
        return null;
    }


    //账套重新初始化
    @SuppressWarnings("unused")
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> accountReinitialization(Account acc, String period) throws BusinessException {

        try {

            Map<String, Object> reMap = new HashMap<>();
            String aid = acc.getAccountID();

            Map<String, Object> param = new HashMap<>();
            param.put("accountID", aid);

            int num1 = acccountInitDao.delSubBook(param);
            int num2 = acccountInitDao.delSub(param);
            int num3 = acccountInitDao.delSubMiddle(param);
            int num4 = acccountInitDao.delSubExcel(param);
            int num5 = acccountInitDao.delComm(param);
            int num6 = acccountInitDao.delAssets(param);
            int num7 = acccountInitDao.delAssetsExcel(param);
            int num8 = acccountInitDao.delAssetsRecord(param);
            int num10 = acccountInitDao.delBalanceSheet(param);
            int num11 = acccountInitDao.delBankaccount2subject(param);
            int num12 = acccountInitDao.delDocuments(param);
            int num13 = acccountInitDao.delBasicExchangeRate(param);
            int num14 = acccountInitDao.delBasicFj(param);
            int num15 = acccountInitDao.delIncomeStatement(param);
            int num16 = acccountInitDao.delBasicMeasure(param);
            int num17 = acccountInitDao.delBasicProgress(param);
            int num18 = acccountInitDao.delReporttax(param);
            int num19 = acccountInitDao.delBasicSubject(param);
            int num20 = acccountInitDao.delCmBkbillAbc(param);
            int num21 = acccountInitDao.delCmBkbillBcm(param);
            int num22 = acccountInitDao.delCmBkbillBcm1(param);
            int num23 = acccountInitDao.delCmBkbillBoc(param);
            int num24 = acccountInitDao.delCmBkbillCcb(param);
            int num25 = acccountInitDao.delCmBkbillCeb(param);
            int num26 = acccountInitDao.delCmBkbillCib(param);
            int num27 = acccountInitDao.delCmBkbillCmb(param);
            int num28 = acccountInitDao.delCmBkbillCmbc(param);
            int num29 = acccountInitDao.delCmBkbillIcbc(param);
            int num30 = acccountInitDao.delCmBkbillJs(param);
            int num31 = acccountInitDao.detCmBkbillNy(param);
            int num32 = acccountInitDao.deltCmBkbillNyNew(param);
            int num33 = acccountInitDao.delCmBkbillPa(param);
            int num34 = acccountInitDao.delCmBkbillSzrcb(param);
            int num35 = acccountInitDao.delCmBkbillZs(param);
            int num36 = acccountInitDao.delInvoiceB(param);
            int num37 = acccountInitDao.delInvoiceH(param);
            int num38 = acccountInitDao.delInvoiceMappingrecord(param);

            int num39 = acccountInitDao.deltQmjzSetting(param);
            int num40 = acccountInitDao.delStatusPeriod(param);
            int num41 = acccountInitDao.delSysOperlog(param);
            int num42 = acccountInitDao.delTempType(param);
            int num43 = acccountInitDao.delUserAcc(param);
            int num44 = acccountInitDao.delVouchB(param);
            int num45 = acccountInitDao.delVouchH(param);
            int num46 = acccountInitDao.delVouchmdB(param);
            int num47 = acccountInitDao.delWaArch(param);
            int num48 = acccountInitDao.delWaExtra(param);
            int num49 = acccountInitDao.delZwPay(param);
            int num50 = acccountInitDao.delTblProCfg(param);


            param.put("mapping_states", 0); //映射状态（0.未映射 1.已映射）
            param.put("initial_states", 0); //初始化状态(0没有初始化，1已经初始化)
            int num51 = acccountInitDao.upAccInit(param);


            reMap.put("msg", "初始化成功");
            reMap.put("code", "0");
            return reMap;
        } catch (Exception e) {
            throw new BusinessException(e);
        }


    }

}

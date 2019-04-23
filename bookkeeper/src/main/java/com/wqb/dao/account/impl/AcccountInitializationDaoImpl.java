package com.wqb.dao.account.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.account.AcccountInitializationDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;


@Component
@Service("acccountInitializationDao")
public class AcccountInitializationDaoImpl implements AcccountInitializationDao {

    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @SuppressWarnings("unused")
    private static Log4jLogger logger = Log4jLogger.getLogger(AcccountInitializationDaoImpl.class);


    @Override
    public int delSubBook(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delSubBook", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delSub(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delSub", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delSubMiddle(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delSubMiddle", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delSubExcel(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delSubExcel", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delComm(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delComm", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delAssets(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delAssets", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delAssetsExcel(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delAssetsExcel", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delAssetsRecord(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delAssetsRecord", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBalanceSheet(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBalanceSheet", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBankaccount2subject(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBankaccount2subject", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delDocuments(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delDocuments", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBasicExchangeRate(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBasicExchangeRate", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBasicFj(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBasicFj", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delIncomeStatement(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delIncomeStatement", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBasicMeasure(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBasicMeasure", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBasicProgress(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBasicProgress", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delReporttax(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delReporttax", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delBasicSubject(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delBasicSubject", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillAbc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillAbc", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillBcm(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillBcm", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillBcm1(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillBcm1", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillBoc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillBoc", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillCcb(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillCcb", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillCeb(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillCeb", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillCib(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillCib", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillCmb(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillCmb", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillCmbc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillCmbc", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillIcbc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillIcbc", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillJs(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillJs", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int detCmBkbillNy(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.detCmBkbillNy", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int deltCmBkbillNyNew(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.deltCmBkbillNyNew", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillPa(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillPa", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillSzrcb(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillSzrcb", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delCmBkbillZs(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delCmBkbillZs", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delInvoiceB(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delInvoiceB", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delInvoiceH(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delInvoiceH", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delInvoiceMappingrecord(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delInvoiceMappingrecord", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int deltQmjzSetting(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.deltQmjzSetting", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delStatusPeriod(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delStatusPeriod", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delSysOperlog(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delSysOperlog", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delTempType(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delTempType", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delUserAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delUserAcc", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delVouchB(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delVouchB", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delVouchH(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delVouchH", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delVouchmdB(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delVouchmdB", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delWaArch(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delWaArch", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delWaExtra(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delWaExtra", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delZwPay(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delZwPay", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int delTblProCfg(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.delete("accInit.delTblProCfg", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }


    @Override
    public int upAccInit(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            return sqlSession.update("accInit.upAccInit", param);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

}

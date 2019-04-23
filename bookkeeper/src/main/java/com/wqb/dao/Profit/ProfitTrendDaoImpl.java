package com.wqb.dao.Profit;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.model.Account;
import com.wqb.model.ProfitTrendVo;
import com.wqb.model.StatusPeriod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Service("profitTrendDao")
public class ProfitTrendDaoImpl implements ProfitTrendDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(ProfitTrendDaoImpl.class);

    //查询利润趋势
    @Override
    public List<ProfitTrendVo> queryProfitTrend(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            /*
             * from t_basic_income_statement where account_id=#{account_id} and
             * account_period in <foreach collection="listMonth" item="id"
             * open="(" close=")" separator=","> #{id} </foreach>
             */
            List<ProfitTrendVo> list = sqlSession.selectList("vat.queryProfitTrend", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    //查询账套状态
    @Override
    public List<StatusPeriod> queryAccountStaus(Map<String, Object> param) throws BusinessException {
        //select * from t_status_period where accountID=#{account_id} and period = #{account_period}
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<StatusPeriod> list = sqlSession.selectList("vat.queryAccountStaus", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    //查询账套开通时间
    @Override
    public Account queryAccount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Account account = null;
        try {
            account = sqlSession.selectOne("vat.queryAccount", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return account;
    }

    //查询利润分析
    @Override
    public ProfitTrendVo queryprofitAnalyze(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            List<ProfitTrendVo> list = sqlSession.selectList("vat.queryprofitAnalyze", param);
            if (list != null && list.size() > 0) {
                BigDecimal curr_yysr = list.get(0).getCurr_yysr();
                if (curr_yysr != null) {
                    return list.get(0);
                } else {
                    if (list.size() > 1) {
                        return list.get(1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

}

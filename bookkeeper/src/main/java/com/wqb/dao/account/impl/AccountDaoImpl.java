package com.wqb.dao.account.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.account.AccountDao;
import com.wqb.model.Account;
import com.wqb.model.StatusPeriod;
import com.wqb.model.User;
import com.wqb.model.UserAccount;
import com.wqb.model.vomodel.AccStatusPeriod;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
@Service("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(AccountDaoImpl.class);

    /**
     * 查询用户名下所有账套信息
     */
    public List<Account> queryByUserID(String userID) throws BusinessException {
        // select * from t_basic_account where userID=#{userID} and statu=1
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("account.queryAccByUserID", userID);
            return list;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【queryByUserID】根据用户ID查询账套异常!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    // 切换账套时变更账套最后使用时间
    public Integer chgAccLastTime(Date lastTime, String accountID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("lastTime", lastTime);
            param.put("accountID", accountID);
            Integer count = sqlSession.update("account.chgAccLastTime", param);
            return count;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【chgAccLastTime】切换账套时变更账套最后使用时间!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * @param account
     * @return
     * @throws BusinessException Integer 返回类型
     * @Title: chgAccInitialStates
     * @Description: 更改初始化状态
     * @date 2018年1月4日 上午11:16:54
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public Integer chgAccInitialStates(Account account) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Integer count = sqlSession.update("account.chgAccInitialStates", account);

            return count;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【chgAccInitialStates】更改初始化状态!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    // 根据账套ID获取账套信息
    public Account queryAccByID(String accountID) throws BusinessException {
        Account account = null;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("account.queryAccByID", accountID);
            if (list != null && list.size() > 0) {
                account = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return account;
    }

    @Override
    public List<Account> queryAccs(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("account.queryAccs", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<Account> queryAccs2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("account.queryAccs2", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<User> queryUserById(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> list = sqlSession.selectList("account.queryUserById", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public int queryAccsNum(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = 0;
        try {
            num = sqlSession.selectOne("account.queryAccsNum", param);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

    @Override
    public List<Account> queryAccsNum2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = null;
        try {
            list = sqlSession.selectList("account.queryAccsNum2", param);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    // 根据条件查询账套
    @Override
    public List<Account> queryAccByCondition(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = null;
        try {
            list = sqlSession.selectList("account.queryAccByCondition", param);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public List<StatusPeriod> queryStatusPeriod(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<StatusPeriod> list = sqlSession.selectList("account.queryStatusPeriod", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【chgAccByCondintion】更改初始化状态!", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    // 更新账套最后使用时间和期间
    @Override
    public int chgAccByCondintion(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = -1;
        try {
            count = sqlSession.update("account.chgAccByCondintion", param);
            return count;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【chgAccByCondintion】更改初始化状态!", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public int insertUserAcc(UserAccount userAcc) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = -1;
        try {
            count = sqlSession.insert("userAcc.insertUserAcc", userAcc);
        } catch (Exception e) {
            logger.error("AccountDaoImpl【insertUserAcc】添加管理查看账套数据!", e);
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public int chgAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Integer count = -1;
        try {
            count = sqlSession.update("userAcc.chgAcc", param);
        } catch (Exception e) {
            logger.error("AccountDaoImpl【chgAcc】更新管理员查看账套记录!", e);
        } finally {
            sqlSession.close();
        }
        return count;
    }

    @Override
    public UserAccount queryAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<UserAccount> list = null;
        try {
            list = sqlSession.selectList("userAcc.queryAcc", param);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    // 根据条件查询账套总数
    @Override
    public Integer queryCountAccByCondition(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Integer num = sqlSession.selectOne("account.queryCountAccByCondition", param);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<AccStatusPeriod> queryAccJoinStatusPeriod(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<AccStatusPeriod> list = null;
        try {
            list = sqlSession.selectList("account.queryAccJoinStatusPeriod", param);
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    // 查询总数
    @Override
    public Integer queryAccJoinStatusPeriodCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Integer num = sqlSession.selectOne("account.queryAccJoinStatusPeriodCount", param);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Object getMonthAddAcc(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.getMonthAddAcc", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getMonthTyAcc(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.getMonthTyAcc", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getDzqyAccCount() throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Map<String, Object>> obj = sqlSession.selectList("account.getDzqyAccCount");
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    /**
     * 根据超级管理员登录系统页面，当点击扇形图中的形状时，触发表单账套客户信息显示 开发时间：2018-07-23 开发人：tangsheng
     */
    @Override
    public Account queryPeriodId(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Account account = null;
        try {
            account = sqlSession.selectOne("account.queryPeriodId", param);
            if (account != null) {
                return account;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return account;
    }

    @Override
    public List<StatusPeriod> queryStaPeriod2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<StatusPeriod> list = sqlSession.selectList("account.queryStaPeriod2", param);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【chgAccByCondintion】更改初始化状态!", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public Object getByxzqy(Map<String, Object> param) throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.getByxzqy", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getSyzqy(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.getSyzqy", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getZtyqys(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.getZtyqys", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object getZqys(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.getZqys", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public int queryAccByConditionCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.selectOne("account.queryAccByConditionCount", param);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    // 查询进行中
    @Override
    public int queryAccRuning(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.selectOne("account.queryAccRuning", param);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    // 查询完成
    @Override
    public int queryAccFinish(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int num = sqlSession.selectOne("account.queryAccFinish", param);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<Account> queryAllAccByUserIdStr(String userId) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> accountList = new ArrayList<Account>();
        try {
            accountList = sqlSession.selectList("account.queryAllAccByUserIdStr", userId);
            if (accountList != null && accountList.size() > 0) {
                return accountList;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return accountList;
    }

    /**
     * @param hashMap
     * @return
     * @throws BusinessException List<Account> 返回类型
     * @Title: queryAllAccByUserIdYear
     * @Description: 按年份查询用户下面所有的帐套
     * @date 2018年7月27日 上午11:00:47
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<Account> queryAllAccByUserIdYear(Map<String, String> hashMap) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> accountList = new ArrayList<Account>();
        try {
            accountList = sqlSession.selectList("account.queryAllAccByUserIdYear", hashMap);
            if (accountList != null && accountList.size() > 0) {
                return accountList;
            }
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return accountList;
    }

    @Override
    public List<String> queryStartStaPer(Map<String, Object> hashMap) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<String> list = sqlSession.selectList("account.queryStartStaPer", hashMap);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<String> queryAidByCondition(Map<String, Object> hashMap) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<String> list = sqlSession.selectList("account.queryAidByCondition", hashMap);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<Account> queryAccByUserID(String userID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Account> list = sqlSession.selectList("account.queryAccByUserID", userID);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<Map<String, Object>> queryAdminFirstData(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<Map<String, Object>> list = sqlSession.selectList("account.queryAdminFirstData", param);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryXzCount(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.queryXzCount", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryHj(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.queryHj", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryXzHj(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.queryXzHj", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryAdminXzHj(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.queryAdminXzHj", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryTy(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.queryTy", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    @Override
    public Object queryHjTy(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = sqlSession.selectOne("account.queryHjTy", param);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return null;
    }

    /**
     * @param account
     * @return
     * @throws BusinessException
     * @Title: mappingStates
     * @Description: 更改映射状态出错
     * @see com.wqb.dao.account.AccountDao#mappingStates(com.wqb.model.Account)
     */
    @Override
    public Integer mappingStates(Account account) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Integer count = sqlSession.update("account.mappingStates", account);
            return count;
        } catch (Exception e) {
            logger.error("AccountDaoImpl【mappingStates】更改映射状态出错!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int updateMappingStates() {
        SqlSession sqlSesion = null;
        int updateMappingStates = 0;
        try {
            sqlSesion = sqlSessionFactory.openSession();
            updateMappingStates = sqlSesion.update("account.updateMappingStates");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AccountDaoImpl【updateMappingStates】更改映射状态出错!", e);
        } finally {
            sqlSesion.close();
        }
        return updateMappingStates;
    }

}

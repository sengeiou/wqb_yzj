package com.wqb.dao.app.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.app.AccountAppDao;
import com.wqb.model.Account;
import com.wqb.model.Test01;
import com.wqb.model.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service("accountAppDao")
public class AccountAppDaoImpl implements AccountAppDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(AccountAppDaoImpl.class);

    //查询用户名下一个账套信息
    @Override
    public Account queryAccByUserID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Account account = null;
        try {
            account = sqlSession.selectOne("appAccount.queryAccByUserID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return account;
    }

    //查询公司所有的账套
    @Override
    public List<Account> queryAllAccByUserIDAndComName(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = new ArrayList<Account>();
        try {
            list = sqlSession.selectList("appAccount.queryAllAccByUserIDAndComName", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    //选择公司进入账套
    @Override
    public Account queryAccByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Account account = new Account();
        try {
            account = sqlSession.selectOne("appAccount.queryAccByID", param);
            //select * from  t_basic_account where accountID = #{accountID} and statu = 1
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return account;
    }

    //根据管理员查询员工
    @Override
    public List<User> queryUserByUserID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> list = null;
        try {
            list = sqlSession.selectList("appAccount.queryUserByUserID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    //根据管理员查询员工
    @Override
    public List<String> queryUserByUserID2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            list = sqlSession.selectList("appAccount.queryUserByUserID2", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    //根据员工查询账套
    @Override
    public List<Account> queryAllByID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = null;
        try {
            list = sqlSession.selectList("appAccount.queryAllByID", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    //根据员工查询账套
    @Override
    public List<Account> queryAllByID2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Account> list = null;
        try {
            list = sqlSession.selectList("appAccount.queryAllByID2", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }

    //根据用户查询权限
    @Override
    public User queryAcc(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = null;
        try {
            user = sqlSession.selectOne("appAccount.queryAcc", param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return user;
    }

    @Override
    public void insertUser(Test01 test01) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("appAccount.insertUser", test01);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

}

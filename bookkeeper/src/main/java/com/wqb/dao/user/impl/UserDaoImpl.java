package com.wqb.dao.user.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.user.UserDao;
import com.wqb.model.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userDao")
public class UserDaoImpl implements UserDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(UserDaoImpl.class);

    /**
     * 用户登录
     */
    public User login(String userName, String password) throws BusinessException {
        SqlSession sqlsession = null;
        try {
            sqlsession = sqlSessionFactory.openSession();
            Map<String, String> map = new HashMap<String, String>();
            map.put("userName", userName);
            map.put("password", password);
            List<User> list = sqlsession.selectList("user.loginSystem", map);
            // select * from t_sys_user where loginUser=#{userName 登录用户名} and
            // pasword =#{password} and state=1;
            if (null != list && list.size() > 0) {
                return list.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("UserDaoImpl【login】根据用户名和密码查询数据库异常!", e);
            return null;
        } finally {
            sqlsession.close();
        }

    }

    // 超级管理员查看自己名下管理员 管理员查看自己名下员工
    public List<User> queryUserByParentID(String userID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> userList = sqlSession.selectList("user.queryUserByParentID", userID);
            return userList;
        } catch (Exception e) {
            logger.error("UserDaoImpl【queryUserByParentID】超级管理员查看自己名下管理员   管理员查看自己名下员工!", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public User queryUserByLineID(String id) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> userList = sqlSession.selectList("user.queryUserByLineID", id);
            if (userList != null && userList.size() == 1) {
                return userList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("UserDaoImpl【queryUserByLineID】根据平台ID获取用户信息异常", e);
            return null;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int addUser(User user) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int insert = sqlSession.insert("user.addUser", user);
            return insert;
        } catch (Exception e) {
            logger.error("UserDaoImpl【addUser】添加用户异常", e);
            throw new BusinessException("addUser error");
        } finally {
            sqlSession.close();
        }
    }

    @Override
    // 修改试用户转正状态
    public int upUserByUid(User user) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("user.upUserByUid", user);
            return update;
        } catch (Exception e) {
            logger.error("UserDaoImpl【upUserByUid】修改试用户转正状态异常", e);
            throw new BusinessException("upUserByUid error");
        } finally {
            sqlSession.close();
        }
    }

    // 根据id查询用户
    @Override
    public User queryUserById(String userID) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            List<User> userList = sqlSession.selectList("user.queryUserById", userID);

            if (userList != null && userList.size() >= 1) {
                return userList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<User> queryUserByID(String userID) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            List<User> list = sqlSession.selectList("user.queryUserByID", userID);
            return list;
        } catch (Exception e) {
            return null;
        } finally {
            sqlSession.close();
        }
    }

    // 查询用户名
    @Override
    public String queryUserName(String uid) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            String uanme = sqlSession.selectOne("user.queryUserName", uid);
            return uanme;
        } catch (Exception e) {
            logger.error("UserDaoImpl【queryUserName】查询用户名异常", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<User> queryDzAdmin(String userType) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            List<User> list = sqlSession.selectList("user.queryUserName", userType);
            return list;
        } catch (Exception e) {
            return null;
        } finally {
            sqlSession.close();
        }
    }

    // 根据用户id 批量查询用户信息
    @Override
    public List<User> queryUserBath(List<String> list) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> userList = sqlSession.selectList("user.queryUserBath", list);
            if (userList != null & userList.size() > 0) {
                return userList;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("UserDaoImpl【queryUserBath】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<User> queryAllUser() throws BusinessException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> userList = sqlSession.selectList("user.queryAllUser");
            if (userList != null & userList.size() > 0) {
                return userList;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("UserDaoImpl【queryUserBath】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updCsID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("user.updCsID", param);
        } catch (Exception e) {
            logger.error("UserDaoImpl【updCsID】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updCsMobile(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("user.updCsMobile", param);
        } catch (Exception e) {
            logger.error("UserDaoImpl【updCsMobile】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public void updSessionIDbyID(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.update("user.updSessionIDbyID", param);
        } catch (Exception e) {
            logger.error("UserDaoImpl【updSessionIDbyID】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public List<User> queryUserByCondition(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            List<User> userList = sqlSession.selectList("user.queryUserByCondition", param);
            if (userList != null & userList.size() > 0) {
                return userList;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("UserDaoImpl【queryUserByCondition】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public int upUserByUid2(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("user.upUserByUid2", param);
            return update;
        } catch (Exception e) {
            logger.error("UserDaoImpl【upUserByUid2】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }

    //
    @Override
    public int updUserByLoginUser(Map<String, Object> param) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            int update = sqlSession.update("user.updUserByLoginUser", param);
            return update;
        } catch (Exception e) {
            logger.error("UserDaoImpl【updUserByLoginUser】", e);
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }

    }
}

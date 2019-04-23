package com.wqb.dao.permission.dao.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.permission.dao.PermissionDao;
import com.wqb.model.Permission;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service("permissionDao")
public class PermissionDaoImpl implements PermissionDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;
    private static Log4jLogger logger = Log4jLogger.getLogger(PermissionDaoImpl.class);

    //查询权限
    public List<Permission> queryPreByUserID(String userID) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Permission> list = null;
        try {
            list = sqlSession.selectList("perm.queryPreByUserID", userID);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return list;
    }
}

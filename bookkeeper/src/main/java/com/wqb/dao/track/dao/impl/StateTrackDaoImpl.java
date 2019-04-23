package com.wqb.dao.track.dao.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.track.dao.StateTrackDao;
import com.wqb.model.StateTrack;
import com.wqb.model.SubjectMessage;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service("stateTrack")
public class StateTrackDaoImpl implements StateTrackDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Log4jLogger logger = Log4jLogger.getLogger(StateTrackDaoImpl.class);

    @Override
    public StateTrack queryState(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            List<StateTrack> list = sqlSession.selectList("stateTrack.queryState", map);

            if (list != null && list.size() > 0) {
                return list.get(0);
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
    public void insertStateTrack(StateTrack stateTrack) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.insert("stateTrack.insertStateTrack", stateTrack);

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            sqlSession.close();
        }

    }

    @Override
    public int upStateByCondition(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.update("stateTrack.upStateByCondition", map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } finally {
            sqlSession.close();
        }
        return num;
    }

    // 查询所有科目
    @Override
    public List<SubjectMessage> querySubTrack(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            List<SubjectMessage> list = sqlSession.selectList("stateTrack.querySubTrack", map);

            if (list != null && list.size() > 0) {
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            sqlSession.close();
        }
    }

    // 更新科目
    @Override
    public int upSubTrack(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int num = -1;
        try {
            num = sqlSession.update("stateTrack.upSubTrack", map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return num;
    }

}

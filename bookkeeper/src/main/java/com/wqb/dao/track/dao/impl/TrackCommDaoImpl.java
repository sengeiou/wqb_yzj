package com.wqb.dao.track.dao.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.track.dao.TrackCommDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
@Service("trackCommDao")
public class TrackCommDaoImpl implements TrackCommDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Log4jLogger logger = Log4jLogger.getLogger(TrackCommDaoImpl.class);

    @Override
    public void insertTrackComm(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            sqlSession.insert("trackComm.insertTrackComm", map);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            sqlSession.close();
        }
    }

}

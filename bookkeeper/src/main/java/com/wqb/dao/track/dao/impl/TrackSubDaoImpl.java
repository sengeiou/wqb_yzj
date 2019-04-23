package com.wqb.dao.track.dao.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.dao.track.dao.TrackSubDao;
import com.wqb.model.TrackSub;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
@Service("trackSubDao")
public class TrackSubDaoImpl implements TrackSubDao {
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    private static Log4jLogger logger = Log4jLogger.getLogger(TrackSubDaoImpl.class);


    @Override
    public void insertTrackSub(TrackSub trackSub) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            sqlSession.insert("trackSub.insertTrackSub", trackSub);
        } finally {
            sqlSession.close();
        }

    }


    @Override
    public int upTrackSub(Map<String, Object> map) throws BusinessException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //update track_sub set period=#{period} where  tr_sub_id=#{tr_sub_id};
        int num = -1;
        try {
            num = sqlSession.update("trackSub.upTrackSub", map);
            int a = 0;
            int b = 1;
            int c = b / a;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(1);
            logger.error("TrackSubDao【upTrackSub】 用户切换账套异常!", e);
            throw new BusinessException(e);
        } finally {
            System.out.println(1);
            sqlSession.close();
            System.out.println(22);

        }
        return num;
    }


}

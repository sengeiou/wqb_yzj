package com.wqb.dao.track.dao;

import com.wqb.common.BusinessException;
import com.wqb.model.TrackSub;

import java.util.Map;

public interface TrackSubDao {
    //添加
    void insertTrackSub(TrackSub trackSub) throws BusinessException;


    int upTrackSub(Map<String, Object> map) throws BusinessException;

}

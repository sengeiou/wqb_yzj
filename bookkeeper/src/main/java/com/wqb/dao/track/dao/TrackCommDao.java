package com.wqb.dao.track.dao;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface TrackCommDao {
    //添加
    void insertTrackComm(Map<String, Object> map) throws BusinessException;

}

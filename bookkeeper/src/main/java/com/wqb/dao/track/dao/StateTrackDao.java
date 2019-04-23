package com.wqb.dao.track.dao;

import com.wqb.common.BusinessException;
import com.wqb.model.StateTrack;
import com.wqb.model.SubjectMessage;

import java.util.List;
import java.util.Map;

public interface StateTrackDao {


    StateTrack queryState(Map<String, Object> map) throws BusinessException;

    void insertStateTrack(StateTrack stateTrack) throws BusinessException;

    int upStateByCondition(Map<String, Object> map) throws BusinessException;

    List<SubjectMessage> querySubTrack(Map<String, Object> map) throws BusinessException;

    int upSubTrack(Map<String, Object> map) throws BusinessException;


}

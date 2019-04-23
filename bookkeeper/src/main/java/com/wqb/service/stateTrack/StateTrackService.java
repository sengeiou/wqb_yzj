package com.wqb.service.stateTrack;

import com.wqb.common.BusinessException;
import com.wqb.model.StateTrack;

import java.util.Map;

public interface StateTrackService {

    StateTrack queryState(Map<String, Object> map) throws BusinessException;


    int upStateByCondition(Map<String, Object> map) throws BusinessException;

    public StateTrack queryState2(Map<String, Object> map) throws BusinessException;

    public Map<String, Object> upkuchu(Map<String, Object> map) throws BusinessException;

    public int upStateSub(Map<String, Object> map) throws BusinessException;

}

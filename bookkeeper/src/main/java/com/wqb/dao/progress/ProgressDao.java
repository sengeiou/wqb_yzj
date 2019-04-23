package com.wqb.dao.progress;

import com.wqb.common.BusinessException;
import com.wqb.model.Progress;

import java.util.List;
import java.util.Map;

public interface ProgressDao {
    List<Progress> queryProgress(Map<String, Object> map) throws BusinessException;

    Integer addProgress(Progress pro) throws BusinessException;

    void chgProgress(Map<String, Object> param) throws BusinessException;
}

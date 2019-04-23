package com.wqb.dao.index;

import com.wqb.common.BusinessException;
import com.wqb.model.IndexEntity;

import java.util.List;
import java.util.Map;

public interface IndexDao {
    List<IndexEntity> queryIndexInfo(Map<String, Object> param) throws BusinessException;
}

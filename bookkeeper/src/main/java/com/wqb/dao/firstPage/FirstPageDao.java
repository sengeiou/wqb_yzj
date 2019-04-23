package com.wqb.dao.firstPage;

import com.wqb.common.BusinessException;
import com.wqb.model.FirstPageEntity;

import java.util.List;
import java.util.Map;

public interface FirstPageDao {
    List<FirstPageEntity> queryFirstPage(Map<String, Object> param) throws BusinessException;
}

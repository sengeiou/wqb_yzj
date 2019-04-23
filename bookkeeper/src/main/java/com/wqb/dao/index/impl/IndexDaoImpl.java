package com.wqb.dao.index.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.index.IndexDao;
import com.wqb.model.IndexEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("indexDao")
public class IndexDaoImpl implements IndexDao {

    @Override
    public List<IndexEntity> queryIndexInfo(Map<String, Object> param) throws BusinessException {

        return null;
    }

}

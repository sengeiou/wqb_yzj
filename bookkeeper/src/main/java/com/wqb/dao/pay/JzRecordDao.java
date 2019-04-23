package com.wqb.dao.pay;

import com.wqb.common.BusinessException;
import com.wqb.model.JzRecord;

import java.util.List;
import java.util.Map;

public interface JzRecordDao {
    List<JzRecord> queryJzRecord(Map<String, Object> param) throws BusinessException;

    void addJzRecord(JzRecord jzRecord) throws BusinessException;

    List<JzRecord> queryOldJz(Map<String, Object> param) throws BusinessException;
}

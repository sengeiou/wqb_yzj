package com.wqb.dao.assets;

import com.wqb.common.BusinessException;
import com.wqb.model.AssetsRecord;

import java.util.List;
import java.util.Map;

public interface AssetsRecordDao {
    // 添加固定资产折旧记录
    void insertAssetsRecord(AssetsRecord assetsRecord) throws BusinessException;

    List<AssetsRecord> queryAssetsRecord(Map<String, Object> param) throws BusinessException;

    void delAssetsRecord(Map<String, Object> param) throws BusinessException;
}

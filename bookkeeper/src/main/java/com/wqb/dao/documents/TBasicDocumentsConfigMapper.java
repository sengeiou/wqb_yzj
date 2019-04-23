package com.wqb.dao.documents;

import com.wqb.model.TBasicDocumentsConfig;
import com.wqb.model.TBasicSubjectParent;

public interface TBasicDocumentsConfigMapper {
    int deleteByPrimaryKey(String pkDocumentsConfigId);

    int insert(TBasicDocumentsConfig record);

    int insertSelective(TBasicDocumentsConfig record);

    TBasicDocumentsConfig selectByPrimaryKey(String pkDocumentsConfigId);

    int updateByPrimaryKeySelective(TBasicDocumentsConfig record);

    int updateByPrimaryKey(TBasicDocumentsConfig record);

    void insertDocumentsConfig(TBasicSubjectParent subParent);
}

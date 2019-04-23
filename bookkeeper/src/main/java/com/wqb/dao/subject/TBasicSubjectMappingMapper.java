package com.wqb.dao.subject;

import com.wqb.model.TBasicMeasure;
import com.wqb.model.TBasicSubjectMapping;

import java.util.List;

public interface TBasicSubjectMappingMapper {

    int uploadSubMappingList(List<TBasicSubjectMapping> tBasicSubjectMappingList);

    int deleteByAccountId(TBasicMeasure tBasicMeasure);

    int deleteByPrimaryKey(Integer pkSubMappingId);

    int insert(TBasicSubjectMapping record);

    int insertSelective(TBasicSubjectMapping record);

    TBasicSubjectMapping selectByPrimaryKey(Integer pkSubMappingId);

    int updateByPrimaryKeySelective(TBasicSubjectMapping record);

    int updateByPrimaryKey(TBasicSubjectMapping record);

    int deleteAll();

    List<TBasicSubjectMapping> querySubMappingList(Integer accountType);

    int deleteMeasureList(List<TBasicSubjectMapping> tBasicSubMappingList);
}

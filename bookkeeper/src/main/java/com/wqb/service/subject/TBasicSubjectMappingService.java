package com.wqb.service.subject;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectMapping;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface TBasicSubjectMappingService {

    Map<String, Object> uploadSubMapping(List<Map<String, Object>> list, HttpSession session, File file) throws BusinessException;

    Map<String, Object> deleteByAccountId(HttpSession session) throws BusinessException;

    int deleteByPrimaryKey(Integer pkSubMappingId);

    Map<String, Object> insert(TBasicSubjectMapping record);

    int insertSelective(TBasicSubjectMapping record);

    TBasicSubjectMapping selectByPrimaryKey(Integer pkSubMappingId);

    int updateByPrimaryKeySelective(TBasicSubjectMapping record);

    int updateByPrimaryKey(TBasicSubjectMapping record);

    int deleteAll();

    Map<String, Object> querySubMappingList(HttpSession session);

    Map<String, Object> deleteSubMappingList(List<TBasicSubjectMapping> tBasicSubMappingList, HttpSession session) throws BusinessException;
}

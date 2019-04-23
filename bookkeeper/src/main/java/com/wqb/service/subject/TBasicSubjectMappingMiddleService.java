package com.wqb.service.subject;

import com.wqb.model.TBasicSubjectMappingMiddle;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface TBasicSubjectMappingMiddleService {
    int deleteByPrimaryKey(Integer pkSubMappingMiddleId);

    int insert(TBasicSubjectMappingMiddle record);

    int insertSelective(TBasicSubjectMappingMiddle record);

    TBasicSubjectMappingMiddle selectByPrimaryKey(Integer pkSubMappingMiddleId);

    int updateByPrimaryKeySelective(TBasicSubjectMappingMiddle record);

    int updateByPrimaryKey(TBasicSubjectMappingMiddle record);

    Map<String, Object> subAutoMapping(HttpSession session);

    List<TBasicSubjectMappingMiddle> querySubMappingMiddleByAccId(String accountId);

    int insertList(List<TBasicSubjectMappingMiddle> subMappingMiddleList);

    int saveOrUpdate(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle);

    TBasicSubjectMappingMiddle querySubMappingMiddle(Map<String, String> subMappingMap);

    /**
     * @param tBasicSubjectMappingMiddle
     * @return List<TBasicSubjectMappingMiddle>    返回类型
     * @Title: validationSubMappingMiddle
     * @Description: 效验科目映射表
     * @date 2018年11月21日  上午11:56:20
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMappingMiddle> validationSubMappingMiddle(TBasicSubjectMappingMiddle tBasicSubjectMappingMiddle);

    /**
     * 根据 账套id 删除映射信息表
     *
     * @param session
     * @return
     */
    int deleteByAccountId(HttpSession session);
}

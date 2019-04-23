package com.wqb.dao.subject;

import com.wqb.common.BusinessException;
import com.wqb.model.SubExcel;
import com.wqb.model.Subject;

import java.util.List;
import java.util.Map;

public interface SubjectDao {
    Integer udpSubDAmt(Map<String, Object> param) throws BusinessException;

    Integer udpSubCAmt(Map<String, Object> param) throws BusinessException;

    // 获取凭证下拉科目
    List<Subject> queryVouSubject(Map<String, Object> param) throws BusinessException;

    // 获取指定用户名下所有科目信息
    List<Subject> querySysSub(String userID, String accountID) throws BusinessException;

    // 获取导入的EXCEl所有科目
    List<SubExcel> queryExcelSub(String userID, String accountID) throws BusinessException;

    //生成凭证用到
    List<Subject> querySubNumber(Map<String, Object> param) throws BusinessException;

    List<Subject> selectMax7Sub(Map<String, Object> param) throws BusinessException;
}

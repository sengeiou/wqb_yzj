package com.wqb.dao.tempType;

import com.wqb.common.BusinessException;
import com.wqb.model.SubjectMessage;
import com.wqb.model.TempType;
import com.wqb.model.vomodel.TempVo;

import java.util.List;
import java.util.Map;

public interface TempTypeDao {


    Integer insertTempType(TempType tmp) throws BusinessException;

    List<TempType> queryTemp(Map<String, Object> map) throws BusinessException;

    int delTem(Map<String, Object> map) throws BusinessException;

    int upTem(Map<String, Object> map) throws BusinessException;

    int queryTempCount(Map<String, Object> map) throws BusinessException;

    Map<String, String> queryGroupSub(Map<String, Object> map) throws BusinessException;

    List<SubjectMessage> queryAllSub(Map<String, Object> map) throws BusinessException;


    int querySubCount(Map<String, Object> map) throws BusinessException;

    int queryCountVB(Map<String, Object> map) throws BusinessException;

    List<TempVo> querySalaryVB(Map<String, Object> map) throws BusinessException;

    List<TempVo> queryGoodsVB(Map<String, Object> map) throws BusinessException;

}

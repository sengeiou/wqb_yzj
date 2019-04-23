package com.wqb.service.tempType;

import com.wqb.common.BusinessException;
import com.wqb.model.TempType;

import java.util.List;
import java.util.Map;

public interface TempTypeService {

    //添加模板
    Integer insertTempType(String aid, String tempName) throws BusinessException;

    List<TempType> queryTemp(Map<String, Object> map) throws BusinessException;

    Map<String, Object> delTem(String tmid) throws BusinessException;

    Map<String, Object> upTem(Integer tempID, String tempName) throws BusinessException;

    Map<String, Object> saveTempVoucher(String accountID, String tempName, String assistName, String pid, String saveAmount, String content) throws BusinessException;


    void generateTemplate(String accountID) throws BusinessException;

    void bathGenerateTemplate() throws BusinessException;
}

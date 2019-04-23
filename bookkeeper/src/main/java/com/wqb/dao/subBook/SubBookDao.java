package com.wqb.dao.subBook;

import com.wqb.common.BusinessException;
import com.wqb.model.StatusPeriod;
import com.wqb.model.SubBook;
import com.wqb.model.vomodel.RedisSub;
import com.wqb.model.vomodel.SubBookMessageVo;
import com.wqb.model.vomodel.SubMessageVo;

import java.util.List;
import java.util.Map;

public interface SubBookDao {

    int insertSubBookBath(List<SubBook> list) throws BusinessException;

    List<SubBook> querySubBook(Map<String, Object> param) throws BusinessException;


    List<SubMessageVo> queryLedger(Map<String, Object> param) throws BusinessException;


    List<SubMessageVo> querySubToDetailAcc(Map<String, Object> param) throws BusinessException;


    int querySubBookCount(Map<String, Object> param) throws BusinessException;


    List<SubBook> queryOneMaxPeriod(Map<String, Object> param) throws BusinessException;


    int delSubBook(Map<String, Object> param) throws BusinessException;

    int delSubBookBath(Map<String, Object> param) throws BusinessException;

    int delSubBookBath2(Map<String, Object> param) throws BusinessException;

    List<StatusPeriod> getRangePeiod(Map<String, Object> param) throws BusinessException;

    List<SubMessageVo> querySubAll(Map<String, Object> param) throws BusinessException;

    SubMessageVo queryDetailSub2(Map<String, Object> param) throws BusinessException;

    int upSubvoucherNo(Map<String, Object> param) throws BusinessException;

    //更新明细账
    int upSubBlanceAmount(Map<String, Object> param) throws BusinessException;

    List<SubBook> queryByVouchID(Map<String, Object> param) throws BusinessException;


    int delSubBook2(Map<Object, Object> reMap1) throws BusinessException;

    List<SubBook> queryByCodes(Map<String, Object> param) throws BusinessException;


    int upSubBlanceAmount2(Map<String, Object> param) throws BusinessException;

    List<SubBook> queryByConditions(Map<String, Object> param) throws BusinessException;

    List<SubBookMessageVo> queryAllSubBook(Map<String, Object> map) throws BusinessException;

    List<RedisSub> queryDetailSubMessage(Map<String, Object> map) throws BusinessException;

}

package com.wqb.service.measure;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicMeasure;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicMeasureService
 * @Description: 数量单位业务层接口
 * @date 2017年12月20日 上午10:02:02
 */
public interface TBasicMeasureService {
    Map<String, Object> deleteByPrimaryKey(Map<String, Object> param, HttpSession session) throws BusinessException;

    Map<String, Object> queryMeasure(HttpSession session) throws BusinessException;

    int insertMeasure(TBasicMeasure tBasicMeasure, HttpSession session) throws BusinessException;

    Map<String, Object> updateMessage(HttpSession session, TBasicMeasure tBasicMeasure) throws BusinessException;

    Map<String, Object> queryMeasureBySymbolOrName(HttpSession session, String symbolOrName) throws BusinessException;
}

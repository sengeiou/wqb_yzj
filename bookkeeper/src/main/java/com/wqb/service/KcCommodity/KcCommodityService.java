package com.wqb.service.KcCommodity;

import com.wqb.common.BusinessException;
import com.wqb.model.KcCommodity;
import com.wqb.model.Page;
import com.wqb.model.TBasicSubjectMessage;

import java.util.List;
import java.util.Map;

public interface KcCommodityService {
    //添加库存商品
    Map<String, Object> insertCommodity(List<Map<String, Object>> list, Map<String, Object> map) throws BusinessException;

    public Page<KcCommodity> queryCommodityList(Map<String, Object> param) throws BusinessException;

    void updateKccommddityByAddSubMessage(TBasicSubjectMessage subject, String type) throws BusinessException;


}

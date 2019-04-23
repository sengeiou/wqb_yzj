package com.wqb.dao.invoice;

import com.wqb.common.BusinessException;
import com.wqb.model.InvoiceMappingrecord;

import java.util.Map;

public interface InvoiceMappingDao {

    public int insertMappingrecord(InvoiceMappingrecord mapping) throws BusinessException;

    public InvoiceMappingrecord queryMappingrecord(Map<String, Object> param) throws BusinessException;

    public int updateMappingrecord(Map<String, Object> param) throws BusinessException;


}

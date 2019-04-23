package com.wqb.service.businessTax;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface BusinessTaxService {
    void queryBusinessTax(Map<String, Object> map) throws BusinessException;

}

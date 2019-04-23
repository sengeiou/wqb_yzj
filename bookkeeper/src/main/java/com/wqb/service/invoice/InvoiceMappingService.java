package com.wqb.service.invoice;

import com.wqb.common.BusinessException;
import com.wqb.model.InvoiceMapping;

import java.util.List;

public interface InvoiceMappingService {
    public void insertBath(List<InvoiceMapping> list) throws BusinessException;

}

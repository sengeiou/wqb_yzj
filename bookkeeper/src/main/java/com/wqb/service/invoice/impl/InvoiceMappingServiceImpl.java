package com.wqb.service.invoice.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.invoice.InvoiceMappingDao;
import com.wqb.model.InvoiceMapping;
import com.wqb.service.invoice.InvoiceMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("invoiceMappingService")
public class InvoiceMappingServiceImpl implements InvoiceMappingService {
    @Autowired
    InvoiceMappingDao invoiceMappingDao;

    @Override
    public void insertBath(List<InvoiceMapping> list) throws BusinessException {
        System.out.println(1);

        //invoiceMappingDao.insertBath(list);
        System.out.println(222);

    }


}

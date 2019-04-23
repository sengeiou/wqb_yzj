package com.wqb.service.voucher.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.voucher.dao.VoucherBodyDao;
import com.wqb.model.VoucherBody;
import com.wqb.service.voucher.VoucherBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("voucherBodyService")
public class VoucherBodyServiceImpl implements VoucherBodyService {
    @Autowired
    VoucherBodyDao voucherBodyDao;

    @Override
    public Integer insertVouchBody(VoucherBody voucherBody) throws BusinessException {
        return voucherBodyDao.insertVouchBody(voucherBody);
    }
}

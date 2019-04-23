package com.wqb.dao.bill;

import com.wqb.common.BusinessException;

public interface BankBillDao {
    void updBillVouByVouID(String vouchID) throws BusinessException;
}

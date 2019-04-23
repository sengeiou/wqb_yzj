package com.wqb.dao.attached;

import com.wqb.common.BusinessException;
import com.wqb.model.VoucherHead;

public interface AttachedDao {


    //根据id查询凭证头
    VoucherHead queryVoucherHeadById(String id) throws BusinessException;


}

package com.wqb.service.voucher;

import com.wqb.common.BusinessException;
import com.wqb.model.VoucherBody;

public interface VoucherBodyService {
    // 插入凭证体
    Integer insertVouchBody(VoucherBody voucherBody) throws BusinessException;
}

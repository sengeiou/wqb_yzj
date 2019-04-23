package com.wqb.mappers.voucher;

import com.wqb.domains.voucher.VoucherHeader;
import com.wqb.mappers.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 凭证主表 Mapper 接口
 * </p>
 *
 * @author Shoven
 * @since 2019-03-16
 */
public interface VoucherHeaderMapper extends BaseMapper<VoucherHeader> {

    int getMaxVoucherNo(@Param("accountId") String accountId, @Param("period") String period);
}

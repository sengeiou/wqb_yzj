package com.wqb.services.subject;

import com.wqb.domains.subject.SubjectBook;
import com.wqb.domains.voucher.Voucher;
import com.wqb.services.base.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Shoven
 * @since 2019-03-25
 */
public interface SubjectBookService extends BaseService<SubjectBook> {

    /**
     * 获取当前账套小于指定凭证号的所有科目明细
     *
     * @param voucher
     * @return
     */
    List<SubjectBook> selectThePreviousListByVoucherNo(Voucher voucher);

    /**
     * 通过凭证获取科目余额明细
     *
     * @param voucherIds
     * @return
     */
    List<SubjectBook> selectListByVoucherIds(List<String> voucherIds);

    /**
     * 为创建凭证变更科目明细（创建）
     *
     * @param voucher
     */
    void changeSubjectBookForCreateVoucher(Voucher voucher);

    /**
     * 为修改凭证变更科目明细（修改）
     *
     * @param oldVoucher
     * @param newVoucher
     */
    void changeSubjectBookForUpdateVoucher(Voucher oldVoucher, Voucher newVoucher);

    /**
     * 为删除凭证变更科目明细（修改、删除）
     *
     * @param oldVoucher
     * @param newVoucher
     */
    void changeSubjectBookForDeleteVoucher(Voucher oldVoucher, Voucher newVoucher);
}

package com.wqb.dao.vat;

import com.wqb.common.BusinessException;
import com.wqb.model.*;
import com.wqb.model.vomodel.RedisSub;

import java.util.List;
import java.util.Map;

//增值税结转
public interface VatDao {
    //进项
    InvoiceBody queryTax(Map<String, Object> map) throws BusinessException;

    //查询二级科目应交增值税是否存在
    SubjectMessage querySubjectVatTwo(Map<String, Object> map) throws BusinessException;

    //查询科目
    List<SubjectMessage> querySubjectVatThree(Map<String, Object> map) throws BusinessException;

    //添加一条新科目 (应交增值税)
    int insertSubject(SubjectMessage sub) throws BusinessException;

    //更新增值税留底金额
    void updateSubject(SubjectMessage sub) throws BusinessException;

    //更新增值税留底金额
    int updateSubjectMap(Map<String, Object> map) throws BusinessException;


    //根据主键查询科目
    SubjectMessage queryByID(String id) throws BusinessException;

    //添加凭证子表
    void insertVoBody(VoucherBody voBody) throws BusinessException;

    //添加凭证主表
    void insertVoHead(VoucherHead voHead) throws BusinessException;

    //期末结账记录表
    void isnertStatusPeriod(StatusPeriod sta) throws BusinessException;

    //查询是否期末结转
    StatusPeriod queryVatSta(StatusPeriod sta) throws BusinessException;

    //凭证子表查询
    List<VoucherBody> queryVoBody(Map<String, Object> map) throws BusinessException;

    //查询凭证头
    VoucherHead queryVoHeahder(Map<String, Object> map) throws BusinessException;

    //根据ID查询凭证头
    VoucherHead queryVoHeahderById(String id) throws BusinessException;

    //根据ID查询凭证子表
    VoucherBody queryVoBodyById(String id) throws BusinessException;

    //查询经销项
    List<InvoiceBody> queryAllInvoce(Map<String, Object> map) throws BusinessException;

    //按照code查询科目
    SubjectMessage querySubByCode(Map<String, Object> map) throws BusinessException;

    //根据手工凭证查找是否有结转的
    VoucherHead queryVoBySource(Map<String, Object> map) throws BusinessException;

    VoucherHead queryVoBySource2(Map<String, Object> map) throws BusinessException;

    /*******测试数据1 satrt 测试完可以删除*********/

    List<User> getAllUser() throws BusinessException;

    List<Account> getAccByUid(Map<String, Object> map) throws BusinessException;

    void upAccSource(Map<String, Object> map) throws BusinessException;

    /*****测试数据1 end *******/

    List<SubjectMessage> querySubByVo(Map<String, Object> map) throws BusinessException;

    //更新科目
    int upSubVo(Map<String, Object> map) throws BusinessException;

    //一键检查凭证更新
    int upVouch(Map<String, Object> map) throws BusinessException;

    int upVouchBody(Map<String, Object> map) throws BusinessException;

    public SubjectMessage querySubById(String subID) throws BusinessException;

    int updateCommBySub(Map<String, Object> map) throws BusinessException;

    //更新库存
    int chgSubByVo(Map<String, Object> map) throws BusinessException;

    //更新科目
    int upSubByVo(Map<String, Object> map) throws BusinessException;

    // 1
    int chgEndBlanceDebit(List<String> list) throws BusinessException;

    //2
    List<String> getEndBlanceSub(List<String> list) throws BusinessException;

    //3
    int chgEndBlanceCredit(List<String> list) throws BusinessException;

    List<TBasicSubjectMessage> querySunYiSub(Map<String, Object> map) throws BusinessException;

    //<select id="queryAccAll"  resultType="com.wqb.model.Account">

    List<Account> queryAccAll() throws BusinessException;

    int upAccCompanyName(Map<String, Object> map) throws BusinessException;

    List<VoucherHead> queryCbjzVo2(Map<String, Object> map) throws BusinessException;

    int upVoTypeByImport(Map<String, Object> map) throws BusinessException;

    List<RedisSub> queryRedisSub(Map<String, Object> map) throws BusinessException;

    List<RedisSub> queryRedisSubByCondition(Map<String, Object> map) throws BusinessException;

    TBasicSubjectMessage queryTBasicSubjectMessageById(String id) throws BusinessException;

    int chgSubAmountByDelete2(Map<String, Object> map) throws BusinessException;

    List<SubjectMessage> querySubjectMessageByPkid(Map<String, Object> map) throws BusinessException;

    int chgSubAmountByDelete3(Map<String, Object> map) throws BusinessException;

    List<TBasicSubjectMessage> queryTBasicSubjectMessageById2(Map<String, Object> map) throws BusinessException;

    List<VoucherHead> queryNextOrPreviousVb(Map<String, Object> map) throws BusinessException;

    int queryCountSubjectMessage(Map<String, Object> map) throws BusinessException;


    List<SubjectMessage> queryProfit(Map<String, Object> map) throws BusinessException;

    int queryProfitCount(Map<String, Object> map) throws BusinessException;


}

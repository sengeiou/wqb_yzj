package com.wqb.dao.subject;

import com.wqb.common.BusinessException;
import com.wqb.model.SubjectMessage;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.Voucher;

import java.util.List;
import java.util.Map;

public interface TBasicSubjectMessageMapper {
    /**
     * @param tBasicSubjectMessage
     * @return int 返回类型
     * @Title: addSubMessage
     * @Description: 添加系统科目
     * @date 2017年12月21日 下午3:45:43
     * @author SiLiuDong 司氏旭东
     */
    int addSubMessage(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessagelist
     * @return int 返回类型
     * @Title: tBasicSubjectMessagelist
     * @Description: 添加系统科目集合
     * @date 2017年12月21日 下午3:46:56
     * @author SiLiuDong 司氏旭东
     */
    int addSubMessageList(List<TBasicSubjectMessage> tBasicSubjectMessagelist);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessage
     * @Description: 查询系统中该账套的全部科目
     * @date 2017年12月21日 下午3:53:03
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessage(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageBySubCode
     * @Description: 根据科目编码查询系统中的科目
     * @date 2017年12月21日 下午3:53:50
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageBySubCode(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageBySubName
     * @Description: 根据科目名称查询系统中的科目
     * @date 2017年12月21日 下午3:54:56
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageBySubName(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: updateMessage
     * @Description: 更新系统中的科目
     * @date 2017年12月21日 下午3:55:47
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> updateMessage(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return int 返回类型
     * @Title: deleteMessageByPrimaryKey
     * @Description: 根据主键删除系统中的科目
     * @date 2018年1月6日 下午12:37:46
     * @author SiLiuDong 司氏旭东
     */
    int deleteMessageByPrimaryKey(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: deleteMessageAll
     * @Description: 删除账套在系统中全部科目
     * @date 2017年12月21日 下午3:57:19
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> deleteMessageAll(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: deleteMessageByAcctperiod
     * @Description: 根据帐套id删除系统本期间中的科目
     * @date 2018年7月7日 下午6:11:39
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> deleteMessageByAcctperiod(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * 变更科目金额(适用于生成凭证操作)
     *
     * @param param
     * @param voucher 凭证
     */
    void chgSubAmountByCreate(Map<String, Object> param, Voucher voucher) throws BusinessException;

    // 变更科目金额(适用于删除凭证操作)
    void chgSubAmountByDelete(Map<String, Object> param, Voucher voucher) throws BusinessException;

    /**
     * @param tBasicSubjectMessage
     * @return String 返回类型
     * @Title: querySubMessageMaxSubCode
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月12日 下午2:02:05
     * @author SiLiuDong 司氏旭东
     */
    String querySubMessageMaxSubCode(TBasicSubjectMessage tBasicSubjectMessage);

    List<TBasicSubjectMessage> querySubByIDAndName(Map<String, Object> param) throws BusinessException;

    List<TBasicSubjectMessage> queryAllSubject(Map<String, Object> param) throws BusinessException;

    /**
     * @param subMessageMap
     * @return
     * @throws BusinessException List<TBasicSubjectMessage> 返回类型
     * @Title: queryLedgerByParameters
     * @Description: 根据起始时间 和结束时间 和科目代码或名称查询科目
     * @date 2018年1月18日 下午2:24:38
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> queryLedgerByParameters(Map<String, String> subMessageMap) throws BusinessException;

    List<TBasicSubjectMessage> querySubjectByName(Map<String, Object> param) throws BusinessException;

    /**
     * @param map
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageByCategory
     * @Description: 根据科目名称或代码 和 科目类别查询科目
     * @date 2018年1月23日 上午10:02:30
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageByCategory(Map<String, Object> map);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageListMoney
     * @Description: 查询真实金额
     * @date 2018年1月25日 下午7:25:04
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageListMoney(TBasicSubjectMessage tBasicSubjectMessage);

    /**
     * @param tBasicSubjectMessage
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageLevel
     * @Description: 指定查询科目级别科目编码名称不为空
     * @date 2018年1月31日 上午9:42:28
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageLevel(TBasicSubjectMessage tBasicSubjectMessage);

    int deleteMessage(TBasicSubjectMessage tBasicSubjectMessage);

    List<TBasicSubjectMessage> querySubByAccAndCode(Map<String, Object> param) throws BusinessException;

    void updQmAmount(Map<String, Object> param) throws BusinessException;

    void changeSubAmount(Map<String, Object> param) throws BusinessException;

    /**
     * @param parameters
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySbujectBalance
     * @Description: 查询科目余额表
     * @date 2018年2月5日 下午8:26:25
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySbujectBalance(Map<String, String> parameters);

    void chgSySubAmount(Map<String, Object> param) throws BusinessException;

    void updQnjlr(Map<String, Object> param) throws BusinessException;

    void updQmAmount1(Map<String, Object> param) throws BusinessException;

    void updQmAmount2(Map<String, Object> param) throws BusinessException;

    List<TBasicSubjectMessage> selectLastArch(Map<String, Object> param) throws BusinessException;

    // 结转销售成本更新对应的科目
    void updatejzcb(Map<String, Object> param) throws BusinessException;

    // 更新科目
    Map<String, Object> updateSubjest(SubjectMessage sub, Map<String, Object> map) throws BusinessException;

    List<TBasicSubjectMessage> queryFuzzySubByName(Map<String, Object> param) throws BusinessException;

    void delUnjzSub(Map<String, Object> param) throws BusinessException;

    List<TBasicSubjectMessage> selectLastArch2(Map<String, Object> param) throws BusinessException;

    void unLdAmount(Map<String, Object> param) throws BusinessException;

    /**
     * @param map
     * @return
     * @throws BusinessException int 返回类型
     * @Title: querySubjectByCode
     * @Description: 根据科目代码查询 以此科目代码开头的科目有几条
     * @date 2018年6月1日 下午4:49:21
     * @author SiLiuDong 司氏旭东
     */
    int querySubjectByCode(Map<String, Object> map) throws BusinessException;

    /**
     * @param tBasicSubjectMessage
     * @return TBasicSubjectMessage 返回类型
     * @Title: querySubMessageBySubcode
     * @Description: 根据科目编码、期间、帐套id查询科目信息
     * @date 2018年7月20日 下午3:44:16
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageBySubcode2(TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException;

    List<TBasicSubjectMessage> querySubject(Map<String, Object> param) throws BusinessException;

    List<TBasicSubjectMessage> querySysBankSubject(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return List<TBasicSubjectMessage> 返回类型
     * @Title: querySbuMessageByMapping
     * @Description: 根据科目名称、科目代码、期间查询科目信息
     * @date 2018年11月8日 下午6:20:16
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySbuMessageByMapping(Map<String, Object> param);

    /**
     * @param parameters
     * @return boolean 返回类型
     * @Title: isLastStage
     * @Description: 判断是否是最末级科目
     * @date 2018年11月22日 下午5:43:12
     * @author SiLiuDong 司氏旭东
     */
    boolean isLastStage(Map<String, Object> parameters);

    TBasicSubjectMessage querySubMessageByPkSubId(String pkSubId) throws BusinessException;

    void delSubMessageByPkSubId(String pkSubId) throws BusinessException;

    void chgSubAmountByDeleteSub(Map<String, Object> param) throws BusinessException;

    void chgSubAmountByAddSub(Map<String, Object> param) throws BusinessException;

    List<TBasicSubjectMessage> queryBankSub(Map<String, Object> param) throws BusinessException;

}

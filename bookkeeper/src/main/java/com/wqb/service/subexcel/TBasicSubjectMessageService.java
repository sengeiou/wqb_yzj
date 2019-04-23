package com.wqb.service.subexcel;

import com.wqb.common.BusinessException;
import com.wqb.model.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectMessageService
 * @Description: 系统科目业务接口
 * @date 2017年12月20日 下午10:52:18
 */
public interface TBasicSubjectMessageService {

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubParent
     * @Description: 查询系统中的科目
     * @date 2017年12月20日 下午10:43:36
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessage(User user, Account account) throws BusinessException;

    /**
     * @param TBasicSubjectMessage
     * @param HttpSession
     * @return int 返回类型
     * @Title: addSubMessage
     * @Description: 添加系统科目
     * @date 2017年12月21日 上午9:08:55
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> addSubMessage(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException;

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: addSubMessageList
     * @Description: 添加系统科目集合
     * @date 2017年12月21日 上午10:34:04
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> addSubMessageList(HttpSession session, List<TBasicSubjectMessage> tBasicSubjectMessage)
            throws BusinessException;

    /**
     * @param session
     * @param subCode
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageBySubCode
     * @Description: 根据科目编码查询系统中的科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessageBySubCode(HttpSession session, String subCode) throws BusinessException;

    /**
     * @param session
     * @param subName
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageBySubName
     * @Description: 根据科目名称查询系统中的科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessageBySubName(HttpSession session, String subName) throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @throws BusinessException
     * @Title: updateMessage
     * @Description: 更新系统中的科目
     * @date 2017年12月21日 上午10:28:30
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> updateMessage(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException;

    /**
     * @param session
     * @param pkSubId
     * @return Map<String, Object> 返回类型
     * @throws BusinessException
     * @Title: deleteMessageByPrimaryKey
     * @Description: 根据主键删除系统中的科目
     * @date 2017年12月21日 上午10:42:49
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> deleteMessageByPrimaryKey(HttpSession session, String pkSubId) throws BusinessException;

    /**
     * @param session
     * @return Map<String, Object> 返回类型
     * @throws BusinessException
     * @Title: deleteMessageAll
     * @Description: 删除账套在系统中全部科目
     * @date 2017年12月21日 上午10:44:07
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> deleteMessageAll(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: deleteMessageByAcctperiod
     * @Description: 根据帐套id删除系统本期间中的科目
     * @date 2018年7月7日 下午6:07:05
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> deleteMessageByAcctperiod(HttpSession session) throws BusinessException;

    List<TBasicSubjectMessage> querySubMessageList(HttpSession session) throws BusinessException;

    Map<String, Object> addSubMessage(HttpSession session, List<TBasicSubjectParent> tBasicSubjectParent)
            throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectExcelList2
     * @throws BusinessException void 返回类型
     * @Title: addSubMessageExcel
     * @Description: 添加系统科目集合
     * @date 2017年12月27日 下午4:57:18
     * @author SiLiuDong 司氏旭东
     */
    void addSubMessageExcel(HttpSession session, List<TBasicSubjectExcel> tBasicSubjectExcelList2)
            throws BusinessException;

    void addSubMessageExcelList(HttpSession session, List<TBasicSubjectExcel> tBasicSubjectExcelList, Map<String, String> ysMap, Map<String, TBasicSubjectParent> code2Info, boolean isSmart)
            throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @param tBasicSubjectExcelList
     * @return Map<String, Object> 返回类型
     * @Title: toKmdzAddList
     * @Description: 科目对照匹配不到手动添加
     * @date 2018年1月10日 上午10:59:43
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> toKmdzAddSubMessageList(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage,
                                                List<TBasicSubjectExcel> tBasicSubjectExcelList) throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySubMessageMaxSubCode
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月12日 下午1:58:07
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessageMaxSubCode(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException;

    void insertSubjectNextPerion(HttpSession session) throws BusinessException;

    List<TBasicSubjectMessage> querySubByIDAndName(Map<String, Object> param) throws BusinessException;

    /**
     * @param session
     * @param subCode
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySubMessageMaxSubCodeStr
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月18日 上午9:04:02
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessageMaxSubCodeStr(HttpSession session, String subCode) throws BusinessException;

    /**
     * @param parameters
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: queryLedgerByParameters
     * @Description: 根据起始时间 和结束时间 和科目代码或名称查询科目
     * @date 2018年1月18日 下午4:08:48
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> queryLedgerByParameters(User user, Account account, Map<String, String> parameters)
            throws BusinessException;

    Map<String, Object> querySubjectByName(HttpSession session, String subName) throws BusinessException;

    /**
     * @param subject  科目名称或代
     * @param category 科目类别
     * @param session
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySubMessageByCategory
     * @Description: 根据科目名称或代码 和 科目类别查询科目
     * @date 2018年1月23日 上午9:55:04
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessageByCategory(HttpSession session, String subject, String category)
            throws BusinessException;

    /**
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: addSubMessageMessageList
     * @Description: 结账时科目 本期结账到下期
     * @date 2018年1月24日 下午7:01:31
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> addSubMessageMessageList(Account account, User user,
                                                 List<TBasicSubjectMessage> tBasicSubjectMessageList) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageListMoney
     * @Description: 查询真实金额
     * @date 2018年1月25日 下午7:14:28
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySubMessageListMoney(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectMessage> 返回类型
     * @Title: querySubMessageLevel
     * @Description: 指定查询科目级别科目编码名称不为空
     * @date 2018年1月31日 上午10:00:58
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageLevel(HttpSession session) throws BusinessException;

    List<TBasicSubjectMessage> querySubMessageNullLevel(HttpSession session) throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: deleteMessage
     * @Description: 单条科目删除
     * @date 2018年1月31日 上午11:08:27
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> deleteMessage(HttpSession session, TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException;

    /**
     * @param parameters
     * @return
     * @throws BusinessException Map<String,Object> 返回类型
     * @Title: querySbujectBalance
     * @Description: 查询科目余额表
     * @date 2018年2月5日 下午3:38:18
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySbujectBalance(User user, Account account, Map<String, String> parameters)
            throws BusinessException;

    /**
     * 凭证页面，科目余额表页面导出功能；
     *
     * @return
     * @author tangsheng time:2018-08-02
     */
    Map<String, Object> querySbujectExcleExport(HttpSession session) throws BusinessException;

    Map<String, Object> querySbujectBalanceAPP(Map<String, Object> param) throws BusinessException;

    Map<String, Object> queryDetailAccountAPP(Map<String, Object> param) throws BusinessException;

    /**
     * @param tBasicSubjectMessage
     * @return
     * @throws BusinessException TBasicSubjectMessage 返回类型
     * @Title: querySubMessageBySubcode
     * @Description: 根据科目编码、期间、帐套id查询科目信息
     * @date 2018年7月20日 下午3:49:53
     * @author SiLiuDong 司氏旭东
     */
    List<TBasicSubjectMessage> querySubMessageBySubcode2(TBasicSubjectMessage tBasicSubjectMessage)
            throws BusinessException;

    List<TBasicSubjectMessage> queryAllSubject(Map<String, Object> param) throws BusinessException;


    List<TBasicSubjectMessage> querySysBankSubject(Map<String, Object> param) throws BusinessException;

    /**
     * @param param
     * @return Map<String, Object>    返回类型
     * @Title: querySbuMessageByMapping
     * @Description: 根据科目名称、科目代码、期间查询科目信息
     * @date 2018年11月8日  下午6:07:36
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> querySbuMessageByMapping(Map<String, Object> param);

    /**
     * @param parameters
     * @return boolean    返回类型
     * @Title: isLastStage
     * @Description:判断是否是最末级科目
     * @date 2018年11月22日  下午5:40:53
     * @author SiLiuDong 司氏旭东
     */
    boolean isLastStage(Map<String, Object> parameters);

    TBasicSubjectMessage querySubMessageByPkSubId(String pkSubId) throws BusinessException;

    void delSubMessageByPkSubId(String pkSubId) throws BusinessException;

    void chgSubAmountByDeleteSub(Map<String, Object> param) throws BusinessException;

    void chgSubAmountByAddSub(Map<String, Object> param) throws BusinessException;
}

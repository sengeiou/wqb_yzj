package com.wqb.service.documents;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.TBasicDocuments;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.User;
import com.wqb.service.subexcel.TBasicSubjectMessageService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface TBasicDocumentsService {

    Map<String, Object> addTicketsCost(User user, Account account, TBasicSubjectMessageService tBasicSubjectMessageService) throws BusinessException;

    /**
     * @param session
     * @param querySbujectBalance
     * @return Map<String, Object>    返回类型
     * @Title: ticketsCostRow
     * @Description: 动态获取单据-费用票列（6601，6602）
     * @date 2018年3月20日  上午9:12:41
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> ticketsCostRow(HttpSession session, Map<String, Object> querySbujectBalance) throws BusinessException;

    ;

    Map<String, Object> addTicketsCostList(HttpSession session, List<Map<String, Object>> subjectMessages) throws BusinessException;

    Map<String, Object> queryDocumentsList(HttpSession session) throws BusinessException;

    List<TBasicDocuments> querySalesDocumentsList(String accountId, String period);

    Map<String, Object> documentsToVoucher(User user, Account account) throws BusinessException;

    /**
     * @param session
     * @param document
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: deleteDocuments
     * @Description: 删除单据
     * @date 2018年3月14日  下午5:36:35
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> deleteDocuments(HttpSession session, String param) throws BusinessException;

    /**
     * @param session
     * @param querySbujectBalance
     * @param creditSubCode
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: procurementCreditRow
     * @Description: 获取采购贷方科目列表 （应付，预付，现金，银行）
     * @date 2018年3月21日  上午11:05:12
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> procurementCreditRow(HttpSession session, Map<String, Object> querySbujectBalance, String creditSubCode) throws BusinessException;

    /**
     * @param session
     * @param querySbujectBalance
     * @return Map<String, Object>    返回类型
     * @Title: procurementInventoryRow
     * @Description: 获取采购库存商品科目列表
     * @date 2018年3月21日  上午10:54:38
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> procurementInventoryRow(HttpSession session, Map<String, Object> querySbujectBalance) throws BusinessException;

    ;

    /**
     * @param session
     * @param querySbujectBalance
     * @return Map<String, Object>    返回类型
     * @Title: salesCreditRow
     * @Description: 获取销售贷方科目列表 （1122应收账款，2203预收账款，1001库存现金，1002银行存款）
     * @date 2018年3月24日  下午4:15:25
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> salesCreditRow(HttpSession session, Map<String, Object> querySbujectBalance) throws BusinessException;

    /**
     * @param session
     * @param querySbujectBalance
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: procurementRawMaterialsRow
     * @Description: 获取采购原材料科目列表
     * @date 2018年3月26日  上午11:48:09
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> procurementRawMaterialsRow(HttpSession session, Map<String, Object> querySbujectBalance) throws BusinessException;

    Map<String, Object> addsalesCreditList(User user, Account account,
                                           TBasicSubjectMessageService tBasicSubjectMessageService) throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: addProcurementInventoryList
     * @Description: 添加 采购库存商品 集合
     * @date 2018年3月28日  上午8:56:53
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> addProcurementInventoryList(HttpSession session, String tBasicSubjectMessage,
                                                    String tBasicSubjectMessageList) throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessage
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: addProcurementRawMaterialsList
     * @Description: 添加 采购原材料科目 集合
     * @date 2018年3月29日  下午3:19:46
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> addProcurementRawMaterialsList(HttpSession session, String tBasicSubjectMessage,
                                                       String tBasicSubjectMessageList) throws BusinessException;

    /**
     * @param session
     * @param tBasicSubjectMessageList
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: addsalesCreditList
     * @Description: 添加 获取销售贷方科目列表 （1122应收账款，2203预收账款，1001库存现金，1002银行存款） 集合
     * @date 2018年3月29日  下午5:08:42
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> addsalesCreditList(HttpSession session, String tBasicSubjectMessageList) throws BusinessException;

    /**
     * @param session
     * @param tBasicDocumentss
     * @return
     * @throws BusinessException Map<String,Object>    返回类型
     * @Title: deleteDocumentsList
     * @Description: 删除单据集合
     * @date 2018年3月29日  下午7:29:22
     * @author SiLiuDong 司氏旭东
     */
    Map<String, Object> deleteDocumentsList(HttpSession session, String tBasicDocumentss) throws BusinessException;

    Map<String, Object> documentsToVouchers(HttpSession session) throws BusinessException;

    public List<TBasicSubjectMessage> getMjSub(HttpSession session, String subCode) throws BusinessException;

}

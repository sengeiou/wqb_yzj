package com.wqb.controller.subject;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.common.SubjectUtils;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.TBasicSubjectMessage;
import com.wqb.model.User;
import com.wqb.service.UserService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectMessageController
 * @Description: 系统中科目类
 * @date 2017年12月20日 下午10:52:31
 */
@Controller
@RequestMapping("/subject")
public class TBasicSubjectMessageController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectMessageController.class);

    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    @Autowired
    PeriodStatusService periodStatusService;

    @Autowired
    private UserService userService;

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: addSubMessage
     * @Description: 添加系统科目
     * @date 2017年12月21日 上午9:27:36
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addSubMessage")
    @ResponseBody
    Map<String, Object> addSubMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            // 获取用户信息
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            Account account = (Account) sessionMap.get("account"); // 获取帐套信息
            String accountId = account.getAccountID();// 账套id
            String busDate = (String) sessionMap.get("busDate");

            // 根据期间查询账套状态是否结账 -- 已结账 不能添加科目
            String isJz = periodStatusService.queryAccStatus(accountId, busDate);
            if (isJz.equals(1) || isJz.equals("1")) {
                result.put("msg", "本期已经结账不能增加科目!!!");
                return result;
            }
            logger.info(accountId + busDate + "新增科目: ",
                    tBasicSubjectMessage.getSubCode() + "--->" + tBasicSubjectMessage.getSubName());
            result = tBasicSubjectMessageService.addSubMessage(session, tBasicSubjectMessage);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMessageController【addSubMessage】, 添加系统科目出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【addSubMessage】, 添加系统科目出错", e);
            return result;
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: addSubMessageList
     * @Description: 添加系统科目集合
     * @date 2017年12月21日 上午10:34:04
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/addSubMessageList")
    @ResponseBody
    Map<String, Object> addSubMessageList(List<TBasicSubjectMessage> tBasicSubjectMessage) {
        logger.info("subject/addSubMessageList" + tBasicSubjectMessage);
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            result = tBasicSubjectMessageService.addSubMessageList(session, tBasicSubjectMessage);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【addSubMessageList】,添加系统科目集合出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【addSubMessageList】,添加系统科目集合出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessage
     * @Description: 查询系统中该账套的全部科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMessage")
    @ResponseBody
    Map<String, Object> querySubMessage() {
        User user = userService.getCurrentUser();
        Account account = userService.getCurrentAccount(user);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = tBasicSubjectMessageService.querySubMessage(user, account);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【querySubMessage】,查询系统中该账套的全部科目出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubMessage】,查询系统中该账套的全部科目出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessage
     * @Description: 根据科目编码查询系统中的科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMessageBySubCode")
    @ResponseBody
    Map<String, Object> querySubMessageBySubCode(String subCode) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = tBasicSubjectMessageService.querySubMessageBySubCode(session, subCode);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【querySubMessageBySubCode】,根据科目编码查询系统中的科目出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubMessageBySubCode】,根据科目编码查询系统中的科目出错", e);
            result.put("message", "fail");
            return result;
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessage
     * @Description: 根据科目名称查询系统中的科目
     * @date 2017年12月20日 下午10:53:46
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMessageBySubName")
    @ResponseBody
    Map<String, Object> querySubMessageBySubName(String subName) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = tBasicSubjectMessageService.querySubMessageBySubName(session, subName);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【querySubMessageBySubName】,根据科目名称查询系统中的科目出错", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubMessageBySubName】,根据科目名称查询系统中的科目出错", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: updateMessage
     * @Description: 更新系统中的科目
     * @date 2017年12月21日 上午10:28:30
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/updateMessage")
    @ResponseBody
    Map<String, Object> updateMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            Map<String, Object> updateMessage = tBasicSubjectMessageService.updateMessage(session,
                    tBasicSubjectMessage);
            result.putAll(updateMessage);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMessageController【updateMessage】,更新系统中的科目出错", e);
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【updateMessage】,更新系统中的科目出错", e);
        }
        return result;
    }

    /**
     * @param pkSubId
     * @return Map<String, Object> 返回类型
     * @Title: deleteMessageByPrimaryKey
     * @Description: 根据主键删除系统中的科目
     * @date 2017年12月21日 上午10:29:43
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteMessageByPrimaryKey")
    @ResponseBody
    Map<String, Object> deleteMessageByPrimaryKey(String pkSubId) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            result = tBasicSubjectMessageService.deleteMessageByPrimaryKey(session, pkSubId);
            result.put("code", 1);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMessageController【deleteMessageByPrimaryKey】,根据主键删除系统中的科目出错", e);
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【deleteMessageByPrimaryKey】,根据主键删除系统中的科目出错", e);
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: deleteMessage
     * @Description: 单条科目删除
     * @date 2018年1月31日 上午11:06:16
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteMessage")
    @ResponseBody
    Map<String, Object> deleteMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        try {
            result = tBasicSubjectMessageService.deleteMessage(session, tBasicSubjectMessage);
            result.put("code", 1);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMessageController【deleteMessageByPrimaryKey】,根据主键删除系统中的科目出错", e);
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【deleteMessageByPrimaryKey】,根据主键删除系统中的科目出错", e);
        }
        return result;
    }

    /**
     * @return Map<String, Object> 返回类型
     * @Title: deleteMessageAll
     * @Description: 删除账套在系统中全部科目
     * @date 2017年12月21日 上午10:31:13
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/deleteMessageAll")
    @ResponseBody
    Map<String, Object> deleteMessageAll() {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = tBasicSubjectMessageService.deleteMessageAll(session);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【deleteMessageAll】,删除账套在系统中全部科目", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【deleteMessageAll】,删除账套在系统中全部科目", e);
            result.put("message", "fail");
            return result;
        }
        result.put("message", "success");
        return result;
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubByIDAndName
     * @Description: 获取全部科目
     * @date 2018年5月15日 上午11:21:28
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubByIDAndName")
    @ResponseBody
    Map<String, Object> querySubByIDAndName(String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();

            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            // User user = (User) sessionMap.get("user");
            String busDate = (String) sessionMap.get("busDate");
            Account account = (Account) sessionMap.get("account");
            param.put("busDate", busDate);
            param.put("keyWord", keyWord);
            param.put("accountID", account.getAccountID());
            List<TBasicSubjectMessage> list = tBasicSubjectMessageService.querySubByIDAndName(param);
            result.put("success", "true");
            result.put("list", list);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMessageController【querySubByIDAndName】,模糊查询科目异常", e);
            result.put("success", "fail");
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubByIDAndName】,模糊查询科目异常", e);
            result.put("success", "fail");
            return result;
        }
        return result;
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubByIDAndNameFoot
     * @Description: 获取最下级科目
     * @date 2018年5月15日 上午11:21:43
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubByIDAndNameFoot")
    @ResponseBody
    Map<String, Object> querySubByIDAndNameFoot(String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", "fail");
        try {
            Map<String, Object> param = new HashMap<String, Object>();

            @SuppressWarnings("unchecked")
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            // User user = (User) sessionMap.get("user");
            String busDate = (String) sessionMap.get("busDate");
            Account account = (Account) sessionMap.get("account");
            param.put("busDate", busDate);
            param.put("keyWord", keyWord);
            param.put("accountID", account.getAccountID());
            List<TBasicSubjectMessage> list = tBasicSubjectMessageService.queryAllSubject(param);
            List<TBasicSubjectMessage> mjSub = null;
            if (list != null && list.size() != 0) {
                mjSub = SubjectUtils.getMjSub(list);
            }

            result.put("success", "true");
            result.put("list", mjSub);
        } catch (BusinessException e) {
            logger.error("TBasicSubjectMessageController【querySubByIDAndName】,模糊查询科目异常", e);
            result.put("success", "fail");
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubByIDAndName】,模糊查询科目异常", e);
            result.put("success", "fail");
            return result;
        }
        return result;
    }

    /**
     * @param tBasicSubjectMessage
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageMaxSubCode
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月17日 下午6:45:09
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMessageMaxSubCode")
    @ResponseBody
    Map<String, Object> querySubMessageMaxSubCode(TBasicSubjectMessage tBasicSubjectMessage) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpSession session = getSession();
        try {
            result = tBasicSubjectMessageService.querySubMessageMaxSubCode(session, tBasicSubjectMessage);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【querySubMessageMaxSubCode】,根据帐套id 上级科目编码 科目级别获取最大的科目代码", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubMessageMaxSubCode】,根据帐套id 上级科目编码 科目级别获取最大的科目代码", e);
            result.put("message", "fail");
            return result;
        }
        return result;
    }

    /**
     * @param subCode
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageMaxSubCodeStr
     * @Description: 根据帐套id 上级科目编码 科目级别获取最大的科目代码
     * @date 2018年1月18日 上午9:04:20
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMessageMaxSubCodeStr")
    @ResponseBody
    Map<String, Object> querySubMessageMaxSubCodeStr(String subCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpSession session = getSession();
        try {
            result = tBasicSubjectMessageService.querySubMessageMaxSubCodeStr(session, subCode);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【querySubMessageMaxSubCode】,根据帐套id 上级科目编码 科目级别获取最大的科目代码", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubMessageMaxSubCode】,根据帐套id 上级科目编码 科目级别获取最大的科目代码", e);
            result.put("message", "fail");
            return result;
        }
        return result;
    }

    /**
     * @param subName
     * @return Map<String, Object> 返回类型
     * @Title: querySubjectByName
     * @Description: 根据科目名称和科目全名查询系统科目
     * @date 2018年1月20日 上午9:26:08
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubjectByName")
    @ResponseBody
    Map<String, Object> querySubjectByName(String subName) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpSession session = getSession();
        try {
            result = tBasicSubjectMessageService.querySubjectByName(session, subName);
        } catch (BusinessException e) {
            result.put("message", "fail");
            logger.error("TBasicSubjectMessageController【querySubjectByName】,根据科目名称和科目全名查询系统科目", e);
            return result;
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubjectByName】,根据科目名称和科目全名查询系统科目", e);
            result.put("message", "fail");
            return result;
        }
        return result;
    }

    /**
     * @param subject  科目名称或代
     * @param category 科目类别
     * @return Map<String, Object> 返回类型
     * @Title: querySubMessageByCategory
     * @Description: 根据科目名称或代码 和 科目类别查询科目
     * @date 2018年1月23日 上午9:34:03
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubMessageByCategory")
    @ResponseBody
    Map<String, Object> querySubMessageByCategory(String subject, String category) {
        // try
        // {
        // // 处理页面乱码
        // subject = new String(subject.getBytes("ISO-8859-1"),"UTF-8");
        // }
        // catch (UnsupportedEncodingException e1)
        // {
        // e1.printStackTrace();
        // }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
        HttpSession session = getSession();
        try {
            Map<String, Object> querySubMessageByCategory = tBasicSubjectMessageService
                    .querySubMessageByCategory(session, subject, category);
            result.putAll(querySubMessageByCategory);
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("TBasicSubjectMessageController【querySubMessageByCategory】,根据科目名称或代码  和 科目类别查询科目", e);
        } catch (Exception e) {
            logger.error("TBasicSubjectMessageController【querySubMessageByCategory】,根据科目名称或代码  和 科目类别查询科目", e);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/kmdzPageDelSubByPKID")
    @ResponseBody
    Map<String, Object> kmdzDelSubByPKID(String pkSubId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            // 非末级科目不让删除 一级科目不让删除
            TBasicSubjectMessage tm = tBasicSubjectMessageService.querySubMessageByPkSubId(pkSubId);
            if (tm != null) {
                String subCode = tm.getSubCode().replaceAll(" ", "");
                if (subCode.length() == 4) {
                    returnMap.put("code", "-1");
                    returnMap.put("msg", "一级科目不允许删除!");
                    return returnMap;
                } else {
                    // 判断是否末级科目
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("accountID", getAccount().getAccountID());
                    param.put("period", getUserDate());
                    param.put("subCode", subCode);
                    param.put("accountId", getAccount().getAccountID());
                    boolean isMj = tBasicSubjectMessageService.isLastStage(param);
                    if (isMj) {
                        // 执行删除 附带变更上级科目余额
                        tBasicSubjectMessageService.delSubMessageByPkSubId(pkSubId);
						/*
						int length = subCode.length();
						int jc = (length - 4) / 3;
						Map<String, Object> para = new HashMap<String, Object>();
						para.put("direction", tm.getDebitCreditDirection() + "");
						para.put("initDebitAmount", tm.getInitDebitBalance());
						para.put("endDebitAmount", tm.getEndingBalanceDebit());

						para.put("initCreditAmount", tm.getInitCreditBalance());
						para.put("endCreditAmount", tm.getEndingBalanceCredit());
						para.put("busDate", getUserDate());
						para.put("accountID", getAccount().getAccountID());
						para.put("subCode", subCode);
						for (int i = jc - 1; i >= 0; i--) {
							String tempSub = subCode.substring(0, 4 + i * 3);
							para.put("subCode", tempSub);
							tBasicSubjectMessageService.chgSubAmountByDeleteSub(para);
						}
						*/

                    } else {
                        returnMap.put("code", "-1");
                        returnMap.put("msg", "非末级科目不能删除,请先删除其末级科目!");
                        return returnMap;
                    }
                }
            } else {
                returnMap.put("code", "-1");
                returnMap.put("msg", "科目不存在,删除失败!");
                return returnMap;
            }
            returnMap.put("code", "1");
            returnMap.put("msg", "删除科目成功!");
            return returnMap;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/kmdzPageAddSub")
    @ResponseBody
    Map<String, Object> kmdzPageAddSub(String pkSubId, String pkSubExcelIDs) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            // 选定的那一条系统科目信息
            TBasicSubjectMessage parentTsm = tBasicSubjectMessageService.querySubMessageByPkSubId(pkSubId);
            String parentSubCode = parentTsm.getSubCode();
            //首先获取到
            String[] pkSubExcelID = pkSubExcelIDs.split("\\,");
            for (int s = 0; s < pkSubExcelID.length; s++) {
                // 选定的添加的那条EXCEL科目信息
                String pkSubExcelId = pkSubExcelID[s];
                // 定义系统科目信息对象
                TBasicSubjectMessage tsm = new TBasicSubjectMessage();

            }
            return returnMap;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String subCode = "1001002";
        int length = subCode.length();
        int jc = (length - 4) / 3;
        for (int i = jc - 1; i >= 0; i--) {
            String temp = subCode.substring(0, 4 + i * 3);
            System.out.println(temp);
        }
    }
}

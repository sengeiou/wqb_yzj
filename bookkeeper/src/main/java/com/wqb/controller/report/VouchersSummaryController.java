package com.wqb.controller.report;

import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.controller.subject.SubjectController;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: VouchersSummary
 * @Description: 凭证汇总表
 * @date 2018年1月18日 上午10:20:12
 */

@Component
@Controller
@RequestMapping("/VouchersSummary")
public class VouchersSummaryController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(SubjectController.class);

    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;

    /**
     * @param subCode
     * @return Map<String, Object>    返回类型
     * @Title: queryVouchersSummary
     * @Description: 查询凭证汇总表
     * @date 2018年1月18日  上午10:23:47
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/queryVouchersSummary")
    @ResponseBody
    Map<String, Object> queryVouchersSummary() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);
//		HttpSession session = getSession();
//		try
//		{
//			result = tBasicSubjectMessageService.querySubMessageMaxSubCodeStr(session, subCode);
//		}
//		catch (BusinessException e)
//		{
//			result.put("message", "fail");
//			logger.error("TBasicSubjectMessageController【querySubMessageMaxSubCode】,根据帐套id 上级科目编码 科目级别获取最大的科目代码", e);
//			return result;
//		}
//		catch (Exception e)
//		{
//			logger.error("TBasicSubjectMessageController【querySubMessageMaxSubCode】,根据帐套id 上级科目编码 科目级别获取最大的科目代码", e);
//			result.put("message", "fail");
//			return result;
//		}
        return result;
    }
}

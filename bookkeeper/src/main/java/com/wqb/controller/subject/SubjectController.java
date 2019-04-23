package com.wqb.controller.subject;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.Subject;
import com.wqb.model.User;
import com.wqb.service.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subject")
public class SubjectController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(SubjectController.class);

    @Autowired
    SubjectService subjectService;

    @RequestMapping("/queryVouSubject")
    @ResponseBody
    Map<String, Object> queryVouSubject(String keyWord) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            HttpSession session = getSession();
            Map<String, Object> sessionMap = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) sessionMap.get("user");
            Account account = (Account) sessionMap.get("account");
            param.put("userID", user.getUserID());
            param.put("accountID", account.getAccountID());
            param.put("sunumber", keyWord);
            List<Subject> list = subjectService.queryVouSubject(param);
            result.put("list", list);
            result.put("message", "success");
            return result;
        } catch (BusinessException e) {
            logger.error("SubjectController【queryVouSubject】,凭证界面查询科目异常", e);
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("SubjectController【queryVouSubject】,凭证界面查询科目异常", e);
            result.put("message", "fail");
            return result;
        }
    }
}

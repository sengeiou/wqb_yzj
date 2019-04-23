package com.wqb.controller.subject;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.subject.TBasicSubjectParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subject")
public class TBasicSubjectParentController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(TBasicSubjectParentController.class);

    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    TBasicSubjectParentService tBasicSubjectParentService;

    /**
     * @param uploadSubExcel
     * @return Map<String, Object> 返回类型
     * @Title: uploadSubExcel
     * @Description: 科目初始化excel表上传
     * @date 2017年12月7日 上午9:49:29
     * @author SiLiuDong 司氏旭东
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/uploadSubParent")
    @ResponseBody
    Map<String, Object> uploadSubject(MultipartFile uploadSubParent) {

        Map<String, Object> result = new HashMap<String, Object>();

        //获取用户信息
        Map<String, Object> userDate = (Map<String, Object>) getRequest().getSession().getAttribute("userDate");
        User user = (User) userDate.get("user");
        Account account = (Account) userDate.get("account");
        //用户id
        String userID = user.getUserID();
        String busDate = (String) userDate.get("busDate");
        //账套id
        String accountID = account.getAccountID();
        if (StringUtil.isEmptyWithTrim(userID) || StringUtil.isEmptyWithTrim(accountID)) {
            result.put("message", "fail");
            return result;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userID", userID);
        map.put("accountID", accountID);
        map.put("userName", user.getUserName());
        try {
            if (null != uploadSubParent && !uploadSubParent.isEmpty()) {
                //1获取文件上传路径
//				String filePath = PathUtil.getClasspath() + Constrants.FILEPATHFILE; //wtpwebapps/wqb/WEB-INF/classes/../../files/

                String filePath = filePaths + "/" + user.getUserID() + "/" + accountID + "/" + userDate + "/";
                //2获取文件名
                String fileName = FileUpload.fileUp(uploadSubParent, filePath,
                        "SubParentInit-" + user.getUserID() + "-" + System.currentTimeMillis());
                //3读取excel表格数据
                List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                //4调用固定资产业务层添加到数据库
                result = tBasicSubjectParentService.insertSubParent(list, map);
            }
            result.put("message", "success");
            return result;
        } catch (Exception e) {
            result.put("message", "fail");
            return result;
        }
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubParent
     * @Description: 查询 会计准则初始数据
     * @date 2017年12月13日 下午4:37:19
     * @author SiLiuDong 司氏旭东
     */
    @RequestMapping("/querySubParent")
    @ResponseBody
    Map<String, Object> querySubParent() {
        HttpSession session = getSession();
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = tBasicSubjectParentService.querySubParent(session);
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
}

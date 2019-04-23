package com.wqb.controller.documents;

import com.wqb.common.FileUpload;
import com.wqb.common.ReadExcal;
import com.wqb.common.StringUtil;
import com.wqb.controller.BaseController;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.documents.TBasicDocumentsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TBasicDocumentsConfigController
 * @Description: 单据配置
 * @date 2018年6月5日 上午9:00:13
 */
@Controller
@Component
@RequestMapping("/documentsConfig")
public class TBasicDocumentsConfigController extends BaseController {
    @Value("${filePaths}")
    private String filePaths;

    @Autowired
    TBasicDocumentsConfigService tBasicDocumentsConfigService;

    @RequestMapping("/documentsConfigView")
    public ModelAndView toDocumentsView(ModelAndView modelAndView) {
        modelAndView.setViewName("documentsConfig/importView");
        return modelAndView;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/uploadDocumentsConfig")
    @ResponseBody
    Map<String, Object> uploadSubject(MultipartFile uploadDocumentsConfig) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", -1);

        //获取用户信息
        Map<String, Object> userDate = (Map<String, Object>) getRequest().getSession().getAttribute("userDate");
        User user = (User) userDate.get("user");
        String busDate = (String) userDate.get("busDate");
        Account account = (Account) userDate.get("account");
        //用户id
        String userID = user.getUserID();
        //账套id
        String accountID = account.getAccountID();
        if (StringUtil.isEmptyWithTrim(userID) || StringUtil.isEmptyWithTrim(accountID)) {
            return result;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userID", userID);
        map.put("accountID", accountID);
        map.put("userName", user.getUserName());
        try {
            if (null != uploadDocumentsConfig && !uploadDocumentsConfig.isEmpty()) {
                //1获取文件上传路径
//				String filePath = PathUtil.getClasspath() + Constrants.FILEPATHFILE; //wtpwebapps/wqb/WEB-INF/classes/../../files/
                String filePath = filePaths + "/" + user.getUserID() + "/" + accountID + "/" + busDate + "/";

                //2获取文件名
                String fileName = FileUpload.fileUp(uploadDocumentsConfig, filePath,
                        "documentsConfigInit-" + user.getUserID() + "-" + System.currentTimeMillis());
                //3读取excel表格数据
                List<Map<String, Object>> list = ReadExcal.readExcel(filePath, fileName, 1, 0);
                //4调用固定资产业务层添加到数据库
                result = tBasicDocumentsConfigService.insertDocumentsConfig(list, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e);
        }
        return result;
    }
}

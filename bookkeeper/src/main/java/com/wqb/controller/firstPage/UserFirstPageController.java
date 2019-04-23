package com.wqb.controller.firstPage;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.model.Account;
import com.wqb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userFirstPage")
public class UserFirstPageController extends BaseController {
    @Autowired
    AccountDao accountDao;
    private static Log4jLogger logger = Log4jLogger.getLogger(UserFirstPageController.class);

    @RequestMapping("/getFirstPageData")
    @ResponseBody
    Map<String, Object> getFirstPageData() {
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            String time = DateUtil.getMoth2(new Date());
            User user = getUser();
            Integer userType = user.getUserType();
            if (userType == 2) {
                // 同行管理员(代账)
                List<Account> accList = accountDao.queryAccByUserID(user.getUserID());
                if (accList != null && accList.size() != 0) {
                    result.put("wfp", accList.size());
                } else {
                    result.put("wfp", 0);
                }
                Map<String, Object> pa = new HashMap<String, Object>();
                pa.put("userID", user.getUserID());
                pa.put("time", time);
                Object tyObj = accountDao.queryTy(pa);
                Map<String, Object> tyMap = (Map<String, Object>) tyObj;
                int amdinTy = Integer.parseInt(tyMap.get("ty").toString());
                result.put("adminTy", amdinTy);
                List<Map<String, Object>> list = accountDao.queryAdminFirstData(pa);
                result.put("list", list);
                // 合计
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userID", user.getUserID());
                Object objHj = accountDao.queryHj(param);
                Map<String, Object> hjMap = (Map<String, Object>) objHj;
                int hj = Integer.parseInt(hjMap.get("hj").toString());

                Map<String, Object> para = new HashMap<String, Object>();
                para.put("userID", user.getUserID());
                para.put("time", time);
                Object objXzHj = accountDao.queryXzHj(para);
                Map<String, Object> xzHjMap = (Map<String, Object>) objXzHj;
                int xzHj = Integer.parseInt(xzHjMap.get("xzhj").toString());

                Map<String, Object> par = new HashMap<String, Object>();
                par.put("userID", user.getUserID());
                par.put("time", time);
                Object objAdminXzHj = accountDao.queryAdminXzHj(par);
                Map<String, Object> adminXzHjMap = (Map<String, Object>) objAdminXzHj;
                int adminXzHj = Integer.parseInt(adminXzHjMap.get("xzhj").toString());

                Object hjtyObj = accountDao.queryHjTy(par);
                Map<String, Object> hjtyMap = (Map<String, Object>) hjtyObj;
                int hjty = Integer.parseInt(hjtyMap.get("hjty").toString());

                result.put("hj", hj);
                result.put("xzhj", xzHj);
                result.put("adminXzhj", adminXzHj);
                result.put("hjty", hjty);

            } else if (userType == 5) {
                // 同行员工(代账)
                // 客户总数
                List<Account> accList = accountDao.queryAccByUserID(user.getUserID());
                if (accList != null) {
                    result.put("sl", accList.size());
                } else {
                    result.put("sl", 0);
                }
                // 当月新增
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userID", user.getUserID());
                param.put("time", time);
                Object obj = accountDao.queryXzCount(param);
                Map<String, Object> slMap = (Map<String, Object>) obj;
                int xz = Integer.parseInt(slMap.get("xz").toString());
                result.put("xz", xz);
                Object hjtyObj = accountDao.queryHjTy(param);
                Map<String, Object> hjtyMap = (Map<String, Object>) hjtyObj;
                int hjty = Integer.parseInt(hjtyMap.get("hjty").toString());
                result.put("hjty", hjty);

            }
        } catch (BusinessException e) {
            logger.error("第四版本获取首页数据异常", e);
            result.put("success", "false");
            return result;
        } catch (Exception e) {
            logger.error("第四版本获取首页数据异常", e);
            result.put("success", "false");
            return result;
        }
        result.put("success", "true");
        return result;
    }
}

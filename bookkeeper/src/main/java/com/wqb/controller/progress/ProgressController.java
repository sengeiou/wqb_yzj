package com.wqb.controller.progress;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.dao.progress.ProgressDao;
import com.wqb.model.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/progress")
public class ProgressController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(ProgressController.class);

    @Autowired
    ProgressDao progressDao;

    @RequestMapping("/getProgress")
    @ResponseBody
    Map<String, Object> getProgress() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", getAccount().getAccountID());
            param.put("period", getUserDate());
            List<Progress> list = progressDao.queryProgress(param);
            if (list.isEmpty()) {
                logger.error("【ProgressController】.getProgress() ----> 获取任务进度异常");
                result.put("message", "查询 任务进度 为空，请检查之后重试！");
                result.put("success", "false");
                return result;
            } else if (list != null && list.size() > 0 && list.get(0) != null) {
                result.put("progress", list.get(0));
            } else {
                logger.error("【ProgressController】.getProgress() ----> 获取任务进度异常");
                result.put("message", "获取任务进度异常");
                result.put("success", "false");
                return result;
            }
        } catch (BusinessException e) {
            logger.error("获取任务进度异常", e);
            result.put("success", "false");
            return result;
        } catch (Exception e) {
            logger.error("获取任务进度异常", e);
            result.put("success", "false");
            return result;
        }
        result.put("success", "true");
        return result;
    }
}

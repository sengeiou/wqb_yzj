package com.wqb.controller.tempType;

import com.wqb.common.BusinessException;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.model.TempType;
import com.wqb.service.tempType.TempTypeService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/tempType")
public class TempTypeController extends BaseController {

    private static Log4jLogger logger = Log4jLogger.getLogger(TempTypeController.class);

    @Autowired
    VatService vatService;
    @Autowired
    TempTypeService tempTypeService;

    @RequestMapping("/insertTempType")
    @ResponseBody
    Map<String, Object> insertTempType(@RequestParam(required = true) String tempName) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            tempTypeService.insertTempType(getAccount().getAccountID(), tempName);
            res.put("code", "0");
            res.put("msg", "添加成功");
            return res;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    @RequestMapping("/saveTempVoucher")
    @ResponseBody
    Map<String, Object> saveTempVoucher(
            @RequestParam(required = true) String tempName,
            @RequestParam(required = true) String pid,
            @RequestParam(required = true) String assistName,
            @RequestParam(required = true) String content,
            String saveAmount) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            String accountID = getAccount().getAccountID();
            Map<String, Object> resMap = tempTypeService.saveTempVoucher(accountID, tempName, assistName, pid, saveAmount, content);
            return resMap;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", "保存异常");
            res.put("info", e.getMessage());
            return res;
        }
    }

    @RequestMapping("/queryTempA")
    @ResponseBody
    Map<String, Object> queryTempA() {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("accountID", getAccount().getAccountID());
            map.put("parentID", 0);
            List<TempType> list = tempTypeService.queryTemp(map);
            res.put("code", "0");
            res.put("msg", list);
            return res;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    @RequestMapping("/queryTempB")
    @ResponseBody
    Map<String, Object> queryTempB() {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("accountID", getAccount().getAccountID());
            map.put("parentIDB", "1");
            List<TempType> list = tempTypeService.queryTemp(map);
            res.put("code", "0");
            res.put("msg", list);
            return res;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    @RequestMapping("/delTem")
    @ResponseBody
    Map<String, Object> delTem(@RequestParam(required = true) String tempID) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> delTem = tempTypeService.delTem(tempID);
            String codes = delTem.get("code").toString();
            if (!codes.equals("0")) {
                res.put("code", "3");
                res.put("msg", delTem.get("msg").toString());
                return res;
            }
            res.put("code", "0");
            res.put("msg", "删除成功");
            return res;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    @RequestMapping("/uPTem")
    @ResponseBody
    Map<String, Object> uPTem(@RequestParam(required = true) Integer tempID, String tempName) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Map<String, Object> delTem = tempTypeService.upTem(tempID, tempName);
            return delTem;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    @RequestMapping("/generateTemplate")
    //修改大类模板名称
    @ResponseBody
    Map<String, Object> generateTemplate1() {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            System.out.println();
            //tempTypeService.bathGenerateTemplate();  //批量生成模板
            //createTemplate();  //账套初始化生成模板
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            res.put("code", "999");
            res.put("msg", e.getMessage());
            return res;
        }
    }


    public void createTemplate() {
        try {
            tempTypeService.generateTemplate("bb31106948194fce97afec2f58f1392a");
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/systemTemplate")
    ModelAndView systemParameterView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("system/systemTemplate");
        Map<String, Object> map = new HashMap<>();
        map.put("code", "huanweiqi");
        modelAndView.addAllObjects(map);
        return modelAndView;
    }


}

package com.wqb.controller.export;

import com.wqb.common.DataExcelExportUtil;
import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.controller.BaseController;
import com.wqb.dao.assets.AssetsDao;
import com.wqb.model.Account;
import com.wqb.model.Assets;
import com.wqb.model.vomodel.SubMessageVo;
import com.wqb.service.subBook.SubBookService;
import com.wqb.service.subexcel.TBasicSubjectMessageService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(ExportController.class);
    @Autowired
    TBasicSubjectMessageService tBasicSubjectMessageService;
    @Autowired
    AssetsDao assetsDao;
    @Autowired
    SubBookService subBookService;

    /**
     * 总账导出
     *
     * @return
     */
    @RequestMapping("/exportGeneralLedger")
    @ResponseBody
    Map<String, Object> exportGeneralLedger(HttpServletRequest request,
                                            @RequestParam(value = "beginTime", required = true) String beginTime,
                                            @RequestParam(value = "endTime", required = true) String endTime,
                                            @RequestParam(value = "startLevel", required = false) String start_level,
                                            @RequestParam(value = "endLevel", required = false) String end_level,
                                            @RequestParam(value = "beginSubCode", required = false) String beginSubCode,
                                            @RequestParam(value = "endSubCode", required = false) String endSubCode,
                                            @RequestParam(value = "ye", required = false) String yeNotZero,
                                            @RequestParam(value = "ye2", required = false) String yeNotZero2, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<>();
            String accountID = getAccount().getAccountID();
            String period = getUserDate();
            map.put("accountID", accountID);
            // 1 起始期间 结束期间
            if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                if (beginTime.equals(endTime)) {
                    map.put("period", beginTime);
                } else {
                    map.put("beginTime", beginTime);
                    map.put("endTime", endTime);
                }
            } else {
                map.put("period", period);
            }
            // beginSubCode endSubCode subCode
            // 2 查询 开始科目 结束科目
            if (!StringUtil.isEmpty(beginSubCode) || !StringUtil.isEmpty(endSubCode)) {
                // a 只选择查询一个科目 subCode
                // b 选择区间查询
                // 1 beginSubCode 不为空 endSubCode 不为空
                // 2 beginSubCode 不为空 endSubCode 为空
                // 3 beginSubCode 为空 endSubCode 不为空
                if (!StringUtil.isEmpty(beginSubCode) && !StringUtil.isEmpty(endSubCode)) {
                    if (beginSubCode.equals(endSubCode)) {
                        map.put("sub_code", beginSubCode);
                    } else {
                        map.put("beginSubCode", beginSubCode);
                        map.put("endSubCode", endSubCode);
                    }
                } else if (!StringUtil.isEmpty(beginSubCode)) {

                    map.put("only_beginSubCode", beginSubCode);

                } else if (!StringUtil.isEmpty(endSubCode)) {

                    map.put("only_endSubCode", endSubCode);
                }
            } else {
                System.out.println("查询全部科目");
            }
            // 3 查询 起始级别 结束级别
            if (!StringUtil.isEmpty(start_level) || !StringUtil.isEmpty(end_level)) {
                // 1 start_level 不为空 end_level 不为空
                // 2 start_level 不为空 end_level 为空
                // 3 start_level 为空 end_level 不为空
                if (!StringUtil.isEmpty(start_level) && !StringUtil.isEmpty(end_level)) {
                    if (start_level.equals(end_level)) {
                        map.put("code_level", Integer.valueOf(start_level));
                    } else {
                        map.put("start_level", Integer.valueOf(start_level));
                        map.put("end_level", Integer.valueOf(end_level));
                    }
                } else if (!StringUtil.isEmpty(start_level)) {

                    map.put("only_start_level", Integer.valueOf(start_level));

                } else if (!StringUtil.isEmpty(end_level)) {
                    map.put("only_end_level", Integer.valueOf(end_level));
                }
            } else {
                // 3.4 start_level 为空 end_level 为空
                // 默认查询一级科目
                map.put("code_level", 1);
            }
            // 4 余额为0不显示 发生额不为0且月不为0
            if (!StringUtil.isEmpty(yeNotZero) || !StringUtil.isEmpty(yeNotZero2)) {
                if (!StringUtil.isEmpty(yeNotZero2)) {
                    map.put("yeNotZero2", "1");
                } else if (!StringUtil.isEmpty(yeNotZero)) {
                    map.put("yeNotZero", "1");
                }
            }
            // 4 判断查询期间是否存在
            if (map.get("beginTime") == null && map.get("period") == null) {

                result.put("code", "6");
                result.put("msg", "查询期间不能为空");
                return result;
            }
            List<Map<String, Object>> glList = subBookService.queryLedger(map);
            List<Object[]> list = new ArrayList<Object[]>();
            if (null != glList && glList.size() > 0) {
                for (int i = 0; i < glList.size(); i++) {
                    Map<String, Object> glMap = glList.get(i);
                    String subCode = glMap.get("subCode").toString();
                    String subName = glMap.get("subName").toString();
                    List<SubMessageVo> glList1 = (List<SubMessageVo>) glMap.get("list");
                    if (glList1 != null && glList1.size() != 0) {
                        for (int j = 0; j < glList1.size(); j++) {
                            SubMessageVo sv = glList1.get(j);
                            Object[] obj = new Object[8];
                            if (j == 0) {
                                obj[0] = subCode;
                                obj[1] = subName;
                            }
                            obj[2] = sv.getAccount_period();
                            obj[3] = sv.getZhaiYao();
                            obj[4] = sv.getJf_amount();
                            obj[5] = sv.getDf_amount();
                            obj[6] = sv.getFx_jd();
                            obj[7] = sv.getAmount();
                            list.add(obj);
                        }
                    }
                }
            }
            DataExcelExportUtil dataExcelExportUtil = new DataExcelExportUtil();
            String[] colName = new String[]{"科目编码", "科目名称", "期间", "摘要", "借方金额", "贷方金额", "方向", "余额"};
            XSSFWorkbook wb = dataExcelExportUtil.exportExcelWorkbook("总账数据导出", colName, list, true, null);
            String fileName = new String("总账数据导出.xlsx".getBytes("GBK"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            wb.write(response.getOutputStream());
            response.getOutputStream().flush();
            result.put("success", "true");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取总账导出数据异常", e);
            result.put("success", "fasle");
            result.put("message", "获取总账导出数据异常!");
            return result;
        }
        return result;
    }

    /**
     * 固定资产导出
     *
     * @return
     */
    @RequestMapping("/exportAssets")
    @ResponseBody
    Map<String, Object> exportAssets(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> sessionMap = (Map<String, Object>) getSession().getAttribute("userDate");
            Account account = (Account) sessionMap.get("account");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountID", account.getAccountID());
            List<Assets> assetsList = assetsDao.queryAssByAcc(param);
            List<Object[]> list = new ArrayList<Object[]>();
            if (assetsList != null && assetsList.size() > 0) {
                for (Assets assets : assetsList) {
                    Object[] obj = new Object[28];
                    obj[0] = assets.getAsCode();// 代码
                    obj[1] = assets.getAsName();// 名称
                    obj[2] = assets.getAsModel();// 型号
                    obj[3] = assets.getAsCategory();// 类别
                    obj[4] = assets.getDepartment();// 使用部门
                    obj[5] = assets.getAsState();// 使用情况
                    obj[6] = assets.getAsPosition();// 存放地点
                    obj[7] = assets.getAsManufactor();// 生产厂家
                    obj[8] = assets.getAsManufactorDate();// 生产日期
                    obj[9] = DateUtil.getTime1(assets.getAsAccountDatea());// 入账日期

                    obj[10] = assets.getIsBeforeUse() == "1" ? "是" : "否";// 入账前已开始使用(1是
                    // 2
                    // 否)
                    obj[11] = assets.getAsBeforeUseDate();// 入账前开始使用日期
                    obj[12] = assets.getSourceway();// 增加方式
                    obj[13] = assets.getDmethod();// 折旧方法
                    obj[14] = assets.getAsEstimatePeriod();// 预计使用期间(工作总量：月为单位)
                    obj[15] = assets.getAsvalue();// 原值
                    obj[16] = assets.getAsAddDeprecia();// 累计折旧
                    obj[17] = assets.getAsCumulativeImpairment();// 累计减值准备
                    obj[18] = assets.getAsWorth();// 净值
                    obj[19] = assets.getAsNetSalvage();// 预计净残值
                    obj[20] = assets.getAsUseDepreciaValue();// 用于折旧计算的原值
                    obj[21] = assets.getAsDepreciaPeriod();// 用于折旧计算的预计使用期间(工作总量)
                    obj[22] = assets.getAsExpectedPeriod();// 预计剩余折旧期间数(工作总量)
                    obj[23] = assets.getGdsubject();// 固定资产科目
                    obj[24] = assets.getAsCumulativeSubject(); // 累计折旧科目
                    obj[25] = assets.getAsEconomicUse();// 紧急用途
                    obj[26] = assets.getDes();// 备注
                    obj[27] = assets.getAsDepreciaSubject();// 折旧费用科目
                    list.add(obj);
                }
            }
            DataExcelExportUtil dataExcelExportUtil = new DataExcelExportUtil();
            //
            String[] colName = new String[]{"代码", "名称", "型号", "类别", "使用部门", "使用情况", "存放地点", "生产厂家", "生产日期", "入账日期",
                    "入账前已开始使用", "入账前开始使用日期", "增加方式", "折旧方法", "预计使用期间(工作总量)", "原值", "累计折旧", "累计减值准备", "净值", "预计净残值",
                    "用于折旧计算的原值", "用于折旧计算的预计使用期间(工作总量)", "预计剩余折旧期间数(工作总量)", "固定资产科目", "累计折旧科目", "经济用途", "备注",
                    "折旧费用科目 "};
            XSSFWorkbook wb = dataExcelExportUtil.exportExcelWorkbook("固定资产折旧数据导出", colName, list, true, null);
            String fileName = new String("固定资产折旧数据导出.xlsx".getBytes("GBK"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            wb.write(response.getOutputStream());
            response.getOutputStream().flush();
            result.put("success", "true");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取固定资产折旧数据导出数据异常", e);
            result.put("success", "fasle");
            result.put("message", "获取固定资产折旧数据导出异常!");
            return result;
        }
        return result;
    }
}

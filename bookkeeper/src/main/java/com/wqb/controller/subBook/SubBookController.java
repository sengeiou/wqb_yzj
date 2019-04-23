package com.wqb.controller.subBook;

import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.controller.BaseController;
import com.wqb.dao.subBook.SubBookDao;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.model.vomodel.SubMessageVo;
import com.wqb.service.UserService;
import com.wqb.service.subBook.SubBookService;
import com.wqb.service.vat.VatService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//总账 与 明细账

@Controller
@RequestMapping("/subBook")
public class SubBookController extends BaseController {

    private static Log4jLogger logger = Log4jLogger.getLogger(SubBookController.class);
    @Autowired
    SubBookService subBookService;

    @Autowired
    VatService vatService;
    @Autowired
    SubBookDao subBookDao;
    private Integer size = 500;

    @Autowired
    private UserService userService;

    // 明细账数据
    @SuppressWarnings("unused")
    @RequestMapping("/getDetailAccount")
    @ResponseBody
    Map<String, Object> getDetailAccount(HttpServletRequest request,
                                         @RequestParam(value = "type", required = true) String type,
                                         @RequestParam(value = "currPage", required = true, defaultValue = "1") Integer currPage,
                                         @RequestParam(value = "subCode", required = false) String sub_code,
                                         @RequestParam(value = "beginTime", required = false) String beginTime,
                                         @RequestParam(value = "endTime", required = false) String endTime,
                                         @RequestParam(value = "beginSubCode", required = false) String beginSubCode,
                                         @RequestParam(value = "endSubCode", required = false) String endSubCode,
                                         @RequestParam(value = "startLevel", required = false) String start_level,
                                         @RequestParam(value = "endLevel", required = false) String end_level,
                                         @RequestParam(value = "ye", required = false) String yeNotZero) {


        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> map = new HashMap<>();
            String accountID = getAccount().getAccountID();
            String period = getUserDate();
            // 每页显示300条数据
            Integer beginSize = (currPage - 1) * size;
            map.put("currPage", currPage);
            map.put("begin", beginSize);
            map.put("pageSize", size);
            map.put("accountID", accountID);
            map.put("type", type);
            String minPeiod = period;

            if ("1".equals(type)) {

                if (!StringUtil.isEmpty(sub_code)) {
                    map.put("sub_code", sub_code.trim());
                } else {
                    map.put("sub_code", "1001");
                }
                map.put("period", period.trim());
                minPeiod = period;
            } else if ("3".equals(type)) {

                if (!StringUtil.isEmpty(sub_code.trim())) {
                    map.put("sub_code", sub_code.trim());
                } else {
                    map.put("sub_code", "1001");
                }

                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    if (beginTime.equals(endTime)) {
                        map.put("period", beginTime);
                    } else {
                        map.put("beginTime", beginTime);
                        map.put("endTime", endTime);
                    }
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(beginTime)) {
                    map.put("period", beginTime);
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime);
                    minPeiod = endTime;
                } else {
                    map.put("period", period);
                    minPeiod = period;
                }
                if (!StringUtil.isEmpty(yeNotZero)) {
                    map.put("yeNotZero", "1");
                }

            } else if ("2".equals(type)) {

                /*************** 来自筛选框的条件 start *****************/
                // 先根据筛选框起始科目与结束科目查询科目余额表数据，从而得到科目范围数据
                // 2 再从科目余额表的范围数据里面取第一条作为查询明细账的科目数据
                Map<String, Object> querySub = new HashMap<>();
                querySub.put("accountID", accountID);
                // 1拼接科目 查询 条件
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    //开始时间和结束时间都不为空的话，那么明细账数据取得是开始时间 到 结束时间。  科目范围应该按照明细账数据取得的最大结束时间为准，尽可能获取最多的科目数据
                    querySub.put("period", endTime);
                } else if (!StringUtil.isEmpty(beginTime)) {
                    querySub.put("period", beginTime);
                } else if (!StringUtil.isEmpty(endTime)) {
                    querySub.put("period", endTime);
                } else {
                    // 开始期间结束期间都没选的话 默认使用当前账套期间
                    querySub.put("period", period);
                }
                // 2 查询 开始科目 结束科目
                if (!StringUtil.isEmpty(beginSubCode) || !StringUtil.isEmpty(endSubCode)) {
                    if (!StringUtil.isEmpty(beginSubCode) && !StringUtil.isEmpty(endSubCode)) {
                        if (beginSubCode.equals(endSubCode)) {
                            querySub.put("sub_code", endSubCode.trim());
                        } else {
                            // 大于等于开始科目
                            querySub.put("beginSubCode", beginSubCode.trim());
                            // 小于等于结束科目
                            // 如果结束科目是一级的话，结束科目的数据应该包括这个科目的所有子级科目
                            String real_end_code = getCodeByEndSubCode(endSubCode.trim());
                            querySub.put("endSubCode", real_end_code);
                        }
                    } else if (!StringUtil.isEmpty(beginSubCode)) {
                        querySub.put("only_beginSubCode", beginSubCode.trim());
                    } else if (!StringUtil.isEmpty(endSubCode)) {
                        querySub.put("only_endSubCode", getCodeByEndSubCode(endSubCode.trim()));
                    }
                } else {
                    // 开始科目结束科目都没选的话 表示查询全部科目数据
                    System.out.println("默认全部科目");
                }
                // 3 查询 起始级别 结束级别
                if (!StringUtil.isEmpty(start_level) || !StringUtil.isEmpty(end_level)) {
                    if (!StringUtil.isEmpty(start_level) && !StringUtil.isEmpty(end_level)) {
                        if (start_level.equals(end_level)) {
                            querySub.put("code_level", Integer.valueOf(start_level.trim()));
                        } else {
                            querySub.put("start_level", Integer.valueOf(start_level.trim()));
                            querySub.put("end_level", Integer.valueOf(end_level.trim()));
                        }
                    } else if (!StringUtil.isEmpty(start_level.trim())) {

                        querySub.put("start_level", start_level.trim());
                        querySub.put("end_level", 4);

                    } else if (!StringUtil.isEmpty(end_level)) {
                        querySub.put("start_level", 1);
                        querySub.put("end_level", Integer.valueOf(end_level.trim()));
                    }
                } else {
                    // 起始级别与结束级别都没选的话 表示查询全部级别数据
                    System.out.println("默认全部级别");
                }
                boolean a = false;
                //
                if (StringUtil.isEmpty(beginSubCode) && StringUtil.isEmpty(endSubCode)) {
                    if (StringUtil.isEmpty(start_level) && StringUtil.isEmpty(end_level)) {
                        a = true;
                    }
                }
                // 开始科目结束科目都没选的话 取1001作为默认科目查询数据
                if (a == true) {
                    map.put("sub_code", "1001");
                } else {
                    // 查询科目数据 即树形控件的数据
                    SubMessageVo sb = subBookDao.queryDetailSub2(querySub);
                    if (sb != null) {
                        map.put("sub_code", sb.getSub_code());
                    } else {
                        if (!StringUtil.isEmpty(beginSubCode)) {
                            map.put("sub_code", beginSubCode.trim());
                        } else if (!StringUtil.isEmpty(endSubCode)) {
                            map.put("sub_code", endSubCode.trim());
                        } else {
                            // 没有查询到符合条件的科目数据
                            result.put("msg", "科目异常");
                            result.put("code", "2");
                            return result;
                        }
                    }
                }

                /***************type=3 查询明细账的条件 start *****************/
                // 1 期间
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    if (beginTime.equals(endTime)) {
                        map.put("period", beginTime.trim());
                    } else {
                        map.put("beginTime", beginTime.trim());
                        map.put("endTime", endTime.trim());
                    }
                    minPeiod = beginTime.trim();
                } else if (!StringUtil.isEmpty(beginTime)) {
                    map.put("period", beginTime.trim());
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime.trim());
                    minPeiod = endTime.trim();
                } else {
                    map.put("period", period);
                    minPeiod = period;
                }

                // 2 明细账余额不为0
                if (!StringUtil.isEmpty(yeNotZero)) {
                    map.put("yeNotZero", "1");
                }
                /*************** 查询明细账的条件 end *****************/

            }

            /*************** 来自筛选框的条件 end *****************/
            if (map.get("sub_code") == null) {
                result.put("code", "3");
                result.put("msg", "科目未知");
                return result;
            }

            map.put("minPeiod", minPeiod);
            // 6 条用业务层查询明细账
            Map<String, Object> mapAcc = subBookService.queryDetailAccount(map);

            if (mapAcc != null && mapAcc.get("result") != null) {
                result.put("subName", mapAcc.get("subName"));
                result.put("cout", mapAcc.get("cout"));
                result.put("msg", mapAcc.get("result"));
            } else {
                result.put("msg", null);
            }
            result.put("currPage", currPage);
            result.put("size", size);
            result.put("subCode", map.get("sub_code"));
            result.put("code", "0");
            return result;

        } catch (Exception e) {
            logger.error("SubBookController【getDetailAccount】 异常!", e);
            e.printStackTrace();
            result.put("code", "1");
            result.put("msg", e.getMessage());
            return result;
        }
    }

    /**************		明细账数据 END		****************/


    /****************************** 总账start **************************************/

    // 总账数据
    @SuppressWarnings("unused")
    @RequestMapping("/getLedgerAccount")
    @ResponseBody
    Map<String, Object> getLedgerAccount(HttpServletRequest request,
                                         @RequestParam(value = "beginTime", required = true) String beginTime,
                                         @RequestParam(value = "endTime", required = true) String endTime,
                                         @RequestParam(value = "startLevel", required = false) String start_level,
                                         @RequestParam(value = "endLevel", required = false) String end_level,
                                         @RequestParam(value = "beginSubCode", required = false) String beginSubCode,
                                         @RequestParam(value = "endSubCode", required = false) String endSubCode,
                                         @RequestParam(value = "ye", required = false) String yeNotZero,
                                         @RequestParam(value = "ye2", required = false) String yeNotZero2) {

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
                        map.put("sub_code", beginSubCode.trim());
                    } else {
                        map.put("beginSubCode", beginSubCode.trim());
                        map.put("endSubCode", endSubCode.trim());
                    }
                } else if (!StringUtil.isEmpty(beginSubCode)) {
                    map.put("only_beginSubCode", beginSubCode.trim());
                } else if (!StringUtil.isEmpty(endSubCode)) {
                    map.put("only_endSubCode", endSubCode.trim());
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

            List<Map<String, Object>> list = subBookService.queryLedger(map);

            result.put("code", "0");
            result.put("msg", list);
            return result;

        } catch (Exception e) {
            logger.error("SubBookController【getLedgerList】 异常!", e);
            e.printStackTrace();
            result.put("code", "1");
            result.put("msg", e.getMessage());
            return result;

        }
    }

    /******************************
     * 总账数据 END
     **************************************/

    // 获取期间范围
    @RequestMapping("/getRangePeriod")
    @ResponseBody
    Map<String, Object> getRangePeriod() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Map<String, Object> param = new HashMap<>();
            String accountID = getAccount().getAccountID(); // 账套id
            String period = getUserDate(); // 账套id
            param.put("accountID", accountID);
            param.put("period", period);
            List<Map<String, String>> rangePeriod = subBookService.getRangePeriod(param);
            result.put("msg", rangePeriod);
            result.put("code", "0");
            return result;
        } catch (Exception e) {
            logger.error("SubBookController【getRangePeriod】查询期间异常!", e);
            result.put("msg", e.getMessage());
            result.put("code", "1");
            return result;
        }
    }


    @RequestMapping("/fastQuery")
    @ResponseBody
    Map<String, Object> fastQuery(HttpServletRequest request,
                                  @RequestParam(value = "type", required = true) String type,
                                  @RequestParam(value = "currPage", required = false) Integer currPage, // 不需要
                                  @RequestParam(value = "beginTime", required = false) String beginTime,
                                  @RequestParam(value = "endTime", required = false) String endTime,
                                  @RequestParam(value = "subCode", required = false) String sub_code, // 不需要
                                  @RequestParam(value = "beginSubCode", required = false) String beginSubCode,
                                  @RequestParam(value = "endSubCode", required = false) String endSubCode,
                                  @RequestParam(value = "startLevel", required = false) String start_level,
                                  @RequestParam(value = "endLevel", required = false) String end_level) {

        Map<String, Object> result = new HashMap<String, Object>(); // 返回结果集

        try {

            Map<String, Object> map = new HashMap<>();
            String accountID = getAccount().getAccountID();
            String period = getUserDate();

            map.put("accountID", accountID);
            map.put("type", type); // 等于1查询全部 等于2查询区间范围

            // 只能查询一个期间的科目数据

            // 等于1从来自总账和明细账菜单
            if (type.equals("1")) {
                map.put("period", period);
            } else {
                // 1 确定期间 科目查询只能选择一个期间
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime);
                } else if (!StringUtil.isEmpty(beginTime)) {
                    map.put("period", beginTime);
                } else if (!StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime);
                } else {
                    map.put("period", period);
                }
                // beginSubCode endSubCode subCode
                // 2 开始科目 结束科目 范围筛选
                if (!StringUtil.isEmpty(beginSubCode) || !StringUtil.isEmpty(endSubCode)) {
                    if (!StringUtil.isEmpty(beginSubCode) && !StringUtil.isEmpty(endSubCode)) {
                        if (beginSubCode.equals(endSubCode)) {
                            map.put("sub_code", endSubCode.trim());
                        } else {
                            map.put("beginSubCode", beginSubCode.substring(0, 4));
                            map.put("endSubCode", getCodeByEndSubCode(endSubCode.trim()));
                            map.put("beginSubCode2", beginSubCode.trim());
                        }
                    } else if (!StringUtil.isEmpty(beginSubCode)) {
                        map.put("only_beginSubCode", beginSubCode.substring(0, 4));
                        map.put("only_beginSubCode2", beginSubCode.trim());
                    } else if (!StringUtil.isEmpty(endSubCode)) {
                        map.put("only_endSubCode", getCodeByEndSubCode(endSubCode.trim()));
                    }
                } else {
                    System.out.println("查询全部科目");
                }
                // 3 查询 起始级别 结束级别
                if (!StringUtil.isEmpty(start_level) || !StringUtil.isEmpty(end_level)) {
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
                    System.out.println("查询全部科目");
                }

            }
            Map<String, Object> list = subBookService.fastQuery(map);
            result.put("code", "0");
            result.put("msg", list);
            return result;
        } catch (Exception e) {
            logger.error("SubBookController【querySubAll】查询科目异常!", e);
            result.put("msg", e.getMessage());
            result.put("code", "1");
            return result;

        }
    }

    // 数量金额导出
    @SuppressWarnings("resource")
    @RequestMapping("/downStockExcel")
    @ResponseBody
    void downStockExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            /*
             * Map<String, Object> param = new HashMap<>(); String accountID =
             * getAccount().getAccountID(); //账套id String period =
             * getUserDate(); //账套id param.put("accountID", accountID);
             * param.put("period", period);
             */
            // 如果是get提交
            String parameter = request.getParameter("sub");
            byte[] bytes = parameter.getBytes("ISO-8859-1");
            String string2 = new String(bytes, "utf-8");

            String string = "你好 我是Num One";
            response.setHeader("content-type", "text/html;charset=UTF-8");

            // 已流的方式下载
            // response.setContentType("application/octet-stream;charset=UTF-8");

            // 制定下载.xls文件类型
            // response.setHeader("content-type",
            // "application/x-xls;charset=UTF-8");
            // .xls application/x-xls

            // .xls application/vnd.ms-excel

            // attachment 哦他期门特 作为附件下载

            /*
             * 其实IE是根据Content-Disposition中filename这个段中文件名的后缀来识别这个文件类型的，所以，
             * 如果有很多种文件类型的时候，可以将Content-Type设置为二进制模式的： Header(
             * "Content-type:   application/octet-stream");
             */

            /*
             * byte[] data=toByteArray(filePath);
             * fileName=URLEncoder.encode(fileName,"utf-8"); response.reset();
             * response.setHeader("Content-Disposition",
             * "attachment;fileName=\""+fileName+"\"");
             * response.addHeader("Content-Length", ""+data.length);
             * response.setContentType("application/octet-stream;charset=UTF-8")
             * ; OutputStream ops=new
             * BufferedOutputStream(response.getOutputStream());
             * ops.write(data); ops.flush(); ops.close();
             * response.flushBuffer();
             */

            // D:/upload
            String filePath = "D:/upload/中广2月平安银行流水帐.xls";
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            /// 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            int available = fis.available();
            byte[] buffer = new byte[fis.available()];
            int read = fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            String ss = filename = URLEncoder.encode(DateUtil.getDays() + "--" + filename, "utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + ss + "\"");
            response.addHeader("Content-Length", "" + buffer.length);
            // response.setContentType("application/octet-stream;charset=UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(buffer);
            ops.flush();
            ops.close();
            response.flushBuffer();

            // 使用一次读取多个字节的方式读取文件一次
            /*
             * byte[] arr = new byte[100]; FileInputStream fins = new
             * FileInputStream(filePath);
             */
            /*
             * int red = 0; while ((red = fins.read(arr))!=-1) {
             *
             * System.out.write(arr, 0, red); }
             */

            /*
             * ServletOutputStream outputStream = response.getOutputStream();
             * outputStream.write(string.getBytes("utf-8"));
             * outputStream.flush(); outputStream.close();
             */
        } catch (Exception e) {
            logger.error("SubBookController【getRangePeriod】查询期间异常!", e);
            e.printStackTrace();

        }
    }


    // 数量金额导出  只有一個區間
    @RequestMapping("/exportStockExcel")
    @ResponseBody
    Map<String, Object> exportStockExcel(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();
        try {

            Map<String, Object> param = new HashMap<>();
            String accountID = getAccount().getAccountID(); // 账套id
            String period = getUserDate(); // 账套id
            param.put("accountID", getAccount());
            param.put("period", period);
            String str = subBookService.exportStockExcel(param, response);
            if (str != null && str.equals("0")) {
                res.put("code", "0");
            } else {
                res.put("code", "1");
                res.put("msg", "暂无数据");
            }
            return res;
        } catch (Exception e) {
            logger.error("SubBookController【getRangePeriod】查询期间异常!", e);
            e.printStackTrace();
            res.put("code", "1");
            res.put("msg", "暂无数据");
            return res;

        }

    }


    // 明细账导出
    @SuppressWarnings("unchecked")
    @RequestMapping("/exportDetailAccount")
    @ResponseBody
    Map<String, Object> exportDetailAccount(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "currPage", required = false, defaultValue = "1") Integer currPage,
            @RequestParam(value = "subCode", required = false) String sub_code,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "beginSubCode", required = false) String beginSubCode,
            @RequestParam(value = "endSubCode", required = false) String endSubCode,
            @RequestParam(value = "startLevel", required = false) String start_level,
            @RequestParam(value = "endLevel", required = false) String end_level,
            @RequestParam(value = "ye", required = false) String yeNotZero) {
        Map<String, Object> res = new HashMap<>();
        try {

            //  type取值来源
            // 1 从总账点击过来
            // 3 从明细账菜单选择过来

            // 2筛选款查询过来

            // 4从快速查询过来
            // 5从分页过来

            //1，2 type为1
            //3 type为2
            // 4 为 3
            //5 为1或者2或者3

            /********************************************************************************/

            Map<String, Object> map = new HashMap<>();
            String accountID = getAccount().getAccountID();
            String period = getUserDate();
            // 每页显示300条数据
            Integer beginSize = (currPage - 1) * size;
            map.put("currPage", currPage);
            map.put("begin", beginSize);
            map.put("pageSize", size);
            map.put("accountID", accountID);
            map.put("type", type);
            //查询明细账的时候判断是否为导出操作，如果是导出操作那么不需要分页
            map.put("exportDetail", "exportDetail");
            String minPeiod = period;
            if ("1".equals(type)) {
                // 总账或者明细账菜单
                if (!StringUtil.isEmpty(sub_code)) {
                    map.put("sub_code", sub_code.trim());
                } else {
                    map.put("sub_code", "1001");
                }
                map.put("period", period);
                minPeiod = period;
            } else if ("3".equals(type)) {
                // 树形插件 带筛选框条件查询
                // 拼接明细账科目与期间查询条件
                // 1 用户从控件上选择的科目
                if (!StringUtil.isEmpty(sub_code)) {
                    map.put("sub_code", sub_code.trim());
                } else {
                    map.put("sub_code", "1001");
                }
                /*** type=3**查询明细账的条件 start ***/
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {

                    if (beginTime.equals(endTime)) {
                        map.put("period", beginTime);
                    } else {
                        map.put("beginTime", beginTime);
                        map.put("endTime", endTime);
                    }
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(beginTime)) {
                    map.put("period", beginTime);
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime);
                    minPeiod = endTime;
                } else {
                    map.put("period", period);
                    minPeiod = period;
                }
                if (!StringUtil.isEmpty(yeNotZero)) {
                    map.put("yeNotZero", "1");
                } /*** type=3**查询明细账的条件 end ***/

            } else if ("2".equals(type)) {

                /*************** 来自筛选框的条件 start *****************/
                // 先根据筛选框起始科目与结束科目查询科目余额表数据，从而得到科目范围数据
                // 2 再从科目数据里面取第一条作为明细账数据
                Map<String, Object> querySub = new HashMap<>();
                querySub.put("accountID", accountID);
                // 1拼接科目 查询 条件
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    querySub.put("period", endTime);
                } else if (!StringUtil.isEmpty(beginTime)) {
                    querySub.put("period", beginTime);
                } else if (!StringUtil.isEmpty(endTime)) {
                    querySub.put("period", endTime);
                } else {
                    // 开始期间结束期间都没选的话 期间使用当前账套期间
                    querySub.put("period", period);
                }
                // 2 查询 开始科目 结束科目
                if (!StringUtil.isEmpty(beginSubCode) || !StringUtil.isEmpty(endSubCode)) {
                    if (!StringUtil.isEmpty(beginSubCode) && !StringUtil.isEmpty(endSubCode)) {
                        if (beginSubCode.equals(endSubCode)) {
                            querySub.put("sub_code", endSubCode.trim());
                        } else {
                            querySub.put("beginSubCode", beginSubCode.trim());
                            querySub.put("endSubCode", getCodeByEndSubCode(endSubCode.trim()));
                        }
                    } else if (!StringUtil.isEmpty(beginSubCode)) {
                        querySub.put("only_beginSubCode", beginSubCode.trim());
                    } else if (!StringUtil.isEmpty(endSubCode)) {
                        querySub.put("only_endSubCode", getCodeByEndSubCode(endSubCode.trim()));
                    }
                } else {
                    // 开始科目结束科目都没选的话 表示查询全部科目数据
                    //System.out.println("");
                }
                // 3 查询 起始级别 结束级别
                if (!StringUtil.isEmpty(start_level) || !StringUtil.isEmpty(end_level)) {
                    if (!StringUtil.isEmpty(start_level) && !StringUtil.isEmpty(end_level)) {
                        if (start_level.equals(end_level)) {
                            querySub.put("code_level", Integer.valueOf(start_level));
                        } else {
                            querySub.put("start_level", Integer.valueOf(start_level));
                            querySub.put("end_level", Integer.valueOf(end_level));
                        }
                    } else if (!StringUtil.isEmpty(start_level)) {

                        querySub.put("start_level", start_level);
                        querySub.put("end_level", 4);

                    } else if (!StringUtil.isEmpty(end_level)) {
                        querySub.put("start_level", 1);
                        querySub.put("end_level", Integer.valueOf(end_level));
                    }
                } else {
                    // 起始级别与结束级别都没选的话 表示查询全部级别数据
                    //System.out.println("");
                }
                boolean a = false;
                //
                if (StringUtil.isEmpty(beginSubCode) && StringUtil.isEmpty(endSubCode)) {
                    if (StringUtil.isEmpty(start_level) && StringUtil.isEmpty(end_level)) {
                        a = true;
						/*if (StringUtil.isEmpty(beginTime) && StringUtil.isEmpty(endTime)) {
							a = true;
						}*/
                    }
                }
                // 开始科目结束科目都没选的话 取1001作为默认科目查询数据
                if (a == true) {
                    map.put("sub_code", "1001");
                } else {
                    // 查询科目数据 即树形控件的数据
                    SubMessageVo sb = subBookDao.queryDetailSub2(querySub);
                    if (sb != null) {
                        map.put("sub_code", sb.getSub_code());
                    } else {
                        if (!StringUtil.isEmpty(beginSubCode)) {
                            map.put("sub_code", beginSubCode.trim());
                        } else if (!StringUtil.isEmpty(endSubCode)) {
                            map.put("sub_code", endSubCode.trim());
                        } else {
                            // 没有查询到符合条件的科目数据
                            res.put("code", "1");
                            res.put("msg", "科目异常");
                            return res;
                        }
                    }
                }

                /*************** 查询明细账的条件 start *****************/
                // 1 期间
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    if (beginTime.equals(endTime)) {
                        map.put("period", beginTime);
                    } else {
                        map.put("beginTime", beginTime);
                        map.put("endTime", endTime);
                    }
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(beginTime)) {
                    map.put("period", beginTime);
                    minPeiod = beginTime;
                } else if (!StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime);
                    minPeiod = endTime;
                } else {
                    map.put("period", period);
                    minPeiod = period;
                }
                // 2 明细账余额不为0
                if (!StringUtil.isEmpty(yeNotZero)) {
                    map.put("yeNotZero", "1");
                }
                /*************** 查询明细账的条件 end *****************/

            }

            /*************** 来自筛选框的条件 end *****************/
            if (map.get("sub_code") == null) {
                res.put("code", "2");
                res.put("msg", "科目异常");
                return res;
            }

            map.put("minPeiod", minPeiod);
            // 6 条用业务层查询明细账
            Map<String, Object> mapAcc = subBookService.queryDetailAccount(map);
            if (mapAcc != null && mapAcc.get("result") != null) {
                System.out.println();
            } else {
                res.put("code", "3");
                res.put("msg", "暂无数据");
                return res;
            }

            /*****************************************************************************/
            List<String> perList = (List<String>) mapAcc.get("perList");
            List<Map<String, Object>> subBookList = (List<Map<String, Object>>) mapAcc.get("result");

            Map<String, Object> param = new HashMap<>();
            param.put("accountID", getAccount());
            param.put("period", perList);
            param.put("sub_code", map.get("sub_code"));
            param.put("subName", mapAcc.get("subName"));
            param.put("subBookList", subBookList);

            HSSFWorkbook book = subBookService.exportDetailAccount(param);

            String days = DateUtil.getDays(); // yyyyMMdd
            String fileNanem = days + "明细账.xls"; // 2018824_数量金额总账.xls

            ServletOutputStream os = response.getOutputStream();
            String downFileName = URLEncoder.encode(fileNanem, "utf-8");
            //response.setContentType("application/octet-stream;charset=UTF-8"); // 设置为流下载
            //response.setContentType("application/vnd.ms-excel"); //设定一个下载类型
            response.setContentType("application//x-xls;charset=UTF-8"); //设定一个下载类型
            response.setHeader("Content-Disposition", "attachment;filename=\"" + downFileName + "\"");

            book.write(os);
            os.flush();
            os.close();
            response.flushBuffer();
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            logger.error("SubBookController【getRangePeriod】查询期间异常!", e);
            e.printStackTrace();
            res.put("code", "5");
            res.put("msg", "暂无数据");
            return res;

        }

    }


    // 明细账导出
    @SuppressWarnings({"unchecked", "unused"})
    @RequestMapping("/exportAllDetailAccount")
    @ResponseBody
    Map<String, Object> exportAllDetailAccount(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "subCode", required = false) String sub_code,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "beginSubCode", required = false) String beginSubCode,
            @RequestParam(value = "endSubCode", required = false) String endSubCode,
            @RequestParam(value = "startLevel", required = false) String start_level,
            @RequestParam(value = "endLevel", required = false) String end_level,
            @RequestParam(value = "ye", required = false) String yeNotZero) {
        Map<String, Object> res = new HashMap<>();
        try {

            Map<String, Object> map = new HashMap<>();
            String accountID = getAccount().getAccountID();
            String period = getUserDate();

            map.put("accountID", accountID);
            map.put("acc", getAccount());

            if ("1".equals(type)) {
                // 总账或者明细账菜单
                map.put("period", period);
            } else if ("2".equals(type) || "3".equals(type)) {
                // 筛选框点击查询
                //1 科目范围 查询条件
                boolean empty = StringUtil.isEmpty(beginSubCode);
                boolean empty2 = StringUtil.isEmpty(endSubCode);
                if (!StringUtil.isEmpty(beginSubCode) && !StringUtil.isEmpty(endSubCode)) {
                    if (beginSubCode.equals(endSubCode)) {
                        map.put("sub_code", beginSubCode);
                    } else {
                        map.put("beginSubCode", beginSubCode);
                        map.put("endSubCode", endSubCode);
                    }
                } else if (!StringUtil.isEmpty(beginSubCode)) {
                    map.put("sub_code", beginSubCode);
                } else if (!StringUtil.isEmpty(endSubCode)) {
                    map.put("sub_code", endSubCode);
                } else {
                    //没有选择科目处理默认查询全部科目
                    System.out.println("查询全部科目");
                }

                // 2起始级别 结束级别 查询条件
                if (!StringUtil.isEmpty(start_level) || !StringUtil.isEmpty(end_level)) {
                    if (!StringUtil.isEmpty(start_level) && !StringUtil.isEmpty(end_level)) {
                        if (start_level.equals(end_level)) {
                            map.put("code_level", Integer.valueOf(start_level.trim()));
                        } else {
                            map.put("start_level", Integer.valueOf(start_level.trim()));
                            map.put("end_level", Integer.valueOf(end_level.trim()));
                        }
                    } else if (!StringUtil.isEmpty(start_level.trim())) {

                        map.put("start_level", start_level.trim());
                        map.put("end_level", 4);

                    } else if (!StringUtil.isEmpty(end_level)) {
                        map.put("start_level", 1);
                        map.put("end_level", Integer.valueOf(end_level.trim()));
                    }
                } else {
                    // 起始级别与结束级别都没选的话 表示查询全部级别数据
                    //没有选择等级的话 默认查询全部等级
                    System.out.println("默认全部级别");
                }

                // 3 期间范围查询条件判断
                if (!StringUtil.isEmpty(beginTime) && !StringUtil.isEmpty(endTime)) {
                    if (beginTime.equals(endTime)) {
                        map.put("period", beginTime);
                    } else {
                        map.put("beginTime", beginTime);
                        map.put("endTime", endTime);
                    }
                } else if (!StringUtil.isEmpty(beginTime)) {
                    map.put("period", beginTime);
                } else if (!StringUtil.isEmpty(endTime)) {
                    map.put("period", endTime);
                } else {
                    //没有选择期间默认 查询当前期间
                    map.put("period", period);
                }
                // 2 明细账余额不为0
                if (!StringUtil.isEmpty(yeNotZero)) {
                    map.put("yeNotZero", "1");
                }
            } else {

                System.out.println("type null ");
            }
            // 调用业务层查询明细账数据
            Map<String, Object> mapAcc = subBookService.queryAllSubBook(map);
            if (mapAcc == null || mapAcc.get("book") == null || mapAcc.get("code").toString().equals("2")) {
                res.put("code", "22");
                res.put("msg", mapAcc.get("msg"));
                return res;
            }
            HSSFWorkbook book = (HSSFWorkbook) mapAcc.get("book");

            String days = DateUtil.getDays(); // yyyyMMdd
            String fileNanem = days + "明细账.xls"; // 2018824_数量金额总账.xls

            ServletOutputStream os = response.getOutputStream();
            String downFileName = URLEncoder.encode(fileNanem, "utf-8");
            response.setContentType("application//x-xls;charset=UTF-8"); //设定一个下载类型
            response.setHeader("Content-Disposition", "attachment;filename=\"" + downFileName + "\"");

            book.write(os);
            os.flush();
            os.close();
            response.flushBuffer();
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            res.put("code", "5");
            res.put("msg", e.getMessage());
            return res;

        }

    }

    private String getCodeByEndSubCode(String endSubCode) {
        if (endSubCode.length() == 4) {
            //1012   1012 999 999 999
            //查询1012开头的所有子级科目   只要计算出1012 最大的子级就就可以
            return endSubCode + "999999999";
        }
        return endSubCode;
    }

    // 导出利润表
    @SuppressWarnings({"unchecked", "unused"})
    @RequestMapping("/exportProfitStatement ")
    @ResponseBody
    Map<String, Object> exportAllDetailAccount(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();
        try {
            User user = userService.getCurrentUser();
            Account account = userService.getCurrentAccount(user);
            String period = account.getUseLastPeriod();
            Map<String, Object> map = subBookService.exportProfitStatement(user, account, period);
            HSSFWorkbook book = (HSSFWorkbook) map.get("book");
            String days = DateUtil.getDays();
            String fileNanem = days + "利润表.xls";
            ServletOutputStream os = response.getOutputStream();
            String downFileName = URLEncoder.encode(fileNanem, "utf-8");
            response.setContentType("application//x-xls;charset=UTF-8"); //设定一个下载类型
            response.setHeader("Content-Disposition", "attachment;filename=\"" + downFileName + "\"");

            book.write(os);
            os.flush();
            os.close();
            response.flushBuffer();
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            res.put("code", "5");
            res.put("msg", e.getMessage());
            return res;

        }

    }


}

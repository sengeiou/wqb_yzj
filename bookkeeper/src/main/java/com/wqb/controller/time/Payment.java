package com.wqb.controller.time;

import com.alibaba.fastjson.JSONObject;
import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.controller.BaseController;
import com.wqb.dao.account.AccountDao;
import com.wqb.dao.pay.DzPayDao;
import com.wqb.dao.user.UserDao;
import com.wqb.httpClient.HttpClientUtil;
import com.wqb.model.DzPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Controller
public class Payment extends BaseController {
    @Autowired
    UserDao userDao;
    @Autowired
    DzPayDao dzPayDao;
    @Autowired
    AccountDao accountDao;
    private static Properties properties = null;
    private static Properties pro1 = null;
    private static Log4jLogger logger = Log4jLogger.getLogger(Payment.class);
    private static String interUrl = "Customer/Chargeback";
    private HttpClientUtil httpClientUtil = new HttpClientUtil();

    // 读取配置文件
    static {
        InputStream inStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config/pay.properties");

        properties = new Properties();
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        InputStream inStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config/address.properties");

        pro1 = new Properties();
        try {
            pro1.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 代账扣费
    public void dzPayFun() {
        try {
            // String busDate = getUserDate();
            // 查询出所有代账管理员
            // "0 15 10 15 * ?"每月15号的10：15触发
            String zwMonth = DateUtil.getSysLastMonth();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("zwMonth", zwMonth);
            param.put("zwType", 2);
            List<DzPay> list = dzPayDao.queryDzPay(param);
            StringBuffer para = new StringBuffer();
            if (list != null && list.size() != 0 && !list.isEmpty()) {
                for (DzPay dzPay : list) {
                    StringBuffer sb = new StringBuffer();
                    String ptID = dzPay.getPtID();
                    long count = dzPay.getRecordTime();
                    long money = 0;
                    String des = null;
                    if (count > Integer.parseInt(properties.get("dzMinRule").toString())) {
                        Integer dzSiglePrice = Integer.parseInt(properties.get("dzPrice").toString());
                        money = count * dzSiglePrice;
                        des = "云代账扣费明细:平台用户ID【" + ptID + "】,月份【" + zwMonth + "】,结账次数【" + count + "】,扣费金额【" + money
                                + "】";
                    } else {
                        money = Integer.parseInt(properties.get("dzMinPay").toString());
                        des = "云代账扣费明细:平台用户ID【" + ptID + "】,月份【" + zwMonth + "】,结账次数【" + count + "】,扣费金额【" + money
                                + "】";
                    }
                    sb.append(ptID).append("$").append(money).append("$").append(des);
                    para.append(sb).append("|");
                }
                // System.out.println(para.toString());
            }
            param.put("zwType", 1);
            List<DzPay> jzList = dzPayDao.queryDzPay(param);
            if (jzList != null && jzList.size() != 0 && !jzList.isEmpty()) {
                for (DzPay dzPay : jzList) {
                    StringBuffer sb = new StringBuffer();
                    String ptID = dzPay.getPtID();
                    long count = dzPay.getRecordTime();
                    double money = count * Double.parseDouble(properties.get("jzPrice").toString());
                    String des = "云记账扣费明细:平台用户ID【" + ptID + "】,月份【" + zwMonth + "】,结账次数【" + count + "】,扣费金额【" + money
                            + "】";
                    sb.append(ptID).append("$").append(money).append("$").append(des);
                    para.append(sb).append("|");
                }
            }
            String url = pro1.getProperty("wqb_url").trim() + "/" + interUrl;
            Map<String, String> createMap = new HashMap<String, String>();
            createMap.put("temp", para.toString());
            createMap.put("dataType", "json");
            String httpOrgCreateTestRtn = httpClientUtil.doPost(url, createMap, pro1.getProperty("wqb_charset"));// {"success":true,"msg":"操作成功","data":[]}
            Map<?, ?> maps = (Map<?, ?>) JSONObject.parse(httpOrgCreateTestRtn);// {msg=操作成功,
            // data=[Ljava.lang.Object;@1c78d7,
            // success=true}
            Object successStr = maps.get("success");
            boolean success = false;
            if (null != successStr && !"".equals(successStr)) {
                success = Boolean.parseBoolean(successStr.toString().trim());
            }

            // "data":[{"CustomerId":"94718b47-0ab4-4c7e-a782-1cc269b22296","IsSucess":true,"Desc":"当月已扣过"}]
            Object[] obj = (Object[]) maps.get("data");
            if (obj.length > 0) {
                for (int i = 0; i < obj.length; i++) {
                    Map<String, Object> map = (Map<String, Object>) obj[i];
                    Object isSucess = map.get("IsSucess");
                    String CustomerId = map.get("CustomerId").toString();
                    if (null != isSucess) {
                        boolean suc = Boolean.parseBoolean(isSucess.toString());
                        Map<String, Object> pa = new HashMap<String, Object>();
                        pa.put("ptID", CustomerId);
                        pa.put("zwMonth", zwMonth);
                        if (!suc) {
                            // 写入记录表(扣费失败)
                            pa.put("payResult", 2);
                            dzPayDao.updPayResult(pa);
                        } else {
                            pa.put("payResult", 1);
                            dzPayDao.updPayResult(pa);
                        }
                    }
                }
            }

        } catch (BusinessException e) {
            logger.error("云记账,代账扣费引发异常", e);
        } catch (Exception e) {
            logger.error("云代,账扣费引发异常", e);
        }
    }
}

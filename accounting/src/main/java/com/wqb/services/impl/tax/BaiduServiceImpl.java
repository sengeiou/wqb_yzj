package com.wqb.services.impl.tax;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wqb.commons.HttpClientUtils;
import com.wqb.commons.PropertiesUtil;
import com.wqb.commons.emun.ResultStatus;
import com.wqb.commons.emun.TicketType;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.invoice.InvoiceBody;
import com.wqb.domains.invoice.InvoiceHeader;
import com.wqb.domains.tax.BaiduVatTicke;
import com.wqb.domains.tax.Ticket;
import com.wqb.services.impl.subject.SubjectBalanceServiceImpl;
import com.wqb.services.invoice.InvoiceBodyService;
import com.wqb.services.invoice.InvoiceHeaderService;
import com.wqb.services.tax.TaxService;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("BaiduService")
public class BaiduServiceImpl implements TaxService {

    private static final String URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice";
    private static final String CLIENTID = "5m9xP58EV2Aaeh8OXES4NFdN";
    private static final String CLIENT_SECRET = "ZfexK1EGE40HsfHUm7fgX4ipCeivOEe5";
    private static final String CONFIG_PATH = new File("src/main/resources/config.properties").getPath();
    private static final Logger logger = LoggerFactory.getLogger(SubjectBalanceServiceImpl.class);

    @Autowired
    private InvoiceHeaderService invoiceHeaderService;

    @Autowired
    private InvoiceBodyService invoiceBodyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Ticket getVatTicke(byte[] ticketImage, User user, Account account) {
        Ticket ticket = new Ticket();
        String res = null;
        long tokenTime = 0, expiresIn = 0;
        try {
            String token = PropertiesUtil.getProperties(CONFIG_PATH, "access_token");
            String tokenTimeStr = PropertiesUtil.getProperties(CONFIG_PATH, "token_time");
            if (StringUtils.isBlank(tokenTimeStr))
                tokenTime = Long.parseLong(tokenTimeStr);
            String expiresInStr = PropertiesUtil.getProperties(CONFIG_PATH, "expires_in");
            if (StringUtils.isBlank(expiresInStr))
                expiresIn = Long.parseLong(expiresInStr);
            if (expired(tokenTime, expiresIn)) {
                token = getAuth(CLIENTID, CLIENT_SECRET);
            }

//            byte[] data = ticketImage.getBytes();// image2byte("F:\\Doc\\Iovoice\\222.png");
            String encodeStr = Base64.getEncoder().encodeToString(ticketImage);
            Map<String, String> map = new HashMap<>();
            map.put("access_token", token);
            map.put("image", encodeStr);
            map.put("accuracy", "high");
            res = HttpClientUtils.post(URL, map);
            JSONObject jsonObject = JSON.parseObject(res);
            Object errorCode = jsonObject.get("error_code");
            Object code = jsonObject.get("code");
            if (errorCode == null) {
                ticket = vatConvert(jsonObject, user, account);
                // 获取发票信息
                InvoiceHeader invoiceHd = ticket.getInvoiceHeader();
                List<InvoiceBody> invoiceBodies = ticket.getInvoiceBodies();
                if (ObjectUtils.anyNotNull(invoiceHd, invoiceBodies)) {
                    InvoiceHeader header = invoiceHeaderService.getOne(invoiceHd);
                    if (header == null) {
                        if (!invoiceHeaderService.save(invoiceHd)) {
                            throw new WqbException("保存发票头信息失败");
                        }
                        if (!invoiceBodyService.saveBatch(invoiceBodies)) {
                            throw new WqbException("保存发票体信息失败");
                        }
                    } else {
                        if (!invoiceHeaderService.updateById(invoiceHd)) {
                            throw new WqbException("修改发票头信息失败");
                        }
                        if (!invoiceBodyService.updateBatchById(invoiceBodies)) {
                            throw new WqbException("修改发票体信息失败");
                        }
                    }
                } else {
                    throw new WqbException("找不到发票信息");
                }
            } else {
                ticket.setResultStatus(ResultStatus.FAIL);
                ticket.setFailReason(jsonObject.getString("error_msg"));
                ticket.setCode(errorCode.toString());
//            throw new BizException(jsonObject.getString("error_msg"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ticket.setResultStatus(ResultStatus.FAIL);
            ticket.setFailReason(e.getMessage());
            throw new BizException("图片转换失败！");
        } catch (IOException e) {
            e.printStackTrace();
            ticket.setResultStatus(ResultStatus.FAIL);
            ticket.setFailReason(e.getMessage());
            throw new BizException("请求数据失败！");
        } catch (Exception e) {
            e.printStackTrace();
            ticket.setResultStatus(ResultStatus.FAIL);
            ticket.setFailReason(e.getMessage());
            throw new BizException("请求数据失败！");
        }
        return ticket;
    }

    @Override
    public Ticket getVatTicke(MultipartFile ticketImage, User user, Account account) {
        Ticket ticket = new Ticket();
        try {
            ticket = getVatTicke(ticketImage.getBytes(), user, account);
        } catch (Exception e) {
            e.printStackTrace();
            ticket.setResultStatus(ResultStatus.FAIL);
            ticket.setFailReason(e.getMessage());
            throw new BizException("请求数据失败！");
        }
        return ticket;
    }

    @Override
    public Ticket getVatTicke(String ticketId, User user, Account account) {
        Ticket ticket = new Ticket();
        ticket.setResultStatus(ResultStatus.FAIL);
        ticket.setFailReason("百度api不提供该接口请求");
        return ticket;
//        throw new BizException("百度api不提供该接口请求！");
    }


    private Ticket vatConvert(JSONObject jsonObject, User user, Account account) {
        Ticket ticket = new Ticket();
        InvoiceHeader header = new InvoiceHeader();
        ArrayList<InvoiceBody> invoiceBodies = new ArrayList<>();
        InvoiceBody body = new InvoiceBody();
        try {
            ticket.setResultStatus(ResultStatus.SUCCESS);
            ticket.setTicketType(TicketType.Invoice);
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
            ticket.setCreateTime(dateFormat.format(new Date()));
            BaiduVatTicke vatTicke = JSONObject.parseObject(jsonObject.getString("words_result"), BaiduVatTicke.class);
            header.setInvoiceHID(UUID.randomUUID().toString().replaceAll("-", ""));
            header.setPeriod(null);
            header.setInvoiceType("0");
            header.setInvoiceCode(vatTicke.getInvoiceCode());
            header.setInvoiceNumber(vatTicke.getInvoiceNum());
            header.setInvoiceName(vatTicke.getInvoiceType());
            header.setInvoiceState("正常");
            header.setInvoiceDate(vatTicke.getInvoiceDate());
//        header.setInvoicePerson("");
            header.setBuyTaxno(vatTicke.getPurchaserRegisterNum());
            header.setBuyCorp(vatTicke.getPurchaserName());
            header.setBuyBankno(vatTicke.getPurchaserBank());
//        header.setAddress(vatTicke.);
            header.setSaleTaxno(vatTicke.getSellerRegisterNum());
            header.setSaleCorp(vatTicke.getSellerName());
            header.setSaleBankno(vatTicke.getSellerBank());
//        header.setCreatePsnID("");
//        header.setCreateDate(null);
//        header.setCreatepsn("");
            header.setPeriod(account.getPeriod());
            header.setAccountID(account.getAccountID());
//        header.setProductVersion("19.0");
            ticket.setInvoiceHeader(header);
            body.setInvoiceBID(UUID.randomUUID().toString().replaceAll("-", ""));
            body.setInvoiceHID(header.getInvoiceHID());
            body.setUserID(user.getUserID());
            body.setPeriod(account.getPeriod().toString());
            body.setAccountID(account.getAccountID());
            if (vatTicke.getCommodityTaxRate() != null) {
                NumberFormat percentInstance = NumberFormat.getPercentInstance();
                Number rate = percentInstance.parse(vatTicke.getCommodityTaxRate().replaceAll("\\[.+\\\"row\\\":.*?,\\\"word\\\":\\\"(.*?)\\\"+[^\\]]+\\]", "$1"));
                body.setTaxRate(BigDecimal.valueOf((Double) rate));
            }
            body.setNtaxAmount(vatTicke.getAmountInFiguers());
            body.setTaxAmount(vatTicke.getTotalTax());
            body.setNamount(vatTicke.getTotalAmount());
            if (vatTicke.getCommodityName() != null) {
                body.setComName(vatTicke.getCommodityName().replaceAll("\\[.+\\\"row\\\":.*?,\\\"word\\\":\\\"(.*?)\\\"+[^\\]]+\\]", "$1"));
            }
//            body.setNnumber(vatTicke.getCommodityNum());
            invoiceBodies.add(body);
            ticket.setInvoiceBodies(invoiceBodies);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BizException("数据转换失败");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("获取发票失败");
        }
        return ticket;
    }

//    public static byte[] image2byte(String path) {
//        byte[] data = null;
//        FileImageInputStream input = null;
//        try {
//            input = new FileImageInputStream(new File(path));
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            byte[] buf = new byte[1024];
//            int numBytesRead = 0;
//            while ((numBytesRead = input.read(buf)) != -1) {
//                output.write(buf, 0, numBytesRead);
//            }
//            data = output.toByteArray();
//            output.close();
//            input.close();
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return data;
//    }

    private Boolean expired(Long tokenTime, Long expiresIn) {
        long now = new Date().getTime();
        if ((now - tokenTime) / 1000 > expiresIn - 3600) {//一小时后到期,秒为单位
            return true;
        }
        return false;
    }

    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            java.net.URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                logger.error(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            //token信息存入配置文件
            PropertiesUtil.setProperties(CONFIG_PATH, "access_token", access_token);
            PropertiesUtil.setProperties(CONFIG_PATH, "token_time", String.valueOf(new Date().getTime()));
            PropertiesUtil.setProperties(CONFIG_PATH, "expires_in", jsonObject.getString("expires_in"));
            return access_token;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new BizException("获取token失败!");
        }
    }
}

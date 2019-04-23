package com.wqb.services.impl.tax;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wqb.commons.HttpClientUtils;
import com.wqb.commons.XmlMapUtil;
import com.wqb.commons.ZipUtil;
import com.wqb.commons.emun.ResultStatus;
import com.wqb.commons.emun.TicketType;
import com.wqb.commons.vo.JyqqcsVO;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.invoice.Invoice;
import com.wqb.domains.invoice.InvoiceBody;
import com.wqb.domains.invoice.InvoiceHeader;
import com.wqb.domains.tax.Goods;
import com.wqb.domains.tax.TickeDetails;
import com.wqb.domains.tax.Ticket;
import com.wqb.services.impl.subject.SubjectBalanceServiceImpl;
import com.wqb.services.invoice.InvoiceBodyService;
import com.wqb.services.invoice.InvoiceHeaderService;
import com.wqb.services.tax.TaxService;
import com.wqb.supports.exceptions.BizException;
import com.wqb.supports.exceptions.WqbException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("GuoPiaoService")
public class GuoPiaoServiceImpl implements TaxService {
    //测试环境
    private static final String URL = "http://218.94.72.202:19090/vat-tgw/api/callservice.do";
    private static final String CLIENTID = "14997271100";
    private static final String TOKEN = "7ddbad2830cd11e9976c00505685c924";
    //正式环境
//    private static final String URL = "http://zhipiaoyun.com/vat-tgw/api/callservice.do";
//    private static final String CLIENTID = "91440300083862406C";
//    private static final String TOKEN = "3590aadc-1fb4-11e9-877c-c0bfc0306721";

    private static final Logger logger = LoggerFactory.getLogger(SubjectBalanceServiceImpl.class);

    @Autowired
    private InvoiceHeaderService invoiceHeaderService;

    @Autowired
    private InvoiceBodyService invoiceBodyService;

    @Override
    public Ticket getVatTicke(byte[] ticketImage, User user, Account account) {
        Ticket ticket = new Ticket();
        String encodeStr = null;
        try {
//            byte[] data = ticketImage.getBytes();
            encodeStr = Base64.getEncoder().encodeToString(ticketImage);
            Map<String, String> map = new HashMap<>();
            map.put("ticketImage", encodeStr);
            String res = httpPost(getRequestParams(map, "FW_HTTP_PJSB"), URL);
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject == null) {
                ticket.setResultStatus(ResultStatus.FAIL);
                return ticket;
            }
            Object ticketId = jsonObject.get("ticketId");
            if (ticketId != null)
                ticket = getVatTicke(ticketId.toString(), user, account);
            else {
                ticket.setResultStatus(ResultStatus.FAIL);
                ticket.setFailReason(jsonObject.getString("message"));
                ticket.setCode(jsonObject.getString("code"));
//            throw new BizException(jsonObject.getString("message"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("获取发票失败");
        }
        return ticket;
    }

    @Override
    public Ticket getVatTicke(MultipartFile ticketImage, User user, Account account) {
        Ticket ticket = new Ticket();
        String encodeStr = null;
        try {
            ticket = getVatTicke(ticketImage.getBytes(), user, account);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("图片转换失败");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("获取发票失败");
        }
        return ticket;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Ticket getVatTicke(String ticketId, User user, Account account) {
        Ticket ticket = new Ticket();
        Map<String, String> map = new HashMap<>();
        map.put("ticketId", ticketId);
        ticket = http(map, "FW_HTTP_PJXQ", user, account);
        return ticket;
    }

    private Ticket http(Map<String, String> map, String serviceId, User user, Account account) {
        Ticket ticket = new Ticket();
        String res = httpPost(getRequestParams(map, serviceId), URL);
        JSONObject jsonObject = JSON.parseObject(res);
        Object code = jsonObject.get("code");
        if (code == null && jsonObject.getString("resultStatus") != null &&
                ResultStatus.codeOf(jsonObject.getString("resultStatus")) == ResultStatus.SUCCESS) {
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
            ticket.setFailReason(jsonObject.getString("failReason"));
            ticket.setCode(jsonObject.getString("code"));
            throw new BizException(jsonObject.getString("failReason"));
        }
        return ticket;
    }


    private Ticket vatConvert(JSONObject jsonObject, User user, Account account) {
        Ticket ticket = new Ticket();
        InvoiceHeader header = new InvoiceHeader();
        ArrayList<InvoiceBody> invoiceBodies = new ArrayList<>();
        InvoiceBody body = new InvoiceBody();
        try {
            ticket.setResultStatus(ResultStatus.codeOf(jsonObject.getString("resultStatus")));
            ticket.setTicketType(TicketType.codeOf(jsonObject.getString("ticketType")));
            ticket.setTicketId(jsonObject.getString("ticketId"));
//        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
            ticket.setCreateTime(jsonObject.getString("createTime"));
            TickeDetails vatTicke = JSONObject.parseObject(jsonObject.getString("dataDetails"), TickeDetails.class);
            header.setInvoiceHID(UUID.randomUUID().toString().replaceAll("-", ""));
            header.setPeriod(null);
        header.setInvoiceType("0");
            header.setInvoiceCode(vatTicke.getInvoiceCode());
            header.setInvoiceNumber(vatTicke.getInvoiceNumber());
            header.setInvoiceName(vatTicke.getInvoiceType());
            header.setInvoiceState("正常");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            header.setInvoiceDate(sdf.parse(vatTicke.getBillingDate()));
//        header.setInvoicePerson("");
            header.setBuyTaxno(vatTicke.getBuyerTaxNumber());
            header.setBuyCorp(vatTicke.getBuyerTaxName());
            header.setBuyBankno(vatTicke.getBuyerBankAccount());
            header.setAddress(vatTicke.getBuyerAddressPhone());
            header.setSaleTaxno(vatTicke.getSellerTaxNumber());
            header.setSaleCorp(vatTicke.getSellerTaxName());
            header.setSaleBankno(vatTicke.getSellerBankAccount());
//        header.setCreatePsnID("");
//        header.setCreateDate(null);
//        header.setCreatepsn("");
//        header.setProductVersion("19.0");
            header.setPeriod(account.getPeriod());
            header.setAccountID(account.getAccountID());
            ticket.setInvoiceHeader(header);
            body.setInvoiceBID(UUID.randomUUID().toString().replaceAll("-", ""));
            body.setInvoiceHID(header.getInvoiceHID());
            body.setUserID(user.getUserID());
            body.setPeriod(account.getPeriod().toString());
            body.setAccountID(account.getAccountID());
            JSONObject details = JSON.parseObject(jsonObject.getString("dataDetails"));
            List<Goods> goods = JSONObject.parseArray(details.getString("goods"), Goods.class);
            if (goods.get(0).getTaxRate() != null) {
                body.setTaxRate(BigDecimal.valueOf(goods.get(0).getTaxRate() * 0.01));
            }
            body.setComName(goods.get(0).getGoodsName());
            body.setNtaxAmount(vatTicke.getAmount());
            body.setTaxAmount(vatTicke.getTax());
            body.setNamount(vatTicke.getAmountExcludeTax());
//       body.setNnumber(vatTicke.getCommodityNum());
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

    private static JyqqcsVO getRequestParams(Map<String, String> body, String serviceId) {
        JyqqcsVO vo = new JyqqcsVO();
        vo.setServiceid(serviceId);
        vo.setBwlx("json");
        vo.setDealid(UUID.randomUUID().toString().replaceAll("-", ""));
        vo.setClientid(CLIENTID);
        vo.setToken(TOKEN);
        vo.setBody(JSON.toJSONString(body));
        return vo;
    }

    private static String httpPost(JyqqcsVO vo, String url) {
        String encode = ZipUtil.zipEncode(JSON.toJSONString(vo), true);

        Map<String, String> params = new HashMap<String, String>();
        params.put("request", encode);


        String response;
        try {
            response = HttpClientUtils.post(url, params);
            if (response == null) {
                return null;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        logger.info("响应数据：{}", response);

        String res = ZipUtil.unzipDecode(response, true);

        return XmlMapUtil.parseRes(res);
    }
}

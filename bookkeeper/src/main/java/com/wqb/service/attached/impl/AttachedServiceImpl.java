package com.wqb.service.attached.impl;

import com.wqb.common.BeanRefUtil;
import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.attached.AttachedDao;
import com.wqb.dao.subject.TBasicSubjectMessageMapper;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.SubjectMessage;
import com.wqb.model.Voucher;
import com.wqb.model.VoucherBody;
import com.wqb.model.VoucherHead;
import com.wqb.service.attached.AttachedService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

//附加税结转
@Component
@Service("attachedService")
public class AttachedServiceImpl implements AttachedService {
    private Map<String, String> invo = new HashMap<>();
    private Map<String, Object> vcq = new HashMap<>();
    private String period;  //期间
    private String account_id;  //账套
    private String user_id;  //用户id
    private String user_name;  //用户id


    @Autowired
    AttachedDao attachedDao;
    @Autowired
    VatDao vatDao;
    @Autowired
    VatService vatService;
    @Autowired
    TBasicSubjectMessageMapper tBasicSubjectMessageMapper;

    @Override
    //@Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> fjsCarryover(Map<String, Object> param) throws BusinessException {
        //附加税结转需要查询应交增值税有没有 有的话才可以结转 没有的话不结转
        //结转金额比例为增值税的12%   7,3,2
        initParm(param);
        String subcode = null;
        SubjectMessage maxSub2 = null;
        Map<String, Object> result = new HashMap<>();
        try {
            //应交增值税子凭证查询
            vcq.put("vcabstact", "结转本月增值税");
            List<VoucherBody> vbList = vatService.queryVoBody(vcq);  //应交增值税凭证分录
            if (vbList == null) {
                result.put("result", "结转增值税为0，本期没有附加税结转");
                result.put("message", "fail");
                return result;
            }

            String vohID = vbList.get(0).getVouchID(); //凭证主键
            vcq.clear();
            vcq.put("vouchID", vohID);
            VoucherHead voHeahder = vatService.queryVoHeahder(vcq);  //应交增值税凭证头
            if (voHeahder.getTotalDbit() == null) {
                throw new BusinessException("结转增值税异常");
            }

            Double dbit = voHeahder.getTotalDbit();
            if (dbit == 0.0 || dbit == 0.00) {
                result.put("result", "本期没有附加税结转");
                result.put("message", "fail");
                return result;
            }

            String totalDbit = voHeahder.getTotalDbit().toString();//凭证借方金额合计

            SubjectMessage yjTaxes1 = null;  //应交税费
            SubjectMessage yiyeTaxes1 = null;  //营业税金及附加
            SubjectMessage subCity2 = null;  //应交城市维护建设税
            SubjectMessage subEducation2 = null;  //应交城市维护建设税
            SubjectMessage subLocalEducation2 = null;  //地方教育费附加

            //科目查询
            yiyeTaxes1 = vatService.querySub("营业税金及附加", "6403", "4");
            if (yiyeTaxes1 == null) {
                yiyeTaxes1 = vatService.createSub("6403", "0", "营业税金及附加", "营业税金及附加");
            }

            yjTaxes1 = vatService.querySub("应交税费", "2221", "4");
            if (yjTaxes1 == null) {
                yjTaxes1 = vatService.createSub("2221", "0", "应交税费", "应交税费");
            }
            subCity2 = vatService.querySub("应交城市维护建设税", "2221", "7");
            if (subCity2 == null) {
                //新的级科目编码
                subcode = vatService.getNumber("2221", "7", "2221000");  //2221013
                subCity2 = vatService.createSub(subcode, "2221", "应交城市维护建设税", "应交税费_应交城市维护建设税");
            }

            subEducation2 = vatService.querySub("教育费附加", "2221", "7");
            if (subEducation2 == null) {
                subcode = vatService.getNumber("2221", "7", "2221000");  //2221013001
                subEducation2 = vatService.createSub(subcode, "2221", "教育费附加", "应交税费_教育费附加");
            }

            subLocalEducation2 = vatService.querySub("地方教育费附加", "2221", "7");
            if (subLocalEducation2 == null) {
                subcode = vatService.getNumber("2221", "7", "2221000");  //2221013001
                subLocalEducation2 = vatService.createSub(subcode, "2221", "地方教育费附加", "应交税费_地方教育费附加");
            }

            String mheader12 = multiplys(totalDbit, "0.12");  //计算税费
            String mheader7 = multiplys(totalDbit, "0.07");
            String mheader3 = multiplys(totalDbit, "0.03");
            String mheader2 = multiplys(totalDbit, "0.02");

            //结果集
            Voucher voucher = new Voucher();
            //创建凭证头
            VoucherHead vohead = new VoucherHead();

            vohead.setVouchID(UUIDUtils.getUUID());//主键
            vohead.setAccountID(account_id);
            vohead.setVcDate(new Date());
            vohead.setCurrency("人民币");  //币别   = 科目计量单位
            vohead.setDes(period.split("-")[1] + "月份附加税结转");  //备注
            vohead.setCurrencyID(null);//币别ID
            vohead.setUpdatePsn(user_id);//修改人
            vohead.setUpdatedate(new Date());//修改时间
            vohead.setUpdatePsnID(user_id);
            vohead.setCreatepsn(user_id);  //创建人
            vohead.setCreatePsnID(user_id);  //创建人ID
            vohead.setCreateDate(System.currentTimeMillis());//创建日期
            vohead.setPeriod(period); //期间
            vohead.setSource(4);  //来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据
            vohead.setTotalCredit(Double.valueOf(mheader12));  //凭证贷方金额合计
            vohead.setTotalDbit(Double.valueOf(mheader12));  //凭证借方金额合计
            vohead.setVouchFlag(1);  //'0:非模凭证1:模板凭证'
            vohead.setVoucherNO(0);  //凭证号
            vatDao.insertVoHead(vohead);

            invo.put("vouchAID", UUIDUtils.getUUID());  //主键
            invo.put("vouchID", vohead.getVouchID());  //凭证主表主键
            invo.put("rowIndex", "1");  //分录号
            invo.put("vcabstact", "结转本月税金");  //摘要
            invo.put("vcsubject", "营业税金及附加");  //科目名称
            invo.put("vcunitID", null);  //计量单位ID
            invo.put("vcunit", "RMB");  //计量单位
            invo.put("debitAmount", mheader12);  //借方金额  12%
            invo.put("creditAmount", null);  //贷方金额
            invo.put("direction", "1");  //方向(1:借2:贷)
            invo.put("subjectID", "6403");  //科目主键
            List<VoucherBody> list = new ArrayList<>();
            VoucherBody voBody1 = new VoucherBody();
            BeanRefUtil.setFieldValue(voBody1, invo);
            list.add(voBody1);
            vatDao.insertVoBody(voBody1);//添加分录1


            invo.put("vouchAID", UUIDUtils.getUUID());  //主键
            invo.put("rowIndex", "2");  //分录号
            invo.put("vcabstact", "结转本月税金");  //摘要
            invo.put("vcsubject", "应交税费_应交城市维护建设税");  //科目名称
            invo.put("debitAmount", null);  //借方金额
            invo.put("creditAmount", mheader7);  //贷方金额
            invo.put("direction", "2");  //方向(1:借2:贷)
            invo.put("subjectID", subCity2.getSub_code());  //科目主键
            VoucherBody voBody2 = new VoucherBody();
            BeanRefUtil.setFieldValue(voBody2, invo);
            list.add(voBody2);
            vatDao.insertVoBody(voBody2);  //添加分录2

            invo.put("vouchAID", UUIDUtils.getUUID());  //主键
            invo.put("rowIndex", "3");  //分录号
            invo.put("vcabstact", "结转本月税金");  //摘要
            invo.put("vcsubject", "应交税费_教育费附加");  //科目名称
            invo.put("debitAmount", null);  //借方金额
            invo.put("creditAmount", mheader3);  //贷方金额
            invo.put("direction", "1");  //方向(1:借2:贷)
            invo.put("subjectID", subEducation2.getSub_code());  //科目主键
            VoucherBody voBody3 = new VoucherBody();
            BeanRefUtil.setFieldValue(voBody3, invo);
            list.add(voBody3);
            vatDao.insertVoBody(voBody3);//添加分录3

            invo.put("vouchAID", UUIDUtils.getUUID());  //主键
            invo.put("rowIndex", "4");  //分录号
            invo.put("vcabstact", "结转本月税金");  //摘要
            invo.put("vcsubject", "应交税费_地方教育费附加");  //科目名称
            invo.put("debitAmount", null);  //借方金额
            invo.put("creditAmount", mheader2);  //贷方金额
            invo.put("direction", "1");  //方向(1:借2:贷)
            invo.put("subjectID", subLocalEducation2.getSub_code());  //科目主键
            VoucherBody voBody4 = new VoucherBody();
            BeanRefUtil.setFieldValue(voBody4, invo);
            list.add(voBody4);
            vatDao.insertVoBody(voBody4);//添加分录4

            voucher.setVoucherHead(vohead);
            voucher.setVoucherBodyList(list);
            //科目更新
            vcq.clear();
            vcq.put("account_id", this.account_id);
            vcq.put("account_period", this.period);
            tBasicSubjectMessageMapper.chgSubAmountByCreate(param, voucher);
            result.put("result", "结转成功");
            result.put("message", "success");
            return result;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    //计算
    private String multiplys(String a, String b) {
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        Double val = b1.multiply(b2).doubleValue();
        return String.valueOf(val);
    }


    //初始化参数
    private void initParm(Map<String, Object> param) {
        this.period = (String) param.get("period");
        this.account_id = (String) param.get("accountID");
        this.user_id = (String) param.get("userID");
        this.user_name = (String) param.get("userName");

        vcq.put("accountID", this.account_id);
        vcq.put("period", this.period);

        invo.put("updatePsnID", this.user_id);  //修改人
        invo.put("updatePsn", this.user_name);  //修改人
        invo.put("updatedate", DateUtil.getTime(new Date()));  //修改时间
        invo.put("accountID", this.account_id);
        invo.put("period", this.period);

        vatService.subinit(param);

    }


}

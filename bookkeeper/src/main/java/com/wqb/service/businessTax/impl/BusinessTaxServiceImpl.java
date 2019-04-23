package com.wqb.service.businessTax.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.businessTax.BusinessTaxDao;
import com.wqb.model.SubjectMessage;
import com.wqb.service.businessTax.BusinessTaxService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("businessTaxService")
public class BusinessTaxServiceImpl implements BusinessTaxService {

    @Autowired
    VatService vatService;
    @Autowired
    BusinessTaxDao busTaxdao;

    //计提企业所得税
    @Override
    //@Transactional(rollbackFor=BusinessException.class)
    public void queryBusinessTax(Map<String, Object> map) throws BusinessException {
        vatService.subinit(map);
        SubjectMessage sub6 = null;

        SubjectMessage sub2 = null;
        SubjectMessage sub22 = null;
        String subcode = null;

        try {
            sub6 = vatService.querySub("所得税费用", "6801", "4");
            if (sub6 == null) {
                sub6 = vatService.createSub("6801", "0", "所得税费用", "所得税费用");
            }

            sub2 = vatService.querySub("应交税费", "2221", "4");
            if (sub2 == null) {
                sub2 = vatService.createSub("2221", "0", "应交税费", "应交税费");
            }

            sub22 = vatService.querySub("企业所得税", "2221", "7");
            if (sub22 == null) {
                subcode = vatService.getNumber("2221", "7", "2221000");
                sub22 = vatService.createSub(subcode, "2221", "企业所得税", "应交税费_企业所得税");
            }


            //作为结果集返回给客户端
		/*		Voucher voucher = new Voucher();
				//构造凭证头  --审核忽略
				VoucherHead voHead = new VoucherHead();
				voHead.setVouchID(UUIDUtils.getUUID());//主键
				voHead.setAccountID(account_id);
				voHead.setVcDate(new Date());
				voHead.setCurrency("人民币");  //币别   = 科目计量单位
				voHead.setDes("结转增值税");  //备注
				voHead.setCurrencyID(null);//币别ID
				voHead.setUpdatePsn(user_name);//修改人
				voHead.setUpdatedate(new Date());//修改时间
				voHead.setUpdatePsnID(user_id);
				voHead.setCreatepsn(user_name);  //创建人
				voHead.setCreatePsnID(user_id);  //创建人ID
				voHead.setCreateDate(new Date());//创建日期
				voHead.setCheckedDate(new Date());
				voHead.setPeriod(this.period); //期间
				voHead.setSource(4);  //来源0:发票1:银行2：固定资产3:工资4:结转损益5.手工凭证 6.单据',
				voHead.setTotalCredit(debit);  //凭证贷方金额合计  ?  //留底金额
				voHead.setTotalDbit(debit);  //凭证借方金额合计    ?  //留底金额
				voHead.setVouchFlag(1);  //'0:非模凭证1:模板凭证'
				voHead.setVoucherNO(0);  //凭证号

				String vouchID = voHead.getVouchID();  //凭证主表ID
				voucher.setVoucherHead(voHead);
				vatDao.insertVoHead(voHead);   //凭证头添加到数据库
*/


        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }








	/*	Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String value = entry.getValue();
			String key = entry.getKey();
		}*/


        SubjectMessage querySub = busTaxdao.queryBusinessTax();
        System.out.println("end........");
    }


}

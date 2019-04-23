package com.wqb.service.stateTrack.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.StringUtil;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.invoice.InvoiceDao;
import com.wqb.dao.track.dao.StateTrackDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.*;
import com.wqb.service.stateTrack.StateTrackService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Service("stateTrackService")
public class StateTrackServiceImpl implements StateTrackService {

    @Autowired
    StateTrackDao stateTrackDao;
    @Autowired
    KcCommodityDao kcCommodityDao;
    @Autowired
    InvoiceDao invoiceDao;
    @Autowired
    VatService vatService;
    @Autowired
    VoucherHeadDao voucherHeadDao;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public StateTrack queryState(Map<String, Object> map) throws BusinessException {
        try {
            if (map.get("busDate") != null) {
                map.put("period", map.get("busDate"));
            }
            StateTrack stateTrack = stateTrackDao.queryState(map);

            if (stateTrack == null) {
                return null;
            }
            Integer isdel = stateTrack.getIsdel();
            Integer type = stateTrack.getType();


            Set<String> keySet = null;
            Set<String> keySet2 = null;
            if (isdel != null && isdel == 1) {

                //如果有一键删除又导入序时薄的话 那么就不能清除库存。
                map.put("source", 11);
                List<VoucherHead> repeatList = voucherHeadDao.queryImportVoucher(map);  //查询是否导入序时薄
                map.remove("source");
                if (null != repeatList && repeatList.size() > 0 && repeatList.get(0) != null && !StringUtil.isEmpty(repeatList.get(0).getVouchID())) {
                    System.out.println("已经导入过序时薄,不需要再清除库存");
                } else {
                    //upkuchu 清除库存。一键删除已经清除库存了，进项销项需要重新入库。如果进销项入库之前客户又去导入进销项发票那么库存又增加了。所有重新入库之前可以再清除一次库存。
                    //如果不考虑客户一键删除之后又重新导入发票那么upkuchu 方法可以不需要
                    //upkuchu(map);
                    //进项 销项 重新入库
                    map.put("invoiceType", "1");
                    Map<String, InvoiceBody> jxMap = invoiceDao.queryNUmAndMount(map);
                    if (jxMap != null && !jxMap.isEmpty()) {

                        if (jxMap != null) {
                            keySet = jxMap.keySet();
                        }
                        //vatService.updateKcCommodity21(jxMap);
                    }

                    map.put("invoiceType", "2");
                    Map<String, InvoiceBody> xxMap = invoiceDao.queryNUmAndMount(map);
                    if (xxMap != null && !xxMap.isEmpty()) {

                        if (jxMap != null) {
                            keySet2 = jxMap.keySet();
                        }
                        //vatService.updateKcCommodity31(xxMap);
                    }
                }

                //更新状态
                map.put("isdel", 0);
                int a = stateTrackDao.upStateByCondition(map);
                if (a < 0) {
                    throw new BusinessException("一键创建凭证更新状态失败");
                }
            }
            return stateTrack;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    //更新状态
/*	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void  upStrackState(Map<String, Object> param) throws BusinessException {
		try {
			// 记录删除状态
			StateTrack sate = queryState2(param);
			Integer type = (Integer) param.get("state_type");
			if (sate == null) {
				vatService.insertStateTrack(type);
			} else {
				param.put("isdel", 1);
				param.put("del_num", sate.getDel_num() + 1);
				param.put("type", type);
				int a = upStateByCondition(param);
				if (a < 0) {
					throw new BusinessException("一键删除更新状态失败");
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}*/


    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> upkuchu(Map<String, Object> map) throws BusinessException {
        Map<String, Object> res = new HashMap<>();
        if (map.get("busDate") != null) {
            map.put("period", map.get("busDate"));
        }
        try {
            List<KcCommodity> list = kcCommodityDao.queryCommodityAll(map);
            if (list == null || list.size() == 0) {
                return null;
            }
            for (int i = 0; i < list.size(); i++) {
                KcCommodity kcc = list.get(i);
                if (kcc == null) {
                    return res;
                }
                Double bq_incomeNum = StringUtil.doubleIsNull(kcc.getBq_incomeNum());
                BigDecimal bq_incomeAmount = StringUtil.bigDecimalIsNull(kcc.getBq_incomeAmount());
                BigDecimal bq_issueAmount = StringUtil.bigDecimalIsNull(kcc.getBq_issueAmount());
                Double bq_issueNum = StringUtil.doubleIsNull(kcc.getBq_issueNum());

                BigDecimal total_incomeAmount = StringUtil.bigDecimalIsNull(kcc.getTotal_incomeAmount());
                Double total_incomeNum = StringUtil.doubleIsNull(kcc.getTotal_incomeNum());
                BigDecimal total_issueAmount = StringUtil.bigDecimalIsNull(kcc.getTotal_issueAmount());
                Double total_issueNum = StringUtil.doubleIsNull(kcc.getTotal_issueNum());

                Double qc_balanceNum = StringUtil.doubleIsNull(kcc.getQc_balanceNum());
                BigDecimal qc_balanceAmount = StringUtil.bigDecimalIsNull(kcc.getQc_balanceAmount());
                BigDecimal qc_balancePrice = StringUtil.bigDecimalIsNull(kcc.getQc_balancePrice());

                if (bq_incomeNum == 0 && bq_issueNum == 0 && bq_incomeAmount.compareTo(BigDecimal.ZERO) == 0 && bq_issueAmount.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                BigDecimal total_incomeAmount1 = total_incomeAmount.subtract(bq_incomeAmount);
                BigDecimal total_issueAmount1 = total_issueAmount.subtract(bq_issueAmount);
                Double total_incomeNum1 = total_incomeNum - bq_incomeNum;
                Double total_issueNum1 = total_issueNum - bq_issueNum;

                KcCommodity kc = new KcCommodity();

                kc.setComID(kcc.getComID());

                kc.setBq_incomeNum(0.0);
                kc.setBq_incomeAmount(new BigDecimal(0));
                kc.setBq_incomePrice(new BigDecimal(0));

                kc.setBq_issueNum(0.0);
                kc.setBq_issueAmount(new BigDecimal(0));
                kc.setBq_issuePrice(new BigDecimal(0));

                kc.setTotal_incomeAmount(total_incomeAmount1);
                kc.setTotal_incomeNum(total_incomeNum1);
                kc.setTotal_issueAmount(total_issueAmount1);
                kc.setTotal_issueNum(total_issueNum1);

                kc.setQm_balanceNum(qc_balanceNum);
                kc.setQm_balanceAmount(qc_balanceAmount);
                kc.setQm_balancePrice(qc_balancePrice);

                kcCommodityDao.updateCommodity(kc);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public StateTrack queryState2(Map<String, Object> map) throws BusinessException {
        try {
            StateTrack stateTrack = stateTrackDao.queryState(map);
            if (stateTrack == null) {
                return null;
            }
            return stateTrack;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public int upStateByCondition(Map<String, Object> map) throws BusinessException {
        int num = -1;
        try {
            num = stateTrackDao.upStateByCondition(map);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public int upStateSub(Map<String, Object> map) throws BusinessException {

        try {
            List<SubjectMessage> list = stateTrackDao.querySubTrack(map);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    SubjectMessage sub = list.get(i);
                    if (sub == null) {
                        continue;
                    }

                    BigDecimal init_debit_balance = bigIsNull(sub.getInit_debit_balance());
                    BigDecimal init_credit_balance = bigIsNull(sub.getInit_credit_balance());

                    BigDecimal current_amount_debit = bigIsNull(sub.getCurrent_amount_debit());
                    BigDecimal current_amount_credit = bigIsNull(sub.getCurrent_amount_credit());

                    BigDecimal year_amount_debit = bigIsNull(sub.getYear_amount_debit());
                    BigDecimal year_amount_credit = bigIsNull(sub.getYear_amount_credit());

                    BigDecimal ending_balance_debit = bigIsNull(sub.getEnding_balance_debit());
                    BigDecimal ending_balance_credit = bigIsNull(sub.getEnding_balance_credit());


                    BigDecimal new_year_amount_debit = year_amount_debit.subtract(current_amount_debit);
                    BigDecimal new_year_amount_credit = year_amount_credit.subtract(current_amount_credit);

                    BigDecimal new_ending_balance_debit = init_debit_balance;
                    BigDecimal new_ending_balance_credit = init_credit_balance;


                    HashMap<String, Object> subMap = new HashMap<>();

                    subMap.put("current_amount_debit", 0);
                    subMap.put("current_amount_credit", 0);

                    subMap.put("year_amount_debit", new_year_amount_debit);
                    subMap.put("year_amount_credit", new_year_amount_credit);

                    subMap.put("ending_balance_debit", init_debit_balance);
                    subMap.put("ending_balance_credit", init_credit_balance);


                    String pk_sub_id = sub.getPk_sub_id();
                    long currentTime = System.currentTimeMillis();
                    subMap.put("update_timestamp", currentTime);
                    subMap.put("pk_sub_id", pk_sub_id);

                    int num = stateTrackDao.upSubTrack(subMap);
                    if (num < 0) {
                        throw new BusinessException(sub.getSub_name() + ":科目更新失败");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return 0;
    }

    public BigDecimal bigIsNull(BigDecimal a) {
        if (a == null) {
            a = new BigDecimal(0);
        }
        return a;
    }


}

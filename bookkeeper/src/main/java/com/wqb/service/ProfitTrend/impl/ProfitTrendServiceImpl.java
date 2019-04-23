package com.wqb.service.ProfitTrend.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.StringUtil;
import com.wqb.dao.Profit.ProfitTrendDao;
import com.wqb.model.Account;
import com.wqb.model.ProfitTrendVo;
import com.wqb.model.StatusPeriod;
import com.wqb.service.ProfitTrend.ProfitTrendService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//薪资业务层
@Component
@Service("profitTrendService")
public class ProfitTrendServiceImpl implements ProfitTrendService {
    @Autowired
    ProfitTrendDao profitTrendDao;
    @Autowired
    VatService vatService;

    // 查询利润趋势
    @Override
    //@Transactional(rollbackFor = BusinessException.class)
    public Map<String, Map<String, Object>> queryProfitTrend(Map<String, Object> param) throws BusinessException {

        List<ProfitTrendVo> listProfitTrend = null;
        Map<String, Map<String, Object>> reMap = new HashMap<>();
        Map<String, Map<String, Object>> treeMap = new TreeMap<String, Map<String, Object>>(new Comparator<String>() {
            @Override
            public int compare(String t1, String t2) {
                return t1.compareTo(t2);
            }
        });
        try {
            String period = param.get("account_period").toString(); // 当前期间
            String accountID = param.get("account_id").toString();

            List<StatusPeriod> accountStaus = profitTrendDao.queryAccountStaus(param);  // 查询当前账套状态

            Account account = profitTrendDao.queryAccount(param);  // 查询账套开通时间
            Date open_date = account.getPeriod();
            String open_period = DateUtil.getMoth2(open_date); // 开通时间

            param.remove("account_period");
            List<String> listMonth = getBeforeMonth(period, open_period);  //选择期间   开通时间
            if (listMonth != null && listMonth.size() > 0) {
                param.put("listMonth", listMonth);  //根据多个期间查询
                listProfitTrend = profitTrendDao.queryProfitTrend(param); // 利润集合
            }

            if (listProfitTrend != null && listProfitTrend.size() > 0) {
                for (ProfitTrendVo profit : listProfitTrend) {
                    Map<String, Object> map = new HashMap<>();
                    String history_period = profit.getPeriod();
                    if (profit.getCurr_yysr() != null) {
                        BigDecimal curr_yysr = StringUtil.bigDecimalIsNull(profit.getCurr_yysr()); // 营业收入
                        BigDecimal curr_yycb = StringUtil.bigDecimalIsNull(profit.getCurr_yycb()); // 营业成本
                        BigDecimal curr_yylr = StringUtil.bigDecimalIsNull(profit.getCurr_yylr()); // 营业利润
                        // 收入 - 成本 - 费用 = 利润
                        // 费用 = 收入 - 成本 - 利润
                        BigDecimal curr_yyfy = curr_yysr.subtract(curr_yycb).subtract(curr_yylr);

                        map.put("sr", curr_yysr);
                        map.put("cb", curr_yycb);
                        map.put("lr", curr_yylr);
                        map.put("fy", curr_yyfy);
                        map.put("period", history_period);
                        reMap.put(history_period, map);
                    }
                }
            }


            Map<String, String> getmonth2 = getmonth2(period);
            for (String periodStr : getmonth2.keySet()) {
                Map<String, Object> map = reMap.get(periodStr);
                if (map == null) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("sr", 0);
                    map2.put("cb", 0);
                    map2.put("lr", 0);
                    map2.put("fy", 0);
                    map2.put("period", periodStr);
                    reMap.put(periodStr, map2);
                }
            }
            treeMap.putAll(reMap);
            return treeMap;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    // 利润分析
    @Override
    //@Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> queryprofitAnalyze(Map<String, Object> param) throws BusinessException {
        String period = param.get("account_period").toString(); // 当前期间
        try {

            param.put("account_period", period);
            ProfitTrendVo profit = profitTrendDao.queryprofitAnalyze(param); // 利润数据
            Map<String, Object> map = new HashMap<>();
            if (profit != null) {
                String history_period = profit.getPeriod();
                BigDecimal curr_yysr = StringUtil.bigDecimalIsNull(profit.getCurr_yysr()); // 营业收入
                BigDecimal curr_yycb = StringUtil.bigDecimalIsNull(profit.getCurr_yycb()); // 营业成本
                BigDecimal curr_yylr = StringUtil.bigDecimalIsNull(profit.getCurr_yylr()); // 营业利润
                // 收入 - 成本 - 费用 = 利润
                // 费用 = 收入 - 成本 - 利润
                BigDecimal curr_yyfy = curr_yysr.subtract(curr_yycb).subtract(curr_yylr);
                // map.put("sr", curr_yysr);
                map.put("cb", curr_yycb);
                map.put("lr", curr_yylr);
                map.put("fy", curr_yyfy);
                map.put("period", period);
            }
            return map;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    // 获取上个月
    public Map<String, String> getmonth2(String date) {  //选择期间
        try {
            Map<String, String> hashMap = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i > -11; i--) {
                Date time = sdf.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                cal.add(Calendar.MONTH, i);// 月份减一
                Date dateTemp = cal.getTime();
                String month = sdf.format(dateTemp);
                hashMap.put(month, month);
            }
            return hashMap;
        } catch (Exception e) {
            return null;
        }
    }

    // 获取上个月
    public List<String> getBeforeMonth(String period, String open_period) {  //选择期间   开通时间

        try {
            List<String> list = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

            Date open_date = sdf.parse(open_period);
            Date period_date = sdf.parse(period);
            if (period_date.before(open_date)) {
                return null;
            }

            for (int i = 0; i > -11; i--) {
                Date time = sdf.parse(period);
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                cal.add(Calendar.MONTH, i);// 月份减一
                Date dateTemp = cal.getTime();
                String month = sdf.format(dateTemp);
                Date bt = sdf.parse(month);
                list.add(month);
                if (bt.equals(open_date)) {
                    break;
                }
            }
            return list;
        } catch (ParseException e) {
            return null;
        }
    }

}

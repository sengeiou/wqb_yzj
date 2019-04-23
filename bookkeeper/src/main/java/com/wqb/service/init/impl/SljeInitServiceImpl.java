package com.wqb.service.init.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.init.SljeInitDao;
import com.wqb.model.Account;
import com.wqb.model.SljeInit;
import com.wqb.service.init.SljeInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Component
@Service("sljeInitService")
public class SljeInitServiceImpl implements SljeInitService {
    @Autowired
    SljeInitDao sljeInitDao;

    //@Transactional
    @Override
    // 数量金额初始化写入数据库
    public void slje2Date(List<Map<String, Object>> list, HttpSession session) throws BusinessException {
        Map<String, Object> userDateMap = (Map<String, Object>) session.getAttribute("userDate");
        Account account = (Account) userDateMap.get("account");
        try {
            // 校验EXCEL数据本身是否重复
            for (int i = 0; i < list.size(); i++) {
                for (int j = list.size() - 1; j > i; j--) {
                    if (!StringUtil.isEmpty(list.get(i).get("map2").toString())) {
                        if (list.get(i).get("map2").toString().equals(list.get(j).get("map2").toString())) {
                            throw new BusinessException("第" + (i + 1) + "行数据与第" + (j + 1) + "行数据重复,请仔细核查!");
                        }
                    }
                }
            }
            // 检查导入数据是否存在重复
            // 查询数据库中是否存在记录 避免重复导入
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                // 科目代码
                String subNumber = map.get("map2") == null ? null : map.get("map2").toString();
                // 根据科目代码和账套检索库
                List<SljeInit> echoList = sljeInitDao.querySljeBySubNumber(account.getAccountID(), subNumber);
                if (null != echoList && echoList.size() > 0) {
                    throw new BusinessException("存在重复数据！不允许导入");
                }
            }
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                // 起始期间
                String beginPeriod = map.get("map0") == null ? null : map.get("map0").toString();
                // 截止期间
                String endPeriod = map.get("map1") == null ? null : map.get("map1").toString();
                // 科目代码
                String subNumber = map.get("map2") == null ? null : map.get("map2").toString();
                // 科目名称
                String suName = map.get("map3") == null ? null : map.get("map3").toString();
                // 借贷方向
                String jdDirect = map.get("map4") == null ? null : map.get("map4").toString();
                // 期初结存数量
                String qcCount = map.get("map5") == null ? null : map.get("map5").toString();
                // 期初结存单价
                String qcPrice = map.get("map6") == null ? null : map.get("map6").toString();
                // 期初结存金额
                String qcAmount = map.get("map7") == null ? null : map.get("map7").toString();
                // 本期收入数量
                String bqsrCount = map.get("map8") == null ? null : map.get("map8").toString();
                // 本期收入金额
                String bqsrAmount = map.get("map9") == null ? null : map.get("map9").toString();
                // 本期发出数量
                String bqfcCount = map.get("map10") == null ? null : map.get("map10").toString();
                // 本期发出金额
                String bqfcAmount = map.get("map11") == null ? null : map.get("map11").toString();
                // 本年累计收入数量
                String bnljsrCount = map.get("map12") == null ? null : map.get("map12").toString();
                // 本年累计收入金额
                String bnljsrAmount = map.get("map13") == null ? null : map.get("map13").toString();
                // 本年累计发出数量
                String bnljfcCount = map.get("map14") == null ? null : map.get("map14").toString();
                // 本年累计发出金额
                String bnljfcAmount = map.get("map15") == null ? null : map.get("map15").toString();
                // 余额借贷方向
                String balanceJDDirect = map.get("map16") == null ? null : map.get("map16").toString();
                // 期末结存数量
                String qmjcCount = map.get("map17") == null ? null : map.get("map17").toString();
                // 期末结存单价
                String qmjcPrice = map.get("map18") == null ? null : map.get("map18").toString();
                // 期末结存金额
                String qmjcAmount = map.get("map19") == null ? null : map.get("map19").toString();
                // 构造实体类
                SljeInit sljeInit = new SljeInit();
                sljeInit.setId(UUIDUtils.getUUID());
                String period = "";
                String year = DateUtil.getYear();
                Integer beginPeriodInt = Integer.parseInt(beginPeriod.split("\\.")[0]);
                if (beginPeriodInt < 10) {
                    period = year + "-0" + beginPeriodInt;
                } else {
                    period = year + "-" + beginPeriodInt;
                }
                if (null != period) {
                    sljeInit.setPeriod(DateUtil.getDate(period));
                }
                sljeInit.setSubNumber(subNumber);
                sljeInit.setSuName(suName);
                sljeInit.setJdDirect(jdDirect);
                sljeInit.setQcCount(Double.parseDouble(qcCount));
                sljeInit.setQcPrice(Double.parseDouble(qcPrice));
                sljeInit.setQcAmount(Double.parseDouble(qcAmount));
                sljeInit.setBqsrCount(Double.parseDouble(bqsrCount));
                sljeInit.setBqsrAmount(Double.parseDouble(bqsrAmount));
                sljeInit.setBqfcCount(Double.parseDouble(bqfcCount));
                sljeInit.setBqfcAmount(Double.parseDouble(bqfcAmount));
                sljeInit.setBnljsrCount(Double.parseDouble(bnljsrCount));
                sljeInit.setBnljsrAmount(Double.parseDouble(bnljsrAmount));
                sljeInit.setBnljfcCount(Double.parseDouble(bnljfcCount));
                sljeInit.setBnljfcAmount(Double.parseDouble(bnljfcAmount));
                sljeInit.setBalanceJDDirect(balanceJDDirect);
                sljeInit.setQmjcCount(Double.parseDouble(qmjcCount));
                sljeInit.setQmjcPrice(Double.parseDouble(qmjcPrice));
                sljeInit.setQmjcAmount(Double.parseDouble(qmjcAmount));
                sljeInit.setAccountID(account.getAccountID());
                // 第一步写入数据库
                sljeInitDao.insertSljeInit(sljeInit);
                // 第二步 修改科目表中对应的科目余额
                // 根据账套查询 数量金额式总账(初始化)
                List<SljeInit> sljeList = sljeInitDao.querySlje(account.getAccountID());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

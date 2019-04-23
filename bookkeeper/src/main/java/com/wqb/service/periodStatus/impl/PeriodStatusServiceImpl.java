package com.wqb.service.periodStatus.impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.model.StatusPeriod;
import com.wqb.service.periodStatus.PeriodStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service("periodStatusService")
public class PeriodStatusServiceImpl implements PeriodStatusService {
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Override
    public List<StatusPeriod> queryStatus(Map<String, Object> param) throws BusinessException {
        //select * from t_status_period  where period=#{busDate} and accountID=#{accountID};
        return periodStatusDao.queryStatus(param);
    }

    @Override
    public void insertPeriodStatu(StatusPeriod statusPeriod) throws BusinessException {
        periodStatusDao.insertPeriodStatu(statusPeriod);

    }


    //根据期间查询账套状态是否结账
    @Override
    public String queryAccStatus(String accountID, String account_period) throws BusinessException {
        try {
            Map<String, Object> qrMap = new HashMap<>();
            qrMap.put("busDate", account_period);
            qrMap.put("accountID", accountID);
            List<StatusPeriod> statusList = periodStatusDao.queryStatus(qrMap);
            if (!statusList.isEmpty() && statusList.size() > 0) {
                Integer isJz = statusList.get(0).getIsJz();
                if (isJz != null && isJz == 1) {
                    return "1";
                }
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    @Override
    //@Transactional
    public int updStatuByCondition(Map<String, Object> param) throws BusinessException {

        int num = periodStatusDao.updStatuByCondition(param);
        // num = 0;
        if (num == 0) {

            throw new BusinessException("删除失败");
        }
        return num;
    }


}

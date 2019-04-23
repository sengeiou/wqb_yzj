package com.wqb.service.detection;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TaxRiskDetectionService
 * @Description: 税务风险检测
 * @date 2018年5月10日 下午3:06:37
 */
public interface TaxRiskDetectionService {
    Map<String, Object> queryDetection(User user, Account account) throws BusinessException;

    Map<String, Object> updateDetection(User user, Account account, int number) throws BusinessException;
}

package com.wqb.service.simallBusiness;

import com.wqb.common.BusinessException;
import com.wqb.model.Account;
import com.wqb.model.User;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * @author 司氏旭东
 * @ClassName: TaxPaymentService
 * @Description: 小规模税金结转 Service
 * @date 2018年4月12日 上午9:38:24
 */
public interface SimallBusinessQMJZService {

    /**
     * @return Map<String, Object>    返回类型
     * @Title: simallBusinessQMJZ
     * @Description: 小规模期末结转
     * @date 2018年4月12日  上午10:01:58
     * @author SiLiuDong 司氏旭东
     */
    public Map<String, Object> simallBusinessQMJZ(Map<String, Object> param, User user, Account account) throws BusinessException;

}

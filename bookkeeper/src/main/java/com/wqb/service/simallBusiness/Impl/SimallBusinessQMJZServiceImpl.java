package com.wqb.service.simallBusiness.Impl;

import com.wqb.common.BusinessException;
import com.wqb.dao.periodStatus.PeriodStatusDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.Account;
import com.wqb.model.User;
import com.wqb.service.arch.ArchService;
import com.wqb.service.assets.AssetsService;
import com.wqb.service.attached.AttachedService;
import com.wqb.service.jzcb.JzcbService;
import com.wqb.service.jzqnjlr.JzqnjlrService;
import com.wqb.service.jzsy.JzsyService;
import com.wqb.service.periodStatus.PeriodStatusService;
import com.wqb.service.simallBusiness.CarryOverOfSalesCostService;
import com.wqb.service.simallBusiness.SimallBusinessQMJZService;
import com.wqb.service.simallBusiness.TaxPayment;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 司氏旭东
 * @ClassName: TaxPaymentServiceImpl
 * @Description: 小规模税金结转 ServiceImpl
 * @date 2018年4月12日 上午9:38:52
 */
@Service("taxPaymentService")
public class SimallBusinessQMJZServiceImpl implements SimallBusinessQMJZService {
    @Autowired
    ArchService archService;
    @Autowired
    VatService vatService;
    @Autowired
    AttachedService attachedService;
    @Autowired
    AssetsService assetsService;
    @Autowired
    JzcbService jzcbService;
    @Autowired
    JzsyService jzsyService;
    @Autowired
    JzqnjlrService jzqnjlrService;
    @Autowired
    PeriodStatusDao periodStatusDao;

    @Autowired
    PeriodStatusService periodStatusService;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    TaxPayment taxPayment;
    @Autowired
    CarryOverOfSalesCostService carryOverOfSalesCostService;

    /**
     * @param param
     * @return
     * @throws BusinessException
     * @Title: simallBusinessQMJZ
     * @Description: 小规模期末结转
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> simallBusinessQMJZ(Map<String, Object> param, User user, Account account) throws BusinessException {

        Map<String, Object> result = new HashMap<String, Object>();

        try {

            vatService.subinit(user, account);

            // 二、发放工资
            //archService.arch2vouch4(session);
            // 一、计提工资
            archService.arch2vouch3(user, account);

            // 判断一季度结转一次
            // 计提税金
            Map<String, Object> taxPaymentVouch = taxPayment.TaxPaymentVouch(user, account);


            // 三.计提小规模增值税
            vatService.smallZzsCarryover(param);

            // 五、计提本月固定资产折旧。 
            assetsService.assets2vouch(user, account);
            // 小规模 结转销售成本
            carryOverOfSalesCostService.CarryOverOfSalesCostVoucher(user, account);
            // 六、结转销售成本   vatService.cbCarryover(param);

            // 七：计提企业所得税
            //vatService.jtCarryover(param);
            // 八、将本月收入结转到本年利润。(结转损溢)
            // jzsyService.doJzsy(session);
            // 九、结转全年净利润。 
            // jzqnjlrService.doJzqnjlr(session);


            Map<String, Object> pa = new HashMap<String, Object>();
            pa.put("accountID", account.getAccountID());
            pa.put("busDate", account.getUseLastPeriod());
            pa.put("isJt", 1);
            param.put("isJt", 1);
            periodStatusDao.updstatusJt(param);
            result.put("success", "true");
            return result;

        } catch (Exception e) {
            result.put("success", "fail");
            result.put("info", e);
            throw new BusinessException(e);
        }

    }
}

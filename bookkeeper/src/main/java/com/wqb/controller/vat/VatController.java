package com.wqb.controller.vat;

import com.wqb.common.*;
import com.wqb.controller.BaseController;
import com.wqb.controller.account.AccountController;
import com.wqb.dao.user.UserDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.dao.voucher.dao.VoucherHeadDao;
import com.wqb.model.Account;
import com.wqb.model.KcCommodity;
import com.wqb.model.User;
import com.wqb.model.Voucher;
import com.wqb.model.vomodel.KcCommodityVo;
import com.wqb.model.vomodel.PageSub;
import com.wqb.service.invoice.InvoiceMappingService;
import com.wqb.service.report.TBasicIncomeStatementService;
import com.wqb.service.vat.VatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

////增值税结转
@Controller
@RequestMapping("/vat")
public class VatController extends BaseController {
    private static Log4jLogger logger = Log4jLogger.getLogger(AccountController.class);
    @Autowired
    VatService vatService;
    @Autowired
    UserDao userDao;
    @Autowired
    VoucherHeadDao voucherHeadDao;
    @Autowired
    PinyinToHanYu pinyinToHanYu;
    @Autowired
    VatDao vatDao;
    @Autowired
    InvoiceMappingService invoiceMappingService;
    @Autowired
    JedisClient jedisClient;
    @Autowired
    TBasicIncomeStatementService basicIncomeService;

    //增值税结转
    @RequestMapping("/zzsjz")
    @ResponseBody
    Map<String, Object> vatSettle(@RequestParam(value = "id", defaultValue = "2") String id) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        try {
            String userID = getUser().getUserID();
            String userName = getUser().getUserName(); //用户姓名
            String accountID = getAccount().getAccountID(); //账套id
            String account_period = getUserDate(); //账套id
            param.put("accountID", accountID);
            param.put("userID", userID);
            param.put("period", account_period);
            param.put("userName", userName);
            result = vatService.zzsCarryover(param);
            if (result == null) {
                result.put("result", "增值税结转失败");
                result.put("message", "fail");
            }
            return result;
        } catch (BusinessException e) {
            logger.error("VatController【settlement】增值税结转异常!", e);
            result.put("result", e.getMessage());
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            logger.error("VatController【settlement】 增值税结转异常!", e);
            result.put("result", "增值税结转失败");
            result.put("message", "fail");
            return result;
        }
    }

    //计提企业所得税结转
    @RequestMapping("/jtjz")
    @ResponseBody
    Map<String, Object> jtjz() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        try {
            String userID = getUser().getUserID(); //用户id
            String userName = getUser().getUserName(); //用户姓名
            String accountID = getAccount().getAccountID(); //账套id
            String account_period = getUserDate(); //账套id
            param.put("accountID", accountID);
            param.put("userID", userID);
            param.put("period", account_period);
            param.put("userName", userName);
            result = vatService.jtCarryover(param);
            return result;
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "fail");
            return result;
        }
    }

    //成本结转
    @RequestMapping("/cbjz")
    @ResponseBody
    Map<String, Object> cbjz() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        try {
            String userID = getUser().getUserID(); //用户id
            String userName = getUser().getUserName(); //用户姓名
            String accountID = getAccount().getAccountID(); //账套id
            String account_period = getUserDate(); //账套id
            param.put("accountID", accountID);
            param.put("userID", userID);
            param.put("userName", userName);
            param.put("period", account_period);

            result = vatService.cbCarryover(param);
            return result;
        } catch (BusinessException e) {
            e.printStackTrace();
            result.put("message", "fail");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "fail");
            return result;
        }
    }

    //结转附增税
    @RequestMapping("/fjsjz")
    @ResponseBody
    Map<String, Object> attSettle() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        try {
            String userID = getUser().getUserID(); //用户id
            String userName = getUser().getUserName(); //用户姓名
            String accountID = getAccount().getAccountID(); //账套id
            String account_period = getUserDate(); //账套id
            param.put("accountID", accountID);
            param.put("userID", userID);
            param.put("userName", userName);
            param.put("period", account_period);
            result = vatService.fjsCarryover(param);
            if (result == null) {
                result.put("result", "附加税结转结转异常");
                result.put("message", "fail");
            }
            return result;
        } catch (BusinessException e) {
            logger.error("AttachedController【fjsjz】附加税结转结转异常!", e);
            result.put("result", "附加税结转结转异常");
            result.put("message", e);
            return result;
        } catch (Exception e) {
            logger.error("AttachedController【fjsjz】 附加税结转结转异常!", e);
            result.put("result", "附加税结转结转异常");
            result.put("message", e);
            return result;
        }
    }

    //测试回滚
    //@RequestMapping("/test01")
    @ResponseBody
    void test01() {
        //如果service 没有try catch 那么回滚可以直接用注解
        //如果service 加了try catch，想要回滚必须除了加注解。必须设置回滚rollbackFor = BusinessException.class，
        //因为异常被捕捉了，回滚不知道。加了rollbackFor就可以正常回滚
        try {

            System.out.println(1);

            vatService.test1();

            System.out.println(2);


/*			long a = 0;
			long b = 0;
			Map<String, Object> map1 = new HashMap<>();
			Map<String, Object> map2 = new HashMap<>();
			//des=#{des,jdbcType=VARCHAR},isproblem = #{isproblem,jdbcType=VARCHAR} where vouchID=#{vouchID}
			String des = "数量不能为负数xxx";
			String isproblem = "444";
			String vouchID = "00b985762a434295a42780e7948d7f98";

			map1.put("vouchID", vouchID);

			map1.put("des", des);
			map1.put("isproblem", isproblem);

			a = System.currentTimeMillis();
			voucherHeadDao.upVouHeadByCheckIdCall(map1);
			b = System.currentTimeMillis();

			System.out.println("..............存储过程。。。"+(b-a));


			 des = "数量数xxx";
			 isproblem = "666";

			 map2.put("vouchID", vouchID);

			map2.put("des", des);
			map2.put("isproblem", isproblem);

			a = System.currentTimeMillis();
			voucherHeadDao.upVouHeadByCheckId(map2);
			b = System.currentTimeMillis();
			System.out.println("......................mybatis。。。"+(b-a));*/

        } catch (Exception e) {
            System.out.println(222);
            e.printStackTrace();
            System.out.println(333);

        }
    }

    // 修改数据使用 更新字段 sys_suer表 字段source type
    @RequestMapping("/modifyAcc")
    void modifyAcc() {
        try {
            System.out.println("0k");
            vatService.modifyAcc();

        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //录入凭证 选择科目下拉列表数据为1405的请求
    @RequestMapping("/queryQmNum")
    @ResponseBody
    Map<String, Object> queryQmNum(String subCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<>();
        try {
            String accountID = getAccount().getAccountID(); //账套id
            String account_period = getUserDate(); //账套id
            param.put("accountID", accountID);
            param.put("period", account_period);
            param.put("sub_code", subCode);
            KcCommodity kc = vatService.queryQmNum(param);
            if (kc != null) {
                KcCommodity new_kc = new KcCommodity();
                new_kc.setQm_balanceNum(kc.getQm_balanceNum() == null ? 0 : kc.getQm_balanceNum());
                result.put("msg", new_kc);
            } else {
                result.put("msg", null);
            }
            result.put("code", "0");
            return result;
        } catch (Exception e) {
            logger.error("VatController【queryQmNum】 查询库存数量异常!", e);
            result.put("msg", e.getMessage());
            result.put("code", "1");
            return result;
        }
    }


    public Map<String, Object> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        String userID = getUser().getUserID(); //用户id
        String userName = getUser().getUserName(); //用户姓名
        String accountID = getAccount().getAccountID(); //账套id
        String account_period = getUserDate(); //账套id
        map.put("accountID", accountID);//账套id
        map.put("userID", userID);
        map.put("userName", userName);
        map.put("period", account_period);
        return map;

    }

    //查询全部末级科目  新增科目 录凭证 选择科目
    @RequestMapping(value = "/queryAllSubToPage")
    @ResponseBody
    Map<String, Object> queryAllSubToPage() {
        try {
            Map<String, Object> res = new HashMap<String, Object>();
            Account account = getAccount();
            String period = getUserDate();
            if (account == null) {
                res.put("msg", "账套为空");
                res.put("code", "1");
                return res;
            }
            List<PageSub> list = vatService.queryAllSubToPage(account.getAccountID(), period, null);
            res.put("data", list);
            res.put("msg", "success");
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> rts = new HashMap<String, Object>();
            rts.put("msg", e.getMessage());
            rts.put("code", "100");
            return rts;
        }
    }


    //新增科目
    @RequestMapping(value = "/addSubMessage")
    @ResponseBody
    Map<String, Object> addSubMessage(
            @RequestParam(required = true) String subCode,
            @RequestParam(required = true) String subName,
            @RequestParam(required = true) String fullName,
            HttpSession session) {
        Account account = getAccount();
        String period = getUserDate();
        try {
            Map<String, Object> res = new HashMap<String, Object>();

            if (account == null || StringUtil.isEmpty(period)) {
                res.put("msg", "vat addSubMessage 账套为空");
                res.put("code", "1");
                return res;
            }
            if (StringUtil.isEmpty(subCode) || StringUtil.isEmpty(subName) || StringUtil.isEmpty(fullName)) {
                res.put("msg", "vat addSubMessage subCode subName fullName is null");
                res.put("code", "2");
                return res;
            }

            Map<String, Object> result = vatService.addSubMessage(account.getAccountID(), period, subCode, subName, fullName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (vatService.getChg() == true) {
                try {
                    vatService.removeCacheSub(account.getAccountID(), period, subCode);
                } catch (BusinessException e1) {
                    e1.printStackTrace();
                }
            }
            Map<String, Object> rts = new HashMap<String, Object>();
            rts.put("msg", e.getMessage());
            rts.put("code", "100");
            return rts;
        }
    }


    @RequestMapping(value = "/addSubQueryAllSubToPage")
    @ResponseBody
    Map<String, Object> addSubQueryAllSubToPage() {
        try {
            Map<String, Object> res = new HashMap<String, Object>();
            Account account = getAccount();
            String period = getUserDate();
            if (account == null) {
                res.put("msg", "账套为空");
                res.put("code", "1");
                return res;
            }
            List<PageSub> list = vatService.queryAllSubToPage(account.getAccountID(), period, "add");
            res.put("data", list);
            res.put("msg", "success");
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> rts = new HashMap<String, Object>();
            rts.put("msg", e.getMessage());
            rts.put("code", "100");
            return rts;
        }
    }


    //点击下一张凭证查询
    @RequestMapping(value = "/nextVb")
    @ResponseBody
    Map<String, Object> nextVb(
            @RequestParam(value = "vid", required = true) String vouchAID,
            @RequestParam(value = "voucherNo", required = true) String voucherNo,
            @RequestParam(required = true) String type) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (StringUtil.isEmpty(type) || (!type.equals("1") && !type.equals("2"))) {
                result.put("msg", "获取凭证类型错误");
                result.put("code", "1");
                result.put("type", type);
                return result;
            }
            String accountID = getAccount().getAccountID();
            String period = getUserDate();
            result = vatService.queryNextOrPrevious(accountID, period, voucherNo, type, "nextVb");
            return result;
        } catch (Exception e) {
            result.put("info", e.getMessage());
            result.put("msg", "获取凭证是否有上下页信息异常!");
            result.put("code", "100");
            return result;
        }

    }

    //点击上一张凭证查询
    @RequestMapping(value = "/previousVb")
    @ResponseBody
    Map<String, Object> previousVb(
            @RequestParam(value = "vid", required = true) String vouchAID,
            @RequestParam(value = "voucherNo", required = true) String voucherNo,
            @RequestParam(required = true) String type) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtil.isEmpty(type) || (!type.equals("1") && !type.equals("2"))) {
                result.put("msg", "获取凭证类型错误");
                result.put("code", "1");
                result.put("type", type);
                return result;
            }
            // type = 1 查询全部凭证  type=2查询修正凭证
            String accountID = getAccount().getAccountID();
            String period = getUserDate();
            result = vatService.queryNextOrPrevious(accountID, period, voucherNo, type, "previousVb");
            return result;
        } catch (Exception e) {
            result.put("info", e.getMessage());
            result.put("msg", "获取凭证是否有上下页信息异常!");
            result.put("code", "100");
            return result;
        }

    }

    //从导航栏 进入凭证新增页面
    @RequestMapping(value = "/goinAddVoucherPage")
    @ResponseBody
    Map<String, Object> goinAddVoucherPage() {
        Map<String, Object> result = new HashMap<>();
        try {
            Account account = getAccount();
            String period = getUserDate();
            User user = getUser();
            //查询是否结转
            Map<String, Object> map = new HashMap<>();
            result = vatService.goinAddVoucherPage(account, period, user);
            return result;
        } catch (Exception e) {
            result.put("info", e.getMessage());
            result.put("msg", "未知异常,请刷新页面重新操作!");
            result.put("code", "100");
            return result;
        }

    }

    //查询库存商品到凭证页面
    @SuppressWarnings("unused")
    @RequestMapping(value = "/queryCommodityToVoucher")
    @ResponseBody
    Map<String, Object> queryCommodityToVoucher() {
        Map<String, Object> result = new HashMap<>();
        try {
            Account account = getAccount();
            String period = getUserDate();
            //查询是否结转
            Map<String, Object> map = new HashMap<>();
            List<KcCommodityVo> list = vatService.queryCommodityToVoucher(account, period);
            if (list != null && list.size() > 0) {
                result.put("code", "0");
                result.put("msg", list);
                return result;
            }
            result.put("code", "1");
            result.put("msg", "未查询到数据");
            return result;
        } catch (Exception e) {
            result.put("info", e.getMessage());
            result.put("msg", "未知异常,请刷新页面重新操作!");
            result.put("code", "100");
            return result;
        }

    }


    //根据库存商品生成凭证数据
    @SuppressWarnings("unused")
    @RequestMapping(value = "/commodityGenerateVoucher1")
    @ResponseBody
    Map<String, Object> commodityGenerateVoucher2(@RequestBody List<KcCommodityVo> list) {
        Map<String, Object> result = new HashMap<>();
        try {
            Account account = getAccount();
            String period = getUserDate();

            result.put("code", "1");
            return result;
        } catch (Exception e) {
            result.put("info", e.getMessage());
            result.put("msg", "未知异常,请刷新页面重新操作!");
            result.put("code", "100");
            return result;
        }

    }

    @SuppressWarnings("unused")
    @RequestMapping(value = "/commodityGenerateVoucher")
    @ResponseBody
    Map<String, Object> commodityGenerateVoucher(@RequestParam(required = true) String comids) {
        Map<String, Object> result = new HashMap<>();
        try {
            Account account = getAccount();
            String period = getUserDate();

            Voucher data = vatService.commodityGenerateVoucher(account.getAccountID(), period, comids);

            if (data == null) {
                result.put("code", "2");
                result.put("msg", "未查询到数据");
                return result;
            }

            result.put("code", "1");
            result.put("msg", data);
            return result;
        } catch (Exception e) {
            result.put("info", e.getMessage());
            result.put("msg", "未知异常,请刷新页面重新操作!");
            result.put("code", "100");
            return result;
        }

    }


    @SuppressWarnings("unused")
    @RequestMapping(value = "/queryProfit")
    @ResponseBody
    Map<String, Object> queryProfit() {
        Map<String, Object> result = new HashMap<>();
        try {

            Map<String, Object> map = basicIncomeService.queryProfit(getAccount(), getUserDate(), getUser());

            if (map == null) {
                result.put("code", "2");
                result.put("msg", "未查询到数据");
                return result;
            }
            if (map.get("code") == null) {
                result.put("code", "3");
                result.put("msg", "未查询到数据");
                return result;
            }
            if (!map.get("code").equals("0")) {
                return result;
            }
            result.put("code", "0");
            result.put("msg", map.get("msg"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("code", "100");
            return result;
        }

    }


    @RequestMapping(value = "/queryProfit1")
    ModelAndView queryProfit1() {
        ModelAndView modelAndView = new ModelAndView();
        try {

            Map<String, Object> map = basicIncomeService.queryProfit(getAccount(), getUserDate(), getUser());
            modelAndView.setViewName("report/rr");
            if (map == null) {
                modelAndView.addObject("msg", null);
                return modelAndView;
            }

            if (map.get("code") == null) {
                modelAndView.addObject("msg", null);
                return modelAndView;
            }

            if (!map.get("code").equals("0")) {
                modelAndView.addObject("msg", null);
                return modelAndView;
            }

            modelAndView.addObject("msg", map.get("msg"));
            return modelAndView;
        } catch (Exception e) {
            modelAndView.addObject("msg", null);
            return modelAndView;
        }

    }


}

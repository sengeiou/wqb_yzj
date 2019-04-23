package com.wqb.service.proof.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.DateUtil;
import com.wqb.common.Log4jLogger;
import com.wqb.common.StringUtil;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.vat.VatDao;
import com.wqb.model.*;
import com.wqb.service.proof.ProofService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Component
@Service("proofService")
public class ProofServiceImpl implements ProofService {

    public static Log4jLogger logger = Log4jLogger.getLogger(ProofServiceImpl.class);
    @Autowired
    KcCommodityDao commodityDao;
    @Autowired
    VatDao vatDao;

    @Override
    public boolean checkCode(Voucher voucher, Integer type) {
        Voucher vo = delEmptyObject(voucher);
        if (vo == null) {
            return false;
        }
        List<VoucherBody> list = vo.getVoucherBodyList();
        for (int i = 0; i < list.size(); i++) {
            VoucherBody vb = list.get(i);
            String sub_code = vb.getSubjectID();
            if (!StringUtil.isEmpty(sub_code)) {
                if (type == 1) {
                    if (sub_code.startsWith("1405")) {
                        return true;
                    }
                } else if (type == 2) {
                    if (sub_code.startsWith("1405")) {
                        return true;
                    }
                } else if (type == 3) {
                    if (sub_code.startsWith("1405") || sub_code.startsWith("1403")) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public boolean checkSubcodeByCb(List<VoucherBody> list, Integer type) throws Exception {
        boolean a = false;
        boolean b = false;
        boolean c = false;
        for (int i = 0; i < list.size(); i++) {
            VoucherBody vb = list.get(i);
            if (vb != null) {
                String sub_code = vb.getSubjectID();
                String dir = vb.getDirection();
                String zhaiyao = vb.getVcabstact();
                if (sub_code != null && dir != null) {
                    if (sub_code.startsWith("1405")) {
                        if (dir.equals("2")) {
                            a = true;
                        } else if (dir.equals("1")) {
                            c = true;
                        }
                    }
                }
                if (!StringUtil.isEmptyWithTrim(zhaiyao)) {
                    if (zhaiyao.indexOf("结转") != -1 && zhaiyao.indexOf("成本") != -1) {
                        b = true;
                    }
                }
            }


        }
        if (type == 1) {
            return a;
        } else if (type == 2 && a == true) {
            return b;
        } else if (type == 3) {
            return c;
        }
        return false;
    }

    @Override
    public boolean checkIsIncome(List<VoucherBody> list) throws Exception {
        boolean a = false;
        for (int i = 0; i < list.size(); i++) {
            VoucherBody vb = list.get(i);
            if (vb != null) {
                String sub_code = vb.getSubjectID();
                String dir = vb.getDirection();
                String zhaiyao = vb.getVcabstact();
                if (sub_code != null && dir != null) {
                    if (sub_code.startsWith("1405") && dir.equals("1")) {
                        a = true;
                    }
                }
            }
        }
        return a;
    }


    @Override
    public boolean checkIsCbjz(List<VoucherBody> list) throws Exception {
        boolean a = false;
        boolean b = false;
        boolean c = false;
        for (int i = 0; i < list.size(); i++) {
            VoucherBody vb = list.get(i);
            if (vb != null) {
                String sub_code = vb.getSubjectID();
                String dir = vb.getDirection();
                String zhaiyao = vb.getVcabstact();
                if (!StringUtil.isEmpty(sub_code) && !StringUtil.isEmpty(dir)) {
                    //有为贷方销项分录
                    if (sub_code.startsWith("1405") && dir.equals("2")) {
                        a = true;
                    }
                    //结转销项成本有借为主营业务收入分录
                    if (sub_code.startsWith("6401") && dir.equals("1")) {
                        b = true;
                    }
                }
                //摘要可能包含结转 和成本
                if (!StringUtil.isEmptyWithTrim(zhaiyao)) {
                    if (zhaiyao.indexOf("结转") != -1 && zhaiyao.indexOf("成本") != -1) {
                        c = true;
                    }
                }
            }
        }
        if (a == true && b == true) {
            return true;
        }
        if (a == true && c == true) {
            return true;
        }

        return false;
    }

    @Override
    //导入序时薄如果出现同样的商品结转两次的处理
    public Voucher merageVo(Voucher voucher) throws Exception {
        Voucher vo = delEmptyObject(voucher);
        if (vo == null) {
            return null;
        }
        List<VoucherBody> list = vo.getVoucherBodyList();


        int len1 = list.size();

        Set<String> hashSet = new HashSet<>();
        for (VoucherBody vb : list) {
            String subCode = vb.getSubjectID();
            hashSet.add(subCode);
        }
        if (len1 == hashSet.size()) {
            return voucher;
        }

        Map<String, VoucherBody> map = new HashMap<>();
        for (VoucherBody vb : list) {
            String sub_code = vb.getSubjectID();
            if (sub_code != null && (sub_code.startsWith("1405") || sub_code.startsWith("1403"))) {

                VoucherBody body = map.get(sub_code);
                if (body != null) {

                    Double amount = StringUtil.doubleIsNull(vb.getCreditAmount());
                    Double number = StringUtil.doubleIsNull(vb.getNumber());

                    Double creditAmount_old = body.getCreditAmount();
                    Double number_old = body.getNumber();

                    Double creditAmount_new = amount + creditAmount_old;
                    Double number_new = number + number_old;

                    body.setCreditAmount(creditAmount_new);
                    body.setNumber(number_new);
                } else {
                    map.put(sub_code, vb);
                }
            } else {
                map.put(sub_code, vb);
            }

        }
        if (!map.isEmpty()) {
            List<VoucherBody> listBody = new ArrayList<>();
            Set<Entry<String, VoucherBody>> entrySet = map.entrySet();
            for (Entry<String, VoucherBody> entry : entrySet) {
                listBody.add(entry.getValue());
            }
            voucher.setVoucherBodyList(listBody);
        }
        return voucher;
    }


    //数据金额表把一个商品从二级变为三级的处理  库存商品_合金铣刀_合金铣刀
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void upComm(TBasicSubjectMessage mess) throws BusinessException {
        try {
            Map<String, Object> map = new HashMap<>();
            String subCode = mess.getSubCode();  //1405003001
            String accountId = mess.getAccountId();
            String period = mess.getAccountPeriod();
            if (subCode != null) {
                if (subCode.startsWith("1405") || subCode.startsWith("1403")) {
                    SubjectMessage sub = vatDao.querySubById(mess.getPkSubId());
                    if (sub != null) {

                        String full_name = sub.getFull_name();
                        Integer code_level = sub.getCode_level();
                        String sub_name = sub.getSub_name();
                        if (code_level > 2 || subCode.length() > 7) {
                            String[] arr = full_name.split("_");
                            if (arr.length >= 3) {
                                //商品二级科目与三级科目相等,说明该商品已经被添加作为二级科目下的一个分类。原来的数量金额表二级科目就要被这个子类代替
                                if (arr[1].equals(arr[2])) {
                                    map.put("accountID", accountId);
                                    map.put("subjectID", subCode.substring(0, 7));
                                    String nextMonth = DateUtil.getNextMonth(period);
                                    map.put("busDate", nextMonth);
                                    map.put("subjectID", subCode.substring(0, 7));
                                    List<KcCommodity> nexttList = commodityDao.queryBysubCode(map);
                                    //该商品在下个期间已经做账了，商品之前期间的数据就不要再修改,以最后一个期间作为修改条件
                                    if (nexttList != null && nexttList.size() > 0) {
                                        return;
                                    }
                                    map.put("busDate", period);
                                    //查询该商品
                                    List<KcCommodity> list = commodityDao.queryBysubCode(map);
                                    //商品从二级改为三级，对应需要修改这个商品之前的信息。把原来的科目编码和商品名称替换成新的。商品上级就不存在了
                                    if (list != null && list.size() > 0) {
                                        KcCommodity commodity = list.get(0);
                                        map.clear();
                                        map.put("sub_code", subCode);
                                        map.put("sub_comName", full_name);
                                        map.put("comNameSpec", arr[1] + "-" + arr[2]);
                                        map.put("comName", arr[1]);
                                        map.put("spec", arr[2]);
                                        map.put("comID", commodity.getComID());
                                        int num = vatDao.updateCommBySub(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    public Voucher delEmptyObject(Voucher vo) {
        VoucherHead vh = vo.getVoucherHead();
        List<VoucherBody> list = vo.getVoucherBodyList();
        if (vh == null || list == null) {
            return null;
        }

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null) {
                    list.remove(i);
                    i--;
                }
            }
        }
        if (list.size() == 0) {
            return null;
        }

        return vo;
    }


    //判断凭证里面是否有相同的商品
    @Override
    public boolean searchCode(List<VoucherBody> list, String subcode) {
        List<String> arrayList = new ArrayList<>();
        for (VoucherBody vb : list) {
            String code = vb.getSubjectID();
            if (code.equals(subcode)) {
                arrayList.add(code);
            }
        }
        if (arrayList.size() > 1) {
            return true;
        }
        return false;
    }


    @Override
    public Double getjfNum(List<VoucherBody> list, String subcode) {
        double num = 0;
        for (VoucherBody vb : list) {
            String code = vb.getSubjectID();
            String dir = vb.getDirection();
            if (!StringUtil.isEmpty(dir) && !StringUtil.isEmpty(code)) {
                if (dir.equals("1") && code.equals(subcode)) {
                    double number = StringUtil.doubleIsNull(vb.getNumber());
                    num = num + number;
                }
            }
        }
        return num;
    }

    @Override
    public Double getdfNum(List<VoucherBody> list, String subcode) {
        double num = 0;
        for (VoucherBody vb : list) {
            String code = vb.getSubjectID();
            String dir = vb.getDirection();
            if (!StringUtil.isEmpty(dir) && !StringUtil.isEmpty(code)) {
                if (dir.equals("2") && code.equals(subcode)) {
                    double number = StringUtil.doubleIsNull(vb.getNumber());
                    num = num + number;
                }
            }
        }
        return num;
    }


    @Override
    public BigDecimal getjfMoney(List<VoucherBody> list, String subcode) {
        double jin_e = 0.0;
        for (VoucherBody vb : list) {
            String code = vb.getSubjectID();
            String dir = vb.getDirection();
            if (!StringUtil.isEmpty(dir) && !StringUtil.isEmpty(code)) {
                if (dir.equals("1") && code.equals(subcode)) {
                    double debitAmount = StringUtil.doubleIsNull(vb.getDebitAmount());
                    jin_e = jin_e + debitAmount;
                }
            }
        }
        BigDecimal money = new BigDecimal(jin_e);
        return money;
    }

    @Override
    public BigDecimal getdfMoney(List<VoucherBody> list, String subcode) {
        double jin_e = 0.0;
        for (VoucherBody vb : list) {
            String code = vb.getSubjectID();
            String dir = vb.getDirection();
            if (!StringUtil.isEmpty(dir) && !StringUtil.isEmpty(code)) {
                if (dir.equals("2") && code.equals(subcode)) {
                    double debitAmount = StringUtil.doubleIsNull(vb.getDebitAmount());
                    jin_e = jin_e + debitAmount;
                }
            }
        }
        BigDecimal money = new BigDecimal(jin_e);
        return money;
    }


    @Override
    public Double jfdfNum(List<VoucherBody> list, String subcode, Integer type) {

        double num = 0;
        for (VoucherBody vb : list) {
            String code = vb.getSubjectID();
            String dir = vb.getDirection();
            if (type == 1) {
                if (dir.equals("1")) {
                    if (code.equals(subcode)) {
                        double number = StringUtil.doubleIsNull(vb.getNumber());
                        num = num + number;
                    }
                }
            }
            if (type == 2) {
                if (dir.equals("2")) {
                    if (code.equals(subcode)) {
                        double number = StringUtil.doubleIsNull(vb.getNumber());
                        num = num + number;
                    }
                }
            }
        }
        return num;
    }


    //统计同张凭证里面出现 相同的商品 借方数量与贷方数量
    @Override
    public Map<String, Double> getGoodNum(List<VoucherBody> list, String subcode) {
        double d1 = 0;
        double d2 = 0;

        for (VoucherBody vb : list) {
            String dr = vb.getDirection();
            String compareCode = vb.getSubjectID();
            //累计借方subjectID商品数量
            if (dr.equals("1") && subcode.equals(compareCode)) {
                d1 = d1 + StringUtil.doubleIsNull(vb.getNumber());
            }
            //累计贷方subjectID商品数量
            if (dr.equals("2") && subcode.equals(compareCode)) {
                d2 = d2 + StringUtil.doubleIsNull(vb.getNumber());
            }
        }
        Map<String, Double> map = new HashMap<>();
        map.put("jf", d1);
        map.put("df", d2);
        return map;

    }


    @Override
    //@Transactional(rollbackFor = BusinessException.class)
    public Map<String, List<TBasicSubjectMessage>> getSub(Map<String, Object> param) throws BusinessException {

        try {

            List<TBasicSubjectMessage> list = vatDao.querySunYiSub(param);
            if (list == null) {
                return null;
            }
            Map<String, TBasicSubjectMessage> subMap = new HashMap<>();
            for (TBasicSubjectMessage sub : list) {
                if (sub == null || (sub != null && StringUtil.isEmpty(sub.getSubCode()))) {
                    throw new BusinessException("ProofServiceImpl getSub sub为空");
                }
                if (sub != null) {
                    subMap.put(sub.getSubCode(), sub);
                }
            }
            Map<String, List<TBasicSubjectMessage>> map = new HashMap<String, List<TBasicSubjectMessage>>();
            List<TBasicSubjectMessage> arr6901 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6001 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6051 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6111 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6301 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6401 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6402 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6403 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6711 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6601 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6602 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6603 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6701 = new ArrayList<>();
            List<TBasicSubjectMessage> arr6801 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                TBasicSubjectMessage sm = list.get(i);
                if (sm != null) {
                    String sub_code = sm.getSubCode();
                    List<String> oneCode = new ArrayList<>();
                    for (TBasicSubjectMessage sb : list) {
                        if (sb != null) {
                            String contentCode = sb.getSubCode();
                            if (contentCode.indexOf(sub_code) != -1 && contentCode.startsWith(sub_code)) {
                                oneCode.add(sub_code);
                            }
                        }
                    }
                    if (oneCode.size() == 1) {
                        String code = oneCode.get(0);
                        if (code.startsWith("6901")) {
                            arr6901.add(subMap.get(code));
                        } else if (code.startsWith("6001")) {
                            arr6001.add(subMap.get(code));
                        } else if (code.startsWith("6051")) {
                            arr6051.add(subMap.get(code));
                        } else if (code.startsWith("6111")) {
                            arr6111.add(subMap.get(code));
                        } else if (code.startsWith("6301")) {
                            arr6301.add(subMap.get(code));
                        } else if (code.startsWith("6401")) {
                            arr6401.add(subMap.get(code));
                        } else if (code.startsWith("6402")) {
                            arr6402.add(subMap.get(code));
                        } else if (code.startsWith("6403")) {
                            arr6403.add(subMap.get(code));
                        } else if (code.startsWith("6711")) {
                            arr6711.add(subMap.get(code));
                        } else if (code.startsWith("6601")) {
                            arr6601.add(subMap.get(code));
                        } else if (code.startsWith("6602")) {
                            arr6602.add(subMap.get(code));
                        } else if (code.startsWith("6603")) {
                            arr6603.add(subMap.get(code));
                        } else if (code.startsWith("6701")) {
                            arr6701.add(subMap.get(code));
                        } else if (code.startsWith("6801")) {
                            arr6801.add(subMap.get(code));
                        }
                    }
                }
            }
            //Collections.sort(arr6901, new ComparatorSub());
            if (arr6901.size() > 0)
                map.put("6901", arr6901);
            if (arr6001.size() > 0)
                map.put("6001", arr6001);
            if (arr6051.size() > 0)
                map.put("6051", arr6051);
            if (arr6111.size() > 0)
                map.put("6111", arr6111);
            if (arr6301.size() > 0)
                map.put("6301", arr6301);
            if (arr6401.size() > 0)
                map.put("6401", arr6401);
            if (arr6402.size() > 0)
                map.put("6402", arr6402);
            if (arr6403.size() > 0)
                map.put("6403", arr6403);
            if (arr6711.size() > 0)
                map.put("6711", arr6711);
            if (arr6601.size() > 0)
                map.put("6601", arr6601);
            if (arr6602.size() > 0)
                map.put("6602", arr6602);
            if (arr6603.size() > 0)
                map.put("6603", arr6603);
            if (arr6701.size() > 0)
                map.put("6701", arr6701);
            if (arr6801.size() > 0)
                map.put("6801", arr6801);

            if (!map.isEmpty()) {
                for (List<TBasicSubjectMessage> tb : map.values()) {
                    Collections.sort(tb, new ComparatorSub());
                }
                return map;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }


	/*	备份
	 * private void generateVo(TBasicSubjectMessage sub, List<VoucherBody> bodyList, Map<String, Object> map5,String uuid,int type) throws BusinessException {
		if (null != sub) {
			int rowIndex = (int) map5.get("rowIndex");
			BigDecimal totalJe =  (BigDecimal) map5.get("totalJe");
			BigDecimal amount = StringUtil.bigSubtract(sub.getEndingBalanceDebit(), sub.getEndingBalanceCredit());
			if(type==2  && amount.compareTo(BigDecimal.ZERO)!=0){
				VoucherBody vb = vatService.createVouchBody(uuid, String.valueOf(rowIndex++), null, sub.getFullName(),null ,amount.doubleValue(), "2", sub.getSubCode(), null);
				bodyList.add(vb);
				totalJe = totalJe.add(amount);
			}
			map5.put("rowIndex", rowIndex);
			map5.put("totalJe",totalJe);
		}
	}*/


}


class ComparatorSub implements Comparator<TBasicSubjectMessage> {
    @Override
    public int compare(TBasicSubjectMessage paramT1, TBasicSubjectMessage paramT2) {

        return paramT1.getSubCode().compareTo(paramT2.getSubCode());
    }

}












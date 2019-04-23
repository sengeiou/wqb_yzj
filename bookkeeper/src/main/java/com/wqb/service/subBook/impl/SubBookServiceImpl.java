package com.wqb.service.subBook.impl;


import com.wqb.common.*;
import com.wqb.dao.KcCommodity.KcCommodityDao;
import com.wqb.dao.subBook.SubBookDao;
import com.wqb.dao.subject.TBasicSubjectParentMapper;
import com.wqb.model.*;
import com.wqb.model.vomodel.*;
import com.wqb.service.report.TBasicIncomeStatementService;
import com.wqb.service.subBook.SubBookService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Component
@Service("SubBookService")
public class SubBookServiceImpl implements SubBookService {
    @Autowired
    SubBookDao subBookDao;
    @Autowired
    KcCommodityDao kcCommodityDao;
    @Autowired
    TBasicSubjectParentMapper tBasicSubjectParentMapper;
    @Autowired
    JedisClient JedisClient;
    @Autowired
    TBasicIncomeStatementService tBasicIncomeStatementService;


    @SuppressWarnings({"unused", "unchecked"})
    @Override
    // @Transactional(rollbackFor = BusinessException.class)
    public Map<String, Object> queryDetailAccount(Map<String, Object> param) throws BusinessException {
        try {

            String exportDetail = (String) param.get("exportDetail");

            int page_size = (int) param.get("pageSize");  //每页显示条数
            //是否为导为出明细账操作，如果是导出操作那么就不需要分页
            if (exportDetail != null) {
                Object rm_begin = param.remove("begin");
                Object rm_pageSize = param.remove("pageSize");
            }
            BigDecimal ye_amount = new BigDecimal(0);  //定义余额
            // 根据查询条件 查询明细账数据
            List<SubBook> list = subBookDao.querySubBook(param);

            // 客户选择还没有做账的期间 只做了第二月，但是期间切换到7月
            if (list == null || (list != null && list.size() == 0))
                return null;
            String subCode = param.get("sub_code").toString();
            // 从明细账菜单默认过来就是1001
            String one_dir = new ParentCodeMapping().getDir(subCode);
            String str_dir = (Integer.valueOf(one_dir) == 1) ? "借" : "贷";
            String zy1 = "本期合计", zy2 = "本年累计";

            Set<String> hashSet = new HashSet<>(); // 期间集合定义
            // 设置科目方向 与金额四舍五入
            for (SubBook ss : list) {
                ss.setDirection(str_dir);
                ss.setDebitAmount(getRownUp(ss.getDebitAmount()));
                ss.setCreditAmount(getRownUp(ss.getCreditAmount()));
                //余额
                ss.setBlanceAmount(getRownUp(ss.getBlanceAmount()));

                hashSet.add(ss.getPeriod());
            }
            // 获取所有期间
            List<String> tempList = new ArrayList<String>(hashSet);
            Collections.sort(tempList);

            // 以期间为单位 对同一个期间的数据进行分组
            Map<String, List<SubBook>> subBook_map = new HashMap<>(); // 定义明细账集合
            for (int i = 0; i < list.size(); i++) {
                SubBook subbook = list.get(i);
                String period = subbook.getPeriod();
                List<SubBook> tem_list = subBook_map.get(period);
                if (tem_list == null) {
                    tem_list = new ArrayList<>();
                    tem_list.add(subbook);
                    subBook_map.put(period, tem_list);
                } else {
                    tem_list.add(subbook);
                    subBook_map.put(period, tem_list);
                }
            }
            Collection<List<SubBook>> values = subBook_map.values();
            // 根据主键ID对不同期间的明细账数据进行排序

            //正确的做法 先按凭证排序分组，对分组的凭证再按主键排序
            for (List<SubBook> sbList : values)
                Collections.sort(sbList, new ComparatorVouchNum());

            // 查询 这个的科目 从开始期间到结束期间的 所有科目数据 一个期间只有一条科目余额表数据
            Map<String, Object> map = new HashMap<>();
            map.put("accountID", param.get("accountID"));
            map.put("sub_code", param.get("sub_code"));
            // 最大期间索引
            String min_period = Collections.min(tempList); // 根据结果查询到的真正的最小期间
            String max_period = Collections.max(tempList); // 根据结果查询到的真正的最大期间
            String user_serceh_min_peiod = (String) param.get("minPeiod"); // 用户选择的最小期间，这个期间可能没有查询到数据,不能作为真正的最小期间
            // 分页明细账跨越两个或者两个期间以上 使用 betewwn 查询
            if (tempList.size() > 1) {
                map.put("beginTime", min_period); // 开始期间
                map.put("endTime", max_period); // 结束期间
            } else {
                // 最小期间意思 比如期间选择范围的话 从2月到7月
                // 但是2到3没有做账的话，明细账就没有2月到3的数据，真正最小期间表示的是从4月开始
                // param.get("minPeiod") 与 tempList.get(0) 这个时候就会不相等
                map.put("period", min_period);
                if (!min_period.equals(user_serceh_min_peiod)) {

                }
            }

            // 查询某个科目期间范围内的所有数据  1405011   2018-01~2018-03
            List<SubMessageVo> subVo_list = subBookDao.querySubToDetailAcc(map);
            if (subVo_list == null || (subVo_list != null && subVo_list.size() == 0))
                throw new BusinessException("明细账service -》querySubToDetailAcc 查询期间科目数据异常");
            // 科目全名称
            String subFullName = subVo_list.get(0).getFull_name();
            // 用期间为单位对科目余额表的数据分组
            Map<String, SubMessageVo> subMessageMap = new HashMap<>();
            for (int i = 0; i < subVo_list.size(); i++) {
                SubMessageVo ss = subVo_list.get(i);
                String account_period = ss.getAccount_period();
                subMessageMap.put(account_period, ss); // 每个期间都有一条数据
            }
            // 查询分页总数 条件查询拼接 余额为0不显示
            String type = param.get("type").toString();
            if (!type.equals("1") && param.get("yeNotZero") != null)
                map.put("yeNotZero", "1");

            int cout = subBookDao.querySubBookCount(map);
            // 计算总页数
            int pageTotal = cout % page_size > 0 ? cout / page_size + 1 : cout / page_size;
            // 只有第一页才有期初数据
            Integer currPage = (Integer) param.get("currPage");

            Set<Entry<String, List<SubBook>>> entrySet = subBook_map.entrySet();

            // 根据期间遍历明细账，给每个期间 添加本年合计 本年累计
            for (Entry<String, List<SubBook>> entry : entrySet) {
                String key = entry.getKey(); // 期间
                // 判断是否有科目数据 有科目数据添加 本期合计 与本年累计
                boolean bool = subMessageMap.containsKey(key);
                if (bool) {
                    // 根据期间计算这个月的最后一天,最后一天需要计算本期合计与本年累计
                    // 2018-02-31 时间格式转换
                    Date max_day = jsUpDate(key, "2");
                    // 期间明细账集合
                    List<SubBook> subBook_list = entry.getValue();
                    // 取第一条科目作为参照物
                    SubBook subBook = subBook_list.get(0);
                    String sub_code = subBook.getSub_code();
                    String sub_name = subBook.getSub_name();
                    String sub_book_period = subBook.getPeriod();
                    String sub_book_direction = subBook.getDirection();

                    // 期间科目余额表数据
                    SubMessageVo subMessageVo = subMessageMap.get(key);
                    Integer debit_credit_direction = subMessageVo.getDebit_credit_direction();
                    // one_dir == debit_credit_direction
                    if (!one_dir.equals(String.valueOf(debit_credit_direction)))
                        throw new BusinessException("明细账service: -one_dir:" + one_dir + ",debit_credit_direction:"
                                + debit_credit_direction + ",sub_book_direction:" + sub_book_direction);
                    String account_period = subMessageVo.getAccount_period();
                    // account_period = key = sub_book_period
                    // key 是分组的期间 ， sub_book_period ：是分组数据里面的第一条 ，
                    // account_period 是科目期间
                    if (!key.equals(account_period) || !key.equals(sub_book_period)
                            || !sub_book_period.equals(account_period))
                        throw new BusinessException("明细账service: -key:" + key + ",account_period:" + account_period
                                + ",sub_book_period:" + sub_book_period);

                    BigDecimal init_debit_balance = subMessageVo.getInit_debit_balance();
                    BigDecimal init_credit_balance = subMessageVo.getInit_credit_balance();

                    BigDecimal current_debit = subMessageVo.getCurrent_amount_debit();
                    BigDecimal current_credit = subMessageVo.getCurrent_amount_credit();

                    BigDecimal ending_balance_debit = subMessageVo.getEnding_balance_debit();
                    BigDecimal ending_balance_credit = subMessageVo.getEnding_balance_credit();

                    BigDecimal year_debit = subMessageVo.getYear_amount_debit();
                    BigDecimal year_credit = subMessageVo.getYear_amount_credit();

                    // 如果是第一页 期间又是开始期间 需要添加期初余额
                    if (currPage == 1 && min_period.equals(key)) {
                        BigDecimal qc_ye = jsYe(one_dir, init_debit_balance, init_credit_balance);
                        String init_dir = (qc_ye.compareTo(BigDecimal.ZERO) != 0) ? str_dir : "平";
                        // 计算期初余额 月初第一天拼接 2018-02-01
                        Date jsUpDate = jsUpDate(key, "1");
                        SubBook subBook0 = createSubBook(init_dir, "期初余额", key, init_debit_balance, init_credit_balance, qc_ye, jsUpDate);
                        // 把期初余额添加到开始期间
                        subBook_list.add(0, subBook0);

                        ye_amount = qc_ye;
                    }
                    BigDecimal ye = jsYe(one_dir, ending_balance_debit, ending_balance_credit);

                    // 创建本年累计与本年到明细账里面去
                    SubBook subBook1 = createSubBook(str_dir, zy1, key, current_debit, current_credit, ye, max_day);
                    SubBook subBook2 = createSubBook(str_dir, zy2, key, year_debit, year_credit, ye, max_day);
                    int size = subBook_list.size();
                    // 把 本年合计 与 本年累计 添加到每期的明细账最后面中去-


                    subBook_list.add(size++, subBook1);
                    subBook_list.add(size++, subBook2);
                }
            }


            List<Map<String, Object>> arr = new ArrayList<>();
            Map<String, List<Object>> test1 = new HashMap<>();
            Set<Entry<String, List<SubBook>>> entrySet2 = subBook_map.entrySet();
            for (Entry<String, List<SubBook>> entry : entrySet2) {
                String key = entry.getKey();
                List<SubBook> value = entry.getValue();
                Map<String, Object> mp = new HashMap<>();
                mp.put("period", key);
                mp.put("list", value);
                arr.add(mp);
            }


            Collections.sort(arr, new ComparatorSuBookPeriod());

            //一 计算明细账余额
            //一   累加法。根据发生额计算期末余额
		/*	for (int i = 0; i < arr.size(); i++) {
				Map<String, Object> map2 = arr.get(i);
				List<SubBook> sb = (List<SubBook>) map2.get("list");
				for (int j = 0; j < sb.size(); j++) {
					SubBook book = sb.get(j);
					String zy = book.getVcabstact();
					if(zy.contains("本年累计") || zy.contains("本期合计") || zy.contains("期初余额")){
						continue;
					}
					BigDecimal debitAmount = book.getDebitAmount();
					BigDecimal creditAmount = book.getCreditAmount();
					//计算余额
					BigDecimal res = jsYe(one_dir, debitAmount, creditAmount,ye_amount);
					//BigDecimal setScale = res.setScale(2, BigDecimal.ROUND_HALF_DOWN);
					book.setBlanceAmount(decimalFormat(res));
					ye_amount = res;
				}
			}*/

            //二  改进 使用期末数据

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("result", arr);
            hashMap.put("cout", cout);
            hashMap.put("subName", subFullName);
            hashMap.put("perList", tempList);  //

            return hashMap;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    //one_dir 借或贷 方向
    //debitAmount 借方金额
    //creditAmount 贷方金额
    //ye_amount 余额
    private BigDecimal jsYe(String one_dir, BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal ye_amount) {

        BigDecimal bq_fs_Amount = null;
        if (one_dir.equals("1"))
            bq_fs_Amount = StringUtil.bigSubtract(debitAmount, creditAmount);
        else
            bq_fs_Amount = StringUtil.bigSubtract(creditAmount, debitAmount);
        BigDecimal bq_qm_ye = bq_fs_Amount.add(ye_amount);
        return bq_qm_ye;
    }

    public static BigDecimal decimalFormat(BigDecimal a) {
        if (a == null)
            a = new BigDecimal(0);
        DecimalFormat df = new DecimalFormat("#0.00");
        String format = df.format(a);
        BigDecimal b = new BigDecimal(format);
        return b;

    }


    private BigDecimal jsYe(String one_dir, BigDecimal debit, BigDecimal credit) {
        BigDecimal ye = new BigDecimal(0);
        if (one_dir.equals("1"))
            ye = StringUtil.bigSubtract(debit, credit);
        else
            ye = StringUtil.bigSubtract(credit, debit);
        BigDecimal ss = getRownUp(ye);
        return ss;
    }

    private Date jsUpDate(String period, String type) {
        String day = "";
        if ("1".equals(type))
            day = period + "-" + "01"; // 2018 - 02 - 01
        else {
            int dayofMonth = DateUtil.getDayofMonth(period);
            day = period + "-" + String.valueOf(dayofMonth);
        }

        Date date1 = DateUtil.fomatToDate(day);
        return date1;

    }

    private SubBook createSubBook(String dir, String zy, String per, BigDecimal debit, BigDecimal credit, BigDecimal ye, Date day) {
        SubBook book = new SubBook();
        book.setDirection(dir);
        book.setVcabstact(zy);

        book.setDebitAmount(decimalFormat(debit));
        book.setCreditAmount(decimalFormat(credit));
        book.setBlanceAmount(decimalFormat(ye));
        book.setUpdateDate(day); // 时间 yyyy-MM-dd
        book.setPeriod(per);

        book.setUp_date(day.getTime());
        book.setSub_code(null);
        book.setSub_name(null);
        book.setVouchNum(null);
        return book;
    }


    /******************************** 明细账end *********************************************/

    /******************************** 总账start *********************************************/

    @SuppressWarnings("unused")
    @Override
    // @Transactional(rollbackFor = BusinessException.class)
    public List<Map<String, Object>> queryLedger(Map<String, Object> param) throws BusinessException {


        try {
            List<SubMessageVo> list = subBookDao.queryLedger(param);
            if (list == null || (list != null && list.isEmpty()))
                return null;
            for (SubMessageVo sub : list) {
                String sub_code = sub.getSub_code();
                String account_period = sub.getAccount_period();
                Integer dir = sub.getDebit_credit_direction();
                BigDecimal ye = jsYe(String.valueOf(dir), sub.getEnding_balance_debit(), sub.getEnding_balance_credit());
                sub.setAmount(ye);

                String qm_dir = (ye.compareTo(BigDecimal.ZERO) == 0) ? "平" : ((dir == 1) ? "借" : "贷");
                sub.setFx_jd(qm_dir);

            }


            List<Map<String, Object>> re_list = new ArrayList<>();
            String period = (String) param.get("period");

            Map<String, List<SubMessageVo>> map4 = new HashMap<>();
            for (SubMessageVo sub : list) {
                String account_period = sub.getAccount_period();
                String sub_code = sub.getSub_code();
                List<SubMessageVo> list2 = map4.get(sub_code);
                if (list2 == null) {
                    list2 = new ArrayList<>();
                    list2.add(sub);
                    map4.put(sub_code, list2);
                } else {
                    list2.add(sub);
                    map4.put(sub_code, list2);
                }
            }

            if (!map4.isEmpty()) {
                Collection<List<SubMessageVo>> values = map4.values();
                for (List<SubMessageVo> vals : values) {
                    Collections.sort(vals, new ComparatorPeiod()); // 期间排序
                    System.out.println();
                }
                String zy0 = "期初余额", zy1 = "本期合计", zy2 = "本年累计";
                for (List<SubMessageVo> subVo : values) {
                    List<SubMessageVo> new_list = new ArrayList<>();
                    int ind = -1;

                    SubMessageVo sb = subVo.get(0);
                    String sub_name = sb.getSub_name();
                    String sub_code = sb.getSub_code();
                    String account_period = sb.getAccount_period();
                    Integer dir = sb.getDebit_credit_direction();

                    BigDecimal init_debit_balance = sb.getInit_debit_balance();
                    BigDecimal init_credit_balance = sb.getInit_credit_balance();
                    BigDecimal init_ye = jsYe(String.valueOf(dir), init_debit_balance, init_credit_balance);
                    String init_dir = (init_ye.compareTo(BigDecimal.ZERO) == 0) ? "平" : ((dir == 1) ? "借" : "贷");
                    SubMessageVo qc_subMessage = craeateSubVo(++ind, "期初余额", init_debit_balance, init_credit_balance,
                            sub_name, sub_code, dir, init_ye, init_dir, account_period);

                    new_list.add(qc_subMessage);

                    for (int i = 0; i < subVo.size(); i++) {
                        SubMessageVo sub_index = subVo.get(i);
                        BigDecimal qm_ye = sub_index.getAmount();
                        qm_ye = getRownUp(qm_ye);
                        String fx_jd = sub_index.getFx_jd();
                        String acc_period = sub_index.getAccount_period();

                        SubMessageVo bq_sub = craeateSubVo(++ind, "本期合计", sub_index.getCurrent_amount_debit(),
                                sub_index.getCurrent_amount_credit(), sub_name, sub_code, dir, qm_ye, fx_jd, acc_period);
                        SubMessageVo bn_sub = craeateSubVo(++ind, "本年累计", sub_index.getYear_amount_debit(),
                                sub_index.getYear_amount_credit(), sub_name, sub_code, dir, qm_ye, fx_jd, acc_period);
                        new_list.add(bq_sub);
                        new_list.add(bn_sub);
                    }

                    Map<String, Object> map5 = new HashMap<>();
                    map5.put("subCode", sub_code);
                    map5.put("subName", sub_name);
                    map5.put("list", new_list);
                    re_list.add(map5);
                }
            }

            if (!re_list.isEmpty()) {
                Collections.sort(re_list, new ComparatorSub());
                return re_list;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private SubMessageVo craeateSubVo(int index, String zy, BigDecimal debit, BigDecimal credit, String sub_name,
                                      String sub_code, Integer dir, BigDecimal ye, String jdStr, String period) {

        SubMessageVo subVo = new SubMessageVo();
        subVo.setZhaiYao(zy);
        subVo.setAccount_period(period);
        subVo.setAmount(ye);
        subVo.setFx_jd(jdStr);
        subVo.setSub_code(sub_code);
        subVo.setSub_name(sub_name);
        subVo.setDebit_credit_direction(dir);
        BigDecimal debit1 = getRownUp(debit);
        BigDecimal credit1 = getRownUp(credit);

        subVo.setJf_amount(debit1);
        subVo.setDf_amount(credit1);
        subVo.setIndex(index);
        return subVo;
    }

    //四舍五入两位小数
    public BigDecimal getRownUp(BigDecimal a) {
        if (a == null)
            return new BigDecimal(0);
        BigDecimal b = a.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        return b;
    }

    @SuppressWarnings("unchecked")
    public String prtObj(Object obj) {

        if (obj == null)
            return "kong";
        String str = "";
        if ((obj instanceof Map<?, ?>)) {
            str += "[";
            Map<String, Object> ss = (Map<String, Object>) obj;

            Set<Entry<String, Object>> entrySet = ss.entrySet();

            Iterator<Entry<String, Object>> iterator = entrySet.iterator();

            while (iterator.hasNext()) {
                Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                String string = value.toString();
                str += key + "=" + string + ";";
            }
            str += "]";

        }
        return str;

    }

    // 获取下个月
    public String getNextMonth(String date, Integer num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date time = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            cal.add(Calendar.MONTH, num);// 月份加或者减1

            Date dateTemp = cal.getTime();
            return sdf.format(dateTemp);

        } catch (ParseException e) {
            return null;
        }
    }

    public String getFormtMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            Date time = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            String day = Integer.toString(year) + "年第" + Integer.toString(month) + "期";
            return day;
        } catch (ParseException e) {
            return null;
        }
    }


    @Override
    public List<Map<String, String>> getRangePeriod(Map<String, Object> param) throws BusinessException {
        try {

            List<Map<String, String>> reList = new ArrayList<>();

            Integer period_max = Constrants.RANG_PERIOD;

            Integer period_min = period_max * (-1);


            String period = param.get("period").toString();
            List<String> list = new ArrayList<>();
            for (int i = period_min; i <= period_max; i++) {
                String nextMonth = getNextMonth(period, i);
                list.add(nextMonth);
            }

            param.put("ids", list);
            List<StatusPeriod> sta_list = subBookDao.getRangePeiod(param);
            if (sta_list == null || (sta_list != null && sta_list.size() == 0))
                return null;

            for (int i = 0; i < sta_list.size(); i++) {
                StatusPeriod statusPeriod = sta_list.get(i);
                String per1 = statusPeriod.getPeriod();
                String per2 = getFormtMonth(per1);
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("day1", per1);
                hashMap.put("day2", per2);
                reList.add(hashMap);
            }

            Collections.sort(reList, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> paramT1, Map<String, String> paramT2) {
                    return paramT1.get("day1").compareTo(paramT2.get("day1"));
                }
            });
            System.out.println();
            return reList;
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }


    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public int delSubBook(Map<String, Object> param) throws BusinessException {
        int delSubBook = subBookDao.delSubBook(param);
        return delSubBook;
    }


    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public int delSubBookBath(Map<String, Object> param) throws BusinessException {
        int delSubBook = subBookDao.delSubBookBath(param);
        return delSubBook;
    }

    @Override
    public int delSubBookBath2(Map<String, Object> param) throws BusinessException {
        int delSubBook = subBookDao.delSubBookBath2(param);
        return delSubBook;
    }


    @Override
    public List<SubMessageVo> querySubAll(Map<String, Object> param) throws BusinessException {

        try {

            List<SubMessageVo> list = subBookDao.querySubAll(param);
            if (list != null)
                Collections.sort(list, new Comparator<SubMessageVo>() {
                    @Override
                    public int compare(SubMessageVo paramT1, SubMessageVo paramT2) {
                        String s1 = paramT1.getSub_code();
                        String s2 = paramT2.getSub_code();
                        int compareTo = s1.compareTo(s2);
                        return compareTo;
                    }
                });
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }


    @Override
    public Map<String, Object> fastQuery(Map<String, Object> param) throws BusinessException {

        try {

            Map<String, Object> map = new HashMap<>();
            List<SubMessageVo> list = subBookDao.queryLedger(param);
            if (list == null || (list != null && list.isEmpty())) {
                map.put("list", null);
                return map;
            }
            String type = (String) param.get("type");

            List<SubMessageVo> list2 = new ArrayList<>();
            Map<String, SubMessageVo> map1 = new HashMap<>();

            List<String> list3 = new ArrayList<>();
            List<Integer> arr_level = new ArrayList<>();
            Set<Integer> hashSet = new HashSet<>();

            for (SubMessageVo sb : list) {
                if (sb == null || (sb != null && StringUtil.isEmpty(sb.getSub_code())))
                    throw new BusinessException("fastQuery sb||sub_code is null");
                if (sb != null && !StringUtil.isEmpty(sb.getSub_code())) {
                    list2.add(sb);
                    list3.add(sb.getSub_code()); // test
                    hashSet.add(sb.getCode_level());
                    map1.put(sb.getSub_code(), sb);
                }
            }

            if (list2.isEmpty()) {
                map.put("list", null);
                return map;
            }

            Collections.sort(list2, new ComparatorCode());

            Collections.sort(list3);
            arr_level.addAll(hashSet);
            Collections.sort(arr_level);

            if (type.equals("2") && arr_level.size() > 1 && (param.get("beginSubCode2") != null || param.get("only_beginSubCode2") != null)) {

                String search_satrt_code = null;
                if (param.get("beginSubCode2") != null)
                    search_satrt_code = (String) param.get("beginSubCode2");
                else
                    search_satrt_code = (String) param.get("only_beginSubCode2");

                List<SubMessageVo> list5 = new ArrayList<>();
                for (SubMessageVo sb2 : list2) {
                    String sub_code = sb2.getSub_code();

                    if (sub_code.compareTo(search_satrt_code) >= 0)
                        list5.add(sb2);
                }
                if (list5.isEmpty())
                    throw new BusinessException("list5 查询异常");

                SubMessageVo min = Collections.min(list5, new ComparatorCode());

                String real_startCode = min.getSub_code();

                List<String> parent_list = new ArrayList<>();
                SubMessageVo sb = map1.get(real_startCode);
                do {
                    String superior_coding = sb.getSuperior_coding();
                    if (!StringUtil.isEmpty(superior_coding))
                        parent_list.add(superior_coding);
                    sb = map1.get(superior_coding);

                } while (sb != null);

                Integer real_level = map1.get(real_startCode).getCode_level();
                if (!parent_list.isEmpty())
                    if (real_level > arr_level.get(0))
                        for (String str_code : parent_list) {
                            SubMessageVo subMessageVo = map1.get(str_code);
                            list5.add(0, subMessageVo);
                        }

                list2 = list5;
            }

            List<FastSubVo> list6 = new ArrayList<>();
            for (SubMessageVo sv : list2) {
                FastSubVo fastSubVo = new FastSubVo();
                fastSubVo.setSub_code(sv.getSub_code());
                fastSubVo.setCode_level(sv.getCode_level());
                fastSubVo.setSub_name(sv.getSub_name());
                fastSubVo.setSuperior_coding(sv.getSuperior_coding());
                list6.add(fastSubVo);
            }
            map.put("list", list6);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    // @SuppressWarnings("deprecation")
    @SuppressWarnings({"deprecation", "unused"})
    @Override
    public String exportStockExcel(Map<String, Object> param, HttpServletResponse response) throws BusinessException {

        try {
            Account account = (Account) param.get("accountID");
            String period = (String) param.get("period");
            String days = DateUtil.getDays();
            String formtMonth = getFormtMonth(period);
            String fileNanem = days + "数量金额总账_" + formtMonth + ".xls";
            Map<String, Object> reMap = new HashMap<>();
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("period", period);
            hashMap.put("accountID", account.getAccountID());
            List<KcCommodity> list = kcCommodityDao.queryCommodityAll(hashMap);
            if (list == null)
                return "1";
            Collections.sort(list, new Comparator<KcCommodity>() {
                @Override
                public int compare(KcCommodity paramT1, KcCommodity paramT2) {
                    return paramT1.getSub_code().compareTo(paramT2.getSub_code());
                }
            });
            ServletOutputStream os = response.getOutputStream();
            String dowFileName = URLEncoder.encode(fileNanem, "utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + dowFileName + "\"");


            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("数量金额总账");
            HSSFDataFormat fm = book.createDataFormat();
            // 标题栏字体
            HSSFFont font1 = book.createFont();
            font1.setFontName("仿宋_GB2312");
            font1.setFontHeightInPoints((short) 15);
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            // 正常字体样式
            HSSFFont normalFont = book.createFont();
            normalFont.setFontName("黑体");
            normalFont.setFontHeightInPoints((short) 10); // 字体大小
            // font2.setFontName("仿宋_GB2312");
            // font1.setFontName("微软雅黑"); //设置字体名称

            // 第三行第四行样式 表头部字体样式
            HSSFFont font3 = book.createFont();
            font3.setFontName("仿宋");
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
            font3.setFontHeightInPoints((short) 14); // 字体大小
            font3.setColor(HSSFColor.DARK_BLUE.index); // 字体颜色 灰色

            // 标题栏样式
            HSSFCellStyle setTitle = book.createCellStyle();
            setTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
            setTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            setTitle.setFont(font1);// 选择需要用到的字体格式

            // 正常样式
            HSSFCellStyle normalStyle = book.createCellStyle();
            setCellStyleBroder(normalStyle);
            normalStyle.setFont(normalFont);// 选择需要用到的字体格式

            // 一、设置背景色：
            // normalStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);//
            // 设置背景色
            // normalStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);*/
            // HSSFColor.GREY_50_PERCENT

            // 头部样式
            HSSFCellStyle headStyle = book.createCellStyle();
            headStyle.cloneStyleFrom(normalStyle);
            headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
            headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
            headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            headStyle.setFont(font3);// 选择需要用到的字体格式

            // style_date.setDataFormat(date_format.getFormat("yyyy-MM-dd
            // HH:mm"));

            // 第一种设置小数保留两位
            /*
             * CellStyle cellStyle = workbook.createCellStyle(); HSSFDataFormat
             * df = workbook.createDataFormat(); //此处设置数据格式
             * cellStyle.setDataFormat(df.getFormat("#,#0.0"));
             * //小数点后保留两位，可以写contentStyle.setDataFormat(df.getFormat("#,#0.00"))
             * ; cell.setCellStyle(cellStyle);
             */
            // 第二种设置小鼠保留两位
            /*
             * DecimalFormat df = new DecimalFormat("#.####");
             * //格式化为四位小数，按自己需求选择； return String.valueOf(df.format(inputValue));
             */ // 返回String类型

            // 设置字体方向向左对准样式
            HSSFCellStyle rightCell = book.createCellStyle();
            rightCell.cloneStyleFrom(normalStyle);
            rightCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //
            // value 必须是double类型才可格式化 #,##0.00
            rightCell.setDataFormat(fm.getFormat("#,##0.00")); // 小数点后保留两位
            // rightCell.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // //小数点后保留两位 excel 内嵌的公式等同 setDataFormat 效果

            // 设置字体方向向右对准样式
            HSSFCellStyle leftCell = book.createCellStyle();
            leftCell.cloneStyleFrom(normalStyle);
            leftCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            int size = list.size();
            int rownCount = 4 + size;

            // 设置单元格样式
            for (int i = 0; i < rownCount; i++) {
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < 19; j++) {
                    HSSFCell cell = row.createCell(j);
                    if (i == 0)
                        cell.setCellStyle(setTitle);
                    else if (i == 1)
                        cell.setCellStyle(normalStyle);
                    else if (i == 2 || i == 3)
                        cell.setCellStyle(headStyle);
                    else if (j < 4 || j == 15)
                        cell.setCellStyle(normalStyle);
                    else
                        cell.setCellStyle(rightCell);
                }
            }

            // 五、设置行高列宽:

            // sheet.setColumnWidth(0, 3766); //第一个参数代表列id(从0开始),第2个参数代表宽度值
            sheet.getRow(0).setHeight((short) 480);
            sheet.getRow(1).setHeight((short) 300);
            sheet.getRow(2).setHeight((short) 320);
            sheet.getRow(3).setHeight((short) 320);
            // 设置其他行高
            for (int i = 4; i < rownCount; i++)
                sheet.getRow(i).setHeight((short) 280);

            // 设置列宽
            for (int i = 0; i < 19; i++) {
                // 只能解决英文、数字列宽自适应，如果该列为中文，会出现列宽不足现象
                // 可通过手动设置列宽解决
                // 首先通过value.getBytes().length方法对比找到该列数据最大长度
                // 通过sheet.setColumnWidth(i,cellLength*2*256);手动设置列宽。
                sheet.autoSizeColumn((short) i, true);
                if (i == 0)
                    sheet.setColumnWidth(i, 4000);
                else if (i == 1)
                    sheet.setColumnWidth(i, 7500);
                else
                    sheet.setColumnWidth(i, 4000);
            }

            // 七、合并单元格:
            @SuppressWarnings("deprecation")
            CellRangeAddress region = new CellRangeAddress(0, 0, (short) 0, (short) 18);
            sheet.addMergedRegion(region);

            sheet.getRow(0).getCell(0).setCellValue("数量金额总账");

            CellRangeAddress region10 = new CellRangeAddress(1, 1, (short) 0, (short) 14);
            CellRangeAddress region11 = new CellRangeAddress(1, 1, (short) 15, (short) 18);
            sheet.addMergedRegion(region10);
            sheet.addMergedRegion(region11);

            String companyName = account.getCompanyName();
            companyName = companyName == null ? "" : companyName;

            // formtMonth
            String towRowStr = companyName + "*" + formtMonth;
            String replace = towRowStr.replace("*", "   ");
            String day = DateUtil.getDay(); // yyyy-MM-dd

            sheet.getRow(1).getCell(0).setCellValue(replace);
            sheet.getRow(1).getCell(15).setCellValue(day); // 日期
            sheet.getRow(1).getCell(15).setCellStyle(rightCell);

            CellRangeAddress region30 = new CellRangeAddress(2, 3, (short) 0, (short) 0);
            CellRangeAddress region31 = new CellRangeAddress(2, 3, (short) 1, (short) 1);
            CellRangeAddress region32 = new CellRangeAddress(2, 3, (short) 2, (short) 2);
            sheet.addMergedRegion(region30);
            sheet.addMergedRegion(region31);
            sheet.addMergedRegion(region32);

            CellRangeAddress region33 = new CellRangeAddress(2, 2, (short) 3, (short) 6);
            CellRangeAddress region37 = new CellRangeAddress(2, 2, (short) 7, (short) 8);
            CellRangeAddress region39 = new CellRangeAddress(2, 2, (short) 9, (short) 10);
            CellRangeAddress region311 = new CellRangeAddress(2, 2, (short) 11, (short) 12);
            CellRangeAddress region313 = new CellRangeAddress(2, 2, (short) 13, (short) 14);
            CellRangeAddress region315 = new CellRangeAddress(2, 2, (short) 15, (short) 18);

            sheet.addMergedRegion(region33);
            sheet.addMergedRegion(region37);
            sheet.addMergedRegion(region39);
            sheet.addMergedRegion(region311);
            sheet.addMergedRegion(region313);
            sheet.addMergedRegion(region315);

            String[] arr0 = new String[]{"科目编码", "科目名称", "单位", "期初余额", "", "", "", "本期借方", "", "本期贷方", "", "本年累计借方",
                    "", "本年累计贷方", "", "期末余额", "", "", ""};
            for (int j = 0; j < 19; j++)
                if (!StringUtil.isEmpty(arr0[j]))
                    sheet.getRow(2).getCell(j).setCellValue(arr0[j]);
            String[] arr = new String[]{"", "", "", "方向", "数量", "单价", "金额", "数量", "金额", "数量", "金额", "数量", "金额", "数量",
                    "金额", "方向", "数量", "单价", "金额"};

            for (int i = 3; i < 19; i++)
                sheet.getRow(3).getCell(i).setCellValue(arr[i]);

            int ind = 4;

            int index = 0;

            // 4 + 3;
            for (int i = ind; i < rownCount; i++) {

                HSSFRow row = sheet.getRow(i); // 行号

                KcCommodity kc = list.get(index); // 数据

                String sub_comName = kc.getSub_comName();

                String sub_code = kc.getSub_code();
                // 期初
                BigDecimal qc_balanceAmount = bigIsNull(kc.getQc_balanceAmount());

                String qc_balanceNum = doubIsNull(kc.getQc_balanceNum());

                BigDecimal qc_balancePrice = bigIsNull(kc.getQc_balancePrice());

                // 本期收入
                BigDecimal bq_incomeAmount = bigIsNull(kc.getBq_incomeAmount());
                String bq_incomeNum = doubIsNull(kc.getBq_incomeNum());
                BigDecimal bq_incomePrice = bigIsNull(kc.getBq_incomePrice());
                // 本期发出
                BigDecimal bq_issueAmount = bigIsNull(kc.getBq_issueAmount());
                String bq_issueNum = doubIsNull(kc.getBq_issueNum());
                BigDecimal bq_issuePrice = bigIsNull(kc.getBq_issuePrice());

                // 本年累计
                BigDecimal total_incomeAmount = bigIsNull(kc.getTotal_incomeAmount());
                String total_incomeNum = doubIsNull(kc.getTotal_incomeNum());
                BigDecimal total_issueAmount = bigIsNull(kc.getTotal_issueAmount());
                String total_issueNum = doubIsNull(kc.getTotal_issueNum());

                // 期末
                BigDecimal qm_balanceAmount = bigIsNull(kc.getQm_balanceAmount());
                String qm_balanceNum = doubIsNull(kc.getQm_balanceNum());
                BigDecimal qm_balancePrice = bigIsNull(kc.getQm_balancePrice());

                row.getCell(0).setCellValue(sub_code);
                row.getCell(1).setCellValue(sub_comName);
                row.getCell(2).setCellValue("");

                DecimalFormat df = new DecimalFormat("0.00");

                row.getCell(3).setCellValue("");
                row.getCell(4).setCellValue(qc_balanceNum);
                row.getCell(5).setCellValue(df.format(qc_balancePrice));
                row.getCell(6).setCellValue(df.format(qc_balanceAmount));

                row.getCell(7).setCellValue(bq_incomeNum);
                row.getCell(8).setCellValue(df.format(bq_incomeAmount));

                row.getCell(9).setCellValue(bq_issueNum);
                row.getCell(10).setCellValue(df.format(bq_issueAmount));

                row.getCell(11).setCellValue(total_incomeNum);
                row.getCell(12).setCellValue(df.format(total_incomeAmount));

                row.getCell(13).setCellValue(total_issueNum);
                row.getCell(14).setCellValue(df.format(bq_issueAmount));

                row.getCell(15).setCellValue("");
                row.getCell(16).setCellValue(Double.parseDouble(qm_balanceNum));

                /*
                 * row.getCell(17).setCellValue(df.format(qm_balancePrice));
                 * row.getCell(18).setCellValue(df.format(qm_balanceAmount));
                 */

                row.getCell(17).setCellValue(Double.parseDouble(df.format(qm_balancePrice)));
                row.getCell(18).setCellValue(Double.parseDouble(df.format(qm_balanceAmount)));

                /*
                 * for (int j = 0; i < 19; j++) {
                 * sheet.getRow(i).getCell(j).setCellValue(sub_comName); }
                 */
                index++;
            }

            book.write(os);
            os.flush();
            os.close();
            response.flushBuffer();
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    // 导出明细账
    @SuppressWarnings({"unchecked", "deprecation"})
    @Override
    public HSSFWorkbook exportDetailAccount(Map<String, Object> param) throws BusinessException {
        try {

            Account account = (Account) param.get("accountID");
            String subFullName = (String) param.get("subName");
            String export_sub_code = (String) param.get("sub_code");

            List<String> perList = (List<String>) param.get("period");
            List<Map<String, Object>> subBookList = (List<Map<String, Object>>) param.get("subBookList");

            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("明细账");
            HSSFDataFormat fm = book.createDataFormat();

            // 标题栏字体
            HSSFFont font1 = book.createFont(); // 创建字体
            font1.setFontName("仿宋_GB2312"); // 设置字体名称
            font1.setFontHeightInPoints((short) 15);// 设置字体大小
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

            // 正常字体样式
            HSSFFont normalFont = book.createFont();
            normalFont.setFontName("黑体");
            normalFont.setFontHeightInPoints((short) 10); // 字体大小

            // 第三行样式 表头部字体样式
            HSSFFont font3 = book.createFont();
            font3.setFontName("仿宋");
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
            font3.setFontHeightInPoints((short) 14); // 字体大小
            font3.setColor(HSSFColor.DARK_BLUE.index); // 字体颜色 灰色

            // 标题栏样式
            HSSFCellStyle titleStyle = book.createCellStyle();
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            titleStyle.setFont(font1);// 选择需要用到的字体格式

            // 正常样式
            HSSFCellStyle normalStyle = book.createCellStyle();
            setCellStyleBroder(normalStyle);
            normalStyle.setFont(normalFont);// 选择需要用到的字体格式

            // 头部样式
            HSSFCellStyle headStyle = book.createCellStyle();
            headStyle.cloneStyleFrom(normalStyle);
            headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
            headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
            headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            headStyle.setFont(font3);// 选择需要用到的字体格式

            // 设置字体方向向左对准样式
            HSSFCellStyle leftCell = book.createCellStyle();
            leftCell.cloneStyleFrom(normalStyle);
            leftCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            leftCell.setDataFormat(fm.getFormat("yyyy-MM-dd  HH:mm:ss"));

            // 设置字体方向向右对准样式
            HSSFCellStyle rightCell = book.createCellStyle();
            rightCell.cloneStyleFrom(normalStyle);
            rightCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //
            rightCell.setDataFormat(fm.getFormat("#,##0.00")); // 小数点后保留两位

            List<SubBook> list = new ArrayList<>();
            for (int i = 0; i < subBookList.size(); i++) {
                Map<String, Object> map = subBookList.get(i);
                List<SubBook> value = (List<SubBook>) map.get("list");
                String str_per = (String) map.get("period");
                for (int j = 0; j < value.size(); j++) {
                    SubBook subBook = value.get(j);
                    list.add(subBook);
                }
            }

            int size = list.size();
            int rownCount = 3 + size;

            // 设置单元格样式
            for (int i = 0; i < rownCount; i++) {
                HSSFRow row = sheet.createRow(i);
                for (int j = 0; j < 8; j++) {
                    HSSFCell cell = row.createCell(j);
                    if (i == 0)
                        cell.setCellStyle(titleStyle);
                    else if (i == 1)
                        cell.setCellStyle(normalStyle);
                    else if (i == 2)
                        cell.setCellStyle(headStyle);
                    else if (j == 4 || j == 5 || j == 7)
                        cell.setCellStyle(rightCell);
                    else
                        cell.setCellStyle(leftCell);
                }
            }

            // 五、设置行高列宽:

            // sheet.setColumnWidth(0, 3766); //第一个参数代表列id(从0开始),第2个参数代表宽度值
            sheet.getRow(0).setHeight((short) 480);
            sheet.getRow(1).setHeight((short) 300);
            sheet.getRow(2).setHeight((short) 450);
            /*
             * sheet1.DefaultColumnWidth=100*256; sheet1.DefaultRowHeight=30*20;
             */
            // 设置其他行高
            for (int i = 3; i < rownCount; i++)
                sheet.getRow(i).setHeight((short) 280);
            // 设置列宽
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn((short) i, true);
                if (i == 0 || i == 1)
                    sheet.setColumnWidth(i, 7500);
                else if (i == 3)
                    sheet.setColumnWidth(i, 6500);
                else
                    sheet.setColumnWidth(i, 5000);
            }
            // 七、合并单元格:
            CellRangeAddress region = new CellRangeAddress(0, 0, (short) 0, (short) 7);
            sheet.addMergedRegion(region);
            sheet.getRow(0).getCell(0).setCellValue("明细账");
            CellRangeAddress region1 = new CellRangeAddress(1, 1, (short) 0, (short) 5);
            CellRangeAddress region2 = new CellRangeAddress(1, 1, (short) 6, (short) 7);
            sheet.addMergedRegion(region1);
            sheet.addMergedRegion(region2);

            String companyName = account.getCompanyName();
            companyName = companyName == null ? "" : companyName;
            // formtMonth
            String formtMonth = null;
            if (perList != null && perList.size() > 1) {
                String min_period = Collections.min(perList);
                String max_period = Collections.max(perList);
                String str1 = getFormtMonth(min_period);
                String str2 = getFormtMonth(max_period);
                formtMonth = str1 + " 至 " + str2;
            } else {
                String min_period = Collections.min(perList);
                formtMonth = getFormtMonth(min_period);

            }
            String towRowStr = companyName + "*" + formtMonth;

            String replace = towRowStr.replace("*", "   ");
            String day = DateUtil.getDay(); // yyyy-MM-dd

            sheet.getRow(1).getCell(0).setCellValue(replace);
            sheet.getRow(1).getCell(6).setCellValue(day); // 日期
            sheet.getRow(1).getCell(6).setCellStyle(rightCell);

            String[] arr = new String[]{"科目", "日期", "凭证字号", "摘要", "借方", "贷方", "方向", "余额"};
            for (int i = 0; i < 8; i++)
                sheet.getRow(2).getCell(i).setCellValue(arr[i]);

            int ind = 0;
            // 读取数据到工作簿
            for (int i = 3; i < rownCount; i++) {
                SubBook sb = list.get(ind);

                String sub_code = sb.getSub_code();
                String sub_name = sb.getSub_name();


                BigDecimal blanceAmount = StringUtil.bigDecimalIsNull(sb.getBlanceAmount());
                BigDecimal creditAmount = StringUtil.bigDecimalIsNull(sb.getCreditAmount());
                BigDecimal debitAmount = StringUtil.bigDecimalIsNull(sb.getDebitAmount());
                String dir = sb.getDirection();
                String zy = sb.getVcabstact();
                Integer vouchNum = sb.getVouchNum();
                Long up_date = sb.getUp_date();
                Date updateDate = sb.getUpdateDate();

                HSSFRow row = sheet.getRow(i); // 行号
                //设置科目名字
                if (i == 3) {
                    String str1 = export_sub_code + "-" + subFullName;
                    row.getCell(0).setCellValue(str1);
                }
                if (vouchNum == null) {
                    zy = "    " + zy;
                    row.getCell(2).setCellValue("");
                } else
                    row.getCell(2).setCellValue("记-" + vouchNum.toString());
                row.getCell(1).setCellValue(updateDate == null ? new Date(up_date) : updateDate);
                row.getCell(3).setCellValue(zy);
                row.getCell(4).setCellValue(debitAmount.doubleValue());
                row.getCell(5).setCellValue(creditAmount.doubleValue());
                row.getCell(6).setCellValue(dir);
                row.getCell(7).setCellValue(blanceAmount.doubleValue());
                ind++;
            }
            return book;
        } catch (Exception e) {
            throw new BusinessException(e);
        }

    }

    // //设置边框
    private void setCellStyleBroder(HSSFCellStyle cellStyle) {
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(HSSFColor.BLACK.index);

        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);

        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    }

    private BigDecimal bigIsNull(BigDecimal num) {
        return num == null ? new BigDecimal(0.00) : num;
    }

    private String doubIsNull(Double num) {
        num = num == null ? 0.0 : num;
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal ss = new BigDecimal(num);
        String format = df.format(ss);
        return format;
        /*
         * BigDecimal b = new BigDecimal(num); double f1 =
         * b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); return f1;
         */
    }


    @SuppressWarnings("unused")
    @Override

    public Map<String, Object> queryAllSubBook(Map<String, Object> map) throws Exception {


        Map<String, Object> res = new HashMap<>();

        List<SubBookMessageVo> list = subBookDao.queryAllSubBook(map);


        if (list == null || list.isEmpty()) {
            res.put("code", "2");
            res.put("msg", "未查询到数据");
            return res;
        }


        List<String> periodRangeList = StringUtil.jsPeriodRange(map);
        if (periodRangeList == null) {
            throw new BusinessException("subBookService queryAllSubBook periodRangeList is null");
        }

        map.put("sub_period", periodRangeList.get(periodRangeList.size() - 1));

        List<RedisSub> subList = subBookDao.queryDetailSubMessage(map);
        if (subList == null || subList.isEmpty()) {
            String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" + Thread.currentThread().getStackTrace()[1].getLineNumber();
            throw new BusinessException(errs + ";subBookService queryAllSubBook subList==null || subList.isEmpty()");
        }

        Map<Integer, SubBookMessageVo> hashMap = new HashMap<>();
        List<SubBook> arrBook = new ArrayList<>(list.size() + 10);

        int j = -1;
        int js = -1;

        String satrtSubCode = "1000";

        ParentCodeMapping parentCodeMapping = new ParentCodeMapping();
        for (int i = 0; i < list.size(); i++) {
            SubBook subBook = new SubBook();
            SubBookMessageVo bookVo = list.get(i);

            Integer sub_bk_id = bookVo.getSub_bk_id();
            String accountID = bookVo.getAccountID();
            String period = bookVo.getPeriod();

            String sub_code = bookVo.getSub_code();
            String sub_name = bookVo.getSub_name();

            BigDecimal blanceAmount = getRownUp(bookVo.getBlanceAmount());
            BigDecimal creditAmount = getRownUp(bookVo.getCreditAmount());
            BigDecimal debitAmount = getRownUp(bookVo.getDebitAmount());
            String vcabstact = bookVo.getVcabstact();
            String vouchAID = bookVo.getVouchAID();
            String vouchID = bookVo.getVouchID();
            Integer vouchNum = bookVo.getVouchNum();
            String direction = bookVo.getDirection();
            Long up_date = bookVo.getUp_date();
            Date updateDate = bookVo.getUpdateDate();

            String account_id = bookVo.getAccount_id();
            String account_period = bookVo.getAccount_period();
            String s_subCode = bookVo.getS_subCode();
            String s_subName = bookVo.getS_subName();
            String full_name = bookVo.getFull_name();
            Integer code_level = bookVo.getCode_level();
            Integer debit_credit_direction = bookVo.getDebit_credit_direction();
            BigDecimal init_credit_balance = bookVo.getInit_credit_balance();
            BigDecimal init_debit_balance = bookVo.getInit_debit_balance();
            BigDecimal current_amount_credit = bookVo.getCurrent_amount_credit();
            BigDecimal current_amount_debit = bookVo.getCurrent_amount_debit();
            BigDecimal year_amount_credit = bookVo.getYear_amount_credit();
            BigDecimal year_amount_debit = bookVo.getYear_amount_debit();
            BigDecimal ending_balance_credit = bookVo.getEnding_balance_credit();
            BigDecimal ending_balance_debit = bookVo.getEnding_balance_debit();

            if (account_period == null || period == null || (!period.equals(account_period))) {
                System.out.println(bookVo);
                System.out.println("Pk_sub_id == " + bookVo.getPk_sub_id());
                throw new BusinessException("account_period==null || period==null || (!period.equals(account_period)), account_period=" + account_period + ",period=" + period);
            }
            if (debit_credit_direction == null || (debit_credit_direction != 1 && debit_credit_direction != 2))
                throw new BusinessException("debit_credit_direction==null || (debit_credit_direction!=1 && debit_credit_direction!=2) debit_credit_direction=" + debit_credit_direction);
            if (s_subCode == null || sub_code == null || (!s_subCode.equals(sub_code)))
                throw new BusinessException("s_subCode==null || sub_code==null || (!s_subCode.equals(sub_code)),s_subCode=" + s_subCode + ",sub_code=" + sub_code);
            if (!accountID.equals(account_id))
                throw new BusinessException("!accountID.equals(account_id),accountID=" + accountID + ",account_id=" + account_id);

            hashMap.put(sub_bk_id, bookVo);

            String str_dir = debit_credit_direction == 1 ? "借" : "贷";

            subBook.setSub_bk_id(sub_bk_id);
            subBook.setVouchAID(vouchAID);
            subBook.setVouchID(vouchID);
            subBook.setSub_code(sub_code);
            subBook.setSub_name(sub_name);
            subBook.setVcabstact(vcabstact);
            subBook.setPeriod(period);

            String blance_dir = (blanceAmount.compareTo(BigDecimal.ZERO) != 0) ? str_dir : "平";
            subBook.setDirection(str_dir);

            subBook.setDebitAmount(debitAmount);
            subBook.setCreditAmount(creditAmount);

            subBook.setBlanceAmount(blanceAmount);
            subBook.setVouchNum(vouchNum);
            subBook.setUp_date(up_date);
            subBook.setUpdateDate(updateDate);


            if (!sub_code.equals(satrtSubCode)) {
                Date min_day = jsUpDate(periodRangeList.get(0), "1");
                BigDecimal qc_ye = jsYe(String.valueOf(debit_credit_direction), init_debit_balance, init_credit_balance);
                String init_dir = (qc_ye.compareTo(BigDecimal.ZERO) != 0) ? str_dir : "平";
                if (sub_code.equals("1122")) {
                    System.out.println();
                }
                SubBook subBook0 = createSubBook2(init_dir, "期初余额", period, init_debit_balance, init_credit_balance, qc_ye, min_day, sub_code, full_name);

                arrBook.add(++j, subBook0);
                satrtSubCode = sub_code;
                ++js;
            }

            arrBook.add(++j, subBook);
        }


        int startIndex = arrBook.size() - 1;
        String compareCode = "6902";

        List<SubBook> arrBook2 = new ArrayList<>(arrBook.size() + 10);

        int n = -1;
        int add_year_num = 0;
        for (int k = 0; k < arrBook.size(); k++) {

            SubBook subBook = arrBook.get(k);
            String sub_code = subBook.getSub_code();

            int next_index = k + 1;
            String zy2 = "本期合计";
            String zy3 = "本年累计";
            if (next_index == arrBook.size()) {

                Integer sub_bk_id = subBook.getSub_bk_id();
                SubBookMessageVo subBookMessageVo = hashMap.get(sub_bk_id);
                if (subBookMessageVo == null)
                    throw new BusinessException("subBookMessageVo==null is hashMap.get(sub_bk_id)==null");
                String full_name = subBookMessageVo.getFull_name();
                String period = subBook.getPeriod();

                Integer debit_credit_direction = subBookMessageVo.getDebit_credit_direction();
                String str_dir = debit_credit_direction == 1 ? "借" : "贷";

                BigDecimal ending_balance_credit = subBookMessageVo.getEnding_balance_credit();
                BigDecimal ending_balance_debit = subBookMessageVo.getEnding_balance_debit();
                BigDecimal year_amount_credit = subBookMessageVo.getYear_amount_credit();
                BigDecimal year_amount_debit = subBookMessageVo.getYear_amount_debit();
                BigDecimal current_amount_credit = subBookMessageVo.getCurrent_amount_credit();
                BigDecimal current_amount_debit = subBookMessageVo.getCurrent_amount_debit();
                Date max_day = jsUpDate(period, "2");

                BigDecimal ye = jsYe(String.valueOf(debit_credit_direction), ending_balance_debit, ending_balance_credit);
                String ending_dir = (ye.compareTo(BigDecimal.ZERO) != 0) ? str_dir : "平";

                SubBook subBook1 = createSubBook2(ending_dir, zy2, period, current_amount_debit, current_amount_credit, ye, max_day, sub_code, full_name);
                SubBook subBook2 = createSubBook2(ending_dir, zy3, period, year_amount_debit, year_amount_credit, ye, max_day, sub_code, full_name);
                int size = arrBook.size();

                arrBook2.add(++n, subBook);

                arrBook2.add(++n, subBook1);
                arrBook2.add(++n, subBook2);
                add_year_num = add_year_num + 2;


            } else {

                SubBook nex_subBook = arrBook.get(next_index);
                if (nex_subBook == null) {
                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" +
                            Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(errs + ",nex_subBook==null k=" + k);
                }
                String next_sub_code = nex_subBook.getSub_code();
                if (next_sub_code == null) {

                    String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" +
                            Thread.currentThread().getStackTrace()[1].getLineNumber();
                    throw new BusinessException(errs + ",next_sub_code==null");

                }
                if (!sub_code.equals(next_sub_code)) {

                    Integer sub_bk_id = subBook.getSub_bk_id();
                    SubBookMessageVo subBookMessageVo = hashMap.get(sub_bk_id);

                    if (subBookMessageVo == null) {
                        throw new BusinessException("subBookMessageVo==null is hashMap.get(sub_bk_id)==null");
                    }

                    String period = subBook.getPeriod();

                    String full_name = subBookMessageVo.getFull_name();
                    Integer debit_credit_direction = subBookMessageVo.getDebit_credit_direction();
                    String str_dir = debit_credit_direction == 1 ? "借" : "贷";

                    BigDecimal ending_balance_credit = subBookMessageVo.getEnding_balance_credit();
                    BigDecimal ending_balance_debit = subBookMessageVo.getEnding_balance_debit();

                    BigDecimal year_amount_credit = subBookMessageVo.getYear_amount_credit();
                    BigDecimal year_amount_debit = subBookMessageVo.getYear_amount_debit();

                    BigDecimal current_amount_credit = subBookMessageVo.getCurrent_amount_credit();
                    BigDecimal current_amount_debit = subBookMessageVo.getCurrent_amount_debit();


                    Date max_day = jsUpDate(period, "2");


                    BigDecimal ye = jsYe(String.valueOf(debit_credit_direction), ending_balance_debit, ending_balance_credit);

                    SubBook subBook1 = createSubBook2(str_dir, zy2, period, current_amount_debit, current_amount_credit, ye, max_day, sub_code, full_name);
                    SubBook subBook2 = createSubBook2(str_dir, zy3, period, year_amount_debit, year_amount_credit, ye, max_day, sub_code, full_name);
                    int size = arrBook.size();

                    arrBook2.add(++n, subBook);
                    arrBook2.add(++n, subBook1);
                    arrBook2.add(++n, subBook2);
                    add_year_num = add_year_num + 2;

                } else {
                    arrBook2.add(++n, subBook);

                }
            }
        }


        Map<String, List<SubBook>> groupSubBook = new HashMap<>();
        for (int i = 0; i < arrBook2.size(); i++) {
            SubBook subBook = arrBook2.get(i);
            String sub_code = subBook.getSub_code();
            String period = subBook.getPeriod();
            if (StringUtil.isEmpty(period) || StringUtil.isEmpty(sub_code)) {
                String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" +
                        Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException(errs + ";groupSubBook StringUtil.isEmpty(period) || StringUtil.isEmpty(sub_code)");
            }
            String key = period + "-" + sub_code;
            List<SubBook> bookList = groupSubBook.get(key);
            if (bookList == null || bookList.size() == 0) {
                List<SubBook> arr = new ArrayList<>();
                arr.add(subBook);
                groupSubBook.put(key, arr);
            } else {
                bookList.add(subBook);
                groupSubBook.put(key, bookList);
            }
        }

        int ff = -1;
        int initSize = subList.size() + arrBook2.size();
        List<SubBook> list3 = new ArrayList<>(initSize);
        for (int i = 0; i < subList.size(); i++) {
            RedisSub redisSub = subList.get(i);
            String pk_sub_id = redisSub.getPk_sub_id();

            String sub_code = redisSub.getSub_code();
            String account_period = redisSub.getAccount_period();

            if (StringUtil.isEmpty(sub_code) || StringUtil.isEmpty(account_period)) {
                String errs = "Class: " + this.getClass().getName() + "method: " + Thread.currentThread().getStackTrace()[1].getMethodName() + " line:" +
                        Thread.currentThread().getStackTrace()[1].getLineNumber();
                throw new BusinessException(errs);
            }

            boolean flg2 = false;
            for (int k = 0; k < periodRangeList.size(); k++) {
                String period = periodRangeList.get(k);
                String key = period + "-" + sub_code;
                List<SubBook> bookList = groupSubBook.get(key);
                if (bookList != null && bookList.size() > 0) {
                    flg2 = true;
                    break;
                }
            }

            if (flg2 == false) {
                BigDecimal ending_balance_credit = redisSub.getEnding_balance_credit();
                BigDecimal ending_balance_debit = redisSub.getEnding_balance_debit();
                String full_name = redisSub.getFull_name();
                String sub_dir = parentCodeMapping.getDir(sub_code);
                String str_dir = sub_dir.equals("1") == true ? "借" : "贷";


                String zy1 = "期初余额";
                Date month_first_time = jsUpDate(periodRangeList.get(0), "1");
                BigDecimal ye = jsYe(sub_dir, ending_balance_debit, ending_balance_credit);
                SubBook subBook1 = createSubBook3(str_dir, zy1, account_period, ye, month_first_time, sub_code, full_name);
                list3.add(++ff, subBook1);
                String zy2 = "本期合计";
                String zy3 = "本年累计";
                for (int k = 0; k < periodRangeList.size(); k++) {
                    String accountPeriod = periodRangeList.get(k);
                    Date month_last_time = jsUpDate(accountPeriod, "2");
                    SubBook subBook2 = createSubBook3(str_dir, zy2, accountPeriod, ye, month_last_time, sub_code, full_name);
                    SubBook subBook3 = createSubBook3(str_dir, zy3, accountPeriod, ye, month_last_time, sub_code, full_name);
                    list3.add(++ff, subBook2);
                    list3.add(++ff, subBook3);
                }
            } else {
                for (int k = 0; k < periodRangeList.size(); k++) {
                    String period = periodRangeList.get(k);
                    String key = period + "-" + sub_code;
                    List<SubBook> bookList = groupSubBook.get(key);
                    if (bookList != null && bookList.size() > 0) {
                        for (SubBook book : bookList) {
                            list3.add(++ff, book);
                        }
                    }
                }

            }
        }


        /***************************生成excel***************************************/
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("明细账");
        HSSFDataFormat fm = book.createDataFormat();

        // 标题栏字体
        HSSFFont font1 = book.createFont(); // 创建字体
        font1.setFontName("仿宋_GB2312"); // 设置字体名称
        font1.setFontHeightInPoints((short) 15);// 设置字体大小
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

        // 正常字体样式
        HSSFFont normalFont = book.createFont();
        normalFont.setFontName("黑体");
        normalFont.setFontHeightInPoints((short) 10); // 字体大小

        // 第三行样式 表头部字体样式
        HSSFFont font3 = book.createFont();
        font3.setFontName("仿宋");
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        font3.setFontHeightInPoints((short) 14); // 字体大小
        font3.setColor(HSSFColor.DARK_BLUE.index); // 字体颜色 灰色

        // 标题栏样式
        HSSFCellStyle titleStyle = book.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        titleStyle.setFont(font1);// 选择需要用到的字体格式

        // 正常样式
        HSSFCellStyle normalStyle = book.createCellStyle();
        setCellStyleBroder(normalStyle);
        normalStyle.setFont(normalFont);// 选择需要用到的字体格式

        // 头部样式
        HSSFCellStyle headStyle = book.createCellStyle();
        headStyle.cloneStyleFrom(normalStyle);
        headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        headStyle.setFont(font3);// 选择需要用到的字体格式

        // 设置字体方向向左对准样式
        HSSFCellStyle leftCell = book.createCellStyle();
        leftCell.cloneStyleFrom(normalStyle);
        leftCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //leftCell.setDataFormat(fm.getFormat("yyyy-MM-dd  HH:mm:ss"));
        leftCell.setDataFormat(fm.getFormat("yyyy-MM-dd"));

        // 设置字体方向向右对准样式
        HSSFCellStyle rightCell = book.createCellStyle();
        rightCell.cloneStyleFrom(normalStyle);
        rightCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //
        //rightCell.setDataFormat(fm.getFormat("#,##0.00")); // 小数点后保留两位


        int size = list3.size();
        int rownCount = 3 + size;

        // 设置单元格样式
        for (int i = 0; i < rownCount; i++) {
            HSSFRow row = sheet.createRow(i);
            for (int jj = 0; jj < 8; jj++) {
                HSSFCell cell = row.createCell(jj);
                if (i == 0)
                    cell.setCellStyle(titleStyle);
                else if (i == 1)
                    cell.setCellStyle(normalStyle);
                else if (i == 2)
                    cell.setCellStyle(headStyle);
                else if (jj == 4 || jj == 5 || jj == 7)
                    cell.setCellStyle(rightCell);
                else
                    cell.setCellStyle(leftCell);
            }
        }

        // 五、设置行高列宽:

        // sheet.setColumnWidth(0, 3766); //第一个参数代表列id(从0开始),第2个参数代表宽度值
        sheet.getRow(0).setHeight((short) 480);
        sheet.getRow(1).setHeight((short) 300);
        sheet.getRow(2).setHeight((short) 450);

        // 设置其他行高
        for (int i = 3; i < rownCount; i++)
            sheet.getRow(i).setHeight((short) 280);
        // 设置列宽
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn((short) i, true);
            if (i == 0)
                sheet.setColumnWidth(i, 7500);
            else if (i == 1)
                sheet.setColumnWidth(i, 4000);
            else if (i == 3)
                sheet.setColumnWidth(i, 6500);
            else if (i == 6)
                sheet.setColumnWidth(i, 3500);
            else
                sheet.setColumnWidth(i, 5000);
        }
        // 七、合并单元格:
        CellRangeAddress region = new CellRangeAddress(0, 0, (short) 0, (short) 7);
        sheet.addMergedRegion(region);
        sheet.getRow(0).getCell(0).setCellValue("明细账");
        CellRangeAddress region1 = new CellRangeAddress(1, 1, (short) 0, (short) 5);
        CellRangeAddress region2 = new CellRangeAddress(1, 1, (short) 6, (short) 7);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);

        Account account = (Account) map.get("acc");
        String companyName = account.getCompanyName();
        companyName = companyName == null ? "" : companyName;
        // formtMonth
        String formtMonth = null;


        if (map.get("period") == null) {
            String str1 = getFormtMonth(periodRangeList.get(0));
            String str2 = getFormtMonth(periodRangeList.get(periodRangeList.size() - 1));
            formtMonth = str1 + " 至 " + str2;
        } else {
            formtMonth = getFormtMonth(periodRangeList.get(0));

        }
        String towRowStr = companyName + "*" + formtMonth;

        String replace = towRowStr.replace("*", "   ");
        String day = DateUtil.getDay(); // yyyy-MM-dd

        sheet.getRow(1).getCell(0).setCellValue(replace);
        sheet.getRow(1).getCell(6).setCellValue(day); // 日期
        sheet.getRow(1).getCell(6).setCellStyle(rightCell);

        String[] arr = new String[]{"科目", "日期", "凭证字号", "摘要", "借方", "贷方", "方向", "余额"};
        for (int i = 0; i < 8; i++)
            sheet.getRow(2).getCell(i).setCellValue(arr[i]);

        int ind = 0;

        for (int i = 3; i < rownCount; i++) {
            SubBook sb = list3.get(ind);

            Integer sub_bk_id = sb.getSub_bk_id();
            String sub_code = sb.getSub_code();
            String sub_name = sb.getSub_name();
            String period = sb.getPeriod();

            BigDecimal blanceAmount = StringUtil.bigDecimalIsNull(sb.getBlanceAmount());
            BigDecimal creditAmount = StringUtil.bigDecimalIsNull(sb.getCreditAmount());
            BigDecimal debitAmount = StringUtil.bigDecimalIsNull(sb.getDebitAmount());
            String dir = sb.getDirection();
            String zy = sb.getVcabstact();
            Integer vouchNum = sb.getVouchNum();
            Long up_date = sb.getUp_date();
            Date updateDate = sb.getUpdateDate();

            HSSFRow row = sheet.getRow(i); // 行号

            int indexOf = zy.indexOf("期初");
            if (i > 2) {
                if (indexOf != -1) {
                    String sub_code_name = sub_code + "-" + sub_name;

                    row.getCell(0).setCellValue(sub_code_name);
                } else {
                    row.getCell(0).setCellValue("");
                }
            }

            if (vouchNum == null) {
                //凭证等于null 只有三种情况  期初 本期合计 本年累计
                //indexOf==-1 排除期初的数据，只在本期合计与本年累计前面添加空格，更加方面excel数据展示
                if (indexOf == -1) {
                    zy = "    " + zy;
                }
                row.getCell(2).setCellValue(""); //把凭证号显示置为空
            } else {
                row.getCell(2).setCellValue("记-" + vouchNum.toString());
            }


            row.getCell(1).setCellValue(updateDate == null ? new Date(up_date) : updateDate);
            row.getCell(3).setCellValue(zy);
            if (sub_code.equals("1122")) {
                String init_j = debitAmount.toPlainString();
                String qi_mo = blanceAmount.toPlainString();


            }
            String val1 = debitAmount.doubleValue() == 0.0 ? "" : debitAmount.toPlainString();
            row.getCell(4).setCellValue(val1);
            String val2 = creditAmount.doubleValue() == 0.0 ? "" : creditAmount.toPlainString();
            row.getCell(5).setCellValue(val2);
            row.getCell(6).setCellValue(dir);
            String val3 = blanceAmount.doubleValue() == 0.0 ? "" : blanceAmount.toPlainString();
            row.getCell(7).setCellValue(val3);
            ind++;
        }


        Map<String, Object> rtmap = new HashMap<>();
        rtmap.put("book", book);
        rtmap.put("code", "0");

        return rtmap;
    }


    private SubBook createSubBook3(String str_dir, String zy, String account_period, BigDecimal ye, Date day, String sub_code, String full_name) {
        SubBook book = new SubBook();
        book.setDirection(str_dir);
        book.setVcabstact(zy);

        book.setDebitAmount(null);
        book.setCreditAmount(null);
        book.setBlanceAmount(getRownUp(ye));
        book.setUpdateDate(day); // 时间 yyyy-MM-dd
        book.setPeriod(account_period);

        book.setUp_date(day.getTime());
        book.setSub_code(sub_code);
        book.setSub_name(full_name);
        book.setVouchNum(null);
        return book;
    }


    @SuppressWarnings("unused")
    private SubBook createSubBook2(String dir, String zy, String period, BigDecimal debit, BigDecimal credit, BigDecimal ye, Date day, String subcode, String fullName) {
        SubBook book = new SubBook();
        book.setDirection(dir);
        book.setVcabstact(zy);
        BigDecimal rownUp = getRownUp(debit);

        book.setDebitAmount(getRownUp(debit));
        book.setCreditAmount(getRownUp(credit));

        BigDecimal rownUp2 = getRownUp(ye);
        String plainString = rownUp2.toPlainString();
        String convExpoToRegular = StringUtil.convExpoToRegular(plainString);

        book.setBlanceAmount(getRownUp(ye));
        book.setUpdateDate(day); // 时间 yyyy-MM-dd
        book.setPeriod(period);

        book.setUp_date(day.getTime());
        book.setSub_code(subcode);
        book.setSub_name(fullName);
        book.setVouchNum(null);
        return book;
    }


    @SuppressWarnings({"deprecation", "unused", "unchecked"})
    @Override

    public Map<String, Object> exportProfitStatement(User user, Account account, String period) throws BusinessException {
        Map<String, Object> res = new HashMap<>();
        try {

            tBasicIncomeStatementService.deleteIncomeStatrment(user, account);
            tBasicIncomeStatementService.addIncomeStatement(user, account);
            Map<String, Object> result = tBasicIncomeStatementService.queryIncomeStatrment(user, account);

            if (result == null || result.size() == 0) {
                throw new BusinessException("queryIncomeStatrment==null,未查询到利润表数据");
            }

            List<TBasicIncomeStatement> liRuns = (List<TBasicIncomeStatement>) result.get("queryIncomeStatrment");

            if (liRuns == null || liRuns.size() == 0 || liRuns.get(0) == null) {
                throw new BusinessException("liRun==null,未查询到利润表数据");
            }

            TBasicIncomeStatement liRun = liRuns.get(0);

            /***************************生成excel***************************************/
            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet(period + "-利润表");

            // 标题栏字体 1
            HSSFFont font1 = book.createFont(); // 创建字体
            font1.setFontName("仿宋_GB2312"); // 设置字体名称
            font1.setFontHeightInPoints((short) 18);// 设置字体大小
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

            // 正常字体样式 2
            HSSFFont normalFont = book.createFont();
            //normalFont.setFontName("黑体");
            normalFont.setFontName("Arial");
            normalFont.setFontHeightInPoints((short) 12); // 字体大小

            // 第三行样式 表头部字体样式 3
            HSSFFont font3 = book.createFont();
            font3.setFontName("仿宋");
            font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
            font3.setFontHeightInPoints((short) 16); // 字体大小
            font3.setColor(HSSFColor.DARK_BLUE.index); // 字体颜色 灰色

            // 标题栏样式 1
            HSSFCellStyle titleStyle = book.createCellStyle();
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            titleStyle.setFont(font1);// 选择需要用到的字体格式

            // 正常样式 2
            HSSFCellStyle normalStyle = book.createCellStyle();
            setCellStyleBroder(normalStyle);
            normalStyle.setFont(normalFont);// 选择需要用到的字体格式

            // 头部样式 3
            HSSFCellStyle headStyle = book.createCellStyle();
            headStyle.cloneStyleFrom(normalStyle);
            headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
            headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
            headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            headStyle.setFont(font3);// 选择需要用到的字体格式


            // 设置字体方向向左对准样式
            HSSFCellStyle leftCell = book.createCellStyle();
            leftCell.cloneStyleFrom(normalStyle);
            leftCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            // 设置字体方向向右对准样式
            HSSFCellStyle rightCell = book.createCellStyle();
            rightCell.cloneStyleFrom(normalStyle);
            rightCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT); //

            HSSFDataFormat fm = book.createDataFormat();
            rightCell.setDataFormat(fm.getFormat("#,##0.00")); // 小数点后保留两位


            int rownCount = 3 + 20;

            // 设置行样式
            for (int i = 0; i < rownCount; i++) {
                HSSFRow row = sheet.createRow(i);
                for (int k = 0; k < 3; k++) {
                    HSSFCell cell = row.createCell(k);
                    if (i == 0) {
                        cell.setCellStyle(titleStyle);
                    } else if (i == 2) {
                        cell.setCellStyle(headStyle);
                    } else {
                        cell.setCellStyle(k == 0 ? leftCell : rightCell);

                    }
                }
            }

            // 五、设置行高列宽:

            // sheet.setColumnWidth(0, 3766); //第一个参数代表列id(从0开始),第2个参数代表宽度值
            sheet.getRow(0).setHeight((short) 800);
            sheet.getRow(1).setHeight((short) 550);
            sheet.getRow(2).setHeight((short) 700);

            // 设置其他行高
            for (int i = 3; i < rownCount; i++) {
                sheet.getRow(i).setHeight((short) 550);
            }

            // 设置列宽
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn((short) i, true);
                if (i == 0) {
                    sheet.setColumnWidth(i, 14000);
                } else if (i == 1) {
                    sheet.setColumnWidth(i, 6500);
                } else {
                    sheet.setColumnWidth(i, 6500);
                }
            }
            // 七、合并单元格:
            CellRangeAddress region = new CellRangeAddress(0, 0, (short) 0, (short) 2);
            sheet.addMergedRegion(region);
            sheet.getRow(0).getCell(0).setCellValue("利润表");

            String formtMonth = getFormtMonth(period);


            sheet.getRow(1).getCell(0).setCellValue(account.getCompanyName());
            sheet.getRow(1).getCell(1).setCellValue(formtMonth); // 日期
            sheet.getRow(1).getCell(2).setCellValue("单位：元");

            //设置第三行标题
            String[] arr = new String[]{"项目", "本年金额", "本期金额"};
            for (int i = 0; i < 3; i++) {
                sheet.getRow(2).getCell(i).setCellValue(arr[i]);
            }

            List<String> list3 = new ArrayList<>();

            // 读取数据到工作簿
            //int rownCount = 3 + 20;
            for (int i = 3; i < rownCount; i++) {
                HSSFRow row = sheet.getRow(i);
                if (i == 3) {
                    setCellVal(row, "营业收入", liRun.getYearSalesFromOperati(), liRun.getCurrentSalesFromOperati());
                } else if (i == 4) {
                    setCellVal(row, "减：营业成本", liRun.getYearLessOperatingCosts(), liRun.getCurrentLessOperatingCosts());
                } else if (i == 5) {
                    setCellVal(row, "营业税金及附加", liRun.getYearOperatingTaxAndAdditions(), liRun.getCurrentOperatingTaxAndAdditions());
                } else if (i == 6) {
                    setCellVal(row, "销售费用", liRun.getYearSellingExpenses(), liRun.getCurrentSellingExpenses());
                } else if (i == 7) {
                    setCellVal(row, "管理费用", liRun.getYearGeneralAndAdministrativeExpense(), liRun.getCurrentGeneralAndAdministrativeExpense());
                } else if (i == 8) {
                    setCellVal(row, "财务费用", liRun.getYearFinaneiaExpense(), liRun.getCurrentFinaneiaExpense());
                } else if (i == 9) {
                    setCellVal(row, "资产减值损失", liRun.getYearLossesOnTheAssetImpairment(), liRun.getCurrentLossesOnTheAssetImpairment());
                } else if (i == 10) {
                    setCellVal(row, "加：公允价值变动收益（损失以“-”号填列", liRun.getYearAddProfitsOrLossesOntheChangesInFairValue(), liRun.getCurrentAddProfitsOrLossesOntheChangesInFairValue());
                } else if (i == 11) {
                    setCellVal(row, "投资收益（损失以“-”号填列）", liRun.getYearInvestmentIncome(), liRun.getCurrentInvestmentIncome());
                } else if (i == 12) {
                    setCellVal(row, "其中：对联营企业和合营企业的投资收益", liRun.getYearAmongInvestmentIncomeFromAffiliatedBusiness(), liRun.getCurrentAmongInvestmentIncomeFromAffiliatedBusiness());
                } else if (i == 13) {
                    setCellVal(row, "二、营业利润（亏损以“-”号填列）", liRun.getYearOperatingIncome(), liRun.getCurrentOperatingIncome());
                } else if (i == 14) {
                    setCellVal(row, "加：营业外收入", liRun.getYearAddNonOperatingIncome(), liRun.getCurrentAddNonOperatingIncome());
                } else if (i == 15) {
                    setCellVal(row, "减：营业外支出", liRun.getYearLessNonOperatingExpense(), liRun.getCurrentLessNonOperatingExpense());
                } else if (i == 16) {
                    setCellVal(row, "其中：非流动资产处置损失", liRun.getYearIncludingLossesFromDisposalOfNonCurrentAssets(), liRun.getCurrentIncludingLossesFromDisposalOfNonCurrentAssets());
                } else if (i == 17) {
                    setCellVal(row, "三、利润总额（亏损总额以“-”号填列）", liRun.getYearIncomeBeforeTax(), liRun.getCurrentIncomeBeforeTax());
                } else if (i == 18) {
                    setCellVal(row, "减：所得税费用", liRun.getYearLessIncomeTax(), liRun.getCurrentLessIncomeTax());
                } else if (i == 19) {
                    setCellVal(row, "四、净利润（净亏损以“-”号填列）", liRun.getYearEntIncome(), liRun.getCurrentEntIncome());
                } else if (i == 20) {
                    setCellVal(row, "五、每股收益", liRun.getYearEarningsPerShare(), liRun.getCurrentEarningsPerShare());
                } else if (i == 21) {
                    setCellVal(row, "（一）基本每股收益", liRun.getYearBasicEps(), liRun.getCurrentBasicEps());
                } else if (i == 22) {
                    setCellVal(row, "（二）稀释每股收益", liRun.getYearDilutedEps(), liRun.getCurrentDilutedEps());
                }
            }
            res.put("book", book);
            res.put("code", "0");
            return res;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }


    @SuppressWarnings("unused")
    private void setCellVal(HSSFRow row, String field, BigDecimal currentYear, BigDecimal currentPeriod) {
        row.getCell(0).setCellValue(field);
        row.getCell(1).setCellValue(StringUtil.bigDecimalIsNull(currentYear).doubleValue());
        row.getCell(2).setCellValue(StringUtil.bigDecimalIsNull(currentPeriod).doubleValue());
    }


}


class ComparatorSub implements Comparator<Map<String, Object>> {
    @Override
    public int compare(Map<String, Object> paramT1, Map<String, Object> paramT2) {
        String s1 = paramT1.get("subCode").toString();
        String s2 = paramT2.get("subCode").toString();
        return s1.compareTo(s2);

    }

}

class ComparatorPeiod implements Comparator<SubMessageVo> {
    @Override
    public int compare(SubMessageVo paramT1, SubMessageVo paramT2) {
        return paramT1.getAccount_period().compareTo(paramT2.getAccount_period());
    }

}

class ComparatorCode implements Comparator<SubMessageVo> {
    @Override
    public int compare(SubMessageVo paramT1, SubMessageVo paramT2) {
        return paramT1.getSub_code().compareTo(paramT2.getSub_code());
    }
}

class ComparatorSuBookPeriod implements Comparator<Map<String, Object>> {
    @Override
    public int compare(Map<String, Object> paramT1, Map<String, Object> paramT2) {
        String per1 = paramT1.get("period").toString();
        String per2 = paramT2.get("period").toString();
        return per1.compareTo(per2);
    }
}

class ComparatorID implements Comparator<SubBook> {
    @Override
    public int compare(SubBook paramT1, SubBook paramT2) {
        return paramT1.getSub_bk_id().compareTo(paramT2.getSub_bk_id());
    }
}

//比较凭证号
class ComparatorVouchNum implements Comparator<SubBook> {
    @Override
    public int compare(SubBook paramT1, SubBook paramT2) {
        int compareTo = paramT1.getVouchNum().compareTo(paramT2.getVouchNum());
        if (compareTo == 0) {
            //库存商品_螺丝 1405011  借100
            //库存商品_螺帽 1405012 借200
            //应交税费_应交增值税_进项税 借51
            //應付公司账款_中财建材有限公司 贷351
            //像这张凭证 1405明细账出现两次以上 ，但是它们都属于同一张凭证，凭证号是相同的，排序的时候就分不出前后顺序。有可能螺帽排在前面，这样明细账显示在页面的时候时间顺序错误了。
            //变成螺帽 借200 余额200 ，螺丝 借100 余额300 。其实正确应该是螺丝借100 余额100，螺帽借200 余额300
            //错误的数据
            //螺丝   200 200
            //螺帽 100 300
            //正确的数据
            //螺丝   100 100
            //螺帽 	100 300
            //解决办法 虽然这两条明细账凭证号相同，但是他们的主键一定不同，每条明细账都有唯一的主键。如果凭证号相同的话，那么再去比较主键，这样先后顺序就不会出错了。
            //主键是自增并且根据凭证顺序生成的

            //如果一个科目出现在同一张凭证两次或者两次以上，那么在按照主键排序
            int compareTo2 = paramT1.getSub_bk_id().compareTo(paramT2.getSub_bk_id());
            return compareTo2;
        } else
            return compareTo;
    }
}

class ComparatorDate implements Comparator<SubBook> {
    @Override
    public int compare(SubBook paramT1, SubBook paramT2) {
        long lo1 = paramT1.getUp_date().longValue();
        long lo2 = paramT2.getUp_date().longValue();
        return ((lo1 == lo2) ? 0 : (lo1 < lo2) ? -1 : 1);

    }

}

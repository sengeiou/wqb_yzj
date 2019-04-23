package com.wqb.service.subexcel.impl;

import com.wqb.common.BusinessException;
import com.wqb.common.StringUtil;
import com.wqb.common.UUIDUtils;
import com.wqb.dao.subexcel.TBasicSubjectExcelMapper;
import com.wqb.model.Account;
import com.wqb.model.TBasicSubjectExcel;
import com.wqb.model.User;
import com.wqb.service.subexcel.TBasicSubjectExcelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 司氏旭东
 * @ClassName: TBasicSubjectExcelServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年1月4日 上午10:34:51
 */
//@Transactional
@Service("tBasicSubjectExcelService")
public class TBasicSubjectExcelServiceImpl implements TBasicSubjectExcelService {
    @Autowired
    TBasicSubjectExcelMapper subExcelDao;

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(new Date());
        System.out.println("当前时间为:" + timeStr);
    }

    /**
     * @param list
     * @param map
     * @param file
     * @return Map<String, Object> 返回类型
     * @Title: uploadSubExcel
     * @Description: 科目初始化excel表上传
     * @date 2018年1月4日 上午10:34:51
     * @author SiLiuDong 司氏旭东
     * @see com.wqb.service.subexcel.TBasicSubjectExcelService#uploadSubExcel(java.util.List, java.util.Map, java.io.File)
     */
    @Override
    //@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> uploadSubExcel(List<Map<String, Object>> list, Map<String, String> map, File file) {
        Map<String, Object> result = new HashMap<String, Object>();
        //定义金蝶Excel导出科目余额表对象
        TBasicSubjectExcel subExcel = null;
        List<TBasicSubjectExcel> tBasicSubjectExcelList = new ArrayList<TBasicSubjectExcel>();
        String subCode = "";
        String subName = "";
        int j = 0;
        try {
            //循环遍历每一行excel表数据
            for (int i = 0; i < list.size(); i++)//从第一行开始读
            {
                String isMultipleSiblings = "1"; // 是否多个同级(0无，1有)
                j = i + 1; //获取真实行数
                subExcel = new TBasicSubjectExcel();

                Map<String, Object> subExcelMap = list.get(i);//获取一行科目表数据
                subExcel.setPkSubExcelId(UUIDUtils.getUUID()); //设置主键
                subExcel.setUserId(map.get("userID")); //设置用户ID
                subExcel.setAccountId(map.get("accountID")); //设置账套ID

                String period = subExcelMap.get("map0").toString().trim(); //期间

                // 判断 不是外币的科目表  最后一行是合计 （合计期间为0读出来是0.0 给过滤掉）
                if ("0".equals(period) || "0.0".equals(period))
                    continue;

                if (null == period)
                    period = "";
                subExcel.setPeriod(period.split("\\.")[0]); // 截取字符串xx.yy  包括点后面所有的.yy

                String typeOfCurrency = subExcelMap.get("map3").toString().trim(); //币别
                subExcel.setTypeOfCurrency(typeOfCurrency);

                String initDebitBalance = subExcelMap.get("map4") == null ? "0" : subExcelMap.get("map4").toString(); //期初余额(借方)
                BigDecimal initDebitBalancebd = new BigDecimal(initDebitBalance);
                subExcel.setInitDebitBalance(initDebitBalancebd);

                String initCreditBalance = subExcelMap.get("map5") == null ? "0" : subExcelMap.get("map5").toString(); //期初余额(贷方)
                BigDecimal initCreditBalancebd = new BigDecimal(initCreditBalance);
                subExcel.setInitCreditBalance(initCreditBalancebd);

                String currentAmountCredit =
                        subExcelMap.get("map6") == null ? null : subExcelMap.get("map6").toString(); //本期发生额(借方)
                BigDecimal currentAmountCreditbd = new BigDecimal(currentAmountCredit);
                subExcel.setCurrentAmountCredit(currentAmountCreditbd);

                String currentAmountDebit = subExcelMap.get("map7") == null ? "0" : subExcelMap.get("map7").toString(); //本期发生额(贷方)
                BigDecimal currentAmountDebitbd = new BigDecimal(currentAmountDebit);
                subExcel.setCurrentAmountDebit(currentAmountDebitbd);

                String yearAmountDebit = subExcelMap.get("map8") == null ? "0" : subExcelMap.get("map8").toString(); //本年累计发生额(借方)
                BigDecimal yearAmountDebitbd = new BigDecimal(yearAmountDebit);
                subExcel.setYearAmountDebit(yearAmountDebitbd);

                String yearAmountCredit = subExcelMap.get("map9") == null ? "0" : subExcelMap.get("map9").toString(); //本年累计发生额(贷方)
                BigDecimal yearAmountCreditbd = new BigDecimal(yearAmountCredit);
                subExcel.setYearAmountCredit(yearAmountCreditbd);

                String endingBalanceDebit =
                        subExcelMap.get("map10") == null ? "0" : subExcelMap.get("map10").toString(); //期末余额(借方)
                BigDecimal endingBalanceDebitbd = new BigDecimal(endingBalanceDebit);
                subExcel.setEndingBalanceDebit(endingBalanceDebitbd);

                String endingBalanceCredit =
                        subExcelMap.get("map11") == null ? "0" : subExcelMap.get("map11").toString(); //期末余额(贷方)
                BigDecimal endingBalanceCreditbd = new BigDecimal(endingBalanceCredit);
                subExcel.setEndingBalanceCredit(endingBalanceCreditbd);

                subExcel.setIsMatching("0");

                Date date = new Date();//获得系统时间.

                String timestamp = String.valueOf(date.getTime());
                subExcel.setUpdateTimestamp(timestamp); //更新 时间（时间戳）

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                String format = sdf.format(date);//将时间格式转换成符合Timestamp要求的格式.
                Timestamp updateDate = Timestamp.valueOf(format);//把时间转换
                subExcel.setUpdateDate(updateDate);

                String path = file.getPath();
                subExcel.setFileUrl(path);

                if (StringUtils.isBlank(list.get(0).get("map1").toString().trim()))
                    return result; // 如果第一行的科目代码为空时 返回错误信息

                /** 科目代码 要是不为空 执行以下操作 **/
                if (StringUtils.isNotBlank(subExcelMap.get("map1").toString().trim()))//科目编码不为空时
                {
                    int next = i + 1;
                    if (next > list.size() - 1) {
                        next = i;
                    }
                    Map<String, Object> map2 = list.get(next);
                    if (null != map2.get("map1") && StringUtils.isNotBlank(map2.get("map1").toString())) // 判断下一行的科目代码不为空时执行
                    {
                        isMultipleSiblings = "0"; // 是否多个同级(0无，1有)（数据库添加默认值）
                    }
                    subExcel.setIsMultipleSiblings(isMultipleSiblings); //是否多个同级(0无，1有)

                    subCode = subExcelMap.get("map1").toString().trim(); //科目编码
                    subExcel.setSubCode(subCode.split("\\.")[0]); // 截取字符串xx.xx  包括点后面所有的

                    subName = subExcelMap.get("map2").toString().trim(); //科目名称
                    subExcel.setSubName(subName.toString().trim());

                } else {
                    isMultipleSiblings = "0";
                    subExcel.setIsMultipleSiblings(isMultipleSiblings); //是否多个同级(0无，1有)
                    subExcel.setSiblingsCoding(subCode.split("\\.")[0]); //同级编码(一个银行多个外币时用到)
                    subExcel.setSiblingsSubName(subName.toString().trim());//同级科目名称(一个银行多个外币时用到)
                }

                tBasicSubjectExcelList.add(subExcel);
//				 把这一行数据插入到数据库
//				subExcelDao.uploadSubExcel(subExcel);
                result.put("msg", "共计导入 " + j + "行。");
            }
            int no = subExcelDao.uploadSubExcelList(tBasicSubjectExcelList);
            result.put("no", "共计导入 " + no + "行。");
            result.put("code", 1);
        } catch (Exception e) {
            result.put("msg", "导入失败，必填字段有空，请检查第" + (j + 1) + "行，状态的属性。");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<TBasicSubjectExcel> querySubByisMatching(Map<String, Object> param) {

        List<TBasicSubjectExcel> list = subExcelDao.querySubByisMatching(param);
        return list;
    }

    /**
     * @param keyWord
     * @return Map<String, Object> 返回类型
     * @Title: querySubExcel
     * @Description: 查询已导入的初始化excel数据
     * @date 2017年12月7日 下午3:12:45
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectExcel> querySubExcel(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicSubjectExcel tBasicSubjectExcel = new TBasicSubjectExcel();
        List<TBasicSubjectExcel> list = new ArrayList<TBasicSubjectExcel>();
        try {
            //获取用户信息
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) userDate.get("user");
            Account account = (Account) userDate.get("account");
            //用户id
            String userId = user.getUserID();
            //账套id
            String accountId = account.getAccountID();
            if (StringUtil.isEmptyWithTrim(userId) || StringUtil.isEmptyWithTrim(accountId)) {
                result.put("message", "fail");
            }
            tBasicSubjectExcel.setAccountId(accountId);
            tBasicSubjectExcel.setUserId(userId);
            list = subExcelDao.querySubExcel(tBasicSubjectExcel);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
        }
        return list;
    }

    /**
     * @param session
     * @return
     * @throws BusinessException List<TBasicSubjectExcel>    返回类型
     * @Title: querySubExcelMoney
     * @Description: 查询系统科目真实金额
     * @date 2018年1月25日  下午8:28:08
     * @author SiLiuDong 司氏旭东
     */
    @Override
    public List<TBasicSubjectExcel> querySubExcelMoney(HttpSession session) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicSubjectExcel tBasicSubjectExcel = new TBasicSubjectExcel();
        List<TBasicSubjectExcel> list = new ArrayList<TBasicSubjectExcel>();
        try {
            //获取用户信息
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) userDate.get("user");
            Account account = (Account) userDate.get("account");
            //用户id
            String userId = user.getUserID();
            //账套id
            String accountId = account.getAccountID();
            if (StringUtil.isEmptyWithTrim(userId) || StringUtil.isEmptyWithTrim(accountId)) {
                result.put("message", "fail");
            }
            tBasicSubjectExcel.setAccountId(accountId);
            tBasicSubjectExcel.setUserId(userId);
            list = subExcelDao.querySubExcelMoney(tBasicSubjectExcel);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
        }
        return list;
    }

    @Override
    public List<TBasicSubjectExcel> querySubBySubSode(Map<String, Object> param) {

        List<TBasicSubjectExcel> list = subExcelDao.querySubBySubSode(param);
        return list;
    }

    @Override
    public Map<String, Object> deleteSubExcelAll(HttpSession session) {
        Map<String, Object> result = new HashMap<String, Object>();
        TBasicSubjectExcel tBasicSubjectExcel = new TBasicSubjectExcel();
        try {
            //获取用户信息
            Map<String, Object> userDate = (Map<String, Object>) session.getAttribute("userDate");
            User user = (User) userDate.get("user");
            Account account = (Account) userDate.get("account");
            //用户id
            String userId = user.getUserID();
            //账套id
            String accountId = account.getAccountID();
            if (StringUtil.isEmptyWithTrim(userId) || StringUtil.isEmptyWithTrim(accountId)) {
                result.put("message", "fail");
            }
            tBasicSubjectExcel.setAccountId(accountId);
            tBasicSubjectExcel.setUserId(userId);
            subExcelDao.deleteSubExcelAll(tBasicSubjectExcel);
//			int no = subExcelDao.deleteSubExcelAll(tBasicSubjectExcel);
//			result.put("no", no);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", -1);
        }
        return result;
    }

    @Override
    public List<TBasicSubjectExcel> updateSubExcel(Map<String, Object> param) {
        TBasicSubjectExcel tBasicSubjectExcel = new TBasicSubjectExcel();
        tBasicSubjectExcel.setAccountId(param.get("userID").toString());
        tBasicSubjectExcel.setAccountId(param.get("accountID").toString());

        List<TBasicSubjectExcel> list = subExcelDao.updateSubExcel(param);
        return list;
    }

    @Override
    public TBasicSubjectExcel getExcelSubByPKID(String pkSubExcelID) throws BusinessException {

        return subExcelDao.getExcelSubByPKID(pkSubExcelID);
    }

}

package com.wqb.common;

import com.wqb.model.TBasicSubjectMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewSubMessageCode {
    private static Log4jLogger logger = Log4jLogger.getLogger(NewSubMessageCode.class);

    /**
     * @param subExcelListLength
     * @return List<Integer>    返回类型
     * @Title: subExcelListLength
     * @Description: 取出 科目长度集合
     * @date 2018年1月8日  下午3:49:34
     * @author SiLiuDong 司氏旭东
     */
    public List<Integer> subMessageListLength(List<TBasicSubjectMessage> tBasicSubjectMessageList) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        Set<Integer> set = new HashSet<Integer>();
        int excelImportCodeLength = 0;
        // 循环遍历 tBasicSubjectExcelList 集合  取出科目编码长度
        for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
            String excelImportCode = tBasicSubjectMessage.getExcelImportCode();
            if (StringUtils.isNotBlank(excelImportCode)) {
                // 获取Excel导入科目编码长度
                excelImportCodeLength = excelImportCode.length();
                // 把获取到的长度放到set中  去重
                set.add(excelImportCodeLength);
            }
        }
        // 把set转换成list
        arrayList.addAll(set);
        return arrayList;
    }

    /**
     * @param tBasicSubjectMessageList
     * @return List<Integer>    返回类型
     * @Title: subMessageListLength2
     * @Description: 取出 科目长度集合2
     * @date 2018年5月21日  上午11:15:30
     * @author SiLiuDong 司氏旭东
     */
    public List<Integer> subMessageListLength2(List<TBasicSubjectMessage> tBasicSubjectMessageList) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        Set<Integer> set = new HashSet<Integer>();
        int subCodeLength = 0;
        // 循环遍历 tBasicSubjectExcelList 集合  取出科目编码长度
        for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
            String subCode = tBasicSubjectMessage.getSubCode();
            if (StringUtils.isNotBlank(subCode)) {
                // 获取科目更改后的导入科目编码长度
                subCodeLength = subCode.length();
                // 把获取到的长度放到set中  去重
                set.add(subCodeLength);
            }
        }
        // 把set转换成list
        arrayList.addAll(set);
        return arrayList;
    }

    /**
     * @param tBasicSubjectExcelList
     * @return List<TBasicSubjectMessage>    返回类型
     * @Title: newSubExcelCodeList
     * @Description: 4-3-3型的科目编码
     * @date 2018年1月8日  下午3:49:44
     * @author SiLiuDong 司氏旭东
     */
    public List<TBasicSubjectMessage> newSubMessageCodeList(List<TBasicSubjectMessage> tBasicSubjectMessageList) {
        // 取出科目长度set集合 [ 4, 7, 9]
        List<Integer> subMessageListLength = subMessageListLength(tBasicSubjectMessageList);
        StringBuffer Length = new StringBuffer();

        for (int i = 0; i < subMessageListLength.size(); i++) {
            Integer integer = subMessageListLength.get(i);
            Length.append(", ");
            Length.append(integer);
            Length.append(" ");
        }
        logger.info("要匹配的科目代码级别为：" + Length + "！！！");

        ArrayList<TBasicSubjectMessage> SubMessageList = new ArrayList<TBasicSubjectMessage>();
        if (subMessageListLength.size() >= 1) // 表示 一级以上的科目
        {
            String zero = "0";
            String stringGet0 = subMessageListLength.get(0).toString();
            int intGet0 = (int) subMessageListLength.get(0);
            int size = subMessageListLength.size();

            String stringGet1 = null;
            int intGet1 = 0;
            if (size > 1) {
                stringGet1 = subMessageListLength.get(1).toString();
                intGet1 = (int) subMessageListLength.get(1);
            }
            String stringGet2 = null;
//			int intGet2 = 0;
            if (size > 2) {
                stringGet2 = subMessageListLength.get(2).toString();
//				intGet2 = (int) subMessageListLength.get(2);
            }

            String stringGet3 = null;
//			int intGet3 = 0;
            if (size > 3) {
                stringGet3 = subMessageListLength.get(3).toString();
//				intGet3 = (int) subMessageListLength.get(3);
            }

            // 更改二级编码
            if ("4".equals(stringGet0) && "6".equals(stringGet1)) // 处理 4-2- 格式的
            {
                for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                    // 获取Excel 导入的科目编码
                    String excelImportCode = tBasicSubjectMessage.getSubCode();
                    //String excelImportCode = tBasicSubjectMessage.getExcelImportCode()
                    // 获取同级科目编码
                    String excelImportSiblingsCoding = tBasicSubjectMessage.getExcelImportSiblingsCoding();

                    if (StringUtils.isNotBlank(excelImportCode) && excelImportCode.length() > intGet0) {
                        //  1为原字符串，2为要插入的字符串，3为插入位置
                        int site = (int) subMessageListLength.get(0);
                        StringBuffer newCode = newSubMessageCode(excelImportCode, zero, site);
                        tBasicSubjectMessage.setSubCode(newCode.toString());
                        SubMessageList.add(tBasicSubjectMessage);
                    } else if (StringUtils.isNotBlank(excelImportSiblingsCoding) && excelImportSiblingsCoding.length() > intGet0) {

                        //  1为原字符串，2为要插入的字符串，3为插入位置
                        StringBuffer newSiblingsCoding = newSubMessageCode(excelImportSiblingsCoding, zero, intGet0);
                        tBasicSubjectMessage.setSiblingsCoding(newSiblingsCoding.toString());
                        SubMessageList.add(tBasicSubjectMessage);
                    } else {
                        SubMessageList.add(tBasicSubjectMessage);
                    }
                }
                List<Integer> SubMessageListLength2 = subMessageListLength2(SubMessageList);
                // 更改三级编码
                if (SubMessageListLength2.size() > 2 && SubMessageListLength2.get(2) == 9) {
                    List<TBasicSubjectMessage> subMessageList2 = new ArrayList<TBasicSubjectMessage>();
//					String int01 = SubMessageListLength2.get(0).toString();
                    int int31 = (int) SubMessageListLength2.get(1);
                    String int11 = SubMessageListLength2.get(1).toString();
                    // 只 处理 以 4-3-？的三级科目
                    //先 判断 "4".equals(int0) 一级科目编码为4为数，
                    //判断 "7".equals(int1) 是否 4-3-？格式的科目编码
                    // tbList.size() > 2 至少有三级科目的
                    // (int)tbList.get(3) < 10    三级科目位数小于10的
                    if ("7".equals(int11) && SubMessageListLength2.size() > 2 && (int) SubMessageListLength2.get(2) < 10) {
                        for (TBasicSubjectMessage tBasicSubjectMessage : SubMessageList) {
                            // 获取更改后的科目编码
                            String subCode = tBasicSubjectMessage.getSubCode();
                            // 获取同级科目编码
                            String siblingsCoding = tBasicSubjectMessage.getSiblingsCoding();
                            if (StringUtils.isNotBlank(subCode) && subCode.length() > int31) {
                                //  1为原字符串，2为要插入的字符串，3为插入位置
                                int s = (int) int31;
                                StringBuffer newSubMessageCode = newSubMessageCode(subCode, zero, s);

                                tBasicSubjectMessage.setSubCode(newSubMessageCode.toString());

                                subMessageList2.add(tBasicSubjectMessage);
                            }
                            // siblingsCoding.length() > int3  同级科目编码大于4
                            // StringUtils.isNotBlank(siblingsCoding) 确认是否有同级编码
                            // 处理同级科目编码 改为 4-3-3
                            else if (StringUtils.isNotBlank(siblingsCoding) && siblingsCoding.length() > int31) {
                                //  1为原字符串，2为要插入的字符串，3为插入位置
                                int s = int31;
                                StringBuffer newCode = newSubMessageCode(siblingsCoding, zero, s);
                                tBasicSubjectMessage.setSiblingsCoding(newCode.toString());
                                subMessageList2.add(tBasicSubjectMessage);
                            } else {
                                subMessageList2.add(tBasicSubjectMessage);
                            }
                        }
                        SubMessageList = new ArrayList<TBasicSubjectMessage>();
                        SubMessageList.addAll(subMessageList2);
                    }
                }
            }
            // 只 处理 以 4-3-？的三级科目
            //先 判断 "4".equals(int0) 一级科目编码为4为数，
            //判断 "7".equals(int1) 是否 4-3-？格式的科目编码
            // tbList.size() > 2 至少有三级科目的
            // (int)tbList.get(3) < 10    三级科目位数小于10的
            else if ("4".equals(stringGet0) && "7".equals(stringGet1) && subMessageListLength.size() > 2 && (int) subMessageListLength.get(2) < 10) {
                for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
                    // 获取Excel导入的科目编码
                    String excelImportCode = tBasicSubjectMessage.getExcelImportCode();
                    // 获取Excel导入的同级科目编码
                    String excelImportSiblingsCoding = tBasicSubjectMessage.getExcelImportSiblingsCoding();

                    if (StringUtils.isNotBlank(excelImportCode) && excelImportCode.length() > intGet1) {
                        //  1为原字符串，2为要插入的字符串，3为插入位置
                        int s = (int) intGet1;
                        StringBuffer newCode = newSubMessageCode(excelImportCode, zero, s);

                        tBasicSubjectMessage.setSubCode(newCode.toString());
                        SubMessageList.add(tBasicSubjectMessage);
                    }
                    // siblingsCoding.length() > int3  同级科目编码大于4
                    // StringUtils.isNotBlank(siblingsCoding) 确认是否有同级编码
                    // 处理同级科目编码 改为 4-3-3
                    else if (StringUtils.isNotBlank(excelImportSiblingsCoding) && excelImportSiblingsCoding.length() > intGet1) {
                        //  1为原字符串，2为要插入的字符串，3为插入位置
                        int s = intGet1;
                        StringBuffer newCode = newSubMessageCode(excelImportSiblingsCoding, zero, s);
                        tBasicSubjectMessage.setSiblingsCoding(newCode.toString());
                        SubMessageList.add(tBasicSubjectMessage);
                    } else {
                        SubMessageList.add(tBasicSubjectMessage);
                    }
                }
            } else if ("4".equals(stringGet0) && subMessageListLength.size() == 1) {
                SubMessageList.addAll(tBasicSubjectMessageList);
            } else if ("4".equals(stringGet0) && "7".equals(stringGet1) && subMessageListLength.size() == 2) {
                SubMessageList.addAll(tBasicSubjectMessageList);
            } else if ("4".equals(stringGet0) && "7".equals(stringGet1) && "10".equals(stringGet2) && subMessageListLength.size() == 3) {
                SubMessageList.addAll(tBasicSubjectMessageList);
            } else if ("4".equals(stringGet0) && "7".equals(stringGet1) && "10".equals(stringGet2) && "13".equals(stringGet3) && subMessageListLength.size() == 4) {
                SubMessageList.addAll(tBasicSubjectMessageList);
            }

        }
        if (SubMessageList != null && SubMessageList.size() > 0) {
            logger.info("成功匹配的科目代码数：", SubMessageList.size());
        } else {
            logger.error("匹配错误： ", "新科目代码为零");
        }

        return SubMessageList;
    }

    /**
     * @param OriginalValue 原字符串
     * @param interpolation 要插入的字符串
     * @param site          插入位置
     * @return StringBuffer    返回类型
     * @Title: newSubExcelCode
     * @Description: 拼接科目方法编码
     * @date 2018年1月8日  下午3:50:24
     * @author SiLiuDong 司氏旭东
     */
    public StringBuffer newSubMessageCode(String OriginalValue, String interpolation, int site) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(OriginalValue.substring(0, site));
        stringBuffer.append(interpolation);
        stringBuffer.append(OriginalValue.substring(site, OriginalValue.length()));
        return stringBuffer;
    }
}

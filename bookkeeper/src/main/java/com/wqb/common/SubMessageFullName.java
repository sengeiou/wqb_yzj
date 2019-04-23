package com.wqb.common;

import com.wqb.model.TBasicSubjectMessage;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 司氏旭东
 * @ClassName: SubMessageFullName
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年1月9日 下午3:29:23
 */
public class SubMessageFullName {
    @Test
    public void TestnewSubMessageFullName() {
        List<TBasicSubjectMessage> tBasicSubjectMessageList = new ArrayList<TBasicSubjectMessage>();

        TBasicSubjectMessage tBasicSubjectMessage = new TBasicSubjectMessage();
        tBasicSubjectMessage.setSubName("库存现金");
        tBasicSubjectMessage.setSubCode("1001");
        tBasicSubjectMessage.setCodeLevel(1);
        tBasicSubjectMessage.setSuperiorCoding("");
        tBasicSubjectMessageList.add(tBasicSubjectMessage);
//
//		TBasicSubjectMessage tBasicSubjectMessage1 = new TBasicSubjectMessage();
//		tBasicSubjectMessage1.setSubName("现金1");
//		tBasicSubjectMessage1.setSubCode("1001001");
//		tBasicSubjectMessage1.setCodeLevel(2);
//		tBasicSubjectMessage1.setSiblingsCoding("1001");
//		tBasicSubjectMessageList.add(tBasicSubjectMessage1);
//
        TBasicSubjectMessage tBasicSubjectMessage2 = new TBasicSubjectMessage();
        tBasicSubjectMessage2.setSubName("现金2");
        tBasicSubjectMessage2.setSubCode("1001002");
        tBasicSubjectMessage2.setCodeLevel(2);
        tBasicSubjectMessage2.setSuperiorCoding("1001");
        tBasicSubjectMessageList.add(tBasicSubjectMessage2);
//
        TBasicSubjectMessage tBasicSubjectMessage3 = new TBasicSubjectMessage();
        tBasicSubjectMessage3.setSubName("现金22");
        tBasicSubjectMessage3.setSubCode("1001002001");
        tBasicSubjectMessage3.setCodeLevel(3);
        tBasicSubjectMessage3.setSuperiorCoding("1001002");
        tBasicSubjectMessageList.add(tBasicSubjectMessage3);
//
//		TBasicSubjectMessage tBasicSubjectMessage4 = new TBasicSubjectMessage();
//		tBasicSubjectMessage4.setSubName("");
//		tBasicSubjectMessage4.setSubCode("");
//		tBasicSubjectMessage4.setCodeLevel(2);
//		tBasicSubjectMessage4.setSiblingsCoding("1001");
//		tBasicSubjectMessageList.add(tBasicSubjectMessage4);


        List<TBasicSubjectMessage> newSubMessageFullName = newSubMessageFullName(tBasicSubjectMessageList);
        //newSubMessageFullName.forEach((tBasicSubjectMessageLists) -> System.out.println(tBasicSubjectMessageLists.getSubCode()+" -- "+tBasicSubjectMessageLists.getFullName()));
    }

    public List<TBasicSubjectMessage> newSubMessageFullName(List<TBasicSubjectMessage> tBasicSubjectMessageList) {
        List<TBasicSubjectMessage> list = new ArrayList<TBasicSubjectMessage>();
        List<TBasicSubjectMessage> list2 = new ArrayList<TBasicSubjectMessage>();
        for (TBasicSubjectMessage tBasicSubjectMessage : tBasicSubjectMessageList) {
            // 取出科目名称
            String subName = tBasicSubjectMessage.getSubName();
            // 获取科目级别
            Integer codeLevel = tBasicSubjectMessage.getCodeLevel();
            // 获取科目代码
            String subCode = tBasicSubjectMessage.getSubCode();
            // 上级科目代码
            String siblingsCoding = tBasicSubjectMessage.getSuperiorCoding();
            // 一级科目
            if (codeLevel != null && codeLevel == 1 && StringUtils.isNotBlank(subName)) {
                // 设置全名
                tBasicSubjectMessage.setFullName(subName);
            }
            // 二级科目
            if (codeLevel != null && codeLevel == 2 && StringUtils.isNotBlank(subName)) {
                for (TBasicSubjectMessage tBasicSubjectMessage2 : tBasicSubjectMessageList) {
                    // 上级科目名称
                    String subName2 = tBasicSubjectMessage2.getSubName();

                    // 科目代码不为空时 父级科目
                    if (StringUtils.isNotBlank(tBasicSubjectMessage2.getSubCode()) && tBasicSubjectMessage2.getSubCode().equals(siblingsCoding)) {
                        // 设置全名
                        tBasicSubjectMessage.setFullName(subName2 + "_" + subName);
                    }
                }
            }
            list.add(tBasicSubjectMessage);
        }

        // 三级科目以上的
        for (TBasicSubjectMessage tBasicSubjectMessage2 : list) {
            // 获取科目级别
            Integer codeLevel = tBasicSubjectMessage2.getCodeLevel();
            // 获取三级科目名称
            String subName = tBasicSubjectMessage2.getSubName();
            // 获取上级科目编码
            String superiorCoding = tBasicSubjectMessage2.getSuperiorCoding();

            if (codeLevel != null && codeLevel == 3 && StringUtils.isNotBlank(subName)) {
                for (TBasicSubjectMessage tBasicSubjectMessage3 : list) {
                    String subCode = tBasicSubjectMessage3.getSubCode();

                    if (StringUtils.isNotBlank(superiorCoding) && superiorCoding.equals(subCode)) {
                        String fullName = tBasicSubjectMessage3.getFullName();
                        tBasicSubjectMessage2.setFullName(fullName + "_" + subName);
                    }
                }
            }
            list2.add(tBasicSubjectMessage2);
        }
        return list2;
    }

}

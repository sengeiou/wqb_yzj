package com.wqb.common;

import com.wqb.model.TBasicSubjectMessage;

import java.util.ArrayList;
import java.util.List;

public class SubjectUtils {
    public static List<TBasicSubjectMessage> getMjSub(List<TBasicSubjectMessage> list) {
        List<TBasicSubjectMessage> resultList = new ArrayList<TBasicSubjectMessage>();
        if (null != list && !list.isEmpty()) {
            for (TBasicSubjectMessage tsm1 : list) {
                if (null == tsm1) {
                    continue;
                }
                int count = 0;
                String subCode1 = tsm1.getSubCode();
                if (subCode1 == null || "".equals(subCode1)) {
                    continue;
                }
                TBasicSubjectMessage tsm2 = null;
                for (int i = 0; i < list.size(); i++) {
                    tsm2 = list.get(i);
                    if (null == tsm2) {
                        continue;
                    }
                    String subCode2 = tsm2.getSubCode();
                    if (subCode2 == null || "".equals(subCode2)) {
                        continue;
                    }
                    if (subCode2.startsWith(subCode1)) {
                        count++;
                    }
                }
                if (count == 1) {
                    resultList.add(tsm1);
                } else {
                    continue;
                }
            }
        }
        return resultList;
    }
}

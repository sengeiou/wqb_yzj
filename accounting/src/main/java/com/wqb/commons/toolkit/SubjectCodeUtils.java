package com.wqb.commons.toolkit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Shoven
 * @since 2019-04-01 20:01
 */
public class SubjectCodeUtils {

    public static final int TOP_CODE_LENGTH = 4;

    public static final int CHILD_CODE_LENGTH = 3;

    public static final String INIT_CHILD_CODE = "001";

    /**
     * 根据科目代码获取基本
     *
     * @param subjectCode
     * @return
     */
    public static String getTopCodeBySubjectCode(String subjectCode) {
        checkArgument(!invalidSubjectCode(subjectCode), "无效的subjectCode: %s", subjectCode);
        return StringUtils.substring(subjectCode, 0, TOP_CODE_LENGTH);
    }

    /**
     * 根据科目代码获取基本
     *
     * @param subjectCode
     * @return
     */
    public static Integer getLevelBySubjectCode(String subjectCode) {
        checkArgument(!invalidSubjectCode(subjectCode), "无效的subjectCode: %s", subjectCode);
        return (subjectCode.length() - TOP_CODE_LENGTH) / CHILD_CODE_LENGTH + 1;
    }

    /**
     * 根据子科目代码获取父科目代码
     *
     * @param subjectCode
     * @return
     */
    public static String getParentSubjectCodeBySubjectCode(String subjectCode) {
        String[] strings = splitSubjectCode(subjectCode);

        if (strings.length == 1) {
            return "0";
        }

        int index = ArrayUtils.indexOf(strings, subjectCode);
        return strings[index + 1];
    }

    /**
     * 根据父科目代码获取子科目代码
     *
     * @param parentCode
     * @param sequence
     * @return
     */
    public static String getSubjectCodeByParentSubjectCode(String parentCode, String sequence) {
        checkArgument(!invalidSubjectCode(parentCode), "parentCode: %s", parentCode);
        if (StringUtils.isBlank(sequence) || !NumberUtils.isDigits(sequence)
                || StringUtils.length(sequence) < CHILD_CODE_LENGTH) {
            return getSubjectCodeByParentSubjectCode(parentCode);
        }

        int len = sequence.length();
        if (len >= CHILD_CODE_LENGTH) {
            return parentCode + StringUtils.substring(sequence, - CHILD_CODE_LENGTH, len);
        }

        return parentCode + StringUtils.repeat('0', CHILD_CODE_LENGTH - len) + sequence;
    }

    public static String getSubjectCodeByParentSubjectCode(String parentCode) {
        return parentCode + INIT_CHILD_CODE;
    }

    /**
     * 分解科目代码 开头的元素为当前科目代码，末尾的元素为顶级科目代码
     *
     * @param subjectCode
     * @return
     */
    public static String[] splitSubjectCode(String subjectCode) {
        checkArgument(!invalidSubjectCode(subjectCode), "无效的subjectCode: %s", subjectCode);

        int len = subjectCode.length();
        if (len < TOP_CODE_LENGTH) {
            return new String[]{subjectCode};
        }

        int num = (int)Math.ceil((double)(len - TOP_CODE_LENGTH) / CHILD_CODE_LENGTH) + 1;

        String[] strings = new String[num];

        int i = TOP_CODE_LENGTH;
        int pos = 0;
        do {
            i = Math.min(i + CHILD_CODE_LENGTH * pos, len);
            strings[pos] = subjectCode.substring(0, i);
        } while (i < len && ++ pos < num);

        ArrayUtils.reverse(strings);
        return strings;
    }

    public static boolean invalidSubjectCode(String str) {
        return StringUtils.isBlank(str) || !NumberUtils.isDigits(str) || StringUtils.length(str) < TOP_CODE_LENGTH;
    }

    public static void main(String[] args) {
        splitSubjectCode("222100101");
    }
}

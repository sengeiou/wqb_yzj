package com.wqb.commons.emun;

/**
 * @author Shoven
 * @since 2019-03-21 9:04
 */
public enum SubjectItem {
    /**
     * 库存现金 1001
     */
    KCXJ("库存现金", "1001"),

    /**
     * 银行存款 1002
     */
    YHCK("银行存款", "1002"),

    /**
     * 原材料
     */
    YCL("原材料", "1403"),

    /**
     * 库存商品 1405
     */
    KCSP("库存商品", "1405"),

    /**
     * 应收账款 1122
     */
    YSZK("应收账款", "1122"),

    /**
     * 预收账款 1123
     */
    YUSZK("预收账款", "1123"),

    /**
     * 其他应收款 1221
     */
    QTYSK("其他应收款", "1221"),

    /**
     * 应付账款 2202
     */
    YFZK("应付账款", "2202"),

    /**
     * 预收账款 2202
     */
    YUFZK("预收账款", "2203"),

    /**
     * 应付职工薪酬 2211
     */
    YFZGXC("应付职工薪酬", "2211"),

    /**
     * 应交税费 2221
     */
    YJSF("应交税费", "2221"),

    /**
     * 应交税费_未交增值税 2221002
     */
    YJSF_WJZZS("未交增值税", "应交税费_未交增值税", "2221002"),

    /**
     * 其他应付款 2241
     */
    QTYFK("其他应付款", "2241"),

    /**
     * 主营业务收入 6001
     */
    ZYYWSR("主营业务收入", "6001"),

     /**
     * 管理费用 6602
     */
    GLFY("管理费用", "6602"),

    /**
     * 管理费用_办公用品 6602001
     */
    GLFY_BGYP("办公用品", "管理费用_办公用品", "6602001"),

    /**
     * 管理费用_房租 6602002
     */
    GLFY_FZ("房租", "管理费用_房租", "6602002"),

    /**
     * 管理费用_物业管理费 6602003
     */
    GLFY_WYGLF("物业管理费", "管理费用_物业管理费", "6602003"),

    /**
     * 管理费用_水电费 6602004
     */
    GLFY_SDF("水电费", "管理费用_水电费", "6602004"),

    /**
     * 管理费用_交际应酬费 6602005
     */
    GLFY_JJYCF("交际应酬费", "管理费用_交际应酬费", "6602005"),

    /**
     * 管理费用_市内交通费 6602006
     */
    GLFY_SNJTF("市内交通费", "管理费用_市内交通费", "6602006"),

    /**
     * 管理费用_差旅费 6602008
     */
    GLFY_CLF("差旅费", "管理费用_差旅费", "6602008"),

    /**
     * 管理费用_工资 6602012
     */
    GLFY_GZ("工资", "管理费用_工资", "6602012"),

    /**
     * 管理费用_福利费 6602016
     */
    GLFY_FLF("福利费", "管理费用_福利费", "6602016"),

    /**
     * 管理费用_咨询费 6602017
     */
    GLFY_ZXF("咨询费", "管理费用_咨询费", "6602017"),

    /**
     * 管理费用_运输费 6602018
     */
    GLFY_YSF("运输费", "管理费用_运输费", "6602018"),

    /**
     * 管理费用_通讯费 6602021
     */
    GLFY_TXF("通讯费", "管理费用_通讯费", "6602021"),


    NONE("", "");

    private String name;

    private String fullName;

    private String code;

    SubjectItem(String name, String code) {
        this(name, name, code);
    }

    SubjectItem(String name, String fullName, String code) {
        this.name = name;
        this.fullName = fullName;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }

    public static SubjectItem codeOf(String code) {
        for (SubjectItem subjectItem : values()) {
            if (subjectItem.getCode().equals(code)) {
                return subjectItem;
            }
        }
        return NONE;
    }
}

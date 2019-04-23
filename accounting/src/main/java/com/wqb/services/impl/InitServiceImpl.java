package com.wqb.services.impl;

import com.wqb.commons.emun.SubjectItem;
import com.wqb.commons.toolkit.MyCollectors;
import com.wqb.commons.toolkit.SubjectCodeUtils;
import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.subject.Subject;
import com.wqb.domains.subject.SubjectBalance;
import com.wqb.domains.subject.SubjectParent;
import com.wqb.services.InitService;
import com.wqb.services.subject.SubjectBalanceService;
import com.wqb.services.subject.SubjectParentService;
import com.wqb.services.subject.SubjectService;
import com.wqb.supports.exceptions.WqbException;
import com.wqb.supports.util.BigDecimals;
import com.wqb.supports.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author Shoven
 * @since 2019-04-09 10:20
 */
@Service
public class InitServiceImpl implements InitService {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectBalanceService subjectBalanceService;

    @Autowired
    private SubjectParentService subjectParentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initSubjectsFromExcel(String fileName, int startIndex, int endIndex) {
        List<List<String>> lists;
        try {
            lists = ExcelUtil.readExcel(fileName, startIndex, endIndex);
        } catch (IOException e) {
            throw new WqbException("读取 " + fileName + " 失败，" + e.getMessage());
        }

        List<Subject> subjects = new ArrayList<>();

        class Helper {
            private Map<String, Subject> cache = new HashMap<>();
            private Map<String, SubjectParent> groupedByCodeSubjectParent;

            private String getParentCode(List<String> row) {
                Subject parentSubject = getThisParent(row);
                if (parentSubject == null) {
                    return null;
                }
                return parentSubject.getCode();
            }

            private String getFullName(List<String> row) {
                Subject parentSubject = getThisParent(row);
                String selfName = getName(row);
                if (parentSubject == null) {
                    return selfName;
                }
                String parentSubjectFullName = parentSubject.getFullName();
                return StringUtils.isNotBlank(parentSubjectFullName)
                        ? parentSubjectFullName + "_" + selfName
                        : parentSubject.getName() + "_" + selfName;
            }

            private Subject getThisParent(List<String> row) {
                String code = getCode(row);
                String parentCode = SubjectCodeUtils.getParentSubjectCodeBySubjectCode(code);
                if (cache.containsKey(parentCode)) {
                    return cache.get(parentCode);
                }
                return null;
            }

            private String getCode(List<String> row) {
                checkNotNull(row,"row 不能为空");
                String str = row.get(1);
                checkArgument(StringUtils.isNotBlank(str), "code 不能为空");
                return new BigDecimal(str).longValue() + "";
            }

            private String getName(List<String> row) {
                checkNotNull(row,"row 不能为空");
                return StringUtils.trimToNull(row.get(2));
            }

            private String getExtendKey(List<String> row) {
                switch (SubjectItem.codeOf(getCode(row))) {
                    case YHCK:
                        return Subject.EXTEND_BANK;
                    case YSZK:
                    case YFZK:
                    case QTYSK:
                    case QTYFK:
                        return Subject.EXTEND_CUSTOMER;
                    case KCSP:
                        return Subject.EXTEND_GOODS;
                    default:
                }
                return null;
            }

            private Integer getDirection(List<String> row) {
                String code = getCode(row);
                SubjectParent subjectParent = groupedByCodeSubjectParent.get(code);
                // 非会计标准一级科目，是二级科目
                // 二级科目的缓存cache中有他的父subject，因为按code排序从小到大先后添加
                if (subjectParent == null) {
                    Subject thisParent = getThisParent(row);
                    checkNotNull(thisParent, "缓存中无 %s 的父科目", code);
                    return thisParent.getDirection();
                }
                return subjectParent.getDir();
            }

            private Integer getCategory(List<String> row) {
                String code = getCode(row);
                SubjectParent subjectParent = groupedByCodeSubjectParent.get(code);
                // 非会计标准一级科目，是二级科目
                // 二级科目的缓存cache中有他的父subject，因为按code排序从小到大先后添加
                if (subjectParent == null) {
                    Subject thisParent = getThisParent(row);
                    checkNotNull(thisParent, "缓存中无 %s 的父科目", code);
                    return thisParent.getCategory();
                }
                return Integer.valueOf(subjectParent.getCategory());
            }
        }

        List<SubjectParent> subjectParentList = subjectParentService.list();
        checkNotNull(subjectParentList, "subjectParent 为空");

        Helper helper = new Helper();
        helper.groupedByCodeSubjectParent = subjectParentList.stream()
                .collect(MyCollectors.singleResultGroupingBy(SubjectParent::getSubCode));


        lists.stream()
                // 过滤code为空
                .filter(row -> StringUtils.isNotBlank(helper.getCode(row)))
                // 按code从小到大排序
                .sorted(Comparator.comparing(helper::getCode))
                // 根据code的第一位分组
                .collect(groupingBy(row -> (int) helper.getCode(row).charAt(0)))
                .forEach((key, rows) -> {
                    // 设置子科目
                    rows.forEach(row -> {
                        String code = helper.getCode(row);
                        String extendKey = helper.getExtendKey(row);

                        Subject subject = new Subject();
                        subject.setCode(code);
                        subject.setName(helper.getName(row));
                        subject.setFullName(helper.getFullName(row));
                        subject.setParentCode(helper.getParentCode(row));
                        subject.setDirection(helper.getDirection(row));
                        subject.setCategory(helper.getCategory(row));
                        subject.setExtendKey(extendKey);
                        subject.setExtend(extendKey == null ? 0 : 1);

                        subjects.add(subject);
                        helper.cache.put(code, subject);
                    });
                });

        if (!subjectService.saveBatch(subjects)) {
            throw new WqbException("初始化科目表失败");
        }
    }

    @Override
    public void initSubjectBalanceFromSubjects(User user, Account account) {
        List<Subject> subjects = subjectService.selectAllFormCache();

        String userId = user.getUserID();
        String accountId = account.getAccountID();
        String period = account.getUseLastPeriod();

        ArrayList<SubjectBalance> list = new ArrayList<>();
        for (Subject subject : subjects) {
            SubjectBalance subjectBalance = new SubjectBalance();
            subjectBalance.setSubCode(subject.getCode());
            subjectBalance.setSuperiorCoding(subject.getParentCode());
            subjectBalance.setUserId(userId);
            subjectBalance.setAccountId(accountId);
            subjectBalance.setAccountPeriod(period);
            subjectBalance.setSubName(subject.getName());
            subjectBalance.setFullName(subject.getFullName());
            subjectBalance.setCategory(subject.getCategory() + "");
            subjectBalance.setSubSource("自动初始化");
            subjectBalance.setCodeLevel(SubjectCodeUtils.getLevelBySubjectCode(subject.getCode()));
            subjectBalance.setSiblingsCoding("1");
            subjectBalance.setExchangeRateState(1);
            subjectBalance.setDebitCreditDirection(subject.getDirection() + "");
            Date now = new Date();
            subjectBalance.setCreateTime(now);
            subjectBalance.setUpdateDate(now);
            subjectBalance.setUpdateTimestamp(System.currentTimeMillis() + "");
            list.add(subjectBalance);
        }

        if (!subjectBalanceService.saveBatch(list)) {
            throw new WqbException("初始化科目余额表失败");
        }
    }

}

package com.wqb.services;

import com.wqb.domains.Account;
import com.wqb.domains.User;

/**
 * @author Shoven
 * @since 2019-04-09 10:20
 */
public interface InitService {


    /**
     * 初始化科目
     *
     * @param fileName
     * @param startIndex
     * @param endIndex
     */
    void initSubjectsFromExcel(String fileName, int startIndex, int endIndex);

    /**
     * 初始化科目余额表
     *
     * @param user
     * @param account
     */
    void initSubjectBalanceFromSubjects(User user, Account account);
}

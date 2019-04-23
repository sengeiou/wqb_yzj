package com.wqb.dao.customer;

import com.wqb.common.BusinessException;
import com.wqb.model.Customer;

import java.util.List;

public interface CustomerDao {
    Customer queryCusByAcc(String accountID) throws BusinessException;


    List<Customer> queryCusBath(List<String> list) throws BusinessException;
}

package com.wqb.dao.userOrder;

import com.wqb.common.BusinessException;
import com.wqb.model.UserOrder;

public interface UserOrderDao {

    int insertUserOrder(UserOrder user) throws BusinessException;

}

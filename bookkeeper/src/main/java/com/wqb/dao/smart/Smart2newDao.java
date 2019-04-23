package com.wqb.dao.smart;

import com.wqb.common.BusinessException;
import com.wqb.model.Smart2new;

import java.util.List;

public interface Smart2newDao {

    public List<Smart2new> queryAllSmart2New() throws BusinessException;
}

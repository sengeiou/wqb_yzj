package com.wqb.dao.attach;

import com.wqb.common.BusinessException;
import com.wqb.model.Attach;

import java.util.List;

public interface AttachDao {
    List<Attach> queryByID(String id) throws BusinessException;

    void delByID(String id) throws BusinessException;

    void addAttach(Attach attach) throws BusinessException;

}

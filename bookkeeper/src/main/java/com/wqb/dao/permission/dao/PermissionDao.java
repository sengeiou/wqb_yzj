package com.wqb.dao.permission.dao;

import com.wqb.common.BusinessException;
import com.wqb.model.Permission;

import java.util.List;

public interface PermissionDao {
    List<Permission> queryPreByUserID(String userID) throws BusinessException;
}

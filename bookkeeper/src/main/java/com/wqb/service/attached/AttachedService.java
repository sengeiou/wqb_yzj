package com.wqb.service.attached;

import com.wqb.common.BusinessException;

import java.util.Map;

public interface AttachedService {
    Map<String, Object> fjsCarryover(Map<String, Object> map) throws BusinessException;

}

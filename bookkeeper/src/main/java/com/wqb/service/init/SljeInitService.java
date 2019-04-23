package com.wqb.service.init;

import com.wqb.common.BusinessException;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface SljeInitService {
    void slje2Date(List<Map<String, Object>> list, HttpSession session) throws BusinessException;
}

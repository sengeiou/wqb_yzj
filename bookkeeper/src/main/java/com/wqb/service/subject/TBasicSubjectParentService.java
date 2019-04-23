package com.wqb.service.subject;

import com.wqb.common.BusinessException;
import com.wqb.model.TBasicSubjectParent;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface TBasicSubjectParentService {

    Map<String, Object> insertSubParent(List<Map<String, Object>> list, Map<String, String> map2);

    Map<String, Object> querySubParent(HttpSession session) throws BusinessException;

    List<TBasicSubjectParent> querySubParentList(HttpSession session);
}

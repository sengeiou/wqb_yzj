package com.wqb.dao.subject;

import com.wqb.model.TBasicSubjectParent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface TBasicSubjectParentMapper {

    void insertSubParent(TBasicSubjectParent subParent);

    List<TBasicSubjectParent> querySubParent(Map<String, Object> param);

    //查询科目方向
    String querySubCode(String str);
}

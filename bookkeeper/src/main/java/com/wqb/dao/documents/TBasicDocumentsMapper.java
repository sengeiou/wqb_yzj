package com.wqb.dao.documents;

import com.wqb.model.TBasicDocuments;

import java.util.List;

public interface TBasicDocumentsMapper {
    int deleteByPrimaryKey(String pkDocumentsId);

    int insert(TBasicDocuments record);

    int insertSelective(TBasicDocuments record);

    TBasicDocuments selectByPrimaryKey(String pkDocumentsId);

    int updateByPrimaryKeySelective(TBasicDocuments record);

    int updateByPrimaryKey(TBasicDocuments record);

    int addTicketsCost(List<TBasicDocuments> tBasicDocumentslist);

    List<TBasicDocuments> queryDocumentsList(TBasicDocuments tBasicDocuments);

    List<TBasicDocuments> querySalesDocumentsList(TBasicDocuments tBasicDocuments);

    int deleteDocumentsList(List<TBasicDocuments> tBasicDocumentsList);
}

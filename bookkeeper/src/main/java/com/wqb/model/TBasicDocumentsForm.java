package com.wqb.model;

import java.io.Serializable;
import java.util.List;

public class TBasicDocumentsForm implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6619677395706580382L;
    private List<TBasicDocuments> tBasicDocumentss;

    public List<TBasicDocuments> gettBasicDocumentss() {
        return tBasicDocumentss;
    }

    public void settBasicDocumentss(List<TBasicDocuments> tBasicDocumentss) {
        this.tBasicDocumentss = tBasicDocumentss;
    }


}

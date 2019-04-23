package com.wqb.model;

import java.io.Serializable;
import java.util.List;

public class TBasicSubjectExcelForm implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6469719017254768542L;
    List<TBasicSubjectExcel> tBasicSubjectExcelList;

    public List<TBasicSubjectExcel> gettBasicSubjectExcelList() {
        return tBasicSubjectExcelList;
    }

    public void settBasicSubjectExcelList(List<TBasicSubjectExcel> tBasicSubjectExcelList) {
        this.tBasicSubjectExcelList = tBasicSubjectExcelList;
    }

}

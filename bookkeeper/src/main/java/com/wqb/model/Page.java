package com.wqb.model;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4634713262141419859L;

    //currentPage 当前页
    private int currentPage = 1;

    // pageSize 每页显示的行数
    private int pageSize = 10;

    //pageTotal 总页数
    private int pageTotal;

    //recordTotal 总条数
    private int recordTotal = 0;

    //content 每页的内容
    private List<T> content;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Page [currentPage=" + currentPage + ", pageSize=" + pageSize + ", pageTotal=" + pageTotal
                + ", recordTotal=" + recordTotal + ", content=" + content + "]";
    }


}

package com.wqb.model;

import java.io.Serializable;

public class Attach implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5245129865730573285L;
    // 附件主键
    private String id;
    // 附件文件名
    private String attachName;
    // 附件后缀名
    private String attachSuffix;
    // 附件访问路径
    private String attachUrl;
    // 来源[0:凭证附件]
    private Integer source;
    // 上传时间
    private Long importDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachSuffix() {
        return attachSuffix;
    }

    public void setAttachSuffix(String attachSuffix) {
        this.attachSuffix = attachSuffix;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getImportDate() {
        return importDate;
    }

    public void setImportDate(Long importDate) {
        this.importDate = importDate;
    }

}

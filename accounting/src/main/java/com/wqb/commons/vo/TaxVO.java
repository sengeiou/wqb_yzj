package com.wqb.commons.vo;

import com.wqb.commons.emun.TaxAction;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class TaxVO implements Serializable {
    private TaxAction action;   //操作名称
    private byte[] imageStream; //图片信息
    private String ticketId;    //票据ID
    private String orgId;   //企业ID
    private MultipartFile ticketImage; //图片文件

    public TaxAction getAction() {
        return action;
    }

    public void setAction(TaxAction action) {
        this.action = action;
    }

    public byte[] getImageStream() {
        return imageStream;
    }

    public void setImageStream(byte[] imageStream) {
        this.imageStream = imageStream;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public MultipartFile getTicketImage() {
        return ticketImage;
    }

    public void setTicketImage(MultipartFile ticketImage) {
        this.ticketImage = ticketImage;
    }
}

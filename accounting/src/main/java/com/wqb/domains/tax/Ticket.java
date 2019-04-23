package com.wqb.domains.tax;

import com.wqb.commons.emun.ResultStatus;
import com.wqb.commons.emun.TicketType;
import com.wqb.domains.invoice.InvoiceBody;
import com.wqb.domains.invoice.InvoiceHeader;

import java.util.List;

public class Ticket {
    private String ticketId;	//票据id
//    private String imageUrl;	//图片url地址链接
    private ResultStatus resultStatus;	//处理结果（processing处理中,fail处理失败,success处理成功）
    private TicketType ticketType;	//票据类型（invoice:增值税发票，train:火车票，plane:飞机票，manual: 其他票）
    private String failReason;	//处理失败票据的失败原因
//    private TickeDetails dataDetails;	//对应的票据票面具体信息（详细字段参考附录）
    private String createTime;	//创建时间 格式 YYYY-MM-DD HH：mm：ss
    private String code;	//错误code，处理失败的时此处有值，具体对应关系参考专有错误码附录
    private InvoiceHeader invoiceHeader;  //发票主表
    private List<InvoiceBody> invoiceBodies;        //发票子表

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", resultStatus=" + resultStatus +
                ", ticketType=" + ticketType +
                ", failReason='" + failReason + '\'' +
                ", createTime='" + createTime + '\'' +
                ", code='" + code + '\'' +
                ", invoiceHeader=" + invoiceHeader +
                ", invoiceBodies=" + invoiceBodies +
                '}';
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InvoiceHeader getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(InvoiceHeader invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public List<InvoiceBody> getInvoiceBodies() {
        return invoiceBodies;
    }

    public void setInvoiceBodies(List<InvoiceBody> invoiceBodies) {
        this.invoiceBodies = invoiceBodies;
    }
}

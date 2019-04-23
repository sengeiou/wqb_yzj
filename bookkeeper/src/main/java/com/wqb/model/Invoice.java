package com.wqb.model;

import java.io.Serializable;

public class Invoice implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2374508374548018378L;
    private InvoiceHead invoiceHead;
    private InvoiceBody invoiceBody;

    public InvoiceHead getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(InvoiceHead invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public InvoiceBody getInvoiceBody() {
        return invoiceBody;
    }

    public void setInvoiceBody(InvoiceBody invoiceBody) {
        this.invoiceBody = invoiceBody;
    }

}

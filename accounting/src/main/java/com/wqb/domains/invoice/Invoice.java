package com.wqb.domains.invoice;

import java.util.List;

/**
 * @author Shoven
 * @since 2019-03-19 14:43
 */
public class Invoice {

    private InvoiceHeader invoiceHeader;

    private List<InvoiceBody> invoiceBodies;

    public Invoice() {
    }

    public Invoice(InvoiceHeader invoiceHeader, List<InvoiceBody> invoiceBodies) {
        this.invoiceHeader = invoiceHeader;
        this.invoiceBodies = invoiceBodies;
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

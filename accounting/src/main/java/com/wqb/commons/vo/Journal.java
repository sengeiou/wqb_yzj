package com.wqb.commons.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Shoven
 * @since 2019-04-03 17:00
 */
public class Journal implements Serializable {

    private String name;

    private String amount;

    private String amountSymbol;

    private String remark;

    @JsonFormat(timezone="GMT+8", pattern="MM-dd")
    private Date date;

    private String summary;

    private List<InvoiceRecord> invoiceRecords;

    private List<SubjectRecord> subjectRecords;

    private List<GoodsRecord> goodsRecords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountSymbol() {
        return amountSymbol;
    }

    public void setAmountSymbol(String amountSymbol) {
        this.amountSymbol = amountSymbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<InvoiceRecord> getInvoiceRecords() {
        return invoiceRecords;
    }

    public void setInvoiceRecords(List<InvoiceRecord> invoiceRecords) {
        this.invoiceRecords = invoiceRecords;
    }

    public List<SubjectRecord> getSubjectRecords() {
        return subjectRecords;
    }

    public void setSubjectRecords(List<SubjectRecord> subjectRecords) {
        this.subjectRecords = subjectRecords;
    }

    public List<GoodsRecord> getGoodsRecords() {
        return goodsRecords;
    }

    public void setGoodsRecords(List<GoodsRecord> goodsRecords) {
        this.goodsRecords = goodsRecords;
    }

    public class SubjectRecord implements Serializable{

        private String originalName;

        private String subjectName;

        private Integer direction;

        private String amount;

        private Integer number;

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public Integer getDirection() {
            return direction;
        }

        public void setDirection(Integer direction) {
            this.direction = direction;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }

    public class InvoiceRecord implements Serializable{

        @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd")
        private Date date;

        private String code;

        private String number;

        private String name;

        private String total;

        private String amount;

        private String taxAmount;

        private String taxRate;

        private String type;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(String taxAmount) {
            this.taxAmount = taxAmount;
        }

        public String getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(String taxRate) {
            this.taxRate = taxRate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class GoodsRecord implements Serializable{

        private String name;

        private String spec;

        private String nameAndSpec;

        private String qcNumber;

        private String qcAmount;

        private String qcPrice;

        private String bqReceivedNumber;

        private String bqReceivedAmount;

        private String bqReceivedPrice;

        private String bqSendNumber;

        private String bqSendAmount;

        private String bqSendPrice;

        private String qmNumber;

        private String qmAmount;

        private String qmPrice;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getNameAndSpec() {
            return nameAndSpec;
        }

        public void setNameAndSpec(String nameAndSpec) {
            this.nameAndSpec = nameAndSpec;
        }

        public String getQcNumber() {
            return qcNumber;
        }

        public void setQcNumber(String qcNumber) {
            this.qcNumber = qcNumber;
        }

        public String getQcAmount() {
            return qcAmount;
        }

        public void setQcAmount(String qcAmount) {
            this.qcAmount = qcAmount;
        }

        public String getQcPrice() {
            return qcPrice;
        }

        public void setQcPrice(String qcPrice) {
            this.qcPrice = qcPrice;
        }

        public String getBqReceivedNumber() {
            return bqReceivedNumber;
        }

        public void setBqReceivedNumber(String bqReceivedNumber) {
            this.bqReceivedNumber = bqReceivedNumber;
        }

        public String getBqReceivedAmount() {
            return bqReceivedAmount;
        }

        public void setBqReceivedAmount(String bqReceivedAmount) {
            this.bqReceivedAmount = bqReceivedAmount;
        }

        public String getBqReceivedPrice() {
            return bqReceivedPrice;
        }

        public void setBqReceivedPrice(String bqReceivedPrice) {
            this.bqReceivedPrice = bqReceivedPrice;
        }

        public String getBqSendNumber() {
            return bqSendNumber;
        }

        public void setBqSendNumber(String bqSendNumber) {
            this.bqSendNumber = bqSendNumber;
        }

        public String getBqSendAmount() {
            return bqSendAmount;
        }

        public void setBqSendAmount(String bqSendAmount) {
            this.bqSendAmount = bqSendAmount;
        }

        public String getBqSendPrice() {
            return bqSendPrice;
        }

        public void setBqSendPrice(String bqSendPrice) {
            this.bqSendPrice = bqSendPrice;
        }

        public String getQmNumber() {
            return qmNumber;
        }

        public void setQmNumber(String qmNumber) {
            this.qmNumber = qmNumber;
        }

        public String getQmAmount() {
            return qmAmount;
        }

        public void setQmAmount(String qmAmount) {
            this.qmAmount = qmAmount;
        }

        public String getQmPrice() {
            return qmPrice;
        }

        public void setQmPrice(String qmPrice) {
            this.qmPrice = qmPrice;
        }
    }


}

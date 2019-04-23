package com.wqb.domains.tax;

public class Goods {
    private String goodsName;	//	货物或应税劳务，服务名称
    private String model;	//	规格型号
    private String units;	//	单位
    private String quantity;	//	数量
    private String unitPrice;	//	单价
    private String amountExcludeTax;	//	金额
    private Double taxRate;	//	税率,类似返回6 代表6%
    private String tax;	//	税额
    private Integer detailNumber;		//货物序列号

    @Override
    public String toString() {
        return "Goods{" +
                "goodsName='" + goodsName + '\'' +
                ", model='" + model + '\'' +
                ", units='" + units + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", amountExcludeTax='" + amountExcludeTax + '\'' +
                ", taxRate=" + taxRate +
                ", tax='" + tax + '\'' +
                ", detailNumber=" + detailNumber +
                '}';
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAmountExcludeTax() {
        return amountExcludeTax;
    }

    public void setAmountExcludeTax(String amountExcludeTax) {
        this.amountExcludeTax = amountExcludeTax;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Integer getDetailNumber() {
        return detailNumber;
    }

    public void setDetailNumber(Integer detailNumber) {
        this.detailNumber = detailNumber;
    }
}

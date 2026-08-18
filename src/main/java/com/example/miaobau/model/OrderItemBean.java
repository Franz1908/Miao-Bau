package com.example.miaobau.model;

import java.math.BigDecimal;

public class OrderItemBean {

    private int orderID;
    private int productID;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal vatFrozen;
    private String productName;

    public OrderItemBean() {}

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public void setProductID(int productID){
        this.productID = productID;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice){
        this.unitPrice = unitPrice;
    }

    public void setVatFrozen(BigDecimal vatFrozen){
        this.vatFrozen = vatFrozen;
    }

    public void setProductName(String productName) { this.productName = productName; }

    public int getOrderID(){
        return orderID;
    }

    public int getProductID(){
        return productID;
    }

    public int getQuantity(){
        return quantity;
    }

    public BigDecimal getUnitPrice(){
        return unitPrice;
    }

    public BigDecimal getVatFrozen(){
        return vatFrozen;
    }

    public String getProductName() { return productName; }

}

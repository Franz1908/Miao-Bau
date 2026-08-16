package com.example.miaobau.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderBean {

    private int orderID;
    private int customerID;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;

    public OrderBean(){}

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public void setProductID(int customerID){
        this.customerID = customerID;
    }

    public void setOrderDate(LocalDateTime orderDate){
        this.orderDate = orderDate;
    }

    public void setTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }

    public int getOrderID(){
        return orderID;
    }

    public int getProductID(){
        return customerID;
    }

    private LocalDateTime getOrderDate(){
        return orderDate;
    }

    private BigDecimal getTotalPrice(){
        return totalPrice;
    }

}

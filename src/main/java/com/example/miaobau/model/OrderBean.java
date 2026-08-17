package com.example.miaobau.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderBean {

    private int orderID;
    private int customerID;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private List <OrderItemBean> items = new ArrayList<>();

    public OrderBean(){}

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public void setCustomerID(int customerID){
        this.customerID = customerID;
    }

    public void setOrderDate(LocalDateTime orderDate){
        this.orderDate = orderDate;
    }

    public void setTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }

    public void setItems(List<OrderItemBean> items){
        this.items = items;
    }

    public int getOrderID(){
        return orderID;
    }

    public int getCustomerID(){
        return customerID;
    }

    public LocalDateTime getOrderDate(){
        return orderDate;
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public List<OrderItemBean> getItems(){
        return items;
    }

}

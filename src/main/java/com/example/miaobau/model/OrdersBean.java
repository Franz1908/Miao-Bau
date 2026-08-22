package com.example.miaobau.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdersBean {

    private int orderID;
    private int customerID;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPhone;
    private List <OrderItemBean> items = new ArrayList<>();

    public OrdersBean(){}

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

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

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

    public String getCustomerEmail() { return customerEmail; }

    public String getCustomerLastName() { return customerLastName; }

    public String getCustomerFirstName() { return customerFirstName; }

    public String getCustomerPhone() { return customerPhone; }

    public void addItem(OrderItemBean item){
        items.add(item);
    }

}

package com.example.miaobau.model;

public class CartItem {

    private ProductBean product;
    private int quantity;

    public CartItem(){}

    public CartItem(ProductBean product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public ProductBean getProduct(){
        return product;
    }

    public int getQuantity(){
        return quantity;
    }
}

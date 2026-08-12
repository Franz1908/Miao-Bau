package com.example.miaobau.model;

import java.util.HashMap;
import java.util.Map;

public class CartBean {
    private Map<Integer, CartItem> cart = new HashMap<>();

    public CartBean() {}

    public Map<Integer, CartItem> getCart(){
        return cart;
    }

    public void addToCart(ProductBean product) {
        int id = product.getProductID();

        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            cart.put(id, new CartItem(product, 1));
        }
    }

    public void addToCart(ProductBean product, int quantity) {
        int id = product.getProductID();

        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cart.put(id, new CartItem(product, quantity));
        }
    }

    public void decreaseQuantity(ProductBean product){
        int id = product.getProductID();

        if(cart.containsKey(id)){
            CartItem item = cart.get(id);
            if (item.getQuantity() == 1) {
                removeFromCart(product);
            }
            else {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
    }

    public void removeFromCart(ProductBean product){
        cart.remove(product.getProductID());
    }

    public void clearCart(){
        cart.clear();
    }

}

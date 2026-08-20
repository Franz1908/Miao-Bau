package com.example.miaobau.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CartBean {

    private Map<Integer, CartItem> cart = new HashMap<>();

    public CartBean() {}

    public Map<Integer, CartItem> getCart(){
        return cart;
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

    public void decreaseQuantity(int productId){
        if(cart.containsKey(productId)){
            CartItem item = cart.get(productId);
            if (item.getQuantity() == 1) {
                removeFromCart(productId);
            }
            else {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
    }

    public void removeFromCart(int productId){
        cart.remove(productId);
    }

    public void clearCart(){
        cart.clear();
    }

    public BigDecimal getTotal(){
        BigDecimal total = BigDecimal.ZERO;

        for(CartItem item : cart.values()){
            total = total.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return total;
    }

}

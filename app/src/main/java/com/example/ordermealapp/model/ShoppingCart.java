// In: com.example.ordermealapp.model.ShoppingCart.java
package com.example.ordermealapp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart instance;
    private Map<Dish, Integer> cartItems = new LinkedHashMap<>(); // Dish -> Quantity

    private ShoppingCart() {}

    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addItem(Dish dish) {
        if (cartItems.containsKey(dish)) {
            cartItems.put(dish, cartItems.get(dish) + 1);
        } else {
            cartItems.put(dish, 1);
        }
    }

    public void removeItem(Dish dish) {
        if (cartItems.containsKey(dish)) {
            if (cartItems.get(dish) > 1) {
                cartItems.put(dish, cartItems.get(dish) - 1);
            } else {
                cartItems.remove(dish);
            }
        }
    }

    public List<Map.Entry<Dish, Integer>> getCartItems() {
        return new ArrayList<>(cartItems.entrySet());
    }

    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<Dish, Integer> entry : cartItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
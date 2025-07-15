package com.example.ordermealapp.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int dishId;
    private String dishName;
    private double dishPrice;
    private int quantity;
    private int dishImageUrl; // To display in order history

    public OrderItem(int dishId, String dishName, double dishPrice, int quantity, int dishImageUrl) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.quantity = quantity;
        this.dishImageUrl = dishImageUrl;
    }

    // Getters
    public int getDishId() { return dishId; }
    public String getDishName() { return dishName; }
    public double getDishPrice() { return dishPrice; }
    public int getQuantity() { return quantity; }
    public int getDishImageUrl() { return dishImageUrl; }

    // Setters (Room might need them for internal mapping)
    public void setDishId(int dishId) { this.dishId = dishId; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public void setDishPrice(double dishPrice) { this.dishPrice = dishPrice; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDishImageUrl(int dishImageUrl) { this.dishImageUrl = dishImageUrl; }
}
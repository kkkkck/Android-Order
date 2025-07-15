package com.example.ordermealapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "dishes")
public class Dish implements Serializable {
    @PrimaryKey(autoGenerate = true) // Add this
    private int id;
    private String name;
    private String description;
    private double price;
    private int imageUrl;
    private int storeId; // To link dish to a store

    public Dish(int id, String name, String description, double price, int imageUrl, int storeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.storeId = storeId;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getImageUrl() { return imageUrl; }
    public int getStoreId() { return storeId; }

    // Setters (Room needs them)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setImageUrl(int imageUrl) { this.imageUrl = imageUrl; }
    public void setStoreId(int storeId) { this.storeId = storeId; }

    // Implement equals and hashCode for Dish (important for ShoppingCart's Map key)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id; // Equality based on ID
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(id).hashCode();
    }
}
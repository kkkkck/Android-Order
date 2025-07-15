package com.example.ordermealapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "stores")
public class Store implements Serializable {
    @PrimaryKey(autoGenerate = true) // Add this
    private int id;
    private String name;
    private int imageUrl; // For store image
    private double rating;

    public Store(int id, String name, int imageUrl, double rating) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getImageUrl() { return imageUrl; }
    public double getRating() { return rating; }

    // Setters (Room needs them)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(int imageUrl) { this.imageUrl = imageUrl; }
    public void setRating(double rating) { this.rating = rating; }

    // Implement equals and hashCode for Store
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id; // Equality based on ID
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(id).hashCode();
    }
}
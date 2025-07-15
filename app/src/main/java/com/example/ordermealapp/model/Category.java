package com.example.ordermealapp.model;

public class Category {
    private int id;
    private String name;
    private int iconResId; // Resource ID for local icon

    public Category(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}
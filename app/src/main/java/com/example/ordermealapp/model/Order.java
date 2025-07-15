package com.example.ordermealapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ordermealapp.database.Converters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity(tableName = "orders")
@TypeConverters({Converters.class})
public class Order implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int storeId;
    private String storeName;
    private double totalPrice;
    private Date orderDate;
    private String status; // "待支付", "已完成", "已取消"
    private List<OrderItem> items;

    public Order(int userId, int storeId, String storeName, double totalPrice, List<OrderItem> items) {
        this.userId = userId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalPrice = totalPrice;
        this.items = items;
        this.orderDate = new Date();
        this.status = "待支付";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
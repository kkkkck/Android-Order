package com.example.ordermealapp.database;

import androidx.room.TypeConverter;
import com.example.ordermealapp.model.OrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromOrderItemList(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<OrderItem>>() {}.getType();
        return gson.toJson(orderItems, type);
    }

    @TypeConverter
    public static List<OrderItem> toOrderItemList(String orderItemString) {
        if (orderItemString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<OrderItem>>() {}.getType();
        return gson.fromJson(orderItemString, type);
    }
}
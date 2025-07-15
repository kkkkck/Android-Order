package com.example.ordermealapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import java.util.List;
import com.example.ordermealapp.model.Dish;

@Dao
public interface DishDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDishes(List<Dish> dishes);

    @Query("SELECT * FROM dishes WHERE storeId = :storeId")
    List<Dish> getDishesByStoreId(int storeId);

    @Query("SELECT * FROM dishes")
    List<Dish> getAllDishes(); // Added for completeness, useful for search etc.
}
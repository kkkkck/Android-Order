package com.example.ordermealapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import java.util.List;
import com.example.ordermealapp.model.Store;

@Dao
public interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStores(List<Store> stores);

    @Query("SELECT * FROM stores")
    List<Store> getAllStores();

    @Query("SELECT * FROM stores WHERE id = :storeId LIMIT 1")
    Store getStoreById(int storeId);
}
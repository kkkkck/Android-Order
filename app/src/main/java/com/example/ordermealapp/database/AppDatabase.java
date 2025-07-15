package com.example.ordermealapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.ordermealapp.dao.DishDao;
import com.example.ordermealapp.dao.OrderDao;
import com.example.ordermealapp.dao.StoreDao;
import com.example.ordermealapp.dao.UserDao;
import com.example.ordermealapp.model.Dish;
import com.example.ordermealapp.model.Order;
import com.example.ordermealapp.model.Store;
import com.example.ordermealapp.model.User;
import com.example.ordermealapp.model.MockData;

import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Store.class, Dish.class, Order.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract StoreDao storeDao();
    public abstract DishDao dishDao();
    public abstract OrderDao orderDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "order_meal_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                StoreDao storeDao = INSTANCE.storeDao();
                DishDao dishDao = INSTANCE.dishDao();
                UserDao userDao = INSTANCE.userDao();


                storeDao.insertStores(MockData.getMockStores());
                dishDao.insertDishes(MockData.getMockDishes());

                if (userDao.getUserByUsername("test") == null) {

                    String hashedPassword = BCrypt.hashpw("123", BCrypt.gensalt());
                    userDao.insertUser(new User("test", hashedPassword));

                }
            });
        }
    };
}
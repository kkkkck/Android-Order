package com.example.ordermealapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermealapp.R;
import com.example.ordermealapp.adapter.OrderAdapter; // Will create this
import com.example.ordermealapp.database.AppDatabase;
import com.example.ordermealapp.model.Order;
import com.example.ordermealapp.utils.SessionManager;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rvOrderHistory;
    private OrderAdapter orderAdapter;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        setTitle("我的订单");

        db = AppDatabase.getDatabase(this);
        sessionManager = SessionManager.getInstance(this);

        rvOrderHistory = findViewById(R.id.rv_order_history);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "用户未登录，无法查看订单", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Order> orders = db.orderDao().getOrdersByUserId(userId);
            runOnUiThread(() -> {
                if (orders != null && !orders.isEmpty()) {
                    orderAdapter = new OrderAdapter(this, orders);
                    rvOrderHistory.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(this, "您还没有任何订单", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
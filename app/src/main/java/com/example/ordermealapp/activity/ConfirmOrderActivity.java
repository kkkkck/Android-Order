package com.example.ordermealapp.activity;// In: com.example.ordermealapp.activity.ConfirmOrderActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermealapp.R;
import com.example.ordermealapp.adapter.CartAdapter;
import com.example.ordermealapp.database.AppDatabase;
import com.example.ordermealapp.model.Dish;
import com.example.ordermealapp.model.Order;
import com.example.ordermealapp.model.OrderItem;
import com.example.ordermealapp.model.ShoppingCart;
import com.example.ordermealapp.model.Store; // For getting store name
import com.example.ordermealapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity {

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnConfirmPayment;
    private CartAdapter cartAdapter;
    private ShoppingCart shoppingCart;
    private AppDatabase db;
    private SessionManager sessionManager;
    private String fetchedStoreName; // Declare here
    private int finalStoreId; // Declare here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        setTitle("确认订单");

        shoppingCart = ShoppingCart.getInstance();
        db = AppDatabase.getDatabase(this);
        sessionManager = SessionManager.getInstance(this);

        rvCartItems = findViewById(R.id.rv_cart_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnConfirmPayment = findViewById(R.id.btn_confirm_payment);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, shoppingCart.getCartItems());
        rvCartItems.setAdapter(cartAdapter);

        updateTotalPrice();

        // Retrieve store name from intent
        // --- START MODIFICATION ---
        Intent intent = getIntent();
        if (intent != null) {
            Store store = (Store) intent.getSerializableExtra("store"); // Assuming you pass the whole store object
            if (store != null) {
                fetchedStoreName = store.getName();
                finalStoreId = store.getId();
            } else {
                // Fallback if store object is not passed
                fetchedStoreName = intent.getStringExtra("storeName");
                finalStoreId = intent.getIntExtra("storeId", -1); // Default to -1 if not found
            }
        }
        if (fetchedStoreName == null || fetchedStoreName.isEmpty()) {
            // Handle case where store name is not properly passed
            // You might want to get it from the dishes in the cart or show an error
            Toast.makeText(this, "未获取到商家信息，请重新尝试下单", Toast.LENGTH_LONG).show();
            finish(); // Exit activity if critical data is missing
            return;
        }
        // --- END MODIFICATION ---

        btnConfirmPayment.setOnClickListener(v -> confirmPayment());
    }

    public void updateTotalPrice() {
        tvTotalPrice.setText(String.format("总计: ¥ %.2f", shoppingCart.getTotalPrice()));
    }

    private void confirmPayment() {
        if (shoppingCart.getCartItems().isEmpty()) {
            Toast.makeText(this, "购物车为空，无法支付", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fetchedStoreName == null || finalStoreId == -1) {
            Toast.makeText(this, "商家信息不完整，无法创建订单", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "用户未登录，请先登录", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        if (!shoppingCart.getCartItems().isEmpty()) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                List<OrderItem> orderItems = new ArrayList<>();
                for (Map.Entry<Dish, Integer> entry : shoppingCart.getCartItems()) {
                    Dish dish = entry.getKey();
                    int quantity = entry.getValue();
                    orderItems.add(new OrderItem(dish.getId(), dish.getName(), dish.getPrice(), quantity, dish.getImageUrl()));
                }


                Order newOrder = new Order(userId, finalStoreId, fetchedStoreName, shoppingCart.getTotalPrice(), orderItems);
                db.orderDao().insertOrder(newOrder);

                runOnUiThread(() -> showPaymentSuccessDialog());
            });
        } else {

            showPaymentSuccessDialog();
        }
    }

    private void showPaymentSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("支付成功")
                .setMessage("您的订单已成功支付！")
                .setPositiveButton("确定", (dialog, which) -> {
                    shoppingCart.clearCart();
                    Intent intent = new Intent(ConfirmOrderActivity.this, OrderHistoryActivity.class); // Go to Order History
                    // --- START MODIFICATION ---
                    // Ensure that StoreDetailActivity is not on top of the stack after payment and order history
                    // If you want to go back to MainActivity directly after order history, you might need to adjust flags
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    // --- END MODIFICATION ---
                    startActivity(intent);
                    finish(); // Finish current activity
                    Toast.makeText(ConfirmOrderActivity.this, "订单完成", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartAdapter.updateCartData();
        updateTotalPrice();
    }
}
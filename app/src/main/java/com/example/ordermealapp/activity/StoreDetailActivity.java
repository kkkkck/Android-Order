package com.example.ordermealapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ordermealapp.R;
import com.example.ordermealapp.adapter.StoreDetailPagerAdapter;
import com.example.ordermealapp.fragment.DishListFragment;
import com.example.ordermealapp.model.Dish;
import com.example.ordermealapp.model.Store;
import com.example.ordermealapp.model.ShoppingCart;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;

public class StoreDetailActivity extends AppCompatActivity {

    private ImageView ivStoreDetailImage;
    private TextView tvStoreDetailName, tvStoreDetailRating;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button btnViewCart;
    private TextView tvCartItemCount, tvCartTotalPrice;

    private Store store;
    private ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        store = (Store) getIntent().getSerializableExtra("store");
        if (store == null) {
            Toast.makeText(this, "店铺信息加载失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle(store.getName());

        ivStoreDetailImage = findViewById(R.id.iv_store_detail_image);
        tvStoreDetailName = findViewById(R.id.tv_store_detail_name);
        tvStoreDetailRating = findViewById(R.id.tv_store_detail_rating);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        btnViewCart = findViewById(R.id.btn_view_cart);
        tvCartItemCount = findViewById(R.id.tv_cart_item_count);
        tvCartTotalPrice = findViewById(R.id.tv_cart_total_price);

        shoppingCart = ShoppingCart.getInstance();


        Glide.with(this).load(store.getImageUrl()).into(ivStoreDetailImage);
        tvStoreDetailName.setText(store.getName());
        tvStoreDetailRating.setText(String.format("评分: %.1f", store.getRating()));


        StoreDetailPagerAdapter pagerAdapter = new StoreDetailPagerAdapter(this, store);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("点菜");
                            break;
                        case 1:
                            tab.setText("评价");
                            break;
                        case 2:
                            tab.setText("商家");
                            break;
                    }
                }).attach();

        btnViewCart.setOnClickListener(v -> {
            if (shoppingCart.getCartItems().isEmpty()) {
                Toast.makeText(this, "购物车为空，请先添加菜品", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(StoreDetailActivity.this, ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });

        updateCartDisplay(); // Initial cart display
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update cart display whenever activity is resumed
        updateCartDisplay();
    }

    // Call this method to update the bottom cart bar
    public void updateCartDisplay() {
        int itemCount = 0;
        for (Map.Entry<Dish, Integer> entry : shoppingCart.getCartItems()) {
            itemCount += entry.getValue();
        }
        double totalPrice = shoppingCart.getTotalPrice();

        tvCartItemCount.setText(String.format("已选: %d件", itemCount));
        tvCartTotalPrice.setText(String.format("总计: ¥ %.2f", totalPrice));

        // Optionally, enable/disable the "Go to checkout" button
        btnViewCart.setOnClickListener(v -> {
            if (shoppingCart.getCartItems().isEmpty()) {
                Toast.makeText(this, "购物车为空，请先添加菜品", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(StoreDetailActivity.this, ConfirmOrderActivity.class);
                // --- START MODIFICATION ---
                intent.putExtra("store", store); // Pass the entire Store object
                // --- END MODIFICATION ---
                startActivity(intent);
            }
        });
    }
}
package com.example.ordermealapp.activity;// In: com.example.ordermealapp.activity.MainActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermealapp.R;
import com.example.ordermealapp.activity.LoginActivity;
import com.example.ordermealapp.activity.UserProfileActivity;
import com.example.ordermealapp.adapter.CategoryAdapter;
import com.example.ordermealapp.adapter.StoreAdapter;
import com.example.ordermealapp.database.AppDatabase;
import com.example.ordermealapp.model.Category;
import com.example.ordermealapp.model.Store;
import com.example.ordermealapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvStores, rvCategories;
    private StoreAdapter storeAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Store> allStoreList; // All stores for filtering
    private List<Store> filteredStoreList; // Stores currently displayed
    private List<Category> categoryList;

    private EditText etSearch;
    private ImageView ivProfileIcon;

    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("首页");

        db = AppDatabase.getDatabase(this);
        sessionManager = SessionManager.getInstance(this);

        etSearch = findViewById(R.id.et_search);
        ivProfileIcon = findViewById(R.id.iv_profile_icon);
        rvStores = findViewById(R.id.rv_stores);
        rvCategories = findViewById(R.id.rv_categories);

        rvStores.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString());
                return true;
            }
            return false;
        });

        // Load categories
        loadCategories();

        // Load stores
        loadStoresFromDb();

        ivProfileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void loadCategories() {
        categoryList = new ArrayList<>();
        // Add your mock categories here (or fetch from DB if you have a Category table)
        // Example categories (adjust based on your actual categories)
        categoryList.add(new Category(1, "美食", R.drawable.ic_category_food));
        categoryList.add(new Category(2, "超市", R.drawable.ic_category_supermarket));
        categoryList.add(new Category(3, "跑腿", R.drawable.ic_category_delivery));
        categoryList.add(new Category(4, "鲜果", R.drawable.ic_category_fruit));
        categoryList.add(new Category(5, "甜点饮品", R.drawable.ic_category_dessert));
        // Add more categories as needed

        // --- START MODIFICATION ---
        // Pass a callback or directly implement click listener here that calls filterStoresByCategory
        categoryAdapter = new CategoryAdapter(this, categoryList, this::filterStoresByCategory);
        rvCategories.setAdapter(categoryAdapter);
        // --- END MODIFICATION ---
    }


    private void loadStoresFromDb() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            allStoreList = db.storeDao().getAllStores();
            runOnUiThread(() -> {
                filteredStoreList = new ArrayList<>(allStoreList); // Initially show all stores
                storeAdapter = new StoreAdapter(this, filteredStoreList);
                rvStores.setAdapter(storeAdapter);
            });
        });
    }

    private void performSearch(String query) {
        if (allStoreList == null) return; // Wait for stores to load

        if (query.isEmpty()) {
            filteredStoreList.clear();
            filteredStoreList.addAll(allStoreList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            filteredStoreList.clear();
            filteredStoreList.addAll(allStoreList.stream()
                    .filter(store -> store.getName().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList()));
        }
        storeAdapter.notifyDataSetChanged(); // Update RecyclerView
        if (filteredStoreList.isEmpty() && !query.isEmpty()) {
            Toast.makeText(this, "未找到相关商家", Toast.LENGTH_SHORT).show();
        }
    }

    // --- START MODIFICATION ---
    // Method to filter stores by category
    public void filterStoresByCategory(Category category) {
        if (allStoreList == null) return;

        // For demonstration, let's assume category 1 is "美食", category 2 is "超市", etc.
        // You'll need a way to link stores to categories.
        // For now, let's just filter by a simple rule or all if category is "全部" or similar.

        // If a category is selected, filter stores.
        // For a proper implementation, `Store` model needs a `categoryId` or a many-to-many relationship.
        // As a placeholder, we'll just show a subset based on category name.
        // In a real app, you would query `db.storeDao().getStoresByCategory(categoryId)`.
        if (category.getName().equals("美食")) { // Assuming category 1 is "美食"
            filteredStoreList.clear();
            // Filter stores that are "food" related - this is a simple example.
            // You might need to add a `categoryId` to your Store model to make this accurate.
            filteredStoreList.addAll(allStoreList.stream()
                    .filter(store -> store.getName().contains("餐厅") || store.getName().contains("餐店") || store.getName().contains("烧烤"))
                    .collect(Collectors.toList()));
        } else if (category.getName().equals("超市")) { // Assuming category 2 is "超市"
            filteredStoreList.clear();
            filteredStoreList.addAll(allStoreList.stream()
                    .filter(store -> store.getName().contains("超市")) // Filter stores with "超市" in name
                    .collect(Collectors.toList()));
        }
        // Add more category filtering logic as needed
        else { // Show all stores if "全部" or no specific filter
            filteredStoreList.clear();
            filteredStoreList.addAll(allStoreList);
        }
        storeAdapter.notifyDataSetChanged();
        Toast.makeText(this, "已过滤商家： " + category.getName(), Toast.LENGTH_SHORT).show();
    }
    // --- END MODIFICATION ---

    @Override
    protected void onResume() {
        super.onResume();
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Re-load stores to ensure data consistency, e.g., after an order or profile update
            loadStoresFromDb(); // This will reset filtering if not handled carefully
            // If you want to maintain current category filter, you need to store selected category ID
            // and re-apply it here after loading all stores.
        }
    }
}
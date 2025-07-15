package com.example.ordermealapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ordermealapp.R;
import com.example.ordermealapp.database.AppDatabase;
import com.example.ordermealapp.model.User;
import com.example.ordermealapp.utils.SessionManager;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private EditText etNickname, etAddress;
    private Button btnSaveProfile, btnLogout, btnMyOrders; // Added btnMyOrders

    private AppDatabase db;
    private SessionManager sessionManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("个人信息");

        db = AppDatabase.getDatabase(this);
        sessionManager = SessionManager.getInstance(this);

        ivAvatar = findViewById(R.id.iv_user_avatar);
        etNickname = findViewById(R.id.et_nickname);
        etAddress = findViewById(R.id.et_address);
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnLogout = findViewById(R.id.btn_logout);
        btnMyOrders = findViewById(R.id.btn_my_orders); // Initialize new button

        loadUserProfile();

        btnSaveProfile.setOnClickListener(v -> saveUserProfile());
        btnLogout.setOnClickListener(v -> logoutUser());
        btnMyOrders.setOnClickListener(v -> { // Listener for "My Orders"
            startActivity(new Intent(UserProfileActivity.this, OrderHistoryActivity.class));
        });
    }

    private void loadUserProfile() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                currentUser = db.userDao().getUserById(userId);
                runOnUiThread(() -> {
                    if (currentUser != null) {
                        etNickname.setText(currentUser.getNickname());
                        etAddress.setText(currentUser.getAddress());
                        if (currentUser.getAvatarUrl() != null && !currentUser.getAvatarUrl().isEmpty()) {
                            Glide.with(this).load(currentUser.getAvatarUrl()).into(ivAvatar);
                        } else {
                            ivAvatar.setImageResource(R.drawable.ic_default_avatar);
                        }
                    } else {
                        Toast.makeText(this, "无法加载用户信息", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveUserProfile() {
        if (currentUser == null) {
            Toast.makeText(this, "用户信息未加载，无法保存", Toast.LENGTH_SHORT).show();
            return;
        }

        String newNickname = etNickname.getText().toString().trim();
        String newAddress = etAddress.getText().toString().trim();

        currentUser.setNickname(newNickname);
        currentUser.setAddress(newAddress);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.userDao().updateUser(currentUser);
            runOnUiThread(() -> {
                Toast.makeText(UserProfileActivity.this, "个人信息保存成功！", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void logoutUser() {
        sessionManager.logoutUser();
        Toast.makeText(this, "您已退出登录", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
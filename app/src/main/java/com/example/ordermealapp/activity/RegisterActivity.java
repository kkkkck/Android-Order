package com.example.ordermealapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.mindrot.jbcrypt.BCrypt;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordermealapp.R;
import com.example.ordermealapp.database.AppDatabase;
import com.example.ordermealapp.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");

        db = AppDatabase.getDatabase(this);

        etUsername = findViewById(R.id.et_username_register);
        etPassword = findViewById(R.id.et_password_register);
        etConfirmPassword = findViewById(R.id.et_confirm_password_register);
        btnRegister = findViewById(R.id.btn_register_submit);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "所有字段都不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- START MODIFICATION ---
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User existingUser = db.userDao().getUserByUsername(username);
            runOnUiThread(() -> {
                if (existingUser != null) {
                    Toast.makeText(RegisterActivity.this, "该用户名已被注册", Toast.LENGTH_SHORT).show();
                } else {
                    // Hash the password before saving
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    User newUser = new User(username, hashedPassword); // Save hashed password
                    db.userDao().insertUser(newUser);
                    Toast.makeText(RegisterActivity.this, "注册成功！请登录", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to LoginActivity
                }
            });
        });
    }
}
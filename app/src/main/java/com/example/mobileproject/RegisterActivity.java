package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.api.RetrofitClient;
import com.example.mobileproject.model.User;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "🔥 quan 🔥";
    private EditText edtUsername, edtEmail, edtPassword, edtPhone;
    private CheckBox cbAgree;
    private Button btnCreateAccount;
    private TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        edtUsername = findViewById(R.id.register_username);
        edtEmail = findViewById(R.id.register_email);
        edtPassword = findViewById(R.id.register_password);
        edtPhone = findViewById(R.id.register_phone);
        cbAgree = findViewById(R.id.cb_agree);
        btnCreateAccount = findViewById(R.id.register_btn_createaccount);
        tvSignIn = findViewById(R.id.register_signin);

        btnCreateAccount.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cbAgree.isChecked()) {
                Toast.makeText(this, "Vui lòng đồng ý với điều khoản dịch vụ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!email.endsWith("@gmail.com")) {
                Toast.makeText(this, "Email phải có đuôi @gmail.com", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() > 30) {
                Toast.makeText(this, "Mật khẩu không quá 30 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            User registerUser = new User();
            registerUser.setUsername(username);
            registerUser.setEmail(email);
            registerUser.setPassword(password);
            registerUser.setPhone(phone);

            ApiService apiService = RetrofitClient.getClient();
            Call<User> call = apiService.register(registerUser);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công"
                                , Toast.LENGTH_LONG).show();
                        Log.d(TAG, "🔥 User ID: " + user.getUserId());
                        Intent intent = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Lỗi đăng ký";
                        try {
                            if (response.errorBody() != null) {
                                JSONObject errorJson = new JSONObject(response.errorBody().string());
                                errorMessage = errorJson.optString("detail", errorMessage);
                                Log.e(TAG, "🔥 Server error response: " + errorJson.toString());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response: ", e);
                        }
                        Toast.makeText(RegisterActivity.this, errorMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG, "Network error: ", t);
                    Toast.makeText(RegisterActivity.this, "Lỗi kết nối: "
                            + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.api.RetrofitClient;
import com.example.mobileproject.model.User;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfileActivity extends AppCompatActivity {
    private static final String TAG = "🔥 quan 🔥";
    private static final int CHANGE_PASSWORD_REQUEST_CODE = 103;
    private EditText profileChangeName, profileChangeEmail, profileChangePhone;
    private Button btnSave, btnCancel;
    private ImageView btnEditPassword;
    SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
    int USER_ID = sharedPreferences.getInt("user_id", 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thaydoihoso);

        profileChangeName = findViewById(R.id.profilechange_name);
        profileChangeEmail = findViewById(R.id.profilechange_email);
        profileChangePhone = findViewById(R.id.profilechange_phone);
        btnSave = findViewById(R.id.profilechange_btnsave);
        btnCancel = findViewById(R.id.profilechange_btncancel);
        btnEditPassword = findViewById(R.id.profile_btnedit);

        // Nhận dữ liệu từ InformationActivity
        Intent intent = getIntent();
        profileChangeName.setText(intent.getStringExtra("name"));
        profileChangeEmail.setText(intent.getStringExtra("email"));
        profileChangePhone.setText(intent.getStringExtra("phone"));

        // Cho phép chỉnh sửa
        profileChangeName.setEnabled(true);
        profileChangeEmail.setEnabled(true);
        profileChangePhone.setEnabled(true);

        btnEditPassword.setOnClickListener(v -> {
            Intent passwordIntent = new Intent(this, ChangePasswordActivity.class);
            startActivityForResult(passwordIntent, CHANGE_PASSWORD_REQUEST_CODE);
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnSave.setOnClickListener(v -> {
            String name = profileChangeName.getText().toString().trim();
            String email = profileChangeEmail.getText().toString().trim();
            String phone = profileChangePhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfile(name, email, phone);
        });
    }

    private void updateProfile(String name, String email, String phone) {
        // Tạo đối tượng User cho request
        User updateUser = new User();
        updateUser.setUserId(USER_ID);
        updateUser.setFullName(name);
        updateUser.setEmail(email);
        updateUser.setPhone(phone);
        Log.d(TAG, "🔥 Request body: { user_id: " + USER_ID + ", full_name: "
                + name + ", email: " + email + ", phone: " + phone + " }");

        // Gọi API bằng Retrofit
        ApiService apiService = RetrofitClient.getClient();
        Call<ResponseBody> call = apiService.updateProfile(updateUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", name);
                        resultIntent.putExtra("email", email);
                        resultIntent.putExtra("phone", phone);
                        setResult(RESULT_OK, resultIntent);
                        Toast.makeText(ChangeProfileActivity.this,
                                "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String errorMessage = "Số điện thoại đã được sử dụng";
                        if (response.errorBody() != null) {
                            String jsonResponse = response.errorBody().string();
                            Log.d(TAG, "🔥 Update profile error response: " + jsonResponse);
                            JSONObject jsonResult = new JSONObject(jsonResponse);
                            errorMessage = jsonResult.optString("error", errorMessage);
                        }
                        Toast.makeText(ChangeProfileActivity.this,
                                errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response: ", e);
                    Toast.makeText(ChangeProfileActivity.this,
                            "Lỗi phân tích dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error: ", t);
                Toast.makeText(ChangeProfileActivity.this,
                        "Lỗi cập nhật thông tin: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_PASSWORD_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
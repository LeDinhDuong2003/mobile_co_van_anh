package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private LinearLayout btnGoogle;
    private TextView txtForgotPassword, txtSignUp;

    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    private static final String TAG = "🔥 MY_APP_LOG 🔥";
    private static final String CLIENT_ID = "466919745508-kdrtei827o3e2ml0tbdsjsb794s8ku6c.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu người dùng đã đăng nhập thì chuyển luôn sang MainActivity
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
            return;
        }

        setContentView(R.layout.dangnhap);

        // Ánh xạ view
        edtEmail = findViewById(R.id.login_username);
        edtPassword = findViewById(R.id.login_password);
        btnSignIn = findViewById(R.id.login_btn_sign_in);
        btnGoogle = findViewById(R.id.login_btn_google);
        txtForgotPassword = findViewById(R.id.login_forgot_password);
        txtSignUp = findViewById(R.id.login_signup);

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(CLIENT_ID)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Khởi tạo launcher cho Google Sign-In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    } else {
                        Toast.makeText(this, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        // Đăng nhập Email/Password (giả lập)
//        btnSignIn.setOnClickListener(v -> {
//            String email = edtEmail.getText().toString().trim();
//            String password = edtPassword.getText().toString().trim();
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
//            } else {
//                new Thread(() -> {
//                    try {
//                        URL url = new URL("http://10.0.2.2:8000/auth/login"); // 10.0.2.2 là localhost của PC
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("Content-Type", "application/json");
//                        conn.setDoOutput(true);
//
//                        String jsonInputString = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
//
//                        try (OutputStream os = conn.getOutputStream()) {
//                            byte[] input = jsonInputString.getBytes("utf-8");
//                            os.write(input, 0, input.length);
//                        }
//
//                        int code = conn.getResponseCode();
//                        InputStream is = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
//
//                        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
//                        StringBuilder response = new StringBuilder();
//                        String responseLine;
//
//                        while ((responseLine = br.readLine()) != null) {
//                            response.append(responseLine.trim());
//                        }
//
//                        String res = response.toString();
//
//                        runOnUiThread(() -> {
//                            if (code == 200) {
//                                Toast.makeText(this, "Đăng nhập thành công: " + res, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(this, "Lỗi đăng nhập: " + res, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                    }
//                }).start();
//            }
//        });


        // Đăng nhập bằng Google
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Quên mật khẩu
        txtForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show());

        // Chuyển sang Sign Up
        txtSignUp.setOnClickListener(v ->
                Toast.makeText(this, "Navigate to Sign Up", Toast.LENGTH_SHORT).show());
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String idToken = account.getIdToken();

            if (idToken != null) {
                Log.d(TAG, "🔥 ID Token: " + idToken);
                Log.e(TAG, "❌ ID Token is null");
//                saveIdToken(idToken);
                Toast.makeText(this, "ID Token: " + idToken, Toast.LENGTH_LONG).show();
            } else {
                Log.w(TAG, "ID Token is null");
                Toast.makeText(this, "ID Token is null", Toast.LENGTH_SHORT).show();
            }


            Toast.makeText(this, "Signed in with Google: " + email, Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
        } catch (ApiException e) {
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode(), e);
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

//    private void saveIdToken(String idToken) {
//        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
//        prefs.edit().putString("google_id_token", idToken).apply();
//    }
}

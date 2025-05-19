package com.example.mobileproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobileproject.ChangeProfileActivity;
import com.example.mobileproject.PreviewActivity;
import com.example.mobileproject.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = "🔥 quan 🔥";
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int PREVIEW_REQUEST_CODE = 101;
    private static final int CHANGE_PROFILE_REQUEST_CODE = 102;
    private ImageView profileAvatar, profileBtnChangeAvatar, profileBtnEdit;
    private TextView profileName;
    private EditText profileEmail, profilePassword, profilePhone;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_info", getContext().MODE_PRIVATE);

        // Khởi tạo các thành phần giao diện
        profileAvatar = view.findViewById(R.id.profile_avatar);
        profileBtnChangeAvatar = view.findViewById(R.id.profile_btnchangeavatar);
        profileBtnEdit = view.findViewById(R.id.profile_btnedit);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profilePassword = view.findViewById(R.id.profile_password);
        profilePhone = view.findViewById(R.id.profile_phone);

        // Lấy thông tin người dùng từ SharedPreferences
        String fullName = sharedPreferences.getString("full_name", "Không có tên");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phone", "");
        String avatarUrl = sharedPreferences.getString("avatar_url", "");

        // Hiển thị thông tin người dùng
        profileName.setText(fullName);
        profileEmail.setText(email);
        profilePhone.setText(phone);
        profilePassword.setText("................."); // Placeholder cho mật khẩu
        loadInitialAvatar(avatarUrl, fullName);

        // Khởi tạo launcher để chọn ảnh
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        Intent previewIntent = new Intent(requireContext(), PreviewActivity.class);
                        previewIntent.putExtra("image_uri", selectedImageUri.toString());
                        startActivityForResult(previewIntent, PREVIEW_REQUEST_CODE);
                    }
                });

        // Xử lý sự kiện nhấn nút thay đổi ảnh đại diện
        profileBtnChangeAvatar.setOnClickListener(v -> {
            if (android.os.Build.VERSION.SDK_INT < 29 &&
                    ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            } else {
                openImagePicker();
            }
        });

        // Xử lý sự kiện nhấn nút chỉnh sửa hồ sơ
        profileBtnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangeProfileActivity.class);
            intent.putExtra("name", profileName.getText().toString());
            intent.putExtra("email", profileEmail.getText().toString());
            intent.putExtra("phone", profilePhone.getText().toString());
            startActivityForResult(intent, CHANGE_PROFILE_REQUEST_CODE);
        });
    }

    private void loadInitialAvatar(String avatarUrl, String fullName) {
        String url = avatarUrl != null && !avatarUrl.isEmpty()
                ? avatarUrl
                : "https://ui-avatars.com/api/?name=" + fullName.replace(" ", "+") + "&background=2196F3&color=fff&size=150";

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .circleCrop()
                .into(profileAvatar);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(requireContext(), "Cần quyền truy cập thư viện để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PREVIEW_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String newAvatarUrl = data.getStringExtra("avatar_url");
            if (newAvatarUrl != null) {
                Glide.with(this)
                        .load(newAvatarUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .circleCrop()
                        .into(profileAvatar);
                // Lưu URL ảnh mới vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("avatar_url", newAvatarUrl);
                editor.apply();
                selectedImageUri = null;
                Toast.makeText(requireContext(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
            } else {
                selectedImageUri = null;
            }
        } else if (requestCode == CHANGE_PROFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String updatedName = data.getStringExtra("name");
            String updatedEmail = data.getStringExtra("email");
            String updatedPhone = data.getStringExtra("phone");
            // Cập nhật giao diện
            profileName.setText(updatedName);
            profileEmail.setText(updatedEmail);
            profilePhone.setText(updatedPhone);
            // Lưu thông tin mới vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("full_name", updatedName);
            editor.putString("email", updatedEmail);
            editor.putString("phone", updatedPhone);
            editor.apply();
            Toast.makeText(requireContext(), "Cập nhật thông tin hồ sơ thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.appfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeProfileAvatarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnProfileAvatarChooseimage;
    private Button btnProfileAvatarConfirm;
    private ImageView ivChangeProfilePic;
    private FirebaseFirestore db;
    private Bitmap selectedImage;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_avatar);
        btnProfileAvatarChooseimage = findViewById(R.id.btn_change_profile_avatar_chooseimage);
        btnProfileAvatarConfirm = findViewById(R.id.btn_change_profile_avatar_confirm);
        ivChangeProfilePic = findViewById(R.id.iv_change_profile_pic);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnProfileAvatarChooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnProfileAvatarConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePic();
            }
        });
    }
    private void chooseImage() {
        // 使用 Intent 打開圖片選擇器
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void changeProfilePic() {
        FirebaseUser user = mAuth.getCurrentUser();
        String image = encodeImage(selectedImage);
        // 创建一个书籍对象
        Map<String, Object> changeProfilePicture = new HashMap<>();
        changeProfilePicture.put("image", image);
        // 将书籍添加到 Cloud Firestore 中的 "books" 集合
        DocumentReference userRef = db.collection("users").document(user.getUid());
        System.out.println(userRef.get());
        userRef.update(changeProfilePicture)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangeProfileAvatarActivity.this, "更換成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ChangeProfileAvatarActivity", "Error updating user document.", e);
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // 获取用户选择的图片的 URI
            Uri imageUri = data.getData();
            try {
                // 将 URI 转换为 Bitmap 或者直接使用 Glide、Picasso 等库加载图片
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // 在 ImageView 中显示选择的图片
                ivChangeProfilePic.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
package com.example.appfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnProfileHome;
    private Button btnProfileSearch;

    private Button btnProfileLogout;
    private Button btnProfileChangePic;
    private Button btnProfileBookLog;
    private ImageView ivProfilePic;
    private TextView tvEmail;
    private FirebaseAuth mAuth;
    private TextView tvMemberInformation;
    private FirebaseFirestore db;
    ManagerInformation managerInformation = ManagerInformation.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnProfileHome = findViewById(R.id.btn_profile_home);

        btnProfileSearch = findViewById(R.id.btn_profile_search);
        btnProfileLogout = findViewById(R.id.btn_profile_logout);
        btnProfileBookLog = findViewById(R.id.btn_profile_book_log);
        btnProfileChangePic = findViewById(R.id.btn_profile_change_pic);
        ivProfilePic = findViewById(R.id.iv_profile_pic);

        tvEmail = findViewById(R.id.tv_email);
        mAuth = FirebaseAuth.getInstance();
        tvMemberInformation = findViewById(R.id.tv_menber_information);
        db = FirebaseFirestore.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        tvEmail.setText("Email: " + email);

        if(managerInformation.isManager == true){tvMemberInformation.setText("管理者資訊");}

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                 if(v.getId() == R.id.btn_profile_home){
                    intent.setClass(Profile.this, MainActivity.class);
                    Profile.this.startActivity(intent);
                }
                else if (v.getId() == R.id.btn_profile_search) {
                    intent.setClass(Profile.this, Search.class);
                    Profile.this.startActivity(intent);
                }else if(v.getId() == R.id.btn_profile_logout) {
                    managerInformation.isManager = false;
                    mAuth.signOut();
                    intent.setClass(Profile.this, SigninActivity.class);
                    Profile.this.startActivity(intent);
                }else if(v.getId() == R.id.btn_profile_change_pic) {
                     intent.setClass(Profile.this, ChangeProfileAvatarActivity.class);
                     Profile.this.startActivity(intent);
                 }
            }
        };
        btnProfileHome.setOnClickListener(listener);
        btnProfileSearch.setOnClickListener(listener);
        btnProfileLogout.setOnClickListener(listener);
        btnProfileChangePic.setOnClickListener(listener);
        loadUserPic();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserPic();
    }
    private void loadUserPic(){
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String imageUrl = documentSnapshot.getString("image");
                            if (imageUrl != null) {
                                ivProfilePic.setImageBitmap(decodeImage(imageUrl));
                            } else {
                                Toast.makeText(Profile.this, "No image found for user", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Profile.this, "User document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                        Log.e("MainActivity", "Error loading user data", e);
                    }
                });
    }
    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
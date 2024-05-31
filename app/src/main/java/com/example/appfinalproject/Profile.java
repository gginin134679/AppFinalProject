package com.example.appfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {
    private Button btnProfileHome;
    private Button btnProfileSearch;
    private Button btnProfileBack;
    private Button btnLog;
    private Button btnBook;
    private ImageView profilePic;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnProfileHome = findViewById(R.id.btn_profile_home);
        btnProfileBack = findViewById(R.id.btn_profile_back);
        btnProfileSearch = findViewById(R.id.btn_profile_search);
        btnLog = findViewById(R.id.btn_profile_log);
        btnBook = findViewById(R.id.btn_profile_book);
        profilePic = findViewById(R.id.iv_profile_pic);
        tvEmail = findViewById(R.id.tv_email);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(v.getId() == R.id.btn_profile_back){
                    intent.setClass(Profile.this, MainActivity.class);
                    Profile.this.startActivity(intent);
                }
                else if(v.getId() == R.id.btn_profile_home){
                    intent.setClass(Profile.this, MainActivity.class);
                    Profile.this.startActivity(intent);
                }
                else if (v.getId() == R.id.btn_profile_search) {
                    intent.setClass(Profile.this, Search.class);
                    Profile.this.startActivity(intent);
                }
            }
        };
        btnProfileBack.setOnClickListener(listener);
        btnProfileHome.setOnClickListener(listener);
        btnProfileSearch.setOnClickListener(listener);
    }
}
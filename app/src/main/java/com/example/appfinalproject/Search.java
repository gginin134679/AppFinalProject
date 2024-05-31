package com.example.appfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Search extends AppCompatActivity {

    private Button btnSearchBack;
    private Button btnSearchHome;
    private Button btnSearchPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearchBack = findViewById(R.id.btn_search_back);
        btnSearchHome = findViewById(R.id.btn_search_home);
        btnSearchPersonal = findViewById(R.id.btn_search_personal);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(v.getId() == R.id.btn_search_back){

                    intent.setClass(Search.this, MainActivity.class);
                    Search.this.startActivity(intent);
                }
                else if(v.getId() == R.id.btn_search_home){
                    intent.setClass(Search.this, MainActivity.class);
                    Search.this.startActivity(intent);
                }
                else if (v.getId() == R.id.btn_search_personal) {
                    intent.setClass(Search.this, Profile.class);
                    Search.this.startActivity(intent);
                }
            }
        };
        btnSearchBack.setOnClickListener(listener);
        btnSearchPersonal.setOnClickListener(listener);
        btnSearchHome.setOnClickListener(listener);
    }
}
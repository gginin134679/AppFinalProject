package com.example.appfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class bookComments extends AppCompatActivity {
    private Button btnBookcommentsAdd;
    private Button btnBookcommentsHome;
    private Button btnBookcommentsSearch;
    private Button btnBookcommentsPersonal;
    private ListView lvBookcomments;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_comments);

        btnBookcommentsAdd = findViewById(R.id.btn_bookcomments_add);
        btnBookcommentsHome = findViewById(R.id.btn_bookcomments_home);
        btnBookcommentsSearch = findViewById(R.id.btn_bookcomments_search);
        btnBookcommentsPersonal = findViewById(R.id.btn_bookcomments_home);
        lvBookcomments = findViewById(R.id.lv_bookcomments);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (v.getId() == R.id.btn_bookcomments_personal) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (v.getId() == R.id.btn_bookcomments_personal) {
                        if (user == null) {
                            intent.setClass(bookComments.this, SigninActivity.class);
                            bookComments.this.startActivity(intent);
                        } else {
                            intent.setClass(bookComments.this, Profile.class);
                            bookComments.this.startActivity(intent);
                        }
                    }
                } else if (v.getId() == R.id.btn_bookcomments_search) {
                    intent.setClass(bookComments.this, Search.class);
                    bookComments.this.startActivity(intent);
                } else if (v.getId() == R.id.btn_bookcomments_home) {
                    intent.setClass(bookComments.this, MainActivity.class);
                    bookComments.this.startActivity(intent);
                }
            }
        };

        btnBookcommentsPersonal.setOnClickListener(listener);
        btnBookcommentsSearch.setOnClickListener(listener);
        btnBookcommentsHome.setOnClickListener(listener);
    }
}
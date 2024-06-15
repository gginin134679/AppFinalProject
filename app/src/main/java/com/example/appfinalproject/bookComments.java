package com.example.appfinalproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class bookComments extends AppCompatActivity {
    private Button btnBookcommentsAdd;
    private Button btnBookcommentsHome;
    private Button btnBookcommentsSearch;
    private Button btnBookcommentsPersonal;
    private ListView lvBookcomments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_comments);

        btnBookcommentsAdd = findViewById(R.id.btn_bookcomments_add);
        btnBookcommentsHome = findViewById(R.id.btn_bookcomments_home);
        btnBookcommentsSearch = findViewById(R.id.btn_bookcomments_search);
        btnBookcommentsPersonal = findViewById(R.id.btn_bookcomments_home);
        lvBookcomments = findViewById(R.id.lv_bookcomments);


    }
}
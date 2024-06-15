package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Feed extends AppCompatActivity {
  private SearchView svFeed;
  private ListView lvFeed;
  private Button btnFeedHome;
  private Button btnFeedSearch;
  private Button btnFeedPersonal;
  private Button btnFeedAdd;
  private FirebaseAuth mAuth;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);

    btnFeedAdd = findViewById(R.id.btn_feed_add);
    svFeed = findViewById(R.id.sv_feed);
    lvFeed = findViewById(R.id.lv_feed);
    btnFeedHome = findViewById(R.id.btn_feed_home);
    btnFeedSearch = findViewById(R.id.btn_feed_search);
    btnFeedPersonal = findViewById(R.id.btn_feed_personal);
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == R.id.btn_feed_personal) {
          FirebaseUser user = mAuth.getCurrentUser();

          if (v.getId() == R.id.btn_feed_personal) {
            if (user == null) {
              intent.setClass(Feed.this, SigninActivity.class);
              Feed.this.startActivity(intent);
            } else {
              intent.setClass(Feed.this, Profile.class);
              Feed.this.startActivity(intent);
            }
          }
        } else if (v.getId() == R.id.btn_main_search) {
          intent.setClass(Feed.this, Search.class);
          Feed.this.startActivity(intent);
        } else if (v.getId() == R.id.btn_feed_home) {
          intent.setClass(Feed.this, MainActivity.class);
          Feed.this.startActivity(intent);
        } else if (v.getId() == R.id.btn_feed_add){
          intent.setClass(Feed.this, Comment.class);
          Feed.this.startActivity(intent);
        }
      }
    };

    btnFeedPersonal.setOnClickListener(listener);
    btnFeedSearch.setOnClickListener(listener);
    btnFeedHome.setOnClickListener(listener);
    btnFeedAdd.setOnClickListener(listener);
  }
}
package com.example.appfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Feed extends AppCompatActivity {
  private ListView lvFeed;
  private Button btnFeedHome;
  private Button btnFeedSearch;
  private Button btnFeedPersonal;
  private Button btnFeedAdd;
  private FirebaseAuth mAuth;
  private ArrayList<BookComment> CommentsList;
  private Feed.CommentAdapter adapter;
  private FirebaseFirestore db;
  private static final String TAG = "Feed";



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);

    lvFeed = findViewById(R.id.lv_feed);
    btnFeedHome = findViewById(R.id.btn_feed_home);
    btnFeedSearch = findViewById(R.id.btn_feed_search);
    btnFeedPersonal = findViewById(R.id.btn_feed_personal);
    btnFeedAdd = findViewById(R.id.btn_feed_add);
    mAuth = FirebaseAuth.getInstance();

    db = FirebaseFirestore.getInstance();
    CommentsList = new ArrayList<>();

    loadComments();

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
        } else if (v.getId() == R.id.btn_feed_search) {
          intent.setClass(Feed.this, Search.class);
          Feed.this.startActivity(intent);
        } else if (v.getId() == R.id.btn_feed_home) {
          intent.setClass(Feed.this, MainActivity.class);
          Feed.this.startActivity(intent);
        } else if (v.getId() == R.id.btn_feed_add) {
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
  private void loadComments() {
    // 从 Cloud Firestore 中读取书籍数据
    db.collection("CommentMessage")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {
                    String Bookname = document.getString("Bookname");
                    String avatarPath = document.getString("avatarPath");
                    String message = document.getString("message");
                    String username = document.getString("username");
                    long time = document.getLong("time");
                    CommentsList.add(new BookComment(username, message, avatarPath, Bookname));
                  }
                  // 数据加载完成后设置适配器
                  adapter = new Feed.CommentAdapter(Feed.this, CommentsList);
                  lvFeed.setAdapter(adapter);
                } else {
                  Log.d(TAG, "Error getting documents: ", task.getException());
                }
              }
            });
  }
  private class CommentAdapter extends ArrayAdapter<BookComment> {
    public CommentAdapter(Context context, ArrayList<BookComment> comments) {
      super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
      }

      BookComment Comment = getItem(position);

      TextView tvItemCommentUsername = convertView.findViewById(R.id.tv_item_comment_username);
      TextView tvItemCommentTime = convertView.findViewById(R.id.tv_item_comment_time);
      TextView tvItemCommentMessage = convertView.findViewById(R.id.tv_item_comment_message);
      ImageView ivItemCommentAvatar = convertView.findViewById(R.id.iv_item_comment_avatar);

      tvItemCommentUsername.setText(Comment.Username);
      tvItemCommentMessage.setText(Comment.Message);
      ivItemCommentAvatar.setImageBitmap(decodeImage(Comment.AvatarPath));

      return convertView;
    }

    private Bitmap decodeImage(String encodedImage) {
      byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
  }
}
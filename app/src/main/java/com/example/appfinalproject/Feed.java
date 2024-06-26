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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity {
  private ListView lvFeed;
  private Button btnFeedHome;
  private Button btnFeedSearch;
  private Button btnFeedPersonal;
  private Button btnFeedAdd;
  private FirebaseAuth mAuth;
  private FeedAdapter feedAdapter;
  private ArrayList<BookComment> comments;
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
    comments = new ArrayList<>();

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
    ManagerInformation managerInformation = ManagerInformation.getInstance();
    String bookname = managerInformation.Bookname;

    db.collection("books")
            .whereEqualTo("title", bookname)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  String bookId = task.getResult().getDocuments().get(0).getId();
                  db.collection("books").document(bookId).collection("comments")
                          .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                              if (task.isSuccessful()) {
                                comments.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                  String username = document.getString("username");
                                  String message = document.getString("message");
                                  String image = document.getString("image");

                                  comments.add(new BookComment(username, message, image));
                                }
                                // 確保在UI執行緒中更新適配器
                                feedAdapter = new FeedAdapter(Feed.this, comments);
                                lvFeed.setAdapter(feedAdapter);
                              } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                              }
                            }
                          });
                } else {
                  Log.d(TAG, "Error getting book: ", task.getException());
                }
              }
            });
  }
  // Inner class for the custom adapter
  private class FeedAdapter extends ArrayAdapter<BookComment> {
    public FeedAdapter(Context context, List<BookComment> comments) {
      super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
      }

      BookComment comment = getItem(position);

      ImageView ivAvatar = convertView.findViewById(R.id.iv_item_comment_avatar);
      TextView tvUsername = convertView.findViewById(R.id.tv_item_comment_username);
      TextView tvMessage = convertView.findViewById(R.id.tv_item_comment_message);

      ivAvatar.setImageBitmap(decodeImage(comment.image));
      tvUsername.setText(comment.username);
      tvMessage.setText(comment.message);

      return convertView;
    }
    private Bitmap decodeImage(String encodedImage) {
      byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
  }
}

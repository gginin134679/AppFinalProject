package com.example.appfinalproject;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Comment extends AppCompatActivity {
    private Button btnCommentSend;
    private Button btnCommentHome;
    private Button btnCommentSearch;
    private Button btnCommentPersonal;
    private TextInputEditText tietComment;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        btnCommentSend = findViewById(R.id.btn_comment_send);
        btnCommentHome = findViewById(R.id.btn_comment_home);
        btnCommentSearch = findViewById(R.id.btn_comment_search);
        btnCommentPersonal = findViewById(R.id.btn_comment_personal);
        tietComment = findViewById(R.id.tiet_comment);

        db = FirebaseFirestore.getInstance();


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (v.getId() == R.id.btn_comment_personal) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (v.getId() == R.id.btn_comment_personal) {
                        if (user == null) {
                            intent.setClass(Comment.this, SigninActivity.class);
                            Comment.this.startActivity(intent);
                        } else {
                            intent.setClass(Comment.this, Profile.class);
                            Comment.this.startActivity(intent);
                        }
                    }
                } else if (v.getId() == R.id.btn_comment_search) {
                    intent.setClass(Comment.this, Search.class);
                    Comment.this.startActivity(intent);
                } else if (v.getId() == R.id.btn_comment_home) {
                    intent.setClass(Comment.this, MainActivity.class);
                    Comment.this.startActivity(intent);
                } else if (v.getId() == R.id.btn_comment_send){
                    sendMsg();
                    intent.setClass(Comment.this, Feed.class);
                    Comment.this.startActivity(intent);
                }
            }
        };

        btnCommentPersonal.setOnClickListener(listener);
        btnCommentSearch.setOnClickListener(listener);
        btnCommentHome.setOnClickListener(listener);
        btnCommentSend.setOnClickListener(listener);
    }

    private void sendMsg() {
        ManagerInformation managerInformation = ManagerInformation.getInstance();
        String username = managerInformation.Email;
        String bookname = managerInformation.Bookname;
        String message = tietComment.getText().toString();
        String image = managerInformation.Image;

        if (username.isEmpty() || bookname.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "請填寫所有欄位", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> comment = new HashMap<>();
        comment.put("bookname", bookname);
        comment.put("username", username);
        comment.put("message", message);
        comment.put("image", image);

        db.collection("books")
                .whereEqualTo("title", bookname)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String bookId = task.getResult().getDocuments().get(0).getId();
                        db.collection("books").document(bookId).collection("comments").add(comment)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "留言成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Comment.this, Feed.class);
                                    startActivity(intent);
                                    finish(); // 確保當前活動結束
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "留言失敗", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "未找到書名", Toast.LENGTH_SHORT).show();
                    }
                });
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
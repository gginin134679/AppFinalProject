package com.example.appfinalproject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
        String msg = tietComment.getText().toString().trim();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().trim();
        //String avatar = FirebaseAuth.getInstance().getCurrentUser();
        //String bookID =
        if (msg.isEmpty()) {
            Toast.makeText(Comment.this, "Please leave your comment before sending it.", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> comment = new HashMap<>();
        comment.put("message", msg);
        comment.put("username", username);
        //comment.put("avatarPath", avatar);
        //comment.put("bookID", bookID);

        db.collection("CommentMassage")
                .add(comment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Comment.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Comment.this, "Failed to leave a comment.", Toast.LENGTH_SHORT).show();
                        Log.e("Comment", "Error leaving comment", e);
                    }
                });
    }
}
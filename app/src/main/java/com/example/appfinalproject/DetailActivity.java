package com.example.appfinalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private TextView tvDetailTitle, tvDetailAuthor,tvDetailContent;
    private ImageView imageView;
    private FirebaseFirestore db;
    private String documentId;
    private Button btnDetailBorrowBook;
    private Button btnDetailFeed;
    private FirebaseAuth auth;
    private String title, author, image,content;
    ManagerInformation managerInformation = ManagerInformation.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDetailTitle = findViewById(R.id.tv_item_book_title);
        tvDetailAuthor = findViewById(R.id.tv_item_book_author);
        tvDetailContent = findViewById(R.id.tv_detail_content);
        imageView = findViewById(R.id.iv_item_book_pic);
        btnDetailFeed = findViewById(R.id.btn_detail_feed);
        btnDetailBorrowBook = findViewById(R.id.btn_detail_borrowbook);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        documentId = getIntent().getStringExtra("documentId");

        if (documentId != null) {
            loadBookDetails(documentId);
        } else {
            Toast.makeText(this, "No document ID provided", Toast.LENGTH_SHORT).show();
        }
        btnDetailBorrowBook.setOnClickListener(v->showBorrowConfirmationDialog());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(v.getId() == R.id.btn_detail_feed){
                    intent.setClass(DetailActivity.this, Feed.class);
                    DetailActivity.this.startActivity(intent);
                }
            }
        };

        btnDetailFeed.setOnClickListener(listener);
    }

    private void loadBookDetails(String documentId) {
        db.collection("books").document(documentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            title = documentSnapshot.getString("title");
                            author = documentSnapshot.getString("author");
                            image = documentSnapshot.getString("image");
                            content = documentSnapshot.getString("content");
                            //
                            managerInformation.Bookname = title;
                            managerInformation.Image = image;
                            //
                            tvDetailTitle.setText("名稱: " + title);
                            tvDetailAuthor.setText("作者: " + author);
                            tvDetailContent.setText("內容: " + content);
                            if (image != null) {
                                imageView.setImageBitmap(decodeImage(image));
                            }
                        } else {
                            Toast.makeText(DetailActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Error loading details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showBorrowConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("借閱")
                .setMessage("您是否要借閱此書籍?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrowBook();
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }
    private void borrowBook() {
        FirebaseUser user = auth.getCurrentUser();
        String userEmail = auth.getCurrentUser().getEmail();

        Map<String, Object> borrowedBook = new HashMap<>();
        borrowedBook.put("userEmail", userEmail);
        borrowedBook.put("title", title);
        borrowedBook.put("author", author);
        borrowedBook.put("image", image);
        borrowedBook.put("content", content);
        db.collection("users").document(user.getUid()).collection("borrowed_books")
                .add(borrowedBook)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(DetailActivity.this, "借閱成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "借閱失敗", Toast.LENGTH_SHORT).show();
                        Log.e("DetailActivity", "借閱錯誤", e);
                    }
                });
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
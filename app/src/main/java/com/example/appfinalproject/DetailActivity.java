package com.example.appfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {
    private TextView textViewTitle, textViewAuthor;
    private ImageView imageView;
    private FirebaseFirestore db;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        imageView = findViewById(R.id.imageView);

        db = FirebaseFirestore.getInstance();

        // 获取传递过来的文档 ID
        documentId = getIntent().getStringExtra("documentId");

        if (documentId != null) {
            loadBookDetails(documentId);
        } else {
            Toast.makeText(this, "No document ID provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBookDetails(String documentId) {
        db.collection("books").document(documentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String title = documentSnapshot.getString("title");
                            String author = documentSnapshot.getString("author");
                            String image = documentSnapshot.getString("image");

                            textViewTitle.setText("名稱: " + title);
                            textViewAuthor.setText("作者: " + author);
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
    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
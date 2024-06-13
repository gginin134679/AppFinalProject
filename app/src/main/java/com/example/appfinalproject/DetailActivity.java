package com.example.appfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewAuthor = findViewById(R.id.textViewAuthor);
        ImageView imageView = findViewById(R.id.imageView);

        // 接收传递过来的数据
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String image = intent.getStringExtra("image");

        // 显示数据
        textViewTitle.setText("名稱"+title);
        textViewAuthor.setText("作者"+author);
        imageView.setImageBitmap(decodeImage(image));
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
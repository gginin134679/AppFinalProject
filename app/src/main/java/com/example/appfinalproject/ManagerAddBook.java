package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ManagerAddBook extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextTitle, editTextAuthor;
    private Button buttonAdd, buttonChooseImage;
    private ImageView imageView;
    private DBHelper dbHelper;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_book);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imageView = findViewById(R.id.imageView);
        dbHelper = new DBHelper(this);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String author = editTextAuthor.getText().toString();
                String image = encodeImage(selectedImage);
                dbHelper.addBook(title, author, image);
                Toast.makeText(ManagerAddBook.this, "Book added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
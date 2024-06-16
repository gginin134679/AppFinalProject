package com.example.appfinalproject;

import android.graphics.Bitmap;

public class BookComment {
    String username;
    String message;
    String image;

    public BookComment(String username, String message, String image) {
        this.username = username;
        this.message = message;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }
}

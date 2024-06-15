package com.example.appfinalproject;

public class BookComment {
    String Username;
    String Message;
    String AvatarPath;
    String bookName;

    public BookComment(String username, String message, String avatarPath, String bookname){
        this.Username = username;
        this.Message = message;
        this.AvatarPath = avatarPath;
        this.bookName = bookname;
    }

    public String getUsername() {
        return Username;
    }

    public String getMessage() {
        return Message;
    }

    public String getAvatarPath() {
        return AvatarPath;
    }

    public String getBookName() {
        return bookName;
    }
}

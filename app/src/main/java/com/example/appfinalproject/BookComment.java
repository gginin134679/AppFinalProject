package com.example.appfinalproject;

public class BookComment {
    String Username;
    String Message;
    String AvatarPath;
    String bookID;

    public BookComment(String username, String message, String avatarPath, String bookID){
        this.Username = username;
        this.Message = message;
        this.AvatarPath = avatarPath;
        this.bookID = bookID;
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

    public String getBookID() {
        return bookID;
    }
}

package com.example.appfinalproject;

public class BookComment {
    String Username;
    String Message;
    String AvatarPath;
    String bookname;
    String documentId;

    public BookComment(String username, String message, String avatarPath, String bookname, String documentId){
        this.Username = username;
        this.Message = message;
        this.AvatarPath = avatarPath;
        this.bookname = bookname;
        this.documentId = documentId;
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

    public String getBookname() {
        return bookname;
    }

    public String getDocumentId() {
        return documentId;
    }
}

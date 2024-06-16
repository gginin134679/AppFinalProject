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

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAvatarPath() {
        return AvatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        AvatarPath = avatarPath;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

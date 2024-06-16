package com.example.appfinalproject;

public class Book {
    String title;
    String author;
    String image;
    String content;
    String documentId;
    Book(String title, String author, String image,String documentId,String content) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.documentId = documentId;
        this.content = content;
    }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getImage() { return image; }
    public String getDocumentId() { return documentId; }
    public String getContent() { return content; }
}

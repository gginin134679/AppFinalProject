package com.example.appfinalproject;

public class Book {
    String title;
    String author;
    String image;
    String documentId;
    Book(String title, String author, String image,String documentId) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.documentId = documentId;
    }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getImage() { return image; }
    public String getDocumentId() { return documentId; }
}

package com.example.appfinalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReturnBooksActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ListView lvReturnBooksBorrowedBooks;

    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private FirebaseFirestore db;
    private String deleteBookId;
    private int posi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_books);
        lvReturnBooksBorrowedBooks = findViewById(R.id.lv_return_books_borrowed_books);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        bookList = new ArrayList<>();
        loadUserBorrowedBooks();
        lvReturnBooksBorrowedBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posi=position;
                showReturnDialog(position);
            }
        });
    }
    private void showReturnDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("歸還")
                .setMessage("您是否要歸還此書籍?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        returnBook(position);
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }
    private void loadUserBorrowedBooks() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .document(user.getUid())
                .collection("borrowed_books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String author = document.getString("author");
                                String image = document.getString("image");
                                String documentId = document.getId();
                                String content = document.getString("content");
                                bookList.add(new Book(title, author, image,documentId,content));
                            }
                            adapter = new ReturnBooksActivity.BookAdapter(ReturnBooksActivity.this, bookList);
                            lvReturnBooksBorrowedBooks.setAdapter(adapter);
                        } else {
                            Log.d("ReturnBooksActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private class BookAdapter extends ArrayAdapter<Book> {
        public BookAdapter(Context context, ArrayList<Book> books) {
            super(context, 0, books);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false);
            }

            Book book = getItem(position);

            TextView textViewTitle = convertView.findViewById(R.id.tv_item_book_title);
            TextView textViewAuthor = convertView.findViewById(R.id.tv_item_book_author);
            ImageView imageView = convertView.findViewById(R.id.iv_item_book_pic);

            textViewTitle.setText("名稱: " + book.title);
            textViewAuthor.setText("作者: " + book.author);

            imageView.setImageBitmap(decodeImage(book.image));

            return convertView;
        }

        private Bitmap decodeImage(String encodedImage) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }
    private void returnBook(final int position) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users")
                .document(user.getUid())
                .collection("borrowed_books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String author = document.getString("author");
                                String image = document.getString("image");

                                if(bookList.get(position).getTitle().equals(title) && bookList.get(position).getAuthor().equals(author) && bookList.get(position).getImage().equals(image)) {
                                    deleteBookId = document.getId();
                                    bookList.remove(position);
                                    bookList.clear();
                                    loadUserBorrowedBooks();
                                    Log.i("ReturnBooksActivity", "run：當前"+ deleteBookId);
                                    db.collection("users").document(user.getUid()).collection("borrowed_books").document(deleteBookId).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("ReturnBooksActivity", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("ReturnBooksActivity", "Error deleting document", e);
                                        }
                                    });
                                    break;
                                }

                            }
                        } else {
                            Log.d("ReturnBooksActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });

        adapter.notifyDataSetChanged();
    }

}
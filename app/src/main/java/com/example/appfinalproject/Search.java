package com.example.appfinalproject;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private Button btnSearchHome;
    private Button btnSearchPersonal;
    private SearchView svSearch;
    private ListView lvSearch;
    private ArrayList<Book> bookList;
    private ArrayList<Book> filteredBookList;
    private BookAdapter adapter;
    private FirebaseFirestore db;
    private static final String TAG = "SearchActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearchHome = findViewById(R.id.btn_search_home);
        btnSearchPersonal = findViewById(R.id.btn_search_personal);
        svSearch = findViewById(R.id.sv_search);
        lvSearch = findViewById(R.id.lv_search);

        //SearchView使用到的
        db = FirebaseFirestore.getInstance();
        bookList = new ArrayList<>();
        filteredBookList = new ArrayList<>();

        loadBooks();
        //
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = adapter.getItem(position);
                Intent intent = new Intent(Search.this, DetailActivity.class);
                intent.putExtra("documentId", selectedBook.getDocumentId());
                startActivity(intent);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                if(v.getId() == R.id.btn_search_home){
                    intent.setClass(Search.this, MainActivity.class);
                    Search.this.startActivity(intent);
                }
                else if (v.getId() == R.id.btn_search_personal) {
                    intent.setClass(Search.this, Profile.class);
                    Search.this.startActivity(intent);
                }
            }
        };
        btnSearchPersonal.setOnClickListener(listener);
        btnSearchHome.setOnClickListener(listener);

        //SearchView 使用
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return false;
            }
        });
    }
    private void loadBooks() {
        // 從 Cloud Firestore 中讀取書籍數據
        db.collection("books")
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
                                bookList.add(new Book(title, author, image,documentId));
                            }
                            // 數據加載完成後設置適配器
                            adapter = new BookAdapter(Search.this, filteredBookList);
                            lvSearch.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void filterBooks(String query) {
        filteredBookList.clear();
        if (query.isEmpty()) {
            // 如果查詢字串為空，則不顯示任何書籍
            adapter.notifyDataSetChanged();
            return;
        }
        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredBookList.add(book);
            }
            else if(book.getAuthor().toLowerCase().contains(query.toLowerCase())){
                filteredBookList.add(book);
            }
        }
        adapter.notifyDataSetChanged();
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

            TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
            TextView textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
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
}
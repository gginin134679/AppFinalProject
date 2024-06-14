package com.example.appfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  //ShenLian Logging for the first commit;
  private Button btnMainPersonal;
  private Button btnMainSearch;

  private FirebaseAuth mAuth;
  private ListView listViewBooks;

  private ArrayList<Book> bookList;
  private BookAdapter adapter;
  private FirebaseFirestore db;
  private static final String TAG = "MainActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btnMainPersonal = findViewById(R.id.btn_main_personal);
    btnMainSearch = findViewById(R.id.btn_main_search);
    listViewBooks = findViewById(R.id.listViewBooks);
    mAuth = FirebaseAuth.getInstance();

    db = FirebaseFirestore.getInstance();
    bookList = new ArrayList<>();
    loadBooks();
    listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book selectedBook = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("documentId", selectedBook.getDocumentId());
        startActivity(intent);
      }
    });
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == R.id.btn_main_personal) {
          FirebaseUser user = mAuth.getCurrentUser();

          if(v.getId() == R.id.btn_main_personal) {
            if(user == null) {
              intent.setClass(MainActivity.this, SigninActivity.class);
              MainActivity.this.startActivity(intent);
            }else {
              intent.setClass(MainActivity.this, Profile.class);
              MainActivity.this.startActivity(intent);
            }
          }
        } else if(v.getId() == R.id.btn_main_search){
          intent.setClass(MainActivity.this, Search.class);
          MainActivity.this.startActivity(intent);
        }
      }
    };
    btnMainPersonal.setOnClickListener(listener);
    btnMainSearch.setOnClickListener(listener);
  }
  private void loadBooks() {
    // 从 Cloud Firestore 中读取书籍数据
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
                  // 数据加载完成后设置适配器
                  adapter = new BookAdapter(MainActivity.this, bookList);
                  listViewBooks.setAdapter(adapter);
                } else {
                  Log.d(TAG, "Error getting documents: ", task.getException());
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

      TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
      TextView textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
      ImageView imageView = convertView.findViewById(R.id.imageView);

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
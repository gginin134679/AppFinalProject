package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ManagerActivity extends AppCompatActivity {
    private Button btnManagerAddBook;
    private Button btnManagerLogout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        btnManagerAddBook = findViewById(R.id.btn_manager_addbook);
        btnManagerLogout= findViewById(R.id.btn_manager_logout);
        mAuth = FirebaseAuth.getInstance();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(v.getId() == R.id.btn_manager_addbook) {

                    intent.setClass(ManagerActivity.this, ManagerAddBook.class);
                    ManagerActivity.this.startActivity(intent);
                }else if(v.getId() == R.id.btn_manager_logout) {
                    mAuth.signOut();
                    intent.setClass(ManagerActivity.this, SigninActivity.class);
                    ManagerActivity.this.startActivity(intent);
                }
            }
        };
        btnManagerAddBook.setOnClickListener(listener);
        btnManagerLogout.setOnClickListener(listener);
    }
}
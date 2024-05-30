package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private final static String defaultEmail = "root@gmail.com";
    private final static String defaultPassword = "root";
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLoginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                if(defaultEmail.equals(email) && defaultPassword.equals(password)) {
                    Intent intent = new Intent();
                    intent.setClass(Login.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        btnLoginLogin.setOnClickListener(listener);
    }
}
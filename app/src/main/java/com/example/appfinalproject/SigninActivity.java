package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {
    private final static String defaultEmail = "root@gmail.com";
    private final static String defaultPassword = "root";
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLoginLogin;
    private Button btnLoginSignUp;
    private FirebaseAuth mAuth;
    ManagerInformation managerInformation = ManagerInformation.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        btnLoginSignUp = findViewById(R.id.btn_login_signup);
        mAuth = FirebaseAuth.getInstance();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_login_login) {
                    String email = etLoginEmail.getText().toString();
                    String password = etLoginPassword.getText().toString();
                    //管理者判斷式
//                    if(email.toString().equals(managerInformation.managerEmail)){managerInformation.isManager = true;}
//                    else{managerInformation.isManager = false;}
                    signIn(email,password);
                }else if (v.getId() == R.id.btn_login_signup) {
                    Intent intent = new Intent();
                    intent.setClass(SigninActivity.this, SignupActivity.class);
                    SigninActivity.this.startActivity(intent);
                }
            }
        };
        btnLoginLogin.setOnClickListener(listener);
        btnLoginSignUp.setOnClickListener(listener);
    }
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 登入成功，更新 UI
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent();
                            if(user.getEmail().toString().equals("manager@gmail.com")) {
                                intent.setClass(SigninActivity.this, ManagerActivity.class);
                                SigninActivity.this.startActivity(intent);
                            }else{
                                intent.setClass(SigninActivity.this, MainActivity.class);
                                SigninActivity.this.startActivity(intent);
                            }
                        } else {
                            // 登入失敗，顯示錯誤訊息
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
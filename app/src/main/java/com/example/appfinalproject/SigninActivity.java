package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {
//    private final static String defaultEmail = "root@gmail.com";
//    private final static String defaultPassword = "root";
    private final static String defaultEmail = "123@gmail.com";
    private final static String defaultPassword = "123456";
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLoginLogin;
    private Button btnLoginSignUp;
    private TextView tvSigninForget;
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
        tvSigninForget = findViewById(R.id.tv_signin_forget);
        mAuth = FirebaseAuth.getInstance();

        //快速登入
        etLoginEmail.setText(defaultEmail);
        etLoginPassword.setText(defaultPassword);

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
                }else if (v.getId() == R.id.tv_signin_forget) {
                    Intent intent = new Intent(SigninActivity.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                }
            }
        };
        btnLoginLogin.setOnClickListener(listener);
        btnLoginSignUp.setOnClickListener(listener);
        tvSigninForget.setOnClickListener(listener);
    }
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 登入成功，更新 UI
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUserDocument(user);
                            Intent intent = new Intent();
                            if(user.getEmail().toString().equals("manager@gmail.com")) {
                                intent.setClass(SigninActivity.this, ManagerActivity.class);
                                SigninActivity.this.startActivity(intent);
                            }else{
                                intent.setClass(SigninActivity.this, MainActivity.class);
                                SigninActivity.this.startActivity(intent);
                            }
                            Toast.makeText(SigninActivity.this, "登入成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 登入失敗，顯示錯誤訊息
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void createUserDocument(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("email", user.getEmail());
                    userRef.set(userData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "User document created", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error creating user document", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Toast.makeText(this, "Error fetching user document", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserDocument(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    createUserDocument(user);
                }
            } else {
                Toast.makeText(this, "Error fetching user document", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
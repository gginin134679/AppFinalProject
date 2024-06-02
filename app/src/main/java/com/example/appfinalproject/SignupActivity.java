package com.example.appfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignupActivity extends AppCompatActivity {
    private EditText etSignupName;
    private EditText etSignEmail;
    private EditText etSignupPassword;
    private Button btnSignupSignup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etSignupName = findViewById(R.id.et_signup_name);
        etSignEmail = findViewById(R.id.et_signup_email);
        etSignupPassword = findViewById(R.id.et_signup_password);
        btnSignupSignup = findViewById(R.id.btn_signup_signup);
        mAuth = FirebaseAuth.getInstance();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etSignEmail.getText().toString();
                String password = etSignupPassword.getText().toString();
                signUp(email,password);
            }
        };
        btnSignupSignup.setOnClickListener(listener);
    }

    private void signUp(String email, String password){
        Task task = mAuth.createUserWithEmailAndPassword(email,password);
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(SignupActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                    SignupActivity.this.finish();
                }
                else{
                    Toast.makeText(SignupActivity.this, "註冊失敗", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.addOnCompleteListener(listener);
    }
}
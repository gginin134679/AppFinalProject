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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etNewPassword;
    private Button btnUpdatePassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etNewPassword = findViewById(R.id.et_new_password);
        btnUpdatePassword = findViewById(R.id.btn_update_password);
        mAuth = FirebaseAuth.getInstance();

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String newPassword = etNewPassword.getText().toString().trim();

        if (newPassword.isEmpty()) {
            Toast.makeText(ChangePasswordActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                finish(); // 返回上一個Activity
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Error updating password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
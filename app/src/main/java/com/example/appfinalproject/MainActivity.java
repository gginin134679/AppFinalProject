package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {

  //ShenLian Logging for the first commit;
  private Button btnMainPersonal;
  private Button btnMainSearch;
  private Button btnMainLogIn;
  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btnMainPersonal = findViewById(R.id.btn_main_personal);
    btnMainSearch = findViewById(R.id.btn_main_search);
    btnMainLogIn = findViewById(R.id.btn_main_logIn);
    mAuth = FirebaseAuth.getInstance();
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
        }
        else if(v.getId() == R.id.btn_main_search){
          intent.setClass(MainActivity.this, Search.class);
          MainActivity.this.startActivity(intent);
        }
        else if(v.getId() == R.id.btn_main_logIn){
          intent.setClass(MainActivity.this, SigninActivity.class);
          MainActivity.this.startActivity(intent);
        }
      }
    };
    btnMainPersonal.setOnClickListener(listener);
    btnMainSearch.setOnClickListener(listener);
    btnMainLogIn.setOnClickListener(listener);
  }
}
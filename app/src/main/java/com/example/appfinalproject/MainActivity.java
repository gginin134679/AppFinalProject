package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

  //ShenLian Logging for the first commit;
  private Button btnMainPersonal;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btnMainPersonal = findViewById(R.id.btn_main_personal);
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (v.getId() == R.id.btn_main_personal) {
          Intent intent = new Intent();
          intent.setClass(MainActivity.this, Login.class);
          MainActivity.this.startActivity(intent);
        }
      }
    };
    btnMainPersonal.setOnClickListener(listener);
  }
}
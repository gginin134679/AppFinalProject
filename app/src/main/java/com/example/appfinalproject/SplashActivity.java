package com.example.appfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();
    private static final String TAG = SplashActivity.class.getSimpleName();
    boolean isSplashActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplashActivity();
                Log.i(TAG, "run: 當前線程："+ Thread.currentThread().getName());
            }
        },2000);
    }
    private void startSplashActivity() {
        if (isSplashActivity == false){

            isSplashActivity = true;
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, SigninActivity.class);
            SplashActivity.this.startActivity(intent);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 点击跳动主界面
        Log.i(TAG, "onTouchEvent: Action "+ event.getAction());
        startSplashActivity();
        return super.onTouchEvent(event);
    }
    @Override
    protected void onDestroy() {
        // 移除延迟函数
        handler.removeCallbacks(null);
        super.onDestroy();
    }
}
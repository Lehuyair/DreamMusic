package com.example.mydreammusicfinal.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydreammusicfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },1500);


    }
    public void checkUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
        finish();
    }
}
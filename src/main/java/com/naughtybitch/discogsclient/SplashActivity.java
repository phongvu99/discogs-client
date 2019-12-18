package com.naughtybitch.discogsclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.naughtybitch.discogsclient.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }
}

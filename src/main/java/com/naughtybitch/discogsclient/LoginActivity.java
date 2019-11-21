package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn (View view){
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void signUp (View view){
        Intent intent1 = new Intent(this, SignUp.class);
        startActivity(intent1);
    }
}

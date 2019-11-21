package com.example.discog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void signin(View view){
        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);
    }

    public void signup(View view){
        Intent intent1 = new Intent(this,SignUp.class);
        startActivity(intent1);
    }

}

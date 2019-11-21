package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button f = (Button) findViewById(R.id.facebook);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.facebook.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Button g = (Button) findViewById(R.id.google);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "http://www.gmail.com";
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(url1));
                startActivity(i1);
            }
        });

    }
}

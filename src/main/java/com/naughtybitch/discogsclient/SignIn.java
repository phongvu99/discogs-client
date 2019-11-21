package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button f = (Button) findViewById(R.id.facebook);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "http://www.facebook.com";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(url2));
                startActivity(i2);
            }
        });

        Button g1 = (Button) findViewById(R.id.google);
        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url3 = "http://www.gmail.com";
                Intent i3 = new Intent(Intent.ACTION_VIEW);
                i3.setData(Uri.parse(url3));
                startActivity(i3);
            }
        });
    }
}

package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class GetStartedActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
    }

    public void signIn(View view) {
        final DiscogsClient instance = DiscogsClient.getInstance();
        String client_id = instance.getConsumer_key();
        String client_secret = instance.getConsumer_secret();
        progressDialog = ProgressDialog.show(this, "Sending Request", "Please wait");
        instance.requestToken(client_id, client_secret, new DiscogsClient.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                Pattern pattern = Pattern.compile(Pattern.quote("&"));
                String[] temp = pattern.split(result);
                ArrayList<String> request_token_response = new ArrayList<>();
                for (String s : temp) {
                    Pattern pattern1 = Pattern.compile(Pattern.quote("="));
                    String[] test = pattern1.split(s);
                    request_token_response.add(test[1]);
                }
                instance.setRequest_token(request_token_response.get(0));
                instance.setRequest_token_secret(request_token_response.get(1));
                Log.i("request_token", "Request_token " + instance.getRequest_token());
                Log.i("request_token_secret", "Request_token_secret " + instance.getRequest_token_secret());
                Toast.makeText(GetStartedActivity.this, "Successfully authenticated, redirecting...", Toast.LENGTH_SHORT).show();
                Toast.makeText(GetStartedActivity.this, "Request token " + result, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GetStartedActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String result) {
                progressDialog.dismiss();
                Toast.makeText(GetStartedActivity.this, "Failed to authenticate(No internet?)", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}

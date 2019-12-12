package com.naughtybitch.discogsclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences user_preferences;
    private Menu menu;
    private WebView webView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);

        // Custom ActionBar
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
//        collapsingToolbarLayout.setTitle("Discogs Authorization");
        myToolbar.setTitle("Authorization");
        setSupportActionBar(myToolbar);

        WebView webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        progressDialog = ProgressDialog.show(SignInActivity.this, "Discogs Login", "Loading...");
        String url = DiscogsClient.getInstance().getAuthorize_url();
        Toast.makeText(this, "URL " + url, Toast.LENGTH_LONG).show();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(url);
        buttonOnClickListener();
    }

    private void buttonOnClickListener() {
        Button button = findViewById(R.id.oauth_verify);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oauth_verify:
                authorizeApp();
        }
    }

    private void saveLogin() {
        DiscogsClient instance = DiscogsClient.getInstance();
        user_preferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_preferences.edit();
        editor.putBoolean("logged_in", true);
        editor.putString("access_token", instance.getAccess_token());
        editor.putString("access_token_secret", instance.getAccess_token_secret());
        editor.apply();
    }

    private void authorizeApp() {
        // Get credentials
        final DiscogsClient instance = DiscogsClient.getInstance();
        String client_id = instance.getConsumer_key();
        String client_secret = instance.getConsumer_secret();
        EditText editText = findViewById(R.id.verfier_code);
        String oauth_verifier = editText.getText().toString();
        instance.setOauth_verifier(oauth_verifier);
        instance.setOauth_verifier(editText.getText().toString());
        progressDialog = ProgressDialog.show(this, "Sending request", "Please wait");
        instance.accessToken(client_id, client_secret, new DiscogsClient.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                Pattern pattern = Pattern.compile(Pattern.quote("&"));
                String[] temp = pattern.split(result);
                ArrayList<String> access_token_response = new ArrayList<>();
                for (String s : temp) {
                    Pattern pattern1 = Pattern.compile(Pattern.quote("="));
                    String[] test = pattern1.split(s);
                    access_token_response.add(test[1]);
                }
                instance.setAccess_token(access_token_response.get(0));
                instance.setAccess_token_secret(access_token_response.get(1));
                Toast.makeText(SignInActivity.this, "Successfully authorized, redirecting to your profile", Toast.LENGTH_SHORT).show();
                Log.i("access_token", "Access_token " + instance.getAccess_token());
                Log.i("access_token_secret", "Access_token_secret " + instance.getAccess_token_secret());
                instance.identityRequest(new DiscogsClient.VolleyCallBackPOJO<User>() {
                    @Override
                    public void onSuccess(User object) {
                        user_preferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = user_preferences.edit();
                        editor.putString("user_name", object.getUsername());
                        editor.apply();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(SignInActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                saveLogin();
                MenuItem item = menu.findItem(R.id.authorize);
                item.setVisible(true);
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String result) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Failed to authenticate(No internet?)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.authorize:
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_login, menu);
        this.menu = menu;
        return true;
    }
}

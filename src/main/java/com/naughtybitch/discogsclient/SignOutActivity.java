package com.naughtybitch.discogsclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.naughtybitch.POJO.IdentityResponse;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignOutActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Custom ActionBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Sign Out");
        setSupportActionBar(myToolbar);

        progressBar = findViewById(R.id.progress_webview);
        webView = (WebView) findViewById(R.id.web_view);
        webView.clearCache(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        String url = "https://www.discogs.com/oauth/revoke?access_key=" + DiscogsClient.getInstance().getAccess_token();
        Log.i("url", "url " + url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(ProgressBar.GONE);
                checkLogin();
            }
        });
        webView.loadUrl(url);
    }

    private DiscogsAPI getDiscogsAPI() {
        final String auth = getCredentials();
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder onGoing = chain.request().newBuilder();
                onGoing.addHeader("Authorization", auth);
                onGoing.addHeader("User-Agent", "Discogsnect/0.1 +http://discogsnect.com");
                return chain.proceed(onGoing.build());
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = RetrofitClient.getRetrofitClient(client);

        return retrofit.create(DiscogsAPI.class);
    }

    private String getCredentials() {
        DiscogsClient instance = DiscogsClient.getInstance();
        Timestamp currentTimestamp = instance.currentTimeStamp();
        ArrayList<String> token = instance.getCredentials(this);
        String auth = "OAuth oauth_consumer_key=\"" + instance.getConsumer_key() + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + token.get(0) + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\", " +
                "oauth_signature=\"" + instance.getConsumer_secret() + token.get(1) + "\"";
        return auth;
    }

    @SuppressLint("ApplySharedPref")
    public void checkLogin() {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<IdentityResponse> call = discogsAPI.getIdentity();
        call.enqueue(new Callback<IdentityResponse>() {
            @Override
            public void onResponse(Call<IdentityResponse> call, Response<IdentityResponse> response) {
                Log.i("sign_out", "response " + response.code());
                if (response.code() == 401) {
                    Toast.makeText(SignOutActivity.this, "Adieu, mon ami!", Toast.LENGTH_SHORT).show();
                    signOut();
                }
            }

            @Override
            public void onFailure(Call<IdentityResponse> call, Throwable t) {
                Log.i("sign_out", "response " + t.getMessage());
                Toast.makeText(SignOutActivity.this, "Failed to sign out (No internet?) or you didn't?", Toast.LENGTH_SHORT).show();
                SignOutActivity.super.onBackPressed();
            }
        });
    }

    @SuppressLint("ApplySharedPref")
    private void signOut() {
        SharedPreferences user_preferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_preferences.edit();
        editor.putBoolean("logged_in", false);
        editor.commit();
        Intent intent = new Intent(SignOutActivity.this, GetStartedActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}

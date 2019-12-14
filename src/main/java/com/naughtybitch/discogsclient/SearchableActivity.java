package com.naughtybitch.discogsclient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.recyclerview.ResultsAdapter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchableActivity extends AppCompatActivity {

    Context context = this;
    List<Result> results;
    RecyclerView recyclerView;
    ResultsAdapter adapter;
    ProgressBar progressBar;

    private static void glide() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Lookup the recyclerView in activity layout

        // Initialize results

        // Create adapter passing in the sample result data

        // Attach the adapter to the recyclerView populate items

        // Set layout manager to position the items

        // That's all!
        initViews();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("query", query);
            searchQuery(query);
        }
    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        recyclerView = (RecyclerView) findViewById(R.id.rc_view);
        adapter = new ResultsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));
    }

    public void searchQuery(String query) {
        DiscogsClient instance = DiscogsClient.getInstance();
        Timestamp currentTimestamp = instance.currentTimeStamp();
        ArrayList<String> token = instance.getCredentials(context);
        final String auth = "OAuth oauth_consumer_key=\"" + instance.getConsumer_key() + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + token.get(0) + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\", " +
                "oauth_signature=\"" + instance.getConsumer_secret() + token.get(1) + "\"";

        Log.i("header", auth);

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

        // Add the interceptor to OkHTTPClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        // Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = RetrofitClient.getRetrofitClient(client);

        /*
        The main purpose of Retrofit is to create HTTP calls from the Java interface based
        on the annotation associated with each method. This is achieved by just passing the
        interface class as parameter to the create method
         */
        DiscogsAPI discogsAPI = retrofit.create(DiscogsAPI.class);
        /*
        Invoke the method corresponding to HTTP request which will return a Call object.
        This Call object will be used to send the actual network request with the specified parameters
         */
        Call<SearchResponse> call = discogsAPI.getSearchResult(query, 50, 1);
        /*
        This is the line which actually sends a network request. Calling enqueue() executes a call
        asynchronously. It has two callback listeners which will invoked on the main thread
         */
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                /* This is the success callback. Though the response type is JSON, with Retrofit
                we get the response in the form of SearchResponse POJO class
                 */
                Log.i("response", "Status " + response.code());
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    SearchResponse sResponse = response.body();
                    results = sResponse.getResults();
                    adapter = new ResultsAdapter(context, results);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("Error ", t.getMessage());
            }
        });


    }

}

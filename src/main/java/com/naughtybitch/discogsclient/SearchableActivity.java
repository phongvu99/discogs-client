package com.naughtybitch.discogsclient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.recyclerview.ResultsAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchableActivity extends AppCompatActivity {

    private static SearchableActivity instance;
    List<Result> results;
    RecyclerView recyclerView;

    public static SearchableActivity getInstance() {
        if (instance == null) {
            synchronized (SearchableActivity.class) {
                if (instance == null) {
                    Log.i("instance", "Creating new instance");
                    instance = new SearchableActivity();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Lookup the recyclerView in activity layout
//        recyclerView = (RecyclerView) findViewById(R.id.rc_view);
        // Initialize results
//        results = Result.createResultsList(1);
//        // Create adapter passing in the sample result data
//        adapter = new ResultsAdapter(results);
//        // Attach the adapter to the recyclerView populate items
//        recyclerView.setAdapter(adapter);
//        // Set layout manager to position the items
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
        initViews();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchQuery(query);
        }
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rc_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));
    }

    public void searchQuery(String query) {
        SharedPreferences userPreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        String access_token = userPreferences.getString("access_token", "null");
        String access_token_secret = userPreferences.getString("access_token_secret", "null");
        DiscogsClient instance = DiscogsClient.getInstance();
        final String auth = "OAuth oauth_consumer_key=\"" + instance.getConsumer_key() + "\", " +
                "oauth_token=\"" + access_token + "\", " +
                "oauth_signature=\"" + instance.getConsumer_secret() + access_token_secret + "\", " +
                "oauth_signature_method=\"PLAINTEXT\"";

        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder onGoing = chain.request().newBuilder();
                onGoing.addHeader("Authorization", auth);
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
                if (response.body() != null) {
                    SearchResponse sResponse = response.body();
                    results = sResponse.getResults();
                    ResultsAdapter adapter = new ResultsAdapter(results);
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

package com.naughtybitch.discogsclient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Result;
import com.naughtybitch.POJO.SearchResponse;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
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

public class SearchableActivity extends AppCompatActivity implements ResultsAdapter.OnResultListener {

    Context context = this;
    List<Result> results;
    Pagination pagination;
    RecyclerView recyclerView;
    ResultsAdapter adapter;
    ProgressBar progressBar;
    TextView empty;
    int next_page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        initViews();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("query", query);
            searchQuery(query);
        }
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initViews() {
        empty = (TextView) findViewById(R.id.card_empty);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        recyclerView = (RecyclerView) findViewById(R.id.rc_view);
        adapter = new ResultsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));
    }

    public void searchQuery(final String query) {
		
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
        final DiscogsAPI discogsAPI = retrofit.create(DiscogsAPI.class);
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
            public void onResponse(final Call<SearchResponse> call, Response<SearchResponse> response) {
                /* This is the success callback. Though the response type is JSON, with Retrofit
                we get the response in the form of SearchResponse POJO class
                 */
                Log.i("response", "Status " + response.code());
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    SearchResponse sResponse = response.body();
                    results = sResponse.getResults();
                    pagination = sResponse.getPagination();
                    if (results.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                    } else {
                        empty.setVisibility(View.GONE);
                    }
                    adapter = new ResultsAdapter(context, results, pagination, SearchableActivity.this, recyclerView);
                    adapter.setOnLoadMoreListener(new ResultsAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            results.add(null);
                            adapter.notifyItemInserted(results.size() - 1);
                            int per_page = pagination.getPerPage();
                            Log.i("next_page", "next page " + next_page);
                            Call<SearchResponse> newCall = discogsAPI.getSearchResult(query, per_page, next_page);
                            newCall.enqueue(new Callback<SearchResponse>() {
                                @Override
                                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                                    results.remove(results.size() - 1);
                                    results.addAll(response.body().getResults());
                                    adapter.notifyItemRangeInserted(results.size(), 50);
                                    adapter.setLoaded();
                                    ++next_page;
                                }

                                @Override
                                public void onFailure(Call<SearchResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("Error ", t.getMessage());
            }
        });


    }

    @Override
    public void onResultClick(int position, Result result) {
        Intent intent;
        switch (result.getType()) {
            case "master":
                intent = new Intent(SearchableActivity.this, AlbumInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("master_id", result.getMasterId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
        }
        Log.i("onResultClick", "onResultClick: clicked" + position);
    }

}

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Result;
import com.naughtybitch.POJO.SearchResponse;
import com.naughtybitch.POJO.ShowAllResponse;
import com.naughtybitch.POJO.Version;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.album.MasterInfoActivity;
import com.naughtybitch.recyclerview.ResultsAdapter;
import com.naughtybitch.recyclerview.VersionAdapter;

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

public class SearchableActivity extends AppCompatActivity implements ResultsAdapter.OnResultListener, VersionAdapter.OnVersionListener {

    int master_id;
    Context context = this;
    List<Result> results;
    List<Version> versions;
    Pagination pagination;
    RecyclerView recyclerView;
    ResultsAdapter resultsAdapter;
    VersionAdapter versionAdapter;
    ProgressBar progressBar;
    TextView empty;
    int next_page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        initViews();
        Intent intent = getIntent();
        try {
            master_id = intent.getExtras().getInt("master_id");
            if (master_id != 0) {
                fetchMasterReleaseVersions(master_id);
            }
            Log.i("master_id", "master id " + master_id);
        } catch (Exception e) {
            // Do smt
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("query", query);
            searchQuery(query);
        }
    }

    public void fetchMasterReleaseVersions(final int master_id) {
        final DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ShowAllResponse> call = discogsAPI.fetchMasterReleaseVersions(master_id, 50, 1);
        call.enqueue(new Callback<ShowAllResponse>() {
            @Override
            public void onResponse(Call<ShowAllResponse> call, Response<ShowAllResponse> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    ShowAllResponse showAllResponse = response.body();
                    pagination = showAllResponse.getPagination();
                    versions = showAllResponse.getVersions();
                    versionAdapter = new VersionAdapter(context, versions, pagination, SearchableActivity.this, recyclerView);
                    versionAdapter.setOnLoadMoreListener(new VersionAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            versions.add(null);
                            versionAdapter.notifyItemInserted(versions.size() - 1);
                            int per_page = pagination.getPerPage();
                            Log.i("next_page", "next page " + next_page);
                            Call<ShowAllResponse> newCall = discogsAPI.fetchMasterReleaseVersions(master_id, per_page, next_page);
                            newCall.enqueue(new Callback<ShowAllResponse>() {
                                @Override
                                public void onResponse(Call<ShowAllResponse> call, Response<ShowAllResponse> response) {
                                    versions.remove(versions.size() - 1);
                                    versions.addAll(response.body().getVersions());
                                    versionAdapter.notifyItemRangeInserted(versions.size(), 50);
                                    versionAdapter.setLoaded();
                                    ++next_page;
                                }

                                @Override
                                public void onFailure(Call<ShowAllResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(versionAdapter);
                }

            }

            @Override
            public void onFailure(Call<ShowAllResponse> call, Throwable t) {

            }
        });
    }

    public DiscogsAPI getDiscogsAPI() {
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

    public String getCredentials() {
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

        Log.i("header", auth);
        return auth;
    }

    private void initViews() {
        empty = (TextView) findViewById(R.id.card_empty);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        recyclerView = (RecyclerView) findViewById(R.id.rc_result);
        resultsAdapter = new ResultsAdapter();
        recyclerView.setAdapter(resultsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));
    }

    public void searchQuery(final String query) {
        final DiscogsAPI discogsAPI = getDiscogsAPI();
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
                    resultsAdapter = new ResultsAdapter(context, results, pagination, SearchableActivity.this, recyclerView);
                    resultsAdapter.setOnLoadMoreListener(new ResultsAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            results.add(null);
                            resultsAdapter.notifyItemInserted(results.size() - 1);
                            int per_page = pagination.getPerPage();
                            Log.i("next_page", "next page " + next_page);
                            Call<SearchResponse> newCall = discogsAPI.getSearchResult(query, per_page, next_page);
                            newCall.enqueue(new Callback<SearchResponse>() {
                                @Override
                                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                                    results.remove(results.size() - 1);
                                    results.addAll(response.body().getResults());
                                    resultsAdapter.notifyItemRangeInserted(results.size(), 50);
                                    resultsAdapter.setLoaded();
                                    ++next_page;
                                }

                                @Override
                                public void onFailure(Call<SearchResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(resultsAdapter);
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
                intent = new Intent(SearchableActivity.this, MasterInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("master_id", result.getMasterId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
        }
        Log.i("onReleaseClick", "onReleaseClick: clicked" + position);
    }

    @Override
    public void onVersionClick(int position, Version version) {
        Log.i("onVersionClick", "onVersionClick: clicked" + position);
    }
}

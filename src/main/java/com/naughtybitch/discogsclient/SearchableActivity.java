package com.naughtybitch.discogsclient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.MasterReleaseVersionsResponse;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.POJO.Result;
import com.naughtybitch.POJO.SearchResponse;
import com.naughtybitch.POJO.Version;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.album.MasterDetailsActivity;
import com.naughtybitch.discogsclient.artist.ArtistDetailsActivity;
import com.naughtybitch.discogsclient.buy.BuyMusicActivity;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.profile.ProfileActivity;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wishlist.WishlistActivity;
import com.naughtybitch.label.LabelDetailsActivity;
import com.naughtybitch.recyclerview.ArtistReleaseAdapter;
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

public class SearchableActivity extends AppCompatActivity implements
        ResultsAdapter.OnResultListener,
        VersionAdapter.OnVersionListener,
        ArtistReleaseAdapter.OnArtistReleaseListener {

    private String view;
    private int master_id, artist_id;
    private Context context = this;
    private List<Result> results;
    private List<Version> versions;
    private Pagination pagination;
    private RecyclerView recyclerView;
    private ResultsAdapter resultsAdapter;
    private VersionAdapter versionAdapter;
    private ArtistReleaseAdapter artistReleaseAdapter;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar myToolbar;
    private MenuItem searchItem;
    private String query;
    TextView empty;
    int next_page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        initViews();
        Intent intent = getIntent();
        try {
            artist_id = intent.getExtras().getInt("artist_id");
            view = intent.getExtras().getString("view_all");
            if (artist_id != 0) {
                fetchArtistReleases(artist_id);
            }
            Log.i("artist_id", "artist id " + artist_id);
        } catch (NullPointerException e) {
            // Do smt
        }
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
            query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("query", query);
            searchQuery(query);
        }
    }

    private void initViews() {

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        myToolbar.setTitle(query);
        setSupportActionBar(myToolbar);

        // DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        navigationViewHandler();

        empty = (TextView) findViewById(R.id.card_empty);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        recyclerView = (RecyclerView) findViewById(R.id.rc_result);
        resultsAdapter = new ResultsAdapter();
        recyclerView.setAdapter(resultsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));
    }

    private void fetchArtistReleases(final int artist_id) {
        final DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ArtistReleasesResponse> call = discogsAPI.fetchArtistReleases(artist_id, "year", "asc"
                , 50, 1);

        call.enqueue(new Callback<ArtistReleasesResponse>() {
            @Override
            public void onResponse(Call<ArtistReleasesResponse> call, Response<ArtistReleasesResponse> response) {
                Log.i("more_by", "response code " + response.code());
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    ArtistReleasesResponse artistResponse = response.body();
                    Pagination pagination = artistResponse.getPagination();
                    Log.i("pagination", "pagination items" + pagination.getItems());
                    final List<Release> releases = artistResponse.getReleases();
                    artistReleaseAdapter = new ArtistReleaseAdapter(context, releases, pagination, SearchableActivity.this, recyclerView);
                    try {
                        if (view.equals("view_all")) {
                            artistReleaseAdapter.setOnLoadMoreListener(new ArtistReleaseAdapter.OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            releases.add(null);
                                            artistReleaseAdapter.notifyItemInserted(releases.size() - 1);
                                        }
                                    });
                                    Log.i("next_page", "next page " + next_page);
                                    Call<ArtistReleasesResponse> newCall = discogsAPI.fetchArtistReleases(artist_id, "year", "asc", 50, next_page);
                                    newCall.enqueue(new Callback<ArtistReleasesResponse>() {
                                        @Override
                                        public void onResponse(Call<ArtistReleasesResponse> call, final Response<ArtistReleasesResponse> response) {
                                            if (response.body() != null) {
                                                recyclerView.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        releases.remove(releases.size() - 1);
                                                        artistReleaseAdapter.notifyItemRemoved(releases.size());
                                                        releases.addAll(response.body().getReleases());
                                                        artistReleaseAdapter.notifyItemRangeInserted(releases.size(), 50);
                                                    }
                                                });
                                                artistReleaseAdapter.setLoaded();
                                                ++next_page;
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArtistReleasesResponse> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        }
                    } catch (NullPointerException e) {
                        Log.i("sucks", "sucks");
                        // Do smt
                    }
                    recyclerView.setAdapter(artistReleaseAdapter);
                } else {
                    progressBar.setVisibility(View.GONE);
                    TextView empty = findViewById(R.id.card_empty);
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArtistReleasesResponse> call, Throwable t) {
                Log.i("more_by_response", "response code " + t.getMessage());
            }
        });

    }

    public void fetchMasterReleaseVersions(final int master_id) {
        final DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<MasterReleaseVersionsResponse> call = discogsAPI.fetchMasterReleaseVersions(master_id, 50, 1);
        call.enqueue(new Callback<MasterReleaseVersionsResponse>() {
            @Override
            public void onResponse(Call<MasterReleaseVersionsResponse> call, Response<MasterReleaseVersionsResponse> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    MasterReleaseVersionsResponse masterReleaseVersionsResponse = response.body();
                    pagination = masterReleaseVersionsResponse.getPagination();
                    versions = masterReleaseVersionsResponse.getVersions();
                    versionAdapter = new VersionAdapter(context, versions, pagination, SearchableActivity.this, recyclerView);
                    versionAdapter.setOnLoadMoreListener(new VersionAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    versions.add(null);
                                    versionAdapter.notifyItemInserted(versions.size() - 1);
                                }
                            });
                            Log.i("next_page", "next page " + next_page);
                            Call<MasterReleaseVersionsResponse> newCall = discogsAPI.fetchMasterReleaseVersions(master_id, 50, next_page);
                            newCall.enqueue(new Callback<MasterReleaseVersionsResponse>() {
                                @Override
                                public void onResponse(Call<MasterReleaseVersionsResponse> call, final Response<MasterReleaseVersionsResponse> response) {
                                    if (response.body() != null) {
                                        recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                versions.remove(versions.size() - 1);
                                                versionAdapter.notifyItemRemoved(versions.size());
                                                versions.addAll(response.body().getVersions());
                                                versionAdapter.notifyItemRangeInserted(versions.size(), 50);
                                            }
                                        });
                                        versionAdapter.setLoaded();
                                        ++next_page;
                                    }
                                }

                                @Override
                                public void onFailure(Call<MasterReleaseVersionsResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(versionAdapter);
                }

            }

            @Override
            public void onFailure(Call<MasterReleaseVersionsResponse> call, Throwable t) {

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
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    results.add(null);
                                    resultsAdapter.notifyItemInserted(results.size() - 1);
                                }
                            });
                            Log.i("next_page", "next page " + next_page);
                            Call<SearchResponse> newCall = discogsAPI.getSearchResult(query, 50, next_page);
                            newCall.enqueue(new Callback<SearchResponse>() {
                                @Override
                                public void onResponse(Call<SearchResponse> call, final Response<SearchResponse> response) {
                                    if (response.body() != null) {
                                        recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                results.remove(results.size() - 1);
                                                resultsAdapter.notifyItemRemoved(results.size());
                                                results.addAll(response.body().getResults());
                                                resultsAdapter.notifyItemRangeInserted(results.size(), 50);
                                            }
                                        });
                                        resultsAdapter.setLoaded();
                                        ++next_page;
                                    }
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
        Bundle bundle = new Bundle();
        switch (result.getType()) {
            case "master":
                intent = new Intent(SearchableActivity.this, MasterDetailsActivity.class);
                bundle.putInt("master_id", result.getMasterId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "release":
                intent = new Intent(SearchableActivity.this, MasterDetailsActivity.class);
                bundle.putInt("release_id", result.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "artist":
                intent = new Intent(SearchableActivity.this, ArtistDetailsActivity.class);
                bundle.putInt("artist_id", result.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "label":
                intent = new Intent(SearchableActivity.this, LabelDetailsActivity.class);
                bundle.putInt("label_id", result.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        Log.i("onReleaseClick", "onReleaseClick: clicked" + position);
    }

    @Override
    public void onVersionClick(int position, Version version) {
        Intent intent = new Intent(SearchableActivity.this, MasterDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("release_id", version.getId());
        intent.putExtras(bundle);
        startActivity(intent);
        Log.i("onVersionClick", "onVersionClick: clicked" + position);
    }

    @Override
    public void onArtistReleaseClick(int position, Release release) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (release.getType()) {
            case "master":
                intent = new Intent(SearchableActivity.this, MasterDetailsActivity.class);
                bundle.putInt("master_id", release.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "release":
                intent = new Intent(SearchableActivity.this, MasterDetailsActivity.class);
                bundle.putInt("release_id", release.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        Log.i("release", "Release position " + position + " release title " + release.getTitle());
    }

    private void navigationViewHandler() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(SearchableActivity.this, MainActivity.class));
                        Toast.makeText(SearchableActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(SearchableActivity.this, ProfileActivity.class));
                        Toast.makeText(SearchableActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(SearchableActivity.this, WishlistActivity.class));
                        Toast.makeText(SearchableActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buy_music:
                        startActivity(new Intent(SearchableActivity.this, BuyMusicActivity.class));
                        Toast.makeText(SearchableActivity.this, "BuyMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(SearchableActivity.this, SellMusicActivity.class));
                        Toast.makeText(SearchableActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(SearchableActivity.this, SettingsActivity.class));
                        Toast.makeText(SearchableActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(SearchableActivity.this, ExploreActivity.class));
                        Toast.makeText(SearchableActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            this.drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_search, menu);
        searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getText(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Intent intent = new Intent(SearchableActivity.this, SearchableActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                Bundle bundle = new Bundle();
                bundle.putString(SearchManager.QUERY, query);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

        });
        return true;
    }
}



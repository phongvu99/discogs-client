package com.naughtybitch.discogsclient.collection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.CollectionResponse;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.album.MasterDetailsActivity;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.profile.ProfileActivity;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wantlist.WantlistActivity;
import com.naughtybitch.recyclerview.ArtistReleaseAdapter;
import com.naughtybitch.recyclerview.WishlistAdapter;

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

public class CollectionActivity extends AppCompatActivity implements ArtistReleaseAdapter.OnArtistReleaseListener, 
CollectionFragment.OnFragmentInteractionListener  {

    private LinearLayout fragment_wantlist;
    private CoordinatorLayout coordinatorLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private SharedPreferences sp;
    private ImageView profile_menu_image;
    private TextView profile_menu_name, profile_menu_email, empty;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private String username;
    private WishlistAdapter wishlistAdapter;
    private ArtistReleaseAdapter artistReleaseAdapter;
    private Pagination pagination;
    private Context context = this;
    private int next_page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Custom ActionBar
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        myToolbar.setTitle("Collection");
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
        }

        empty = (TextView) findViewById(R.id.card_empty);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        recyclerView = (RecyclerView) findViewById(R.id.rc_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            toggle.setDrawerIndicatorEnabled(true);
                            setTitle(R.string.collection);
                        }
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            toggle.setDrawerIndicatorEnabled(false);
                            getSupportActionBar().setHomeButtonEnabled(true);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                    }
                });
        initView();
    }

    private void initView() {
        fragment_wantlist = findViewById(R.id.fragment_wantlist);
        fragment_wantlist.setVisibility(View.GONE);
        coordinatorLayout = findViewById(R.id.fragment_searchable);
        recyclerView = findViewById(R.id.rc_result);
        wishlistAdapter = new WishlistAdapter();
        recyclerView.setAdapter(wishlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        navigationView = findViewById(R.id.navigation_view);
        View nav_header = navigationView.getHeaderView(0);
        profile_menu_email = nav_header.findViewById(R.id.profile_menu_email);
        profile_menu_name = nav_header.findViewById(R.id.profile_menu_name);
        profile_menu_image = nav_header.findViewById(R.id.profile_menu_image);
        navigationViewHandler();
        sp = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        username = sp.getString("user_name", null);
        if (username != null) {
            fetchProfile(username);
            fetchCollection(username);
        }
    }

    private void fetchCollection(final String username) {
        final DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<CollectionResponse> call = discogsAPI.fetchCollection(username, 0, 50, 1);
        call.enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(Call<CollectionResponse> call, Response<CollectionResponse> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    CollectionResponse collectionResponse = response.body();
                    pagination = collectionResponse.getPagination();
                    if (pagination.getItems() == 0) {
                        coordinatorLayout.setVisibility(View.GONE);
                    }
                    final List<Release> releases = collectionResponse.getReleases();
                    artistReleaseAdapter = new ArtistReleaseAdapter(context, releases, pagination, CollectionActivity.this, recyclerView);
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
                            Call<CollectionResponse> newCall = discogsAPI.fetchCollection(username, 0, 6, next_page);
                            newCall.enqueue(new Callback<CollectionResponse>() {
                                @Override
                                public void onResponse(Call<CollectionResponse> call, final Response<CollectionResponse> response) {
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
                                public void onFailure(Call<CollectionResponse> call, Throwable t) {

                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(artistReleaseAdapter);
                } else {
                    coordinatorLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.GONE);
            }
        });

    }

    private void updateProfile(ProfileResponse profileResponse) {
        Glide.with(this)
                .load(profileResponse.getAvatarUrl())
                .error(R.drawable.discogs_vinyl_record_mark)
                .placeholder(R.drawable.discogs_vinyl_record_mark)
                .into(profile_menu_image);
        profile_menu_name.setText(profileResponse.getName());
        profile_menu_email.setText(profileResponse.getEmail());
    }

    private void fetchProfile(String username) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ProfileResponse> call = discogsAPI.fetchProfile(username);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    updateProfile(profileResponse);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("PROFILE_CAT", t.getMessage());
            }
        });
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

    public void navigationViewHandler() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.collection);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(CollectionActivity.this, MainActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(CollectionActivity.this, ProfileActivity.class));
                        break;
                    case R.id.collection:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(CollectionActivity.this, WantlistActivity.class));
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(CollectionActivity.this, SellMusicActivity.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(CollectionActivity.this, SettingsActivity.class));
                        break;
                    case R.id.explore:
                        startActivity(new Intent(CollectionActivity.this, ExploreActivity.class));
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            this.drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                    return true;
                }
                break;
            case R.id.search:
                Intent intent = new Intent(CollectionActivity.this, ExploreActivity.class);
                startActivity(intent);
                break;
        }

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do smt
    }
    
    @Override
    public void onArtistReleaseClick(int position, Release release) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(CollectionActivity.this, MasterDetailsActivity.class);
        bundle.putInt("release_id", release.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

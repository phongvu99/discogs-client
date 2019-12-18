package com.naughtybitch.discogsclient.album;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.MasterReleasesResponse;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.adapter.SliderAdapter;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.buy.BuyMusicActivity;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.profile.ProfileActivity;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wishlist.WishlistActivity;
import com.naughtybitch.recyclerview.MoreByAdapter;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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


public class MasterInfoActivity extends AppCompatActivity implements MoreByAdapter.OnMoreByListener {

    private int master_id;
    private List<String> genres, styles;
    private TextView title, artist, year, genre, style;
    private SliderAdapter sliderAdapter;
    private SliderView sliderView;
    private MoreByAdapter adapter;
    private RecyclerView recyclerView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_info);
        master_id = getIntent().getExtras().getInt("master_id");
        Log.i("master_id", "master_id " + master_id);
      
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        setSupportActionBar(myToolbar);

        navigationViewHandler();

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
        initView();
        fetchData();
    }
  
    public void navigationViewHandler() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(MasterInfoActivity.this, MainActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(MasterInfoActivity.this, ProfileActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(MasterInfoActivity.this, WishlistActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buy_music:
                        startActivity(new Intent(MasterInfoActivity.this, BuyMusicActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "BuyMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(MasterInfoActivity.this, SellMusicActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(MasterInfoActivity.this, SettingsActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(MasterInfoActivity.this, ExploreActivity.class));
                        Toast.makeText(MasterInfoActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
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

        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(MasterInfoActivity.this, ExploreActivity.class);
                startActivity(intent);
                break;
        }

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
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }

    public void initView() {
        genre = findViewById(R.id.master_genre);
        style = findViewById(R.id.master_style);
        title = findViewById(R.id.master_title);
        artist = findViewById(R.id.master_artist);
        year = findViewById(R.id.master_year);
        recyclerView = findViewById(R.id.rc_view);
        adapter = new MoreByAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        sliderView = findViewById(R.id.slider_view);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
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

    public void fetchMoreBy(int artist_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ArtistReleasesResponse> call = discogsAPI.fetchArtistReleases(artist_id, "year", "asc"
                , 9, 1);

        call.enqueue(new Callback<ArtistReleasesResponse>() {
            @Override
            public void onResponse(Call<ArtistReleasesResponse> call, Response<ArtistReleasesResponse> response) {
                Log.i("status", "response code " + response.code());
                if (response.body() != null) {
                    ArtistReleasesResponse artistResponse = response.body();
                    List<Release> releases = artistResponse.getReleases();
                    adapter = new MoreByAdapter(context, releases, MasterInfoActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArtistReleasesResponse> call, Throwable t) {
                Log.i("status", "response code " + t.getMessage());
            }
        });

    }

    public void updateView(MasterReleasesResponse masterResponse) {
        StringBuilder stringBuilder = null;
        try {
            styles = masterResponse.getStyles();

            stringBuilder = new StringBuilder();
            for (String style : styles) {
                stringBuilder.append(style);
                if (styles.indexOf(style) == styles.size() - 1) {
                    break;
                }
                stringBuilder.append(", ");
            }
            style.setText("Style: " + stringBuilder);
        } catch (NullPointerException e) {
            e.printStackTrace();
            style.setText("Style: " + "Freestyle");
            Log.i("damn", "damn");
        }
        stringBuilder.delete(0, stringBuilder.capacity());
        genres = masterResponse.getGenres();
        for (String genre : genres) {
            stringBuilder.append(genre);
            if (genres.indexOf(genre) == genres.size() - 1) {
                break;
            }
            stringBuilder.append(", ");
        }
        genre.setText("Genre: " + stringBuilder);
        title.setText("Title: " + masterResponse.getTitle());
        artist.setText("Artist: " + masterResponse.getArtists().get(0).getName());
        year.setText("Year: " + masterResponse.getYear());
    }

    public void fetchData() {
        DiscogsAPI discogsAPI = getDiscogsAPI();

        Call<MasterReleasesResponse> call = discogsAPI.fetchMasterData(master_id);

        call.enqueue(new Callback<MasterReleasesResponse>() {
            @Override
            public void onResponse(Call<MasterReleasesResponse> call, Response<MasterReleasesResponse> response) {
                Log.i("status", "response code " + response.code());
                if (response.body() != null) {
                    MasterReleasesResponse masterResponse = response.body();
                    int artist_id = masterResponse.getArtists().get(0).getId();
                    updateView(masterResponse);
                    fetchMoreBy(artist_id);
                    sliderAdapter = new SliderAdapter(context, masterResponse);
                    sliderView.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onFailure(Call<MasterReleasesResponse> call, Throwable t) {
                Log.i("status", "response code " + t.getMessage());
            }
        });


    }

    @Override
    public void onReleaseClick(int position, Release release) {
        Log.i("release", "Release position " + position + "release title " + release.getTitle());
    }
}

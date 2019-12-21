package com.naughtybitch.discogsclient.artist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.ArtistResponse;
import com.naughtybitch.POJO.Group;
import com.naughtybitch.POJO.Member;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.adapter.SliderAdapter;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.SearchableActivity;
import com.naughtybitch.discogsclient.buy.BuyMusicActivity;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.profile.ProfileActivity;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wishlist.WishlistActivity;
import com.naughtybitch.recyclerview.ArtistMGAdapter;
import com.naughtybitch.recyclerview.MoreByAdapter;
import com.naughtybitch.recyclerview.TracklistAdapter;
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


public class ArtistDetailsActivity extends AppCompatActivity implements
        View.OnClickListener, ArtistMGAdapter.OnArtistMemberListener,
        ArtistMGAdapter.OnArtistGroupListener {

    private Pagination pagination;
    private TextView show_all, view_all;
    private int artist_id;
    private List<Group> groups;
    private List<Member> members;
    private TextView title, artist, year, genre, style, total_length, released, formats, labels, tracklist;
    private SliderAdapter sliderAdapter;
    private TracklistAdapter tracklistAdapter;
    private SliderView sliderView;
    private MoreByAdapter morebyAdapter;
    private ArtistMGAdapter artistMGAdapter;
    private RecyclerView rc_moreby, rc_tracklist, rc_member;
    private LinearLayout details_holder, moreby_holder;
    private NestedScrollView nestedScrollView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar myToolbar;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_details);
        initView();

        try {
            artist_id = getIntent().getExtras().getInt("artist_id");
            if (artist_id != 0) {
                fetchArtist(artist_id);
                fetchArtistReleases(artist_id);
            }
            Log.i("artist_id", "artist id " + artist_id);
        } catch (NullPointerException e) {
            // Do smt
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void initView() {

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

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

        // Disable AppBar scrolling in coordinator layout
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(new AppBarLayout.Behavior());
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });

        rc_member = findViewById(R.id.rc_members);
        rc_member.setLayoutManager(new LinearLayoutManager(context));
        rc_member.setAdapter(artistMGAdapter);
        artistMGAdapter = new ArtistMGAdapter();
        moreby_holder = findViewById(R.id.more_by_holder);
        moreby_holder.setVisibility(View.INVISIBLE);
        details_holder = findViewById(R.id.details_holder);
        nestedScrollView = findViewById(R.id.master_container);
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        view_all = findViewById(R.id.more_by_all);
        tracklist = findViewById(R.id.tracklist);
        released = findViewById(R.id.master_released);
        formats = findViewById(R.id.master_formats);
        labels = findViewById(R.id.master_labels);
        show_all = findViewById(R.id.show_all);
        show_all.setOnClickListener(this);
        total_length = findViewById(R.id.tracklist_total_length);
        total_length.setVisibility(View.INVISIBLE);
        rc_tracklist = findViewById(R.id.rc_tracklist);
        tracklistAdapter = new TracklistAdapter();
        rc_tracklist.setAdapter(tracklistAdapter);
        rc_tracklist.setLayoutManager(new LinearLayoutManager(context));
        genre = findViewById(R.id.master_genre);
        style = findViewById(R.id.master_style);
        title = findViewById(R.id.master_title);
        artist = findViewById(R.id.master_artist);
        year = findViewById(R.id.master_year);
        rc_moreby = findViewById(R.id.rc_moreby);
        morebyAdapter = new MoreByAdapter();
        rc_moreby.setAdapter(morebyAdapter);
        rc_moreby.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        sliderView = findViewById(R.id.slider_view);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        if (v.getId() == R.id.show_all) {
            intent = new Intent(ArtistDetailsActivity.this, SearchableActivity.class);
            bundle.putInt("artist_id", artist_id);
            bundle.putString("view_all", "view_all");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void fetchArtistReleases(final int artist_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ArtistReleasesResponse> call = discogsAPI.fetchArtistReleases(artist_id, "year", "asc"
                , 10, 1);
        call.enqueue(new Callback<ArtistReleasesResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArtistReleasesResponse> call, Response<ArtistReleasesResponse> response) {
                Log.i("more_by", "response code " + response.code());
                if (response.body() != null) {
                    ArtistReleasesResponse artistReleasesResponse = response.body();
                    pagination = artistReleasesResponse.getPagination();
                    show_all.setText("Show all " + pagination.getItems() + " release");
                }
            }

            @Override
            public void onFailure(Call<ArtistReleasesResponse> call, Throwable t) {
                Log.i("show_all_artist", "response code " + t.getMessage());
            }
        });

    }

    private void updateToolbarTitle(final ArtistResponse response) {
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(response.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "NewApi"})
    private void updateArtistView(ArtistResponse artistResponse) {
        // title, artist, year, genre, style, total_lengths
        updateToolbarTitle(artistResponse);
        rc_member.setVisibility(View.VISIBLE);
        moreby_holder.setVisibility(View.GONE);
        artist.setVisibility(View.VISIBLE);
        total_length.setVisibility(View.GONE);
        details_holder.setVisibility(View.VISIBLE);
        nestedScrollView.setOnTouchListener(null);
        sliderView.setVisibility(View.VISIBLE);
        tracklist.setVisibility(View.VISIBLE);
        genre.setVisibility(View.VISIBLE);
        rc_moreby.setVisibility(View.GONE);
        rc_tracklist.setVisibility(View.GONE);
        show_all.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(artistResponse.getName());
        CharSequence profile = artistResponse.getProfile();
        genre.setText(profile);
        int type = 0;
        try {
            groups = artistResponse.getGroups();
            Log.i("groups", "Group " + groups.isEmpty());
            type = 1; // Member
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            members = artistResponse.getMembers();
            Log.i("members", "Member " + members.isEmpty());
            type = 2; // Group
        } catch (NullPointerException e) {
            // Do smt
        }
        if (type == 0) {
            artist.setText(artistResponse.getRealname());
            tracklist.setVisibility(View.GONE);
            rc_member.setVisibility(View.GONE);
            sliderAdapter = new SliderAdapter(context, artistResponse);
            sliderView.setSliderAdapter(sliderAdapter);
        } else if (type == 1) { // Member
            artist.setText(artistResponse.getRealname());
            tracklist.setText("Groups");
            artistMGAdapter = new ArtistMGAdapter(context, groups, this);
            rc_member.setAdapter(artistMGAdapter);
            sliderAdapter = new SliderAdapter(context, artistResponse);
            sliderView.setSliderAdapter(sliderAdapter);
        } else { // Group
            artist.setText("Group");
            tracklist.setText("Members");
            artistMGAdapter = new ArtistMGAdapter(context, members, this);
            rc_member.setAdapter(artistMGAdapter);
            sliderAdapter = new SliderAdapter(context, artistResponse);
            sliderView.setSliderAdapter(sliderAdapter);
        }

    }

    @Override
    public void onArtistMemberClick(int position, Member member) {
        Intent intent = new Intent(ArtistDetailsActivity.this, ArtistDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("artist_id", member.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onArtistGroupClick(int position, Group group) {
        Intent intent = new Intent(ArtistDetailsActivity.this, ArtistDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("artist_id", group.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void fetchArtist(int artist_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ArtistResponse> call = discogsAPI.fetchArtist(artist_id);
        call.enqueue(new Callback<ArtistResponse>() {
            @Override
            public void onResponse(Call<ArtistResponse> call, Response<ArtistResponse> response) {
                Log.i("artist", "response code " + response.code());
                if (response.body() != null) {
                    ArtistResponse artistResponse = response.body();
                    updateArtistView(artistResponse);
                }
            }

            @Override
            public void onFailure(Call<ArtistResponse> call, Throwable t) {
                Log.i("artist_response", "response code " + t.getMessage());
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
        return auth;
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
                        startActivity(new Intent(ArtistDetailsActivity.this, MainActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(ArtistDetailsActivity.this, ProfileActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(ArtistDetailsActivity.this, WishlistActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buy_music:
                        startActivity(new Intent(ArtistDetailsActivity.this, BuyMusicActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "BuyMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(ArtistDetailsActivity.this, SellMusicActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(ArtistDetailsActivity.this, SettingsActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(ArtistDetailsActivity.this, ExploreActivity.class));
                        Toast.makeText(ArtistDetailsActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ArtistDetailsActivity.this, ExploreActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }

}

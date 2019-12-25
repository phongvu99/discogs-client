package com.naughtybitch.discogsclient.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.MasterReleaseResponse;
import com.naughtybitch.POJO.MasterReleaseVersionsResponse;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.POJO.ReleaseResponse;
import com.naughtybitch.POJO.Tracklist;
import com.naughtybitch.POJO.Want;
import com.naughtybitch.adapter.SliderAdapter;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.SearchableActivity;
import com.naughtybitch.discogsclient.collection.CollectionActivity;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.profile.ProfileActivity;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wishlist.WishlistActivity;
import com.naughtybitch.recyclerview.MoreByAdapter;
import com.naughtybitch.recyclerview.TracklistAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MasterDetailsActivity extends AppCompatActivity implements
        MoreByAdapter.OnMoreByListener,
        View.OnClickListener {

    private TextView show_all, view_all;
    private int master_id, release_id, artist_id;
    private List<String> genres, styles;
    private List<Tracklist> tracklists;
    private TextView title, artist, year, genre, style, total_length, released, formats, labels, tracklist;
    private SliderAdapter sliderAdapter;
    private TracklistAdapter tracklistAdapter;
    private SliderView sliderView;
    private MoreByAdapter morebyAdapter;
    private RecyclerView rc_moreby, rc_tracklist, rc_members;
    private LinearLayout details_holder, moreby_holder;
    private NestedScrollView nestedScrollView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar myToolbar;
    private Context context = this;
    private SharedPreferences sp;
    private ImageView profile_menu_image;
    private TextView profile_menu_name, profile_menu_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_details);
        initView();
        try {
            master_id = getIntent().getExtras().getInt("master_id");
            if (master_id != 0) {
                fetchMaster();
                fetchMasterReleaseVersions(master_id);
            }
            Log.i("master_id", "master_id " + master_id);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            release_id = getIntent().getExtras().getInt("release_id");
            if (release_id != 0) {
                fetchRelease(release_id);
            }
            Log.i("release_id", "release_id " + release_id);
        } catch (NullPointerException e) {
            // Do smt
        }

        sp = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        String username = sp.getString("user_name", null);
        initView();
        if (username != null) {
            fetchProfile(username);
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
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        params.setBehavior(new AppBarLayout.Behavior());
//        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
//            @Override
//            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
//                return false;
//            }
//        });

        moreby_holder = findViewById(R.id.more_by_holder);
        moreby_holder.setVisibility(View.INVISIBLE);
        rc_members = findViewById(R.id.rc_members);
        details_holder = findViewById(R.id.details_holder);
        nestedScrollView = findViewById(R.id.master_container);
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        view_all = findViewById(R.id.view_all);
        view_all.setOnClickListener(this);
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
        navigationView = findViewById(R.id.navigation_view);
        View nav_header = navigationView.getHeaderView(0);
        profile_menu_email = nav_header.findViewById(R.id.profile_menu_email);
        profile_menu_name = nav_header.findViewById(R.id.profile_menu_name);
        profile_menu_image = nav_header.findViewById(R.id.profile_menu_image);
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

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.show_all:
                intent = new Intent(MasterDetailsActivity.this, SearchableActivity.class);
                bundle.putInt("master_id", master_id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.view_all:
                intent = new Intent(MasterDetailsActivity.this, SearchableActivity.class);
                bundle.putInt("artist_id", artist_id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


    public void updateToolbarTitle(final ReleaseResponse response) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(response.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void updateToolbarTitle(final MasterReleaseResponse response) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle(response.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void updateReleaseView(ReleaseResponse releaseResponse) {
        // title, artist, year, genre, style, total_lengths
        updateToolbarTitle(releaseResponse);
        moreby_holder.setVisibility(View.VISIBLE);
        rc_members.setVisibility(View.GONE);
        details_holder.setVisibility(View.VISIBLE);
        nestedScrollView.setOnTouchListener(null);
        sliderView.setVisibility(View.VISIBLE);
        total_length.setVisibility(View.VISIBLE);
        tracklist.setVisibility(View.VISIBLE);
        rc_tracklist.setVisibility(View.VISIBLE);
        show_all.setVisibility(View.GONE);
        genre.setVisibility(View.VISIBLE);
        released.setVisibility(View.VISIBLE);
        formats.setVisibility(View.VISIBLE);
        labels.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        artist.setVisibility(View.VISIBLE);
        year.setVisibility(View.VISIBLE);
        style.setVisibility(View.VISIBLE);
        if (releaseResponse.getReleased() == null) {
            released.setText("Released " + "Unknown");
        } else {
            released.setText("Released " + releaseResponse.getReleased());
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (String desc : releaseResponse.getFormats().get(0).getDescriptions()) {
                stringBuilder.append(desc);
                if (releaseResponse.getFormats().get(0).getDescriptions().indexOf(desc) ==
                        releaseResponse.getFormats().get(0).getDescriptions().size() - 1) {
                    break;
                }
                stringBuilder.append(", ");
            }
            if (releaseResponse.getFormatQuantity() != 0 && releaseResponse.getFormatQuantity() != 1) {
                formats.setText(releaseResponse.getFormatQuantity() + "x" +
                        releaseResponse.getFormats().get(0).getName() + ", " + stringBuilder);
            } else {
                formats.setText(releaseResponse.getFormats().get(0).getName() + ", " + stringBuilder);
            }
        } catch (NullPointerException e) {
            // Do smt
            formats.setVisibility(View.GONE);
        }
        labels.setText(releaseResponse.getLabels().get(0).getName() + " (#" +
                releaseResponse.getLabels().get(0).getCatno() + ")");
        title.setText("Title: " + releaseResponse.getTitle());
        artist.setText("Artist: " + releaseResponse.getArtists().get(0).getName());
        try {
            styles = releaseResponse.getStyles();
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
            style.setText("Style: " + "Freestyle");
        }
        stringBuilder = new StringBuilder();
        genres = releaseResponse.getGenres();
        for (String genre : genres) {
            stringBuilder.append(genre);
            if (genres.indexOf(genre) == genres.size() - 1) {
                break;
            }
            stringBuilder.append(", ");
        }
        genre.setText("Genre: " + stringBuilder);
        if (releaseResponse.getYear() != 0) {
            year.setText("Year: " + releaseResponse.getYear());
        } else {
            year.setText("Year: Unknown");
        }
        tracklists = releaseResponse.getTracklist();
        updateTotalLength(tracklists);
        tracklistAdapter = new TracklistAdapter(context, tracklists);
        rc_tracklist.setAdapter(tracklistAdapter);
        sliderAdapter = new SliderAdapter(context, releaseResponse);
        sliderView.setSliderAdapter(sliderAdapter);
    }

    private void fetchRelease(int release_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ReleaseResponse> call = discogsAPI.fetchRelease(release_id);
        call.enqueue(new Callback<ReleaseResponse>() {
            @Override
            public void onResponse(Call<ReleaseResponse> call, Response<ReleaseResponse> response) {
                if (response.body() != null) {
                    ReleaseResponse releaseResponse = response.body();
                    artist_id = releaseResponse.getArtists().get(0).getId();
                    fetchArtistReleases(artist_id);
                    updateReleaseView(releaseResponse);
                }
            }

            @Override
            public void onFailure(Call<ReleaseResponse> call, Throwable t) {

            }
        });
    }

    private void fetchMasterReleaseVersions(int master_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<MasterReleaseVersionsResponse> call = discogsAPI.fetchMasterReleaseVersions(master_id, 1, 1);
        call.enqueue(new Callback<MasterReleaseVersionsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MasterReleaseVersionsResponse> call, Response<MasterReleaseVersionsResponse> response) {
                if (response.body() != null) {
                    MasterReleaseVersionsResponse masterReleaseVersionsResponse = response.body();
                    Pagination pagination = masterReleaseVersionsResponse.getPagination();
                    Log.i("response_show_all", "response " + response.code());
                    show_all.setText("Show all " + pagination.getItems() + " versions");
                }
            }

            @Override
            public void onFailure(Call<MasterReleaseVersionsResponse> call, Throwable t) {

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

    private void fetchArtistReleases(final int artist_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ArtistReleasesResponse> call = discogsAPI.fetchArtistReleases(artist_id, "year", "asc"
                , 10, 1);
        call.enqueue(new Callback<ArtistReleasesResponse>() {
            @Override
            public void onResponse(Call<ArtistReleasesResponse> call, Response<ArtistReleasesResponse> response) {
                Log.i("more_by", "response code " + response.code());
                if (response.body() != null) {
                    ArtistReleasesResponse artistResponse = response.body();
                    List<Release> releases = artistResponse.getReleases();
                    morebyAdapter = new MoreByAdapter(MasterDetailsActivity.this, context, releases);
                    rc_moreby.setAdapter(morebyAdapter);
                } else {
                    TextView empty = findViewById(R.id.card_empty);
                    empty.setVisibility(View.VISIBLE);
                    view_all.setVisibility(View.GONE);
                    rc_moreby.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArtistReleasesResponse> call, Throwable t) {
                Log.i("more_by_response", "response code " + t.getMessage());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void updateTotalLength(List<Tracklist> tracklists) {
        String[] data;
        int length_seconds = 0;
        int secs, mins;
        ArrayList<Integer> seconds = new ArrayList<>();
        Pattern pattern = Pattern.compile(Pattern.quote(":"));
        try {
            for (Tracklist tracklist : tracklists) {
                if (tracklist.getType().equals("track") && !tracklist.getDuration().equals("")) {
                    data = pattern.split(tracklist.getDuration());
                    seconds.add(Integer.parseInt(data[0]) * 60);
                    seconds.add(Integer.parseInt(data[1]));
                }
            }
        } catch (NumberFormatException e) {
            total_length.setVisibility(View.GONE);
            return;
        }
        for (int second : seconds) {
            length_seconds += second;
        }
        if (seconds.isEmpty()) {
            total_length.setVisibility(View.GONE);
            return;
        }
        mins = length_seconds / 60;
        secs = length_seconds % 60;
        if (Integer.toString(secs).length() == 1) {
            total_length.setText("Total length " + "~ " + mins + ":" + "0" + secs);
        } else {
            total_length.setText("Total length " + "~ " + mins + ":" + secs);
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void updateMasterView(MasterReleaseResponse masterResponse) {
        updateToolbarTitle(masterResponse);
        moreby_holder.setVisibility(View.VISIBLE);
        rc_members.setVisibility(View.GONE);
        details_holder.setVisibility(View.VISIBLE);
        nestedScrollView.setOnTouchListener(null);
        sliderView.setVisibility(View.VISIBLE);
        total_length.setVisibility(View.VISIBLE);
        genre.setVisibility(View.VISIBLE);
        tracklist.setVisibility(View.VISIBLE);
        rc_tracklist.setVisibility(View.VISIBLE);
        show_all.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        artist.setVisibility(View.VISIBLE);
        year.setVisibility(View.VISIBLE);
        style.setVisibility(View.VISIBLE);
        updateTotalLength(masterResponse.getTracklist());
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
            style.setText("Style: " + "Freestyle");
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
        if (masterResponse.getYear() != 0) {
            year.setText("Year: " + masterResponse.getYear());
        } else {
            year.setText("Year: Unknown");
        }

    }

    private void fetchMaster() {
        DiscogsAPI discogsAPI = getDiscogsAPI();

        Call<MasterReleaseResponse> call = discogsAPI.fetchMasterData(master_id);

        call.enqueue(new Callback<MasterReleaseResponse>() {
            @Override
            public void onResponse(Call<MasterReleaseResponse> call, Response<MasterReleaseResponse> response) {
                Log.i("fetch_data", "response code " + response.code());
                if (response.body() != null) {
                    MasterReleaseResponse masterResponse = response.body();
                    tracklists = masterResponse.getTracklist();
                    artist_id = masterResponse.getArtists().get(0).getId();
                    updateMasterView(masterResponse);
                    fetchArtistReleases(artist_id);
                    tracklistAdapter = new TracklistAdapter(context, tracklists);
                    rc_tracklist.setAdapter(tracklistAdapter);
                    sliderAdapter = new SliderAdapter(context, masterResponse);
                    sliderView.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onFailure(Call<MasterReleaseResponse> call, Throwable t) {
                Log.i("fetch_data", "response code " + t.getMessage());
            }
        });


    }

    @Override
    public void onReleaseClick(int position, Release release) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (release.getType()) {
            case "master":
                intent = new Intent(MasterDetailsActivity.this, MasterDetailsActivity.class);
                bundle.putInt("master_id", release.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "release":
                intent = new Intent(MasterDetailsActivity.this, MasterDetailsActivity.class);
                bundle.putInt("release_id", release.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        Log.i("release", "Release position " + position + " release title " + release.getTitle());
    }

    @Override
    public void onReleaseClick(int position, Want want) {

    }

    private void navigationViewHandler() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(MasterDetailsActivity.this, MainActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(MasterDetailsActivity.this, ProfileActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.collection:
                        startActivity(new Intent(MasterDetailsActivity.this, CollectionActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(MasterDetailsActivity.this, WishlistActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(MasterDetailsActivity.this, SellMusicActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(MasterDetailsActivity.this, SettingsActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(MasterDetailsActivity.this, ExploreActivity.class));
                        Toast.makeText(MasterDetailsActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(MasterDetailsActivity.this, ExploreActivity.class);
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

}

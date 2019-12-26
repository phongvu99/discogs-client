package com.naughtybitch.discogsclient.label;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.GsonBuilder;
import com.naughtybitch.POJO.LabelReleasesResponse;
import com.naughtybitch.POJO.LabelResponse;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.ParentLabel;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.POJO.Sublabel;
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
import com.naughtybitch.discogsclient.wantlist.WantlistActivity;
import com.naughtybitch.recyclerview.ArtistMGAdapter;
import com.naughtybitch.recyclerview.LabelCompaniesAdapter;
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

public class LabelDetailsActivity extends AppCompatActivity implements LabelCompaniesAdapter.OnParentLabelListener,
        LabelCompaniesAdapter.OnSublabelListener, View.OnClickListener {

    private DiscogsAPI discogsAPI;
    private Pagination pagination;
    private TextView show_all, view_all;
    private int label_id;
    private List<Sublabel> sublabels;
    private ParentLabel parentLabel;
    private TextView title, artist, year, genre, style, total_length, released, formats, labels, tracklist, label_title, label_status;
    private SliderAdapter sliderAdapter;
    private TracklistAdapter tracklistAdapter;
    private LabelCompaniesAdapter labelCompaniesAdapter;
    private SliderView sliderView;
    private MoreByAdapter morebyAdapter;
    private ArtistMGAdapter artistMGAdapter;
    private RecyclerView rc_moreby, rc_tracklist, rc_member;
    private LinearLayout details_holder, moreby_holder;
    private FrameLayout parent_label;
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
    private TextView profile_menu_name, profile_menu_email, empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_details);

        sp = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        String username = sp.getString("user_name", null);
        initView();
        if (username != null) {
            fetchProfile(username);
        }

        try {
            label_id = getIntent().getExtras().getInt("label_id");
            if (label_id != 0) {
                fetchLabel(label_id);
                fetchLabelReleases(label_id);
            }
            Log.i("label_id", "label id " + label_id);
        } catch (NullPointerException e) {
            // Do smt
        }

    }

    @Override
    public void onParentLabelClick(int position, ParentLabel parentLabel) {
        Intent intent = new Intent(LabelDetailsActivity.this, LabelDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("label_id", parentLabel.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSublabelClick(int position, Sublabel sublabel) {
        Intent intent = new Intent(LabelDetailsActivity.this, LabelDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("label_id", sublabel.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void updateLabelView(LabelResponse labelResponse) {

        updateToolbarTitle(labelResponse);
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
        title.setText(labelResponse.getName());
        CharSequence profile = labelResponse.getProfile();
        genre.setText(profile);
        artist.setText("Label");
        tracklist.setText("Companies");
        try { // Parent label
            sublabels = labelResponse.getSublabels();
            // adapter here
            labelCompaniesAdapter = new LabelCompaniesAdapter(sublabels, this);
            rc_member.setAdapter(labelCompaniesAdapter);
            sliderAdapter = new SliderAdapter(context, labelResponse);
            sliderView.setSliderAdapter(sliderAdapter);
            try {
                parentLabel = labelResponse.getParentLabel();
                parent_label.setVisibility(View.VISIBLE);
                parent_label.setOnClickListener(this);
                label_title.setText(parentLabel.getName());
                label_status.setText(R.string.label_status2);
            } catch (NullPointerException e) {
                // Do smt
                tracklist.setVisibility(View.GONE);
                parent_label.setVisibility(View.GONE);
                rc_member.setVisibility(View.GONE);
                return;
            }
            return;
        } catch (NullPointerException e) {
            // Do smt
        }
        try { // Sublabel
            parentLabel = labelResponse.getParentLabel();
            // adapter here
            labelCompaniesAdapter = new LabelCompaniesAdapter(parentLabel, this);
            rc_member.setAdapter(labelCompaniesAdapter);
            sliderAdapter = new SliderAdapter(context, labelResponse);
            sliderView.setSliderAdapter(sliderAdapter);
        } catch (NullPointerException e) {
            // Do smt
            tracklist.setVisibility(View.GONE);
            parent_label.setVisibility(View.GONE);
            rc_member.setVisibility(View.GONE);
        }
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

    private void fetchLabelReleases(int label_id) {
        Call<LabelReleasesResponse> call = discogsAPI.fetchLabelReleases(label_id, 1, 1);
        call.enqueue(new Callback<LabelReleasesResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<LabelReleasesResponse> call, Response<LabelReleasesResponse> response) {
                Log.i("label releases", "response code " + response.code());
                if (response.body() != null) {
                    LabelReleasesResponse labelReleasesResponse = response.body();
                    pagination = labelReleasesResponse.getPagination();
                    show_all.setText("Show all " + pagination.getItems() + " releases");
                }
            }

            @Override
            public void onFailure(Call<LabelReleasesResponse> call, Throwable t) {
                Log.i("label releases", "response code " + t.getMessage());
            }
        });

    }
    private void fetchLabel(int label_id) {
        discogsAPI = getDiscogsAPI();
        Call<LabelResponse> call = discogsAPI.fetchLabel(label_id);
        call.enqueue(new Callback<LabelResponse>() {
            @Override
            public void onResponse(Call<LabelResponse> call, Response<LabelResponse> response) {
                Log.i("label", "response code " + response.code());
                Log.i("label response", "response " + new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if (response.body() != null) {
                    LabelResponse labelResponse = response.body();
                    updateLabelView(labelResponse);
                    try {
                        parentLabel = labelResponse.getParentLabel();
                        parentLabel = labelResponse.getParentLabel();
                    } catch (NullPointerException e) {
                        // Do smt
                    }
                }
            }

            @Override
            public void onFailure(Call<LabelResponse> call, Throwable t) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initView() {
        navigationView = findViewById(R.id.navigation_view);
        View nav_header = navigationView.getHeaderView(0);
        profile_menu_email = nav_header.findViewById(R.id.profile_menu_email);
        profile_menu_name = nav_header.findViewById(R.id.profile_menu_name);
        profile_menu_image = nav_header.findViewById(R.id.profile_menu_image);

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

        parent_label = findViewById(R.id.parent_label_container);
        label_title = findViewById(R.id.label_title);
        label_status = findViewById(R.id.label_status);
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
        view_all = findViewById(R.id.view_all);
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

    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.show_all:
                intent = new Intent(LabelDetailsActivity.this, SearchableActivity.class);
                bundle.putInt("label_id", label_id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.parent_label_container:
                intent = new Intent(LabelDetailsActivity.this, LabelDetailsActivity.class);
                bundle.putInt("label_id", parentLabel.getId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void updateToolbarTitle(final LabelResponse response) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(response.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
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
                        startActivity(new Intent(LabelDetailsActivity.this, MainActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(LabelDetailsActivity.this, ProfileActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.collection:
                        startActivity(new Intent(LabelDetailsActivity.this, CollectionActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(LabelDetailsActivity.this, WantlistActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(LabelDetailsActivity.this, SellMusicActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(LabelDetailsActivity.this, SettingsActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(LabelDetailsActivity.this, ExploreActivity.class));
                        Toast.makeText(LabelDetailsActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(LabelDetailsActivity.this, ExploreActivity.class);
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

package com.naughtybitch.label;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.google.gson.GsonBuilder;
import com.naughtybitch.POJO.Group;
import com.naughtybitch.POJO.LabelResponse;
import com.naughtybitch.POJO.Member;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.ParentLabel;
import com.naughtybitch.POJO.Sublabel;
import com.naughtybitch.adapter.SliderAdapter;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.SearchableActivity;
import com.naughtybitch.discogsclient.artist.ArtistDetailsActivity;
import com.naughtybitch.recyclerview.ArtistMGAdapter;
import com.naughtybitch.recyclerview.LabelCompaniesAdapter;
import com.naughtybitch.recyclerview.MoreByAdapter;
import com.naughtybitch.recyclerview.TracklistAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabelDetailsActivity extends ArtistDetailsActivity implements LabelCompaniesAdapter.OnParentLabelListener,
        LabelCompaniesAdapter.OnSublabelListener {

    private Pagination pagination;
    private TextView show_all, view_all;
    private int label_id;
    private List<Group> groups;
    private List<Member> members;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_details);
        initView();

        try {
            label_id = getIntent().getExtras().getInt("label_id");
            if (label_id != 0) {
                fetchLabel(label_id);
            }
            Log.i("label_id", "label id " + label_id);
        } catch (NullPointerException e) {
            // Do smt
        }

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
        show_all.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        title.setText(labelResponse.getName());
        CharSequence profile = labelResponse.getProfile();
        genre.setText(profile);
        try { // Parent label
            sublabels = labelResponse.getSublabels();
            Log.i("sublabel", "sublabel size" + sublabels.size());
            artist.setText("Label");
            tracklist.setText("Companies");
            // adapter here
            labelCompaniesAdapter = new LabelCompaniesAdapter(sublabels, this);
            rc_member.setAdapter(labelCompaniesAdapter);
            sliderAdapter = new SliderAdapter(context, labelResponse);
            sliderView.setSliderAdapter(sliderAdapter);
            try {
                parentLabel = labelResponse.getParentLabel();
                parent_label.setVisibility(View.VISIBLE);
                label_title.setText(parentLabel.getName());
                label_status.setText(R.string.label_status2);

                Log.i("how2", "updateLabelView: ");
            } catch (NullPointerException e) {
                // Do smt
                tracklist.setVisibility(View.GONE);
                parent_label.setVisibility(View.GONE);
                rc_member.setVisibility(View.GONE);
            }
            return;
        } catch (NullPointerException e) {
            // Do smt
        }
        try { // Sublabel
            parentLabel = labelResponse.getParentLabel();
            artist.setText("Label");
            tracklist.setText("Companies");
            // adapter here
            labelCompaniesAdapter = new LabelCompaniesAdapter(parentLabel, this);
            rc_member.setAdapter(labelCompaniesAdapter);
            sliderAdapter = new SliderAdapter(context, labelResponse);
            sliderView.setSliderAdapter(sliderAdapter);
        } catch (NullPointerException e) {
            // Do smt
            Log.i("sublabel", "sublabel size" + parentLabel.getName());
            tracklist.setVisibility(View.GONE);
            parent_label.setVisibility(View.GONE);
            rc_member.setVisibility(View.GONE);
        }
    }

    @Override
    public void onParentLabelClick(int position, ParentLabel parentLabel) {

    }

    @Override
    public void onSublabelClick(int position, Sublabel sublabel) {

    }

    private void fetchLabel(int label_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<LabelResponse> call = discogsAPI.fetchLabel(label_id);
        call.enqueue(new Callback<LabelResponse>() {
            @Override
            public void onResponse(Call<LabelResponse> call, Response<LabelResponse> response) {
                Log.i("label", "response code " + response.code());
                Log.i("label response", "response " + new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if (response.body() != null) {
                    LabelResponse labelResponse = response.body();
                    updateLabelView(labelResponse);
                }
            }

            @Override
            public void onFailure(Call<LabelResponse> call, Throwable t) {

            }
        });
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
            intent = new Intent(LabelDetailsActivity.this, SearchableActivity.class);
            bundle.putInt("label_id", label_id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void updateToolbarTitle(final LabelResponse response) {
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

    @Override
    public DiscogsAPI getDiscogsAPI() {
        return super.getDiscogsAPI();
    }

    @Override
    public String getCredentials() {
        return super.getCredentials();
    }

    @Override
    public void navigationViewHandler() {
        super.navigationViewHandler();
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

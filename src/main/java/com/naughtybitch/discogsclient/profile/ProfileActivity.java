package com.naughtybitch.discogsclient.profile;

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
import android.widget.TextView;

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

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.sell.OrderFragment;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wantlist.WantlistActivity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnFragmentInteractionListener,
        OrderFragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private SharedPreferences sp;
    private ImageView profile_menu_image;
    private TextView profile_menu_name, profile_menu_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Custom ActionBar
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Profile");
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

        // Create a new Fragment to be placed in the activity layout
        ProfileFragment firstFragment = ProfileFragment.newInstance();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();

        navigationViewHandler();

        sp = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        String username = sp.getString("user_name", null);
        initView();
        if (username != null) {
            fetchProfileMenu(username);
        }
    }

    private void initView() {
        navigationView = findViewById(R.id.navigation_view);
        View nav_header = navigationView.getHeaderView(0);
        profile_menu_email = nav_header.findViewById(R.id.profile_menu_email);
        profile_menu_name = nav_header.findViewById(R.id.profile_menu_name);
        profile_menu_image = nav_header.findViewById(R.id.profile_menu_image);
    }

    private void updateProfileMenu(ProfileResponse profileResponse) {
        Glide.with(this)
                .load(profileResponse.getAvatarUrl())
                .error(R.drawable.discogs_vinyl_record_mark)
                .placeholder(R.drawable.discogs_vinyl_record_mark)
                .into(profile_menu_image);
        profile_menu_name.setText(profileResponse.getName());
        profile_menu_email.setText(profileResponse.getEmail());
    }

    private void fetchProfileMenu(String username) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ProfileResponse> call = discogsAPI.fetchProfile(username);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    updateProfileMenu(profileResponse);
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
        navigationView.setCheckedItem(R.id.profile);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        break;
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(ProfileActivity.this, WantlistActivity.class));
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(ProfileActivity.this, SellMusicActivity.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                        break;
                    case R.id.explore:
                        startActivity(new Intent(ProfileActivity.this, ExploreActivity.class));
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

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
            case R.id.search:
                Intent intent = new Intent(ProfileActivity.this, ExploreActivity.class);
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
}

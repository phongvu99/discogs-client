package com.naughtybitch.discogsclient.explore;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.View;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.SearchableActivity;
import com.naughtybitch.discogsclient.buy.BuyMusicActivity;
import com.naughtybitch.discogsclient.profile.ProfileActivity;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wishlist.WishlistActivity;

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

public class ExploreActivity extends AppCompatActivity implements
        ExploreFragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private MenuItem searchItem;
    private SharedPreferences sp;
    private ImageView profile_menu_image;
    private TextView profile_menu_name, profile_menu_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Custom ActionBar
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
        }

        // Create a new Fragment to be placed in the activity layout
        ExploreFragment firstFragment = ExploreFragment.newInstance();

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
            fetchProfile(username);
        }

    }


    private void initView() {
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                navigateToFragment(ExploreFragment.newInstance());
                break;
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.getCheckedItem().setCheckable(false);
        }
        super.onStop();
    }

    public void navigationViewHandler() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.explore);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent intent_main = new Intent(ExploreActivity.this, MainActivity.class);
                        Toast.makeText(ExploreActivity.this, "MainActivity", Toast.LENGTH_SHORT).show();
                        startActivity(intent_main);
                        break;
                    case R.id.profile:
                        startActivity(new Intent(ExploreActivity.this, ProfileActivity.class));
                        Toast.makeText(ExploreActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(ExploreActivity.this, WishlistActivity.class));
                        Toast.makeText(ExploreActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.buy_music:
                        startActivity(new Intent(ExploreActivity.this, BuyMusicActivity.class));
                        Toast.makeText(ExploreActivity.this, "BuyMusicFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(ExploreActivity.this, SellMusicActivity.class));
                        Toast.makeText(ExploreActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(ExploreActivity.this, SettingsActivity.class));
                        Toast.makeText(ExploreActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_search, menu);
        searchItem = menu.findItem(R.id.search);
        searchItem.expandActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setQueryHint(getResources().getText(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Intent intent = new Intent(ExploreActivity.this, SearchableActivity.class);
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            this.drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do stuff
    }

}

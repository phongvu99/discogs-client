package com.naughtybitch.discogsclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class WishlistActivity extends AppCompatActivity implements
        WishlistFragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        WishlistFragment firstfragment = WishlistFragment.newInstance();
        firstfragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstfragment).commit();
        navigationViewHandler();
    }

    public void navigationViewHandler() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setCheckedItem(R.id.wish_list);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(WishlistActivity.this, MainActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(WishlistActivity.this, ProfileActivity.class));
                        Toast.makeText(WishlistActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.wish_list:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.buy_music:
                        startActivity(new Intent(WishlistActivity.this, BuyMusicActivity.class));
                        Toast.makeText(WishlistActivity.this, "BuyMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(WishlistActivity.this, SellMusicActivity.class));
                        Toast.makeText(WishlistActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(WishlistActivity.this, SettingsActivity.class));
                        Toast.makeText(WishlistActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(WishlistActivity.this, ExploreActivity.class));
                        Toast.makeText(WishlistActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
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
            case R.id.search:
                Intent intent = new Intent(WishlistActivity.this, ExploreActivity.class);
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

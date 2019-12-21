package com.naughtybitch.discogsclient.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.android.material.navigation.NavigationView;
import com.naughtybitch.discogsclient.MainActivity;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.buy.BuyMusicActivity;
import com.naughtybitch.discogsclient.explore.ExploreActivity;
import com.naughtybitch.discogsclient.sell.OrderFragment;
import com.naughtybitch.discogsclient.sell.SellMusicActivity;
import com.naughtybitch.discogsclient.settings.SettingsActivity;
import com.naughtybitch.discogsclient.wishlist.WishlistActivity;

public class ProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnFragmentInteractionListener,
        OrderFragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        // Create a new Fragment to be placed in the activity layout
        ProfileFragment firstFragment = ProfileFragment.newInstance();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();

        navigationViewHandler();
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
                        startActivity(new Intent(ProfileActivity.this, WishlistActivity.class));
                        Toast.makeText(ProfileActivity.this, "ProfileFragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.buy_music:
                        startActivity(new Intent(ProfileActivity.this, BuyMusicActivity.class));
                        Toast.makeText(ProfileActivity.this, "BuyMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sell_music:
                        startActivity(new Intent(ProfileActivity.this, SellMusicActivity.class));
                        Toast.makeText(ProfileActivity.this, "SellMusicActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                        Toast.makeText(ProfileActivity.this, "SettingsActivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        startActivity(new Intent(ProfileActivity.this, ExploreActivity.class));
                        Toast.makeText(ProfileActivity.this, "ExploreActivity", Toast.LENGTH_SHORT).show();
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

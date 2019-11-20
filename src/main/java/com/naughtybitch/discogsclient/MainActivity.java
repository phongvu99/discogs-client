package com.naughtybitch.discogsclient;

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

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AccountFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, CartFragment.OnFragmentInteractionListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private MenuItem searchItem;

    // Declare Variables
    ListView list;
    AdapterClass adapter;
    SearchView editSearch;
    String[] searchQueries;
    ArrayList<SearchQuery> arrayList = new ArrayList<SearchQuery>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        searchQueries = new String[]{"TextView", "ListView", "SearchView",
                "RatingBar", "ToolBar", "Button", "EditText", "ToggleButton",
                "ImageView", "SlidingDrawer", "Android"};

        list = (ListView) findViewById(R.id.list_view);
        for (String searchQuery : searchQueries) {
            SearchQuery searchQuery1 = new SearchQuery(searchQuery);
            // Binds all strings into an array
            arrayList.add(searchQuery1);
        }
        adapter = new AdapterClass(this, arrayList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                drawerLayout.closeDrawers();
                switch (id) {
                    case R.id.account:
                        navigateToFragment(AccountFragment.newInstance());
                        Toast.makeText(MainActivity.this, "AccountFragment", Toast.LENGTH_SHORT).show();
                    case R.id.settings:
                        navigateToFragment(SettingsFragment.newInstance());
                        Toast.makeText(MainActivity.this, "SettingsFragment", Toast.LENGTH_SHORT).show();
                    case R.id.cart:
                        navigateToFragment(CartFragment.newInstance());
                        Toast.makeText(MainActivity.this, "CartFragment", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onAccountFragmentInteraction(Uri uri) {
        // Do stuff
    }

    @Override
    public void onSettingsFragmentInteraction(Uri uri) {
        // Do different stuff
    }

    @Override
    public void onCartFragmentInteraction(Uri uri) {
        // Do something
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            this.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchItem = menu.findItem(R.id.search_bar);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getText(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        return true;
    }
}


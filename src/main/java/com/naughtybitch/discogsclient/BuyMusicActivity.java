package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BuyMusicActivity extends AppCompatActivity implements View.OnClickListener,
        CartFragment.OnFragmentInteractionListener,
        DWFragment.OnFragmentInteractionListener,
        PurchasesFragment.OnFragmentInteractionListener,
        OIMFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_music);
        buttonOnClickListener();

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
        HomeFragment firstFragment = HomeFragment.newInstance();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do smt
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void buttonOnClickListener() {
        Button btn_cart = findViewById(R.id.cart);
        Button btn_purch = findViewById(R.id.purchases);
        Button btn_dw = findViewById(R.id.detailed_wishlist);
        Button btn_oim = findViewById(R.id.offer_i_made);
        btn_cart.setOnClickListener(this);
        btn_purch.setOnClickListener(this);
        btn_dw.setOnClickListener(this);
        btn_oim.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart:
                Toast.makeText(this, "????????", Toast.LENGTH_SHORT).show();
                navigateToFragment(CartFragment.newInstance());
                break;
            case R.id.purchases:
                navigateToFragment(PurchasesFragment.newInstance());
                break;
            case R.id.offer_i_made:
                navigateToFragment(OIMFragment.newInstance());
                break;
            case R.id.detailed_wishlist:
                navigateToFragment(DWFragment.newInstance());
                break;
        }
    }
}

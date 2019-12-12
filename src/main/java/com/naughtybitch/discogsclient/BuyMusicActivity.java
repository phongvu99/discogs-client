package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BuyMusicActivity extends AppCompatActivity implements
        BuyMusicFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        DWFragment.OnFragmentInteractionListener,
        PurchasesFragment.OnFragmentInteractionListener,
        OIMFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_music);

        // Create a new Fragment to be placed in the activity layout
        BuyMusicFragment firstFragment = BuyMusicFragment.newInstance();

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
}

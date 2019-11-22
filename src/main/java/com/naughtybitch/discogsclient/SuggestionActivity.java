package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class SuggestionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_suggestion);
        PagerAdapter adapter = new HomeFragmentPagerAdapter(
                getSupportFragmentManager());
        AlbumImagesAdapter adapter1 = new AlbumImagesAdapter(this);
        ViewPager pager1 = (ViewPager) findViewById(R.id.pager1);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager1.setOffscreenPageLimit(5);
        pager1.setAdapter(adapter1);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
    }

}

package com.naughtybitch.discogsclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


public class AlbumInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_album_info);
        PagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        ImageAdapter adapter1 = new ImageAdapter(this);
        ViewPager pager1 = (ViewPager) findViewById(R.id.pager1);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager1.setOffscreenPageLimit(5);
        pager1.setAdapter(adapter1);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
    }

}

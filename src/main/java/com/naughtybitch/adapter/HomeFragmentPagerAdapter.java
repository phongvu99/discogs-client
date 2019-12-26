package com.naughtybitch.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.naughtybitch.discogsclient.chart.TopArtistsFragment;
import com.naughtybitch.discogsclient.chart.TopTracksFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 2;
    private String titles[] = new String[]{"Artists Chart", "Tracks Chart"};

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT; // number of pages for a ViewPager
    }

    @Override
    public Fragment getItem(int page) {
// returns an instance of Fragment corresponding to the specified page
        switch (page) {
            case 0:
                return TopArtistsFragment.newInstance();
            case 1:
                return TopTracksFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int page) {
// returns a tab title corresponding to the specified page
        return titles[page];
    }
}
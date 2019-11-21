package com.naughtybitch.discogsclient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT; // number of pages for a ViewPager
    }

    @Override
    public Fragment getItem(int page) {
        switch (page) {
            case 0:
                return SuggestionsFragment.newInstance("image0", "text0");
            case 1:
                return SuggestionsFragment.newInstance("image1", "text1");
            case 2:
                return SuggestionsFragment.newInstance("image2", "text2");
            default:
                return null;
        }
    }

}


package vn.edu.usth.mobileapplicationdevelopment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String titles[] = new String[]{"Hanoi", "Paris", "Toulouse"};

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
            case 0: return VPContentFragment.newInstance("image0", "text0");
            case 1: return VPContentFragment.newInstance("image1", "text1");
            case 2: return VPContentFragment.newInstance("image2", "text2");
        }
        VPContentFragment EmptyFragment;
        return EmptyFragment = new VPContentFragment();
    }

    @Override
    public CharSequence getPageTitle(int page) {
// returns a tab title corresponding to the specified page
        return titles[page];
    }
}

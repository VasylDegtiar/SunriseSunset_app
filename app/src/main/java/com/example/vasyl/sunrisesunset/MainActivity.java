package com.example.vasyl.sunrisesunset;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.vasyl.sunrisesunset.fragments.CurrentLocationFragment;
import com.example.vasyl.sunrisesunset.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPager();
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager() {
        mViewPager = findViewById(R.id.viewpager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(fragmentManager);
        mViewPager.setAdapter(adapter);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        private final int CURRENT_LOCATION_POSITION = 0;
        private final int SEARCH_POSITION = 1;
        private final int NUMBER_OF_FRAGMENTS = 2;

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case CURRENT_LOCATION_POSITION:
                    return CurrentLocationFragment.newInstance();
                case SEARCH_POSITION:
                    return SearchFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_FRAGMENTS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case CURRENT_LOCATION_POSITION:
                    return getString(R.string.current_position);
                case SEARCH_POSITION:
                    return getString(R.string.search);
                default:
                    return null;
            }

        }
    }
}





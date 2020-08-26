package com.example.cluster;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherViewAdapter extends FragmentPagerAdapter {
    //Number of tabs
    private int numOfTabs;

    /**
     * Constructor for tabbed activity
     * @param fm Fragment manger
     * @param numOfTabs Number of tabs
     */
    public TeacherViewAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    /**
     * Select fragment based on position
     * @param position for the tab
     * @return Fragment to post
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PromptFragment();
            case 1:
                return new ResponsesFragment();
            default:
                return null;
        }

    }

    /**
     * Gives the number of tabs
     * @return number of tabs
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }

}

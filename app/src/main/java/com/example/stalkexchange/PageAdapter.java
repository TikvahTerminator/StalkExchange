package com.example.stalkexchange;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Chose to use FragmentStatePagerAdapter over FragmentPagerAdapter because I wanted to ensure memory was being used as efficiently as possible. This class extends
 * FragmentStatePagerAdapter to manage the ViewPager. This allows for a Tab layout to be set up, making the GUI much cleaner and easier to use.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    //Variables
    private int numtabs;
    private ArrayList<tPrice> t;
    PageAdapter(FragmentManager fm, int num) {
        super(fm);
        this.numtabs = num;
    }

    /**
     * Overridden getItem method from the FragmentStatePagerAdapter class to create the specific fragments related to the specific tab.
     * @param position Position of the tab within the TabLayout
     * @return new fragment or null depending on switch case.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new listTab();
            case 1:
                return new dodoTab();
            case 2:
                return new userTab();
            case 3:
                return new creditTab();
            default:
                return null;
        }
    }

    /**
     * Overridden getCount method to ensure the correct amount of tabs are returned.
     * @return Number of tabs the tablayout possesses.
     */
    @Override
    public int getCount() {
        return numtabs;
    }

}

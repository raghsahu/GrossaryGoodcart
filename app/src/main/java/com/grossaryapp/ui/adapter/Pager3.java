package com.grossaryapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.grossaryapp.ui.activity.Allnotification;
import com.grossaryapp.ui.activity.Nouse_other.Offers;

/**
 * Created by Raghvendra Sahu on 29/10/2019.
 */
public class Pager3 extends FragmentStatePagerAdapter {
    int tabCount;
    /*public Pager3(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= this.tabCount;
    }
*/
    public Pager3(FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.tabCount=tabCount;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               Allnotification tab1 = new Allnotification();
                return tab1;
            case 1:
               Offers tab2 = new  Offers();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

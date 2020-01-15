package com.grossaryapp.ui.activity.Nouse_other;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.grossaryapp.ui.activity.Nouse_other.Filtertab;

/**
 * Created by Raghvendra Sahu on 30/10/2019.
 */
public class Pager4 extends FragmentStatePagerAdapter {
    int tabCount;
    public Pager4(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               Filtertab tab1 = new Filtertab();
                return tab1;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

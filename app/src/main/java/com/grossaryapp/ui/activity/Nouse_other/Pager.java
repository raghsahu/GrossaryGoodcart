package com.grossaryapp.ui.activity.Nouse_other;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.grossaryapp.ui.activity.Nouse_other.Tab1Activity;
import com.grossaryapp.ui.activity.Nouse_other.Tab2Activity;
import com.grossaryapp.ui.activity.Nouse_other.Tab3Activity;

/**
 * Created by Raghvendra Sahu on 23/10/2019.
 */
public class Pager extends FragmentStatePagerAdapter {
    int tabCount;
    public Pager( FragmentManager fm) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;

    }

    public Pager(FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.tabCount=tabCount;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               Tab1Activity tab1 = new  Tab1Activity();
                return tab1;
            case 1:
                Tab2Activity tab2 = new Tab2Activity();
                return tab2;
            case 2:
                Tab3Activity tab3 = new  Tab3Activity();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

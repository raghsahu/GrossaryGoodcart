package com.grossaryapp.ui.activity.Nouse_other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.grossaryapp.R;
import com.grossaryapp.ui.activity.Category_Product.CategoryActivity;

public class ActivityFilter extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    ImageView cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout2);
        cancel=findViewById(R.id.cancel);
        tabLayout.addTab(tabLayout.newTab().setText("Sortby"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager4 adapter = new Pager4(getSupportFragmentManager(),tabLayout.getTabCount());

        //Adding adapter to pager
       viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                int position = tab.getPosition();
                switch (position) {
                    case 0:

                        tabLayout.setSelectedTabIndicatorColor(Color.RED);
                        tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                        /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                        /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                        Log.e("Position", String.valueOf(0));
                        break;

                    default:
                        break;
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityFilter.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

        int position = tab.getPosition();
        switch (position) {
            case 0:
                        /*tabLayout.getTabAt(0).getCustomView().setBackgroundColor(Color.parseColor("#000"));
                          tabLayout.setSelectedTabIndicatorColor(Color.WHITE);*/
                /* .setBackgroundColor(Color.WHITE);*/
                tabLayout.setSelectedTabIndicatorColor(Color.RED);
                tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                Log.e("Position", String.valueOf(0));
                break;

            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
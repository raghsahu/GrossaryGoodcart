package com.grossaryapp.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grossaryapp.R;
import com.grossaryapp.ui.model.DrawerItem;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 04-10-2019.
 */

public class LeftDrawerAdapter extends BaseAdapter {


    ArrayList<DrawerItem> Drawerlist = new ArrayList<>();
    Context mContext;
    private int selectedIndex;
    DrawerItem drawerItem;
    ImageView iconimg;
    ImageView icon_profile;

    public LeftDrawerAdapter(Context context, ArrayList<DrawerItem> listArray) {
        mContext = context;
        Drawerlist = listArray;
        //fragmentManager = fm;

    }

    @Override
    public int getCount() {
        return Drawerlist.size();    // total number of elements in the list
    }

    @Override
    public Object getItem(int i) {
        return Drawerlist.get(i);    // single item in the list
    }

    @Override
    public long getItemId(int i) {
        return i;                   // index number
    }

    @Override
    public View getView(int index, View view, final ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            view = inflater.inflate(R.layout.list_item_drawer, parent, false);
        }

        drawerItem = Drawerlist.get(index);

        TextView itemtxt = view.findViewById(R.id.list_item_id);
        iconimg = view.findViewById(R.id.icon_id);
        icon_profile = view.findViewById(R.id.icon_profile);
        LinearLayout linearLayout = view.findViewById(R.id.layout_id);


        //set name and icon on listview
        itemtxt.setText(drawerItem.getItemName());
        icon_profile.setImageResource((int) drawerItem.getIconImg());

        //Selected Item  Highlighted
        if (selectedIndex != -1 && index == selectedIndex) {

            //Log.e("test123455",""+drawerItem.getItemName());

            //PP call selectedIcon method
            setSelectedIconImage();

            linearLayout.setBackgroundResource(R.drawable.listselection_bg);
            itemtxt.setTextColor(Color.parseColor("#FFFFFF"));

            //TextStyle Bold
            itemtxt.setTypeface(Typeface.DEFAULT_BOLD);

        } else {

            itemtxt.setTextColor(Color.parseColor("#000000"));
            linearLayout.setBackground(null);
            //linearLayout.setBackgroundResource(R.drawable.listdeselection_bg);
            itemtxt.setTypeface(Typeface.DEFAULT);

        }


        return view;
    }


    //PP method to make selected icon white
    public void setSelectedIconImage() {


        switch (drawerItem.getItemName()) {
            case "Home":
                icon_profile.setImageResource(R.drawable.ic_home_w);
                break;

            case "My Account":
                icon_profile.setImageResource(R.drawable.ic_user_w);
                break;

            case "Change Location":
                icon_profile.setImageResource(R.drawable.ic_location_white);
                break;

            case "Contact Us":
                icon_profile.setImageResource(R.drawable.ic_phone_w);
                break;

            case "About Us":
                icon_profile.setImageResource(R.drawable.ic_about_us_w);
                break;

            case "Setting":
                icon_profile.setImageResource(R.drawable.ic_settings_white);
                break;

            case "Share App":
                icon_profile.setImageResource(R.drawable.ic_menu_share_w);
                break;

            case "LogOut":
                icon_profile.setImageResource(R.drawable.ic_logout_w);

                break;
        }
    }

    public void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
    }
}

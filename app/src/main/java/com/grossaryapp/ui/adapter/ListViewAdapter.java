package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.grossaryapp.R;
import com.grossaryapp.ui.model.Navigationcategory;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 01/11/2019.
 */
public class ListViewAdapter extends ArrayAdapter<Navigationcategory> {
    private List<Navigationcategory> heroList;

    //the context object
    private Context mCtx;
    public ListViewAdapter(List<Navigationcategory> heroList, Context mCtx) {
        super(mCtx, R.layout.content_activity_navigation, heroList);
        this.heroList = heroList;
        this.mCtx = mCtx;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.content_activity_navigation, null, true);

        //getting text views

        ImageView  textViewImageUrl = listViewItem.findViewById(R.id.circleView);

        //Getting the hero for the specified position
       Navigationcategory hero = heroList.get(position);

        //setting hero values to textviews

       /* textViewImageUrl.setText(hero.getImage());*/

        //returning the listitem
        return listViewItem;
    }
}


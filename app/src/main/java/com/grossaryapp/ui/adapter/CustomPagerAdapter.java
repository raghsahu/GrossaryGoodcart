package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.model.Navigation;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<Navigation> herolist;

    public CustomPagerAdapter(Context context, ArrayList<Navigation> hero) {
        this.context = context;
        this.herolist = hero;
    }

    public int[] sliderImage = {
            R.drawable.home_slider1,
            R.drawable.home_slider2,
            R.drawable.home_slider3
    };

    @Override
    public int getCount() {
        return herolist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.list_item_welcome, container, false);

        ImageView imageView = view.findViewById(R.id.iv_icon);
        //imageView.setImageResource(herolist.get(position).getImage());

        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/banner/" + herolist.get(position).getImage())
                .override(300, 200)
                .into(imageView);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

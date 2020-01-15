package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.activity.Category_Product.CategoryActivity;
import com.grossaryapp.ui.model.Navigationcategory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raghvendra Sahu on 02/11/2019.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Navigationcategory>navigationcategories;
    public CustomRecyclerAdapter(Context context, List navigationcategories) {
        this.context = context;
        this.navigationcategories = navigationcategories;
    }
    @Override
    public CustomRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(navigationcategories.get(position));

        Navigationcategory pu =navigationcategories.get(position);
        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/categories/" + navigationcategories.get(position).getImage())
                .override(300, 200)
                .into(holder.circleView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigationcategory cpu = (Navigationcategory) view.getTag();

                Intent intent=new Intent(context, CategoryActivity.class);
                context.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return navigationcategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView circleView;
        public ViewHolder(View itemView) {
            super(itemView);
            circleView=itemView.findViewById(R.id.circleView);


        }
    }
}

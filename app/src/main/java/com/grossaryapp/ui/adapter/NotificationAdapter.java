package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.activity.ActivityNotification;
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;
import com.grossaryapp.ui.model.GetLatestProducts;
import com.grossaryapp.ui.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NotificationModel> getLatestProducts;

    public NotificationAdapter(ActivityNotification activityNotification,
                               ArrayList<NotificationModel> notificationModels) {
        context=activityNotification;
        getLatestProducts=notificationModels;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(getLatestProducts.get(position));

        NotificationModel  pu =getLatestProducts.get(position);
        holder.tv_date.setText(pu.getDate_time());
        holder.tv_message.setText(pu.getMessage());


    }

    @Override
    public int getItemCount() {
        return getLatestProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date,tv_message;

        public ViewHolder( View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_message = itemView.findViewById(R.id.tv_message);

        }
    }



}

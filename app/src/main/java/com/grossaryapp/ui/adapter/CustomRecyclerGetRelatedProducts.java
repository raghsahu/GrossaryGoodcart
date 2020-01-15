package com.grossaryapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;
import com.grossaryapp.ui.model.GetRelatedProducts;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 06/11/2019.
 */
public class CustomRecyclerGetRelatedProducts extends RecyclerView.Adapter<CustomRecyclerGetRelatedProducts.ViewHolder>{
    private Context context;
    private List<GetRelatedProducts> getRelatedProducts;


    public CustomRecyclerGetRelatedProducts(Context context, List getRelatedProducts) {
        this.context = context;
        this.getRelatedProducts = getRelatedProducts;
    }

    @NonNull
    @Override
    public CustomRecyclerGetRelatedProducts.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.getrelatedproductsitems, parent, false);
        CustomRecyclerGetRelatedProducts.ViewHolder viewHolder = new CustomRecyclerGetRelatedProducts.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerGetRelatedProducts.ViewHolder holder, final int position) {
        holder.itemView.setTag(getRelatedProducts.get(position));

        GetRelatedProducts pu =getRelatedProducts.get(position);

        Log.d("image<>",getRelatedProducts.get(position).getImage());
        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/products/" +getRelatedProducts.get(position).getImage())
                .override(300, 200)
                .into(holder.imagerelatedproducts);

        holder.relatedproductslinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,SingleProductsActivity.class);

                intent.putExtra("url_slug", getRelatedProducts.get(position).getUrl_slug());

                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return getRelatedProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagerelatedproducts;
        LinearLayout relatedproductslinearlayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imagerelatedproducts=itemView.findViewById(R.id.imagerelatedproducts);
            relatedproductslinearlayout=itemView.findViewById(R.id.relatedproductslinearlayout);
        }
    }
}

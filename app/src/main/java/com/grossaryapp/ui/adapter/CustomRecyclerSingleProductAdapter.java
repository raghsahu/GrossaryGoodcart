package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.model.SingleProduct;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 05/11/2019.
 */
public class CustomRecyclerSingleProductAdapter extends RecyclerView.Adapter<CustomRecyclerSingleProductAdapter.ViewHolder>{
    private Context context;
    private List<SingleProduct> singleProducts;
    String wishlist_status;
    Session session;


    public CustomRecyclerSingleProductAdapter(Context context, List singleProducts, String wishlist_status) {
        this.context = context;
        this.singleProducts = singleProducts;
        this.wishlist_status = wishlist_status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleproductlistitems, parent, false);
       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerSingleProductAdapter.ViewHolder holder, final int position) {
        holder.itemView.setTag(singleProducts.get(position));

        session=new Session(context);
       SingleProduct pu =singleProducts.get(position);

        Log.d("image<>",singleProducts.get(position).getImage());
        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/products/" +singleProducts.get(position).getImage())
                .override(300, 200)
                .into(holder.image);


    }


    @Override
    public int getItemCount() {
        return singleProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favimage,favimage2;
        TextView name,product_id,mrp,price;
        public ViewHolder( View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.Singleimage);
            favimage=itemView.findViewById(R.id.favimage);

            favimage2 = itemView.findViewById(R.id.favimage2);
        }
    }
}

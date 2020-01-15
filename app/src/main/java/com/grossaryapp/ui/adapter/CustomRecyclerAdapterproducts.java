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
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;
import com.grossaryapp.ui.model.GetLatestProducts;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 02/11/2019.
 */
public class CustomRecyclerAdapterproducts extends RecyclerView.Adapter<CustomRecyclerAdapterproducts.ViewHolder> {
    private Context context;
    private List<GetLatestProducts> getLatestProducts;

    public CustomRecyclerAdapterproducts(Context context, List getLatestProducts) {
        this.context = context;
        this.getLatestProducts = getLatestProducts;
    }
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.getcategoryitems, parent, false);
       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(getLatestProducts.get(position));

      GetLatestProducts  pu =getLatestProducts.get(position);
      holder.name.setText(pu.getName());
        holder.price.setText(pu.getPrice());
        holder.add.setText(pu.getMrp());
        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/products/" + getLatestProducts.get(position).getImage())
                .override(300, 200)
                .into(holder.productimage);

   holder.card_single_pro.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           Intent intent=new Intent(context, SingleProductsActivity.class);
           intent.putExtra("url_slug", getLatestProducts.get(position).getUrl_slug());
           context.startActivity(intent);

       }
   });






    }

    @Override
    public int getItemCount() {
        return getLatestProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      ImageView productimage;
      TextView name,price,add;
      CardView card_single_pro;
        public ViewHolder( View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.productimage);
            name = itemView.findViewById(R.id.name);
           price = itemView.findViewById(R.id.price);
            add = itemView.findViewById(R.id.add);
            card_single_pro = itemView.findViewById(R.id.card_single_pro);
        }
    }
}

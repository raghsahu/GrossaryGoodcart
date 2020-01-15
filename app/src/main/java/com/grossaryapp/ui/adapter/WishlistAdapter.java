package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.model.GetLatestProducts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.remove_from_wishlist;

/**
 * Created by Raghvendra Sahu on 15/11/2019.
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private Context context;
    private List<GetLatestProducts> getLatestProducts;
    Session session;

    public WishlistAdapter(Context context, List getLatestProducts) {
        this.context = context;
        this.getLatestProducts = getLatestProducts;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlistadapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(getLatestProducts.get(position));

        session=new Session(context);

        GetLatestProducts  pu =getLatestProducts.get(position);
        holder.name.setText(pu.getName());
        holder.price.setText(pu.getPrice());
       // holder.add.setText(pu.getMrp());
        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/products/" + getLatestProducts.get(position).getImage())
                .override(300, 200)
                .into(holder.productimage);

        holder.ll_food_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, SingleProductsActivity.class);
                intent.putExtra("url_slug", getLatestProducts.get(position).getUrl_slug());
                context.startActivity(intent);

            }
        });

        holder.favimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remove_favorite(getLatestProducts.get(position).getId(),getLatestProducts.get(position));

            }
        });



        Log.e("wishlist_size", ""+getLatestProducts.size());

    }

    private void Remove_favorite(final String id, final GetLatestProducts getLatestProsition) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, remove_from_wishlist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sss",response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");


                            if (result.equalsIgnoreCase("true")) {
//                                JSONObject data=obj.getJSONObject("data");
//
//                                String id=data.getString("id");
                                getLatestProducts.remove(getLatestProsition);
                                notifyDataSetChanged();

                            }
                            else {
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
              //  params.put("product_id", id);
               // params.put("user_id",  session.getUserId());
                params.put("id",  id);

                return params;
            }
        };



        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    @Override
    public int getItemCount() {
        return getLatestProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productimage,favimage;
        TextView name,price,add;
        CardView card_single_pro;
        LinearLayout ll_food_details;
        public ViewHolder( View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.productimage);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
           // add = itemView.findViewById(R.id.add);
            card_single_pro = itemView.findViewById(R.id.card_single_pro);
            ll_food_details = itemView.findViewById(R.id.ll_food_details);
            favimage = itemView.findViewById(R.id.favimage);
        }
    }



}

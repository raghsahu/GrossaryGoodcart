package com.grossaryapp.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.model.AddCart;
import com.grossaryapp.ui.model.GetProductCategorywise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.grossaryapp.ui.activity.Category_Product.CategoryActivity.tv_count_cart;

/**
 * Created by Raghvendra Sahu on 04/11/2019.
 */
public class CustomRecyclerGetCategorywise extends RecyclerView.Adapter<CustomRecyclerGetCategorywise.ViewHolder> {
    private Context context;
    private List<GetProductCategorywise> getProductCategorywises;
    List<AddCart> addCartList=new ArrayList<>();
    Session session;

    public CustomRecyclerGetCategorywise(Context context, List getProductCategorywises) {
        this.context = context;
        this.getProductCategorywises = getProductCategorywises;
    }
    @Override
    public CustomRecyclerGetCategorywise.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemscategorywise, parent, false);
        CustomRecyclerGetCategorywise.ViewHolder viewHolder = new CustomRecyclerGetCategorywise.ViewHolder(v);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final CustomRecyclerGetCategorywise.ViewHolder holder, final int position) {
        holder.itemView.setTag(getProductCategorywises.get(position));
        session=new Session(context);
        GetProductCategorywise  pu =getProductCategorywises.get(position);
        holder.name.setText(pu.getName());
        holder.product_id.setText(pu.getProduct_id());
        holder.price.setText("Rs "+pu.getPrice());
        holder.mrp.setText("MRP "+pu.getMrp());

        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/products/" + getProductCategorywises.get(position).getImage())
                .override(300, 200)
                .into(holder.image);

        if (getProductCategorywises.get(position).getCart_status()
                .equalsIgnoreCase("0")){

            holder.btn_add_cart.setVisibility(View.VISIBLE);
            holder.ll_plus_minus.setVisibility(View.GONE);
        }else {
            holder.btn_add_cart.setVisibility(View.GONE);
            holder.ll_plus_minus.setVisibility(View.VISIBLE);

        }
        //**********
        if (getProductCategorywises.get(position).getCart_quantity()!=null &&
                !getProductCategorywises.get(position).getCart_quantity().equalsIgnoreCase("null")){
            holder.tv_count_number.setText(getProductCategorywises.get(position).getCart_quantity().toString());
        }

     holder.linearlayoutcategoryget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, SingleProductsActivity.class);
                intent.putExtra("url_slug", getProductCategorywises.get(position).getUrl_slug());
                context.startActivity(intent);
            }
        });

     holder.btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Connectivity.isConnected(context)){
                    Log.e("pro_id", getProductCategorywises.get(position).getId());
                    AddToCart(getProductCategorywises.get(position).getId(),holder.btn_add_cart,holder.ll_plus_minus);
                }else Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
            }
        });

     ///**********************plus minus cart item
        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=getProductCategorywises.get(position).getId();
                Log.e("item_id_cart", item_id);
                int qty = Integer.valueOf(holder.tv_count_number.getText().toString());
                if (qty > 0) {
                    qty = qty - 1;
                    // holder.tv_count_number.setText(String.valueOf(qty));
                }
                Log.e("item_qty", String.valueOf(qty));
                // String qty_nmbr=holder.tv_count_number.getText().toString();
                if (Connectivity.isConnected(context)){
                    UpdateItemInCart(getProductCategorywises.get(position).getId(),position,String.valueOf(qty),
                            holder.tv_count_number,holder.btn_add_cart,holder.ll_plus_minus);
                }

            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=getProductCategorywises.get(position).getId();
                Log.e("item_id_cart", item_id);
                int qty = Integer.valueOf(holder.tv_count_number.getText().toString());
                qty = qty + 1;
                Log.e("item_qty", String.valueOf(qty));
                // holder.tv_count_number.setText(String.valueOf(qty));

                // String qty_nmbr=holder.tv_count_number.getText().toString();
                if (Connectivity.isConnected(context)){
                    UpdateItemInCart(getProductCategorywises.get(position).getId(),position,String.valueOf(qty),
                            holder.tv_count_number,holder.btn_add_cart,holder.ll_plus_minus);
                }

            }
        });



    }

    private void UpdateItemInCart(final String item_id, final int position, final String qty_nmbr, final TextView tv_count_number,
                                  final Button btn_add_cart, final LinearLayout ll_plus_minus) {
        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.update_cart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cart_update",response.toString());
                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    // Log.d("cartitem", heroArray.toString());
                                    JSONObject heroObject = heroArray.getJSONObject(i);

                                    //creating a hero object and giving them the values from json object
//                                    AddCart hero = new AddCart(
//                                            heroObject.getString("url_slug"),
//                                            heroObject.getString("name"),
//                                            heroObject.getString("price"),
                                    String qty=  heroObject.getString("quantity");
                                    String product_ids=  heroObject.getString("product_ids");
//                                            heroObject.getString("id"),
//                                            heroObject.getString("image"));
//
//                                        addCarts.add(hero);

                                    if (item_id.equalsIgnoreCase(product_ids)){
                                        getProductCategorywises.get(position).setCart_quantity(qty);

                                    }

                                }

                              //  if (!getProductCategorywises.get(position).getCart_quantity().equalsIgnoreCase("0")){

                                if (!qty_nmbr.equals(0) && !qty_nmbr.equalsIgnoreCase("0")){
                                    Log.e("update_qty0", tv_count_number.getText().toString());
                                    tv_count_number.setText(getProductCategorywises.get(position).getCart_quantity());
                                }
                                else {
                                    Log.e("update_qty2", qty_nmbr);
                                    ll_plus_minus.setVisibility(View.GONE);
                                    btn_add_cart.setVisibility(View.VISIBLE);

                                }

                                if (qty_nmbr.equalsIgnoreCase("0") ){
                                    Log.e("update_qty3", qty_nmbr);
                                    ll_plus_minus.setVisibility(View.GONE);
                                    btn_add_cart.setVisibility(View.VISIBLE);


                                }

                                Log.e("update_qty1", qty_nmbr);
                               // Log.e("update_qtyqty1", qty);
                            } else {
                            }

                           // notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", item_id);
                params.put("quantity", qty_nmbr);
                params.put("user_id", session.getUserId());

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);




    }

    private void AddToCart(final String product_id, final Button btn_add_cart, final LinearLayout ll_plus_minus) {
        String url= Base_Url.BASE_URL+Base_Url.add_to_cart_Api;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrr_cart",response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                Integer total_item_count = obj.getInt("total_item_count");
                                tv_count_cart.setText(total_item_count.toString());

                                JSONArray heroArray = obj.getJSONArray("data");
                                Toast.makeText(context, "Add to cart successfully", Toast.LENGTH_SHORT).show();

                              // btn_add_cart.setText("Added");

                                btn_add_cart.setVisibility(View.GONE);
                                ll_plus_minus.setVisibility(View.VISIBLE);
                                //now looping through all the elements of the json array
                                if (heroArray!=null){
                                    for (int i = 0; i < heroArray.length(); i++) {
                                        Log.d("cart", heroArray.toString());
                                        //getting the json object of the particular index inside the array
                                        JSONObject heroObject = heroArray.getJSONObject(i);

                                        //creating a hero object and giving them the values from json object
                                        AddCart hero = new AddCart(
                                                heroObject.getString("url_slug"),
                                                heroObject.getString("name"),
                                                heroObject.getString("price"),
                                                heroObject.getString("quantity"),
                                                heroObject.getString("id"),
                                                heroObject.getString("image"),
                                                heroObject.getString("product_ids"));

                                        addCartList.add(hero);

                                    }
                                }

                            } else {
                            }
                            Log.d("Addcart", "" + addCartList.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product_id);
                params.put("quantity", "1");
                params.put("user_id", session.getUserId());

                return params;
            }
        };



        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);



    }

    @Override
    public int getItemCount() {
        return getProductCategorywises.size();
    }

    public void updateList(ArrayList<GetProductCategorywise> contact_li) {
        getProductCategorywises = contact_li;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearlayoutcategoryget,ll_plus_minus;
        ImageView image,iv_plus,iv_minus;
        TextView name,product_id,mrp,price,tv_count_number;
        Button btn_add_cart;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            iv_plus = itemView.findViewById(R.id.iv_plus);
            iv_minus = itemView.findViewById(R.id.iv_minus);
            product_id = itemView.findViewById(R.id.product_id);
            mrp = itemView.findViewById(R.id.mrp);
            price= itemView.findViewById(R.id.price);
            tv_count_number= itemView.findViewById(R.id.tv_count_number);
            ll_plus_minus= itemView.findViewById(R.id.ll_plus_minus);
            btn_add_cart= itemView.findViewById(R.id.btn_add_cart);
            linearlayoutcategoryget=itemView.findViewById(R.id.linearlayoutcategoryget);

        }

    }

}

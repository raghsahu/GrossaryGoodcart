package com.grossaryapp.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.InCart.AddcartActivity;
import com.grossaryapp.ui.model.AddCart;
import com.grossaryapp.ui.model.SelectedSubscriptionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Raghvendra Sahu on 14/12/2019.
 */
public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>{
    private Context context;
    private List<AddCart> addCarts;
    Session session;
    // public static String Total_Payable_Amt;
    AddcartActivity myActivity;
    public static Set<String> LinkedMultiselect = new LinkedHashSet<String>();
    ArrayList<SelectedSubscriptionModel>selectedSubscriptionModels=new ArrayList<>();
    public JSONArray passArray;

    public SubscriptionAdapter(Context context, List addCarts) {
        this.context = context;
        this.addCarts = addCarts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscriptionitems, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(addCarts.get(position));
//        try {
//            mAdapterCallback = ((CustomRecyclerAddCart.AdapterCallback) context);
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Activity must implement AdapterCallback.");
//        }
        session=new Session(context);
        AddCart pu =addCarts.get(position);
        holder.name.setText(pu.getName());
        // holder.quantity.setText(" "+pu.getQuantity());
        holder.tv_count_number.setText(pu.getQuantity().toString());
        holder.price.setText(pu.getPrice());

        Glide.with(context)
                .load("https://logicalsofttech.com/goodcart/assets/uploaded/products/" + addCarts.get(position).getImage())
                .override(300, 200)
                .into(holder.image);

        try {
            int qty = Integer.valueOf(holder.tv_count_number.getText().toString());
            if (qty >= 1) {
                double totalamount=Double.parseDouble(holder.tv_count_number.getText().toString())
                        * Double.valueOf(addCarts.get(position).getPrice());

                holder.tv_total_qty_amt.setText(String.valueOf(totalamount));
            }

        }catch (Exception e){

        }

        try {
            double all_qty, all_amt;
            double total_pay = 0;
            for (int i=0; i<addCarts.size(); i++){

                all_qty=  Double.parseDouble(addCarts.get(i).getQuantity());
                all_amt=  Double.parseDouble(addCarts.get(i).getPrice());

                total_pay= +total_pay+(all_qty * all_amt);

            }
            Log.e("total_pay", ""+total_pay);

            String Total_Payable_Amt=String.valueOf(total_pay);

           // mAdapterCallback.onMethodCallback(Total_Payable_Amt);

        }catch (Exception e){

        }

        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=addCarts.get(position).getId();

                if (Connectivity.isConnected(context)){
                  removeItemFromCat(item_id,addCarts.get(position));
                }

            }
        });

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=addCarts.get(position).getProduct_ids();

                int qty = Integer.valueOf(holder.tv_count_number.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                   // holder.tv_count_number.setText(String.valueOf(qty));
                }

                if (Connectivity.isConnected(context)){
                   UpdateItemInCart(item_id,position,String.valueOf(qty),holder.tv_count_number);
                }

            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=addCarts.get(position).getProduct_ids();

                int qty = Integer.valueOf(holder.tv_count_number.getText().toString());
                qty = qty + 1;

                if (Connectivity.isConnected(context)){
                   UpdateItemInCart(item_id,position,String.valueOf(qty), holder.tv_count_number);
                }

            }
        });

        //checkbox check-uncheck
        holder.check_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // int i=0;
                if (holder.check_subscription.isChecked()){
                    LinkedMultiselect.add(addCarts.get(position).getProduct_ids());
                    Log.e("checkMulti_add", ""+LinkedMultiselect);

                    selectedSubscriptionModels.add(new SelectedSubscriptionModel(addCarts.get(position).getProduct_ids()
                    ,addCarts.get(position).getQuantity(),addCarts.get(position).getPrice()));

                   // Log.e("checkMulti_list", ""+selectedSubscriptionModels.get(i).getProduct_ids());

                    getSelected_item();

                }else {
                    LinkedMultiselect.remove(addCarts.get(position).getProduct_ids());
                    Log.e("checkMulti_remove", ""+LinkedMultiselect);

                   // selectedSubscriptionModels.remove(i);
                  //  Log.e("remove_list", ""+selectedSubscriptionModels.get(i).getProduct_ids());

                    for (int i=0; i<selectedSubscriptionModels.size();i++ ){

                        if (addCarts.get(position).getProduct_ids().equalsIgnoreCase(
                                selectedSubscriptionModels.get(i).getProduct_ids())){

                            selectedSubscriptionModels.remove(i);

                        }

                    }

                    getSelected_item();
                }

            }
        });

    }

    private void getSelected_item() {
        Log.e("checkMulti_size", ""+selectedSubscriptionModels.size());
        passArray = new JSONArray();

        if (selectedSubscriptionModels!=null && !selectedSubscriptionModels.isEmpty()){

            for (int i=0; i<selectedSubscriptionModels.size(); i++){

                Log.e("subs_selected_id", ""+selectedSubscriptionModels.get(i).getProduct_ids());
                Log.e("subs_selected_price", ""+selectedSubscriptionModels.get(i).getPrice());
                Log.e("subs_selected_qty", ""+selectedSubscriptionModels.get(i).getQuantity());

                JSONObject jObjP = new JSONObject();

                try {
                    jObjP.put("product_id", selectedSubscriptionModels.get(i).getProduct_ids());
                    jObjP.put("quantity", selectedSubscriptionModels.get(i).getQuantity());
                    jObjP.put("price", selectedSubscriptionModels.get(i).getPrice());
                    //jObjP.put("name", addCartList.get(i).getName());
                    // jObjP.put("url_slug", addCartList.get(i).getUrl_slug());


                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("cart_subs_jsaon" ,""+ passArray.toString());

            }
        }else {
            Log.e("no_item_found", "no_item");
        }
    }

    public interface AdapterCallback {
        void onMethodCallback(String Total_Payable_Amt);
    }

    private void UpdateItemInCart(final String item_id, final int addposition, final String qty_nmbr, final TextView tv_count_number) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.update_subcription,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("subscription_update",response.toString());
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
//                                            heroObject.getString("id"),
//                                            heroObject.getString("image"));
//
//                                        addCarts.add(hero);

                                    if (i==addposition){
                                        addCarts.get(addposition).setQuantity(qty);
                                        tv_count_number.setText(addCarts.get(addposition).getQuantity());
                                        Log.e("update_qty", tv_count_number.getText().toString());
                                    }

                                }
                            } else {
                            }

                            //addCarts.remove(addposition);

                            notifyDataSetChanged();

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

    private void removeItemFromCat(final String item_id, final AddCart addposition) {

        final ProgressDialog progressDialog = new ProgressDialog(context,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.remove_from_subscription,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("subs_remove",response.toString());
                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    Log.d("cartitem", heroArray.toString());
                                    //getting the json object of the particular index inside the array
                                    JSONObject heroObject = heroArray.getJSONObject(i);

                                }
                            } else {
                            }

                            addCarts.remove(addposition);
                            notifyDataSetChanged();

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
                // params.put("product_id", "6");
                params.put("id", item_id);
                params.put("user_id", session.getUserId());

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }



    public String PassArray(){

    return passArray.toString();
    }



    @Override
    public int getItemCount() {
        return addCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,iv_remove,iv_plus,iv_minus;
        TextView name,quantity,price,tv_count_number,tv_total_qty_amt;
        CheckBox check_subscription;

        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.quantity);
            price= itemView.findViewById(R.id.price);
            image= itemView.findViewById(R.id.image);
            iv_remove= itemView.findViewById(R.id.iv_remove);
            iv_plus= itemView.findViewById(R.id.iv_plus);
            iv_minus= itemView.findViewById(R.id.iv_minus);
            tv_count_number= itemView.findViewById(R.id.tv_count_number);
            tv_total_qty_amt= itemView.findViewById(R.id.tv_total_qty_amt);
            check_subscription= itemView.findViewById(R.id.check_subscription);

        }
    }




}

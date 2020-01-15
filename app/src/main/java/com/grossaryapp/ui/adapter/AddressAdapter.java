package com.grossaryapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.SessionManager.Shared_Pref;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.activity.Profile.Add_AddressActivity;
import com.grossaryapp.ui.activity.InCart.AddcartActivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.model.AddressModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raghvendra Sahu on 18/11/2019.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private Context context;
    private List<AddressModel> addCarts;
    Session session;
    // public static String Total_Payable_Amt;
    AddcartActivity myActivity;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private int mCheckedPostion;
    private RadioButton lastCheckedRB = null;


    public AddressAdapter(Context context, List addCarts) {
        this.context = context;
        this.addCarts = addCarts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(addCarts.get(position));

        session=new Session(context);
        AddressModel pu =addCarts.get(position);
        holder.tv_address.setText(pu.getAddress()+" "+pu.getAddress2()+" "+pu.getArea_name()
        +" "+pu.getCity_name()+" "+pu.getZipcode());


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=addCarts.get(position).getId();

                DeleteAddress(item_id,addCarts.get(position));


            }
        });

        holder.iv_edit_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=addCarts.get(position).getId();

                Intent intent=new Intent(context, Add_AddressActivity.class);
                intent.putExtra("Address_Edit", item_id);
                context.startActivity(intent);
               // EditAddress(item_id,addCarts.get(position));


            }
        });

        Log.e("radio_po_id", Shared_Pref.getAddresId(context));
    if (addCarts.get(position).getId().equalsIgnoreCase(Shared_Pref.getAddresId(context))){
        holder.radio_select.setChecked(true);
       //mCheckedPostion =position;
        Log.e("radio_pos", ""+position);
        Log.e("radio_pos1", addCarts.get(position).getId());
        lastCheckedRB = holder.radio_select;



    }
       //holder.radio_select.setChecked(position == mCheckedPostion);

        holder.radio_select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // Toast.makeText(context, ""+addCarts.get(position).getId(), Toast.LENGTH_SHORT).show();

                RadioButton checked_rb = (RadioButton) v;
                if(lastCheckedRB != null){
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;

                Shared_Pref.setAddresId(context,addCarts.get(position).getId());

            }
        });



    }

    private void DeleteAddress(final String item_id, final AddressModel addressModel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.delete_shipping_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrr_del_add", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();
                                // String id = jsonObject1.getString("id");

                                addCarts.remove(addressModel);
                                notifyDataSetChanged();
                            } else {
                                // Toast.makeText(AddcartActivity.this, "No item available", Toast.LENGTH_SHORT).show();
                            }

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
                params.put("id", item_id);

                return params;
            }
        };


        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    @Override
    public int getItemCount() {
        return addCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_set_default,iv_delete,iv_edit_addr;
        TextView tv_address;
        CardView card_set;
        RadioButton radio_select;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_address=itemView.findViewById(R.id.tv_address);
            iv_set_default=itemView.findViewById(R.id.iv_set_default);
            card_set=itemView.findViewById(R.id.card_set);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            radio_select=itemView.findViewById(R.id.radio_select);
            iv_edit_addr=itemView.findViewById(R.id.iv_edit_addr);

        }
    }


}

package com.grossaryapp.ui.activity.InCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.Category_Product.SingleProductsActivity;
import com.grossaryapp.ui.adapter.CustomRecyclerAddCart;
import com.grossaryapp.ui.model.AddCart;
import com.grossaryapp.ui.model.Total_PayModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddcartActivity extends AppCompatActivity implements CustomRecyclerAddCart.AdapterCallback,SwipeRefreshLayout.OnRefreshListener {

        ImageView iv_back;
        TextView placeorder, tv_paytotal_amt;
        Context context;
        RecyclerView recyclerView1;
        CustomRecyclerAddCart nAdapter;
        ArrayList<AddCart> addCartList;
        Session session;
        private String TotalAmount;
    SwipeRefreshLayout swipeToRefresh;


    @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcart);
            nAdapter = new CustomRecyclerAddCart(this);
        context = AddcartActivity.this;

        iv_back = findViewById(R.id.iv_back);
        placeorder = findViewById(R.id.placeorder);
        tv_paytotal_amt = findViewById(R.id.tv_paytotal_amt);
        session = new Session(AddcartActivity.this);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeToRefresh.setOnRefreshListener(AddcartActivity.this);

        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerviewcart);
        addCartList = new ArrayList<>();

        if (Connectivity.isConnected(AddcartActivity.this)) {
            getCartItem();
        } else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //place order
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!addCartList.isEmpty() && addCartList!=null && addCartList.size()!=0){

                    //GetAllItemJsonArray();
                    JSONArray passArray = GetAllItemJsonArray();
                    Log.e("passArray", ""+passArray.toString());

                    if (Connectivity.isConnected(context)){
                        SubmitTotalAmount(TotalAmount,passArray.toString());

                    }else {
                        Toast.makeText(context, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(AddcartActivity.this, "No item available", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private JSONArray GetAllItemJsonArray() {
        // retrive data from cart list
        JSONArray passArray = new JSONArray();
        if (addCartList.size() > 0) {

            for (int i = 0; i < addCartList.size(); i++) {

                JSONObject jObjP = new JSONObject();

                try {
                    jObjP.put("product_id", addCartList.get(i).getProduct_ids());
                    jObjP.put("quantity", addCartList.get(i).getQuantity());
                    jObjP.put("price", addCartList.get(i).getPrice());
                   //jObjP.put("name", addCartList.get(i).getName());
                   // jObjP.put("url_slug", addCartList.get(i).getUrl_slug());

                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.e("cart_jsaon" ,""+ passArray.toString());

        }

        return passArray;
    }

    private void SubmitTotalAmount(final String totalAmount, final String passArray) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.cart_total_calculation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("totalcart", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = obj.getJSONObject("data");

                                String gst = jsonObject1.getString("gst");
                                String shippinig_charge = jsonObject1.getString("shippinig_charge");
                                String discount = jsonObject1.getString("discount");
                                String total_item_price = jsonObject1.getString("total_item_price");
                                Integer total_payable = jsonObject1.getInt("total_payable");

                                Total_PayModel total_payModel=new Total_PayModel(gst,shippinig_charge,discount
                                        ,total_item_price,total_payable);
                                ArrayList<Total_PayModel>total_amt=new ArrayList();
                                total_amt.add(total_payModel);

                             Intent intent = new Intent(AddcartActivity.this, BillingActivity.class);
                             intent.putExtra("total_payModel", total_payModel);
                             intent.putExtra("passArray", passArray);
                            // intent.putParcelableArrayListExtra("addCartList",  addCartList);
                             startActivity(intent);

                            } else {

                                Toast.makeText(AddcartActivity.this, "No item available", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("product_id", "6");
                // params.put("quantity", "1");
                params.put("cart_total", totalAmount);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void getCartItem () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_cart_product,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrcart", response.toString());
                        try {
                            addCartList.clear();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    Log.d("cartitem", heroArray.toString());
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
                                swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                swipeToRefresh.setVisibility(View.GONE);
                                Toast.makeText(AddcartActivity.this, "No item available", Toast.LENGTH_SHORT).show();

                            }

                            Log.d("Addcart", "" + addCartList.size());
                            nAdapter = new CustomRecyclerAddCart(AddcartActivity.this, addCartList);

                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(AddcartActivity.this, RecyclerView.VERTICAL, false);
                            recyclerView1.setLayoutManager(mLayoutManger);
                            recyclerView1.setItemAnimator(new DefaultItemAnimator());
                            recyclerView1.setAdapter(nAdapter);
                            nAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("product_id", "6");
                // params.put("quantity", "1");
                params.put("user_id", session.getUserId());

                return params;
            }
        };


        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMethodCallback(String Total_Payable_Amt) {

        TotalAmount=Total_Payable_Amt;

        tv_paytotal_amt.setText("Total ₹ : "+TotalAmount);

    }

    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(AddcartActivity.this)) {
            getCartItem();
        } else {
            Toast.makeText(AddcartActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }
}


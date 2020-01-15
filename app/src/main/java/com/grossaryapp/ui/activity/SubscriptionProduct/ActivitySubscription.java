package com.grossaryapp.ui.activity.SubscriptionProduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.adapter.SubscriptionAdapter;
import com.grossaryapp.ui.model.AddCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.grossaryapp.ui.adapter.SubscriptionAdapter.LinkedMultiselect;

public class ActivitySubscription extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ImageView iv_back;
    RecyclerView recycler_subscription;
    ArrayList<AddCart> addCartList=new ArrayList<>();
    SubscriptionAdapter subscriptionAdapter;
    Session session;
    SwipeRefreshLayout swipeToRefresh;
    TextView tv_placeorder;
    String Spin_ProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        session = new Session(ActivitySubscription.this);
        iv_back=findViewById(R.id.iv_back);
        tv_placeorder=findViewById(R.id.tv_placeorder);


        recycler_subscription=findViewById(R.id.recycler_subscription);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeToRefresh.setOnRefreshListener(ActivitySubscription.this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Connectivity.isConnected(ActivitySubscription.this)){
            getSubscriptionProduct();

        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }

        tv_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = null;
                if(LinkedMultiselect != null) {
                    StringBuilder sb = new StringBuilder();
                    Iterator<String> it = LinkedMultiselect.iterator();
                    if(it.hasNext()) {
                        sb.append(it.next());
                    }
                    while(it.hasNext()) {
                        sb.append(",").append(it.next());
                    }
                    result = sb.toString();
                }
                Spin_ProductId=result;
                Log.e("Spin_ProductId", Spin_ProductId);
              //  Toast.makeText(ActivitySubscription.this, "mm "+Spin_Student, Toast.LENGTH_SHORT).show();

                String pass_array=subscriptionAdapter.PassArray();


                if (pass_array!=null && !pass_array.isEmpty()){
                    Intent intent=new Intent(ActivitySubscription.this, SubscribePlaceOrder.class);
                    intent.putExtra("Spin_ProductId", Spin_ProductId);
                    intent.putExtra("passArray",pass_array );
                    startActivity(intent);
                }else {
                    Toast.makeText(ActivitySubscription.this, "Please select product", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getSubscriptionProduct() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_subscription_product,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrr_subs", response.toString());
                        try {
                            addCartList.clear();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    Log.d("subsitem", heroArray.toString());
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
                                Toast.makeText(ActivitySubscription.this, "No item available", Toast.LENGTH_SHORT).show();

                            }

                            Log.d("Addcart", "" + addCartList.size());
                            subscriptionAdapter = new SubscriptionAdapter(ActivitySubscription.this, addCartList);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivitySubscription.this, RecyclerView.VERTICAL, false);
                            recycler_subscription.setLayoutManager(mLayoutManger);
                            recycler_subscription.setAdapter(subscriptionAdapter);
                            subscriptionAdapter.notifyDataSetChanged();

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


        VolleySingleton.getInstance(ActivitySubscription.this).addToRequestQueue(stringRequest);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LinkedMultiselect.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LinkedMultiselect.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LinkedMultiselect.clear();
    }

    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(ActivitySubscription.this)) {
            getSubscriptionProduct();
        } else {
            Toast.makeText(ActivitySubscription.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }
}

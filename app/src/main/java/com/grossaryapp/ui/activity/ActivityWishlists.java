package com.grossaryapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.InCart.AddcartActivity;
import com.grossaryapp.ui.adapter.WishlistAdapter;
import com.grossaryapp.ui.model.GetLatestProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityWishlists extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ImageView iv_back;
    RecyclerView recycler_wishes;
    Session session;
    WishlistAdapter wishlistAdapter;
    SwipeRefreshLayout swipeToRefresh;
    List<GetLatestProducts> WishLists=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlists);
        session=new Session(ActivityWishlists.this);

        recycler_wishes=findViewById(R.id.recycler_wishes);
        iv_back=findViewById(R.id.iv_back);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeToRefresh.setOnRefreshListener(ActivityWishlists.this);

        if (Connectivity.isConnected(ActivityWishlists.this)){
            GetWishlistPro();
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void GetWishlistPro() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_wishlist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrcart", response.toString());
                        try {
                            WishLists.clear();
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
                                    GetLatestProducts wishlistModel = new GetLatestProducts(
                                            heroObject.getString("id"),
                                            heroObject.getString("url_slug"),
                                            heroObject.getString("product_id"),
                                            heroObject.getString("name"),
                                            heroObject.getString("mrp"),
                                            heroObject.getString("price"),
                                            heroObject.getString("sub_cate_name"),
                                            heroObject.getString("cate_name"),
                                            heroObject.getString("image"));

                                    WishLists.add(wishlistModel);
                                }
                                swipeToRefresh.setVisibility(View.VISIBLE);
                            } else {
                                swipeToRefresh.setVisibility(View.GONE);
                                Toast.makeText(ActivityWishlists.this, "No item available", Toast.LENGTH_SHORT).show();

                            }

                           // Log.d("Addcart", "" + addCartList.size());
                            wishlistAdapter = new WishlistAdapter(ActivityWishlists.this, WishLists);
                            GridLayoutManager manager = new GridLayoutManager(ActivityWishlists.this, 2, RecyclerView.VERTICAL, false);
                            recycler_wishes.setLayoutManager(manager);
                            recycler_wishes.setAdapter(wishlistAdapter);
                            wishlistAdapter.notifyDataSetChanged();

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

        VolleySingleton.getInstance(ActivityWishlists.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {


        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(ActivityWishlists.this)) {
            GetWishlistPro();
        } else {
            Toast.makeText(ActivityWishlists.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }
}

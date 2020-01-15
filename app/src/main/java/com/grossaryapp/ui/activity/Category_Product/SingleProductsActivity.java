package com.grossaryapp.ui.activity.Category_Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.grossaryapp.ui.activity.InCart.AddcartActivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.adapter.CustomRecyclerGetRelatedProducts;
import com.grossaryapp.ui.adapter.CustomRecyclerSingleProductAdapter;
import com.grossaryapp.ui.model.AddCart;
import com.grossaryapp.ui.model.GetRelatedProducts;
import com.grossaryapp.ui.model.SingleProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.add_to_favourite;
import static com.grossaryapp.ui.Retrofit.Base_Url.add_to_subscription;

public class SingleProductsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ImageView image,favimage2, favimage,iv_back;
    RelativeLayout rl_wallet;
    TextView name1,mrp1,price1,quantity1,tv_seeall,tv_cart_total;
    Context context;
    String url_slug;
    Button add_to_cart,add_to_subs;
    RecyclerView recyclerView1;
    CustomRecyclerSingleProductAdapter nAdapter;
    RecyclerView.LayoutManager layoutManager1;
    List<SingleProduct> singleProductList;
    List<AddCart> addCartList=new ArrayList<>();
    RequestQueue rq1;
    private static final String URL_SingleProduct = "http://logicalsofttech.com/goodcart/Api/single_product";

    RecyclerView recyclerView2;
    CustomRecyclerGetRelatedProducts kAdapter;
    RecyclerView.LayoutManager layoutManager2;
    List<GetRelatedProducts> getRelatedProductsList;
    Session session;
    private String product_id,id;
    SwipeRefreshLayout swipeToRefresh;
    LinearLayout ll_plus_minus;
    ImageView iv_plus,iv_minus;
    TextView tv_count_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_products);
        context=SingleProductsActivity.this;

        session=new Session(this);
        image = findViewById(R.id.image);
        favimage=findViewById(R.id.favimage);
        tv_cart_total=findViewById(R.id.tv_cart_total);
        add_to_subs=findViewById(R.id.add_to_subs);
        ll_plus_minus=findViewById(R.id.ll_plus_minus);
        iv_plus=findViewById(R.id.iv_plus);
        iv_minus=findViewById(R.id.iv_minus);
        tv_count_number=findViewById(R.id.tv_count_number);

        favimage2 = findViewById(R.id.favimage2);
        rl_wallet = findViewById(R.id.tv_wallet);
        name1 = findViewById(R.id.name);
        mrp1 = findViewById(R.id.mrp);
        price1 = findViewById(R.id.price);
        quantity1 = findViewById(R.id.quantity);
        add_to_cart = findViewById(R.id.add_to_cart);
        tv_seeall = findViewById(R.id.tv_seeall);
        iv_back = findViewById(R.id.iv_back);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeToRefresh.setOnRefreshListener(SingleProductsActivity.this);


        try {
            Intent intent=getIntent();
            url_slug=intent.getStringExtra("url_slug");
            Log.d("www",url_slug.toString());
        }catch (Exception e){

        }

        recyclerView1 = (RecyclerView) findViewById(R.id.recyclersingle);
        singleProductList=new ArrayList<>();
        recyclerView2 = (RecyclerView) findViewById(R.id.relatedproducts);
        getRelatedProductsList=new ArrayList<>();

        if (Connectivity.isConnected(this)){
            Log.e("user_id_login", session.getUserId());
            ProductList();
        }else Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();


        rl_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SingleProductsActivity.this, AddcartActivity.class);
                startActivity(intent);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isConnected(SingleProductsActivity.this)){
                    Add_toCart();
                }else Toast.makeText(SingleProductsActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();

            }
        });

        tv_seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SingleProductsActivity.this, CategoryActivity.class);
                startActivity(intent);

            }
        });

        favimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isConnected(context)){
                    add_to_favouriteApi(id);
                }else {
                    Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        favimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "already added wishlist", Toast.LENGTH_SHORT).show();

            }
        });
    add_to_subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (add_to_subs.getText().equals("SUBSCRIBED")){
                    Toast.makeText(SingleProductsActivity.this, "Already subscribe", Toast.LENGTH_SHORT).show();
                }else {
                    if (Connectivity.isConnected(context)){
                        Add_to_Subscribe();
                    }else {
                        Toast.makeText(context, "Please check internet", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

    //************************************************
        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=id;


                int qty = Integer.valueOf(tv_count_number.getText().toString());
                qty = qty + 1;

                // holder.tv_count_number.setText(String.valueOf(qty));

                // String qty_nmbr=holder.tv_count_number.getText().toString();
                if (Connectivity.isConnected(context)){
                    UpdateItemInCart(item_id,String.valueOf(qty));
                }

            }
        });

        //*********************************************
        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_id=id;

                int qty = Integer.valueOf(tv_count_number.getText().toString());
                if (qty > 0) {
                    qty = qty - 1;
                    // holder.tv_count_number.setText(String.valueOf(qty));
                }

                // String qty_nmbr=holder.tv_count_number.getText().toString();
                if (Connectivity.isConnected(context)){
                    UpdateItemInCart(item_id,String.valueOf(qty));
                }

            }
        });

    }

    private void UpdateItemInCart(final String item_id, final String qty_nmbr) {

        Log.e("item_id1", item_id);
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

                                if (!qty_nmbr.equalsIgnoreCase("0")){
                                    tv_count_number.setText(qty_nmbr);

                                }else {
                                    add_to_cart.setVisibility(View.VISIBLE);
                                    ll_plus_minus.setVisibility(View.GONE);
                                }

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

//                                    if (i==addposition){
//                                        addCarts.get(addposition).setQuantity(qty);
//                                        tv_count_number.setText(addCarts.get(addposition).getQuantity());
//                                        Log.e("update_qty", tv_count_number.getText().toString());
//                                    }
                                }
                            } else {
                            }


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

    private void Add_to_Subscribe() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, add_to_subscription,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("sss12",response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");


                            if (result.equalsIgnoreCase("true")) {
//                                JSONObject data=obj.getJSONObject("data");
                                Toast.makeText(SingleProductsActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                                add_to_subs.setText("SUBSCRIBED");

                            }
                            else {
                                Toast.makeText(SingleProductsActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
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
                params.put("product_id", id);
                params.put("user_id",  session.getUserId());
                params.put("quantity",  "1");

                return params;
            }
        };



        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void add_to_favouriteApi(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, add_to_favourite,
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
                                favimage2.setVisibility(View.VISIBLE);
                                favimage.setVisibility(View.GONE);


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
                params.put("product_id", id);
                params.put("user_id",  session.getUserId());

                return params;
            }
        };



        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void Add_toCart() {
        String url=Base_Url.BASE_URL+Base_Url.add_to_cart_Api;
//    Log.e("product_id", product_id);
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
                                    tv_cart_total.setText(total_item_count.toString());
                                    JSONArray heroArray = obj.getJSONArray("data");
                                    Toast.makeText(SingleProductsActivity.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();

//                                    if(cart_status.equalsIgnoreCase("0")){
//                                        add_to_cart.setVisibility(View.VISIBLE);
//                                        ll_plus_minus.setVisibility(View.GONE);
//                                    }else {
                                        add_to_cart.setVisibility(View.GONE);
                                        ll_plus_minus.setVisibility(View.VISIBLE);
                                   // }

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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_id", id);
                    params.put("quantity", "1");
                    params.put("user_id", session.getUserId());

                    return params;
                }
            };



            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);



        }

    private void ProductList() {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_SingleProduct,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("sss",response.toString());
                            try {
                                singleProductList.clear();
                                getRelatedProductsList.clear();

                                JSONObject obj = new JSONObject(response);
                                String result = obj.getString("result");
                                Integer total_item_count = obj.getInt("total_item_count");

                                tv_cart_total.setText(total_item_count.toString());

                                JSONObject obj1=new JSONObject(response);

                                if (result.equalsIgnoreCase("true")) {
                                    JSONObject data=obj1.getJSONObject("data");

                                    id=data.getString("id");
                                    String sub_cate_id=data.getString("sub_cate_id");
                                    String url_slug=data.getString("url_slug");
                                     product_id=data.getString("product_id");
                                    String name=data.getString("name");
                                    String mrp=data.getString("mrp");
                                    String price=data.getString("price");
                                    String description=data.getString("description");
                                    String sub_cate_name=data.getString("sub_cate_name");
                                    String cate_name=data.getString("cate_name");
                                    String image=data.getString("image");
                                    String total_review=data.getString("total_review");
                                    String avg_rating=data.getString("avg_rating");
                                    String wishlist_status=data.getString("wishlist_status");
                                    String cart_status=data.getString("cart_status");
                                    String cart_quantity=data.getString("cart_quantity");
                                    String subcription_status=data.getString("subcription_status");
                                    String subcription_quantity=data.getString("subcription_quantity");

                                    //Log.d("hhh",name.toString());
                                   // Log.d("wishlist_status",wishlist_status.toString());

                                    if (subcription_status.equalsIgnoreCase("1")){
                                    add_to_subs.setText("SUBSCRIBED");

                                    }else {
                                        add_to_subs.setText("ADD SUBSCRIBE");
                                    }

                                    if(cart_status.equalsIgnoreCase("0")){
                                        add_to_cart.setVisibility(View.VISIBLE);
                                        ll_plus_minus.setVisibility(View.GONE);
                                    }else {
                                        add_to_cart.setVisibility(View.GONE);
                                        ll_plus_minus.setVisibility(View.VISIBLE);
                                    }

                                    if (cart_quantity!=null && !cart_quantity.equalsIgnoreCase("null")){
                                        tv_count_number.setText(cart_quantity.toString());

                                    }


                                    Log.e("wishlist_status", wishlist_status);
                                    if (wishlist_status.equalsIgnoreCase("0")){
                                        favimage.setVisibility(View.VISIBLE);
                                        favimage2.setVisibility(View.GONE);
                                    }else {
                                        favimage2.setVisibility(View.VISIBLE);
                                        favimage.setVisibility(View.GONE);
                                    }

                                    name1.setText(name);
                                    mrp1.setText(mrp);
                                    price1.setText(price);
                                   // quantity1.setText("Quantity- "+ product_id);

                                    JSONArray heroArray = data.getJSONArray("images");

                                    //now looping through all the elements of the json array
                                    for (int i = 0; i < heroArray.length(); i++) {
                                        Log.d("<><>a", heroArray.toString());
                                        //getting the json object of the particular index inside the array
                                        JSONObject heroObject = heroArray.getJSONObject(i);

                                        //creating a hero object and giving them the values from json object
                                        SingleProduct hero = new SingleProduct(
                                                heroObject.getString("id"),
                                                heroObject.getString("product_id"),
                                                heroObject.getString("image"));

                                        singleProductList.add(hero);
                                       // Log.d("jjjj", "" + heroObject.getString("image"));

                                    }
                                      JSONArray relatedArray=data.getJSONArray("related_products");
                                        for (int i = 0; i < relatedArray.length(); i++) {
                                            Log.d("<><>mm", relatedArray.toString());
                                            JSONObject relatedobject = relatedArray.getJSONObject(i);
                                            GetRelatedProducts hero = new GetRelatedProducts(
                                                    relatedobject.getString("id"),
                                                    relatedobject.getString("url_slug"),
                                                    relatedobject.getString("product_id"),
                                                    relatedobject.getString("name"),
                                                    relatedobject.getString("mrp"),
                                                    relatedobject.getString("price"),
                                                    relatedobject.getString("quantity"),
                                                    relatedobject.getString("sub_cate_name"),
                                                    relatedobject.getString("cate_name"),
                                                    relatedobject.getString("image"));


                                           getRelatedProductsList.add(hero);

                                    }

                                //*******set Adapter
                                    nAdapter = new CustomRecyclerSingleProductAdapter(SingleProductsActivity.this, singleProductList,wishlist_status);
                                    RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(SingleProductsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    recyclerView1.setLayoutManager(mLayoutManger);
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView1.setAdapter(nAdapter);
                                    nAdapter.notifyDataSetChanged();


                                    kAdapter = new CustomRecyclerGetRelatedProducts(SingleProductsActivity.this, getRelatedProductsList);
                                    RecyclerView.LayoutManager nLayoutManger = new LinearLayoutManager(SingleProductsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    recyclerView2.setLayoutManager(nLayoutManger);
                                    recyclerView2.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView2.setAdapter(kAdapter);
                                    kAdapter.notifyDataSetChanged();

                                    swipeToRefresh.setVisibility(View.VISIBLE);
                                }
                                else {
                                    swipeToRefresh.setVisibility(View.GONE);
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
                    params.put("url_slug", url_slug);
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
    public void onRefresh() {
        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(SingleProductsActivity.this)) {
            ProductList();
        } else {
            Toast.makeText(SingleProductsActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Connectivity.isConnected(SingleProductsActivity.this)) {
            ProductList();
        } else {
            Toast.makeText(SingleProductsActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

    }
}


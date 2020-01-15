package com.grossaryapp.ui.activity.Category_Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.activity.InCart.AddcartActivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.adapter.CustomRecyclerAdapter;
import com.grossaryapp.ui.adapter.CustomRecyclerGetCategorywise;
import com.grossaryapp.ui.model.GetProductCategorywise;
import com.grossaryapp.ui.model.GetproductAllCategory;
import com.grossaryapp.ui.model.Navigationcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private TabLayout tabLayout;
    TextView All;
    SwipeRefreshLayout swipeToRefresh;
    private LinearLayout ll_no_data_found, linearlayoutcategory,linearlayoutcategoryget,linearlayoutcategory1;

    //This is our viewPager
    private ViewPager viewPager;
    TextView filter;
    SearchView search;
    Context context;
    String  url_slug;

    RecyclerView recyclerView;
    CustomRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Navigationcategory> navigationcategorieslist;
    RequestQueue rq;
    private static final String JSON_URL = "http://logicalsofttech.com/goodcart/Api/getCategories";

    RecyclerView recyclerView1;
    CustomRecyclerGetCategorywise nAdapter;
    RecyclerView.LayoutManager layoutManager1;
    List<GetProductCategorywise> getProductCategorywiseList;
    RequestQueue rq1;
    private static final String URL_TAB = "http://logicalsofttech.com/goodcart/Api/getProductCategoryWise";

    RecyclerView recyclerView2;
    RecyclerView.LayoutManager layoutManager2;
    List<GetproductAllCategory> getproductAllCategoryList;
    RequestQueue rq2;
    ImageView iv_back;
    RelativeLayout rl_wallet;
    public static TextView tv_count_cart;
     String Cat_id;
     String filter_type="alphabetical";
     Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        session=new Session(this);

        context=CategoryActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filter = findViewById(R.id.filter);
        search = findViewById(R.id.search);
        tv_count_cart = findViewById(R.id.tv_count_cart);
        rl_wallet = findViewById(R.id.tv_wallet);
        iv_back = findViewById(R.id.iv_back);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        ll_no_data_found = findViewById(R.id.ll_no_data_found);
        linearlayoutcategory=findViewById(R.id.linearlayoutcategory);
        linearlayoutcategoryget=findViewById(R.id.linearlayoutcategoryget);
        linearlayoutcategory1=findViewById(R.id.linearlayoutcategory1);
        All=findViewById(R.id.All);
        All.setText("All");

        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeToRefresh.setOnRefreshListener(CategoryActivity.this);


        rq = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView1 = (RecyclerView) findViewById(R.id.recycleViewContainer4);
        getProductCategorywiseList=new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);

      /*  recyclerView.setLayoutManager(layoutManager);*/

        navigationcategorieslist = new ArrayList<>();
        getproductAllCategoryList=new ArrayList<>();
        recyclerView2=findViewById(R.id.recycleViewContainer4);

        if (Connectivity.isConnected(CategoryActivity.this)){
            sendRequest1();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       // viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // viewPager.setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
                Cat_id = navigationcategorieslist.get(position).getId();

                Log.d("mmm",Cat_id.toString());
                if (Connectivity.isConnected(CategoryActivity.this)){
                    call(Cat_id,filter_type);
                }else Toast.makeText(CategoryActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

                });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(CategoryActivity.this, ActivityFilter.class);
               // startActivity(intent);

                PopupWindow popupwindow_obj = popupDisplay();
                popupwindow_obj.showAsDropDown(filter, -40, 18);
               // popupDisplay();


            }
        });
        search.onActionViewExpanded();
        search.setIconified(true);
        search.clearFocus();
        search.setFocusable(false);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterSearch(newText);
                return false;
            }
        });
        rl_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, AddcartActivity.class);
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });


    }

    private void filterSearch(String newText) {
            // ArrayList<SearchProductList> temp = new ArrayList();
            ArrayList <GetProductCategorywise> Contact_li= new ArrayList<GetProductCategorywise>();
            for (GetProductCategorywise smodel : getProductCategorywiseList) {
                //use .toLowerCase() for better matches
                if (smodel.getName().toLowerCase().startsWith(newText.toLowerCase())) {
                    Contact_li.add(smodel);
                }
            }
            //update recyclerview
        nAdapter.updateList(Contact_li);

    }

    public PopupWindow popupDisplay() {
        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) CategoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.flitertab, null);

        final RadioButton radio_low_high = view.findViewById(R.id.radio_low_high);
        final RadioButton radio_high_low = view.findViewById(R.id.radio_high_low);
        final RadioButton radio_alpha = view.findViewById(R.id.radio_alpha);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        radio_low_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_low_high.setChecked(true);
                radio_high_low.setChecked(false);
                radio_alpha.setChecked(false);

                filter_type="low_to_high";
                call(Cat_id,filter_type);
                popupWindow.dismiss();

            }
        });

        radio_high_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_low_high.setChecked(false);
                radio_high_low.setChecked(true);
                radio_alpha.setChecked(false);

              filter_type="high_to_low";
                call(Cat_id,filter_type);
                popupWindow.dismiss();

            }
        });
        radio_alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_low_high.setChecked(false);
                radio_high_low.setChecked(false);
                radio_alpha.setChecked(true);

                filter_type="alphabetical";
                call(Cat_id,filter_type);
                popupWindow.dismiss();

            }
        });


        return popupWindow;

       }


    private void call(final String cat_id, final String filter_type) {

      //  final String id = navigationcategorieslist.get(position).getId();

       // Log.d("nnn",id.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("nami",response.toString());
                        try {
                            getProductCategorywiseList.clear();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            Integer total_item_count = obj.getInt("total_item_count");
                            tv_count_cart.setText(total_item_count.toString());
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    Log.d("<><>c", heroArray.toString());
                                    //getting the json object of the particular index inside the array
                                    JSONObject heroObject = heroArray.getJSONObject(i);

                                    //creating a hero object and giving them the values from json object
                                    GetProductCategorywise hero = new GetProductCategorywise(
                                            heroObject.getString("id"),
                                            heroObject.getString("url_slug"),
                                            heroObject.getString("product_id"),
                                            heroObject.getString("name"),
                                            heroObject.getString("mrp"),
                                            heroObject.getString("price"),
                                            heroObject.getString("sub_cate_name"),
                                            heroObject.getString("cate_name"),
                                            heroObject.getString("image"),
                                            heroObject.getString("cart_status"),
                                            heroObject.getString("cart_quantity"));

                                    getProductCategorywiseList.add(i,hero);
                                    Log.d("getproductCategory", "" + getProductCategorywiseList.size());
                                    swipeToRefresh.setVisibility(View.VISIBLE);
                                }
                            } else {
                                swipeToRefresh.setVisibility(View.GONE);
                            }


                            nAdapter = new CustomRecyclerGetCategorywise(CategoryActivity.this, getProductCategorywiseList);
                            @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(CategoryActivity.this, LinearLayoutManager.VERTICAL, false);
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
                params.put("category_id", cat_id);
                params.put("filter", filter_type);
              params.put("user_id",session.getUserId() );

                return params;
            }
        };



        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void sendRequest1() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><><>", response.toString());
                        try {
                            navigationcategorieslist.clear();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {
                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    Log.d("error",heroArray.toString());
                                    //getting the json object of the particular index inside the array
                                    JSONObject heroObject = heroArray.getJSONObject(i);

                                        Navigationcategory hero = new Navigationcategory(
                                                heroObject.getString("id"),
                                                heroObject.getString("name"),
                                                heroObject.getString("image"));

                                        navigationcategorieslist.add(i,hero);

                                }

                                Log.d("categorysize", "" + navigationcategorieslist.size());



                            } else {
                                Toast.makeText(CategoryActivity.this, "error",Toast.LENGTH_LONG).show();
                            }
                            Log.e("sizeCategory", "" + navigationcategorieslist.size());
                            if (tabLayout != null) {
                                tabLayout.removeAllTabs();
                            }



                            Navigationcategory hero = new Navigationcategory("0",
                                    "All","rrr.png");
                            navigationcategorieslist.add(0,hero);
                            Log.d("tab_categorieslist", "" + navigationcategorieslist.size());

                            for (int j = 0; j < navigationcategorieslist.size(); j++) {

                                tabLayout.addTab(tabLayout.newTab().setText(navigationcategorieslist.get(j).getName()));
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
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //viewPager.setCurrentItem(tab.getPosition());

        int position = tab.getPosition();
        Cat_id = navigationcategorieslist.get(position).getId();
         call(Cat_id,filter_type);
    }

    @Override
        public void onTabUnselected (TabLayout.Tab tab){

        }

        @Override
        public void onTabReselected (TabLayout.Tab tab){

        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(false);

        if (Connectivity.isConnected(CategoryActivity.this)) {

            // String url = Base_Url.BaseUrl + getOrderByRestaurant_Id;
            call(Cat_id,filter_type);
            //ToastClass.showToast(getContext(),"new fragemnt");
        } else {
            Toast.makeText(CategoryActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Connectivity.isConnected(CategoryActivity.this)){
            sendRequest1();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

    }
}

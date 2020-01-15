package com.grossaryapp.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.Category_Product.CategoryActivity;
import com.grossaryapp.ui.activity.InCart.AddcartActivity;
import com.grossaryapp.ui.activity.InCart.OrderhistoryActivity;
import com.grossaryapp.ui.activity.Profile.ActivityProfile;
import com.grossaryapp.ui.activity.SubscriptionProduct.ActivitySubscription;
import com.grossaryapp.ui.adapter.CustomPagerAdapter;
import com.grossaryapp.ui.adapter.CustomRecyclerAdapter;
import com.grossaryapp.ui.adapter.CustomRecyclerAdapter2;
import com.grossaryapp.ui.adapter.CustomRecyclerAdapterproducts;
import com.grossaryapp.ui.adapter.LeftDrawerAdapter;
import com.grossaryapp.ui.gps.GPSTracker;
import com.grossaryapp.ui.model.DrawerItem;
import com.grossaryapp.ui.model.GetLatestProducts;
import com.grossaryapp.ui.model.Getplans;
import com.grossaryapp.ui.model.Navigation;
import com.grossaryapp.ui.model.Navigationcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.grossaryapp.ui.Retrofit.Base_Url.Images_Profile;

public class ActivityNavigation extends AppCompatActivity
        implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 101;
    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    private ListView mDrawerList;
    private ImageView iv_drawer;
    LinearLayout ll_drawer;
    private TextView tv_name, viewall;
    RelativeLayout tv_wallet;
    LeftDrawerAdapter leftDrawerAdapter;
    public ArrayList<DrawerItem> List_Item;
    public ArrayList<Navigation> hero = new ArrayList<>();
    ;
    public ArrayList<String> List_Item2 = new ArrayList<>();
    public int ScreenPos = 0;
    DrawerItem drawerItem;
    boolean doubleBackToExitPressedOnce = false;
    private GPSTracker tracker;
    private Double lat;
    private Double lng;
    public ImageView  location, locback, notification;
    public static ImageView nav_img_profile;
    public LinearLayout ll_nav_header;

    private TextView nav_tv_name, nav_tv_viewProfile,tv_total_count;

    private ViewPager VP_banner_slidder;
    private CircleIndicator CI_indicator;
   
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private String URL_BANNERS = "http://logicalsofttech.com/goodcart/Api/getBanners";
    public int[] sliderImage = {
            R.drawable.home_slider1,
            R.drawable.home_slider2,
            R.drawable.home_slider3
    };
    ImageView circleView;

    ListView listView;
    List<Navigationcategory> heroList;
    private View header;


       RecyclerView recyclerView;
    CustomRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Navigationcategory> navigationcategorieslist;
    RequestQueue rq;
    private static final String JSON_URL = "http://logicalsofttech.com/goodcart/Api/getCategories";

    RecyclerView recyclerView1;
    CustomRecyclerAdapter2 nAdapter;
    RecyclerView.LayoutManager layoutManager1;
    List<Getplans> getplansList;
    RequestQueue rq1;
    private static final String PLANS_URL = "http://logicalsofttech.com/goodcart/Api/getPlans";

    RecyclerView recyclerView2;
    CustomRecyclerAdapterproducts rAdapter;
    RecyclerView.LayoutManager layoutManager2;
    List<GetLatestProducts>getLatestProductsList ;
    RequestQueue rq2;
    private static final String PRODUCTS_URL = "http://logicalsofttech.com/goodcart/Api/getLatestProduct";
    Session session;

    TextView tv_see_all_plans;
    //SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        rq = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        session=new Session(ActivityNavigation.this);
//        swipeToRefresh = findViewById(R.id.swipeToRefresh);
//        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
//        swipeToRefresh.setOnRefreshListener(ActivityNavigation.this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        navigationcategorieslist = new ArrayList<>();

        sendRequest();

        rq1 = Volley.newRequestQueue(this);
        recyclerView1 = (RecyclerView) findViewById(R.id.recycleViewContainer1);
        recyclerView1.setHasFixedSize(true);

        layoutManager1 = new LinearLayoutManager(this);

        recyclerView1.setLayoutManager(layoutManager1);

        getplansList = new ArrayList<>();

        Getplans();

        rq2 = Volley.newRequestQueue(this);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycleViewContainer3);

       getLatestProductsList = new ArrayList<>();

        GetLatestProducts();

        /*session = new Session(this);
        if (session != null) {
            user_id = session.getUser().user_id;
            user_name = session.getUser().user_name;
            level = session.getUser().level_rides;
            image = session.getUser().image;
            fcm_id = session.getTokenId();
        }*/

        tracker = new GPSTracker(this);


        initView();
        clickListner();

    }


    private void initView() {
        List_Item = new ArrayList<>();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        iv_drawer = findViewById(R.id.iv_drawer);
        ll_drawer = findViewById(R.id.ll_drawer);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_see_all_plans = findViewById(R.id.tv_see_all_plans);
        tv_total_count = findViewById(R.id.tv_total_count);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        //mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        View header = getLayoutInflater().inflate(R.layout.nav_header_activity_navigation, null);
        ll_nav_header = header.findViewById(R.id.ll_nav_header);
        nav_img_profile = header.findViewById(R.id.nav_img_profile);
        nav_tv_name = header.findViewById(R.id.nav_tv_name);
        nav_tv_viewProfile = header.findViewById(R.id.nav_tv_viewProfile);

        try {
            nav_tv_name.setText(session.getUser_name());
            nav_tv_viewProfile.setText(session.getEmail());
        }catch (Exception e){

        }

        setProfileImgName();


        VP_banner_slidder = findViewById(R.id.user_home_viewpager);
        CI_indicator = findViewById(R.id.user_home_ci_indicator);
        location = findViewById(R.id.location);
        locback = findViewById(R.id.locback);
        viewall = findViewById(R.id.viewall);
        notification = findViewById(R.id.notification);
        circleView = findViewById(R.id.circleView);
        heroList = new ArrayList<>();


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this,LocationActivity.class);
                startActivity(intent);
            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this,ActivityNotification.class);
                startActivity(intent);
            }
        });

        tv_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this, AddcartActivity.class);
                startActivity(intent);
            }
        });

        tv_see_all_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this,SeeAllPlansActivity.class);
                startActivity(intent);
            }
        });

        if (tracker.canGetLocation() == true) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + String.valueOf(lat));
            Log.e("current_Lon ", " " + String.valueOf(lng));
            //getAddress(lat,lng);
        } else if (tracker.canGetLocation() == false) {

            tracker.showSettingsAlert();
        }

        /*if (NetworkUtil.isNetworkConnected(this)) {
            try {
                //url = API.BASE_URL + "getNearByPlace";
                url = API.BASE_URL + "getNearByRatePlace";
                Log.e("getNearByRatePlace url", url);
                getNearPlaceList(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }*/



        mDrawerList.addHeaderView(header);

       /* mAdapter = new NearTouristListAdapter(touristList, this);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);*/


        //Drawer Item
        DrawerItem();
        //Setup Drawer
        SetupDrawer();


        //loadHeroList();

        /*final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String main_title = editTextPassword.getText().toString().trim();*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BANNERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("<><><", response.toString());
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("<><><", heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                hero.add(i, new Navigation(heroObject.getString("id"), heroObject.getString("image"),
                                        heroObject.getString("main_title"), heroObject.getString("sub_title")));

                                //adding the hero to herolist

                                //call benner
                                Banner_Call(hero);
                                Log.d("imagesize", "" + hero.size());

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

        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    public void setProfileImgName() {

        Glide.with(ActivityNavigation.this)
                .load(Images_Profile + session.getPro_Image())
                .placeholder(R.drawable.ic_logo)
                //.override(300, 200)
                //.error(R.drawable.ic_logo)
                .into(nav_img_profile);
    }


    public void sendRequest() {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityNavigation.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkk", response.toString());
                        progressDialog.dismiss();
                        try {
                            navigationcategorieslist.clear();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            String result=obj.getString("result");
                            Log.d("checresult", result);
                            if (result.equalsIgnoreCase("true")){
                                Integer total_item_count=obj.getInt("total_item_count");
                                tv_total_count.setText(total_item_count.toString());
                            }

                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("error",heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Navigationcategory hero = new Navigationcategory(
                                        heroObject.getString("id"),
                                        heroObject.getString("name"),
                                        heroObject.getString("image"));

                                navigationcategorieslist.add(hero);

                            }
                            Log.d("categorysize_pro", "" + navigationcategorieslist.size());
                            Log.d("categorysize_api", "" + heroArray.length());


                            mAdapter = new CustomRecyclerAdapter(ActivityNavigation.this, navigationcategorieslist);

                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityNavigation.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(mLayoutManger);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session.getUserId());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }


    public void  Getplans(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PLANS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("kkkk", response.toString());
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("error",heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                               Getplans hero = new Getplans(
                                        heroObject.getString("id"),
                                        heroObject.getString("name"),
                                       heroObject.getString("description"),
                                        heroObject.getString("image"));

                               getplansList.add(hero);
                                Log.d("categorysize", "" +  getplansList.size());

                            }


                            nAdapter = new CustomRecyclerAdapter2(ActivityNavigation.this, getplansList);

                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityNavigation.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView1.setLayoutManager(mLayoutManger);
                            recyclerView1.setItemAnimator(new DefaultItemAnimator());
                            recyclerView1.setAdapter(nAdapter);

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

    public void GetLatestProducts(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("nnnn", response.toString());
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                Log.d("err",heroArray.toString());
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                GetLatestProducts hero = new GetLatestProducts(
                                        heroObject.getString("id"),
                                        heroObject.getString("url_slug"),
                                        heroObject.getString("product_id"),
                                        heroObject.getString("name"),
                                        heroObject.getString("mrp"),
                                        heroObject.getString("price"),
                                        heroObject.getString("sub_cate_name"),
                                        heroObject.getString("cate_name"),
                                        heroObject.getString("image"));

                               getLatestProductsList.add(hero);
                                Log.d("categorysize", "" +  getLatestProductsList.size());

                            }


                            rAdapter = new CustomRecyclerAdapterproducts(ActivityNavigation.this, getLatestProductsList);

                           // RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(ActivityNavigation.this,2);
                            GridLayoutManager manager = new GridLayoutManager(ActivityNavigation.this, 2, RecyclerView.VERTICAL, false);
                            recyclerView2.setLayoutManager(manager);
                           // recyclerView2.addItemDecoration(new ItemOffsetDecoration(10));

                            //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityNavigation.this, R.dimen.item_offset);
                           //recyclerView2.addItemDecoration(itemDecoration);

                            recyclerView2.setAdapter(rAdapter);

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


/*
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this,LocationActivity.class);
                startActivity(intent);
            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this,CategoryActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityNavigation.this,ActivityNotification.class);
                startActivity(intent);
            }
        });*/





        //recyclerview = findViewById(R.id.recycler_view);
        //ll_no_record = findViewById(R.id.ll_no_record);
        //swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);


        //set header profile name image
        /*if (!user_name.equalsIgnoreCase(null) && !user_name.equalsIgnoreCase("")) {
            tv_name.setText(user_name);
            tv_level.setText(level + " level");
        }
        if (!image.equalsIgnoreCase(null) && !image.equalsIgnoreCase("")) {
            Picasso.with(this).load(image).placeholder(R.drawable.ic_profile).into(img_profile);
        }*/


        //if location is enable
       /* if (tracker.canGetLocation() == true) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + String.valueOf(lat));
            Log.e("current_Lon ", " " + String.valueOf(lng));
            //getAddress(lat,lng);
        } else if (tracker.canGetLocation() == false) {

            tracker.showSettingsAlert();
        }*/

        /*if (NetworkUtil.isNetworkConnected(this)) {
            try {
                //url = API.BASE_URL + "getNearByPlace";
                url = API.BASE_URL + "getNearByRatePlace";
                Log.e("getNearByRatePlace url", url);
                getNearPlaceList(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }*/


       
       /* mDrawerList.addHeaderView(header);

       *//* mAdapter = new NearTouristListAdapter(touristList, this);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);*//*


        //Drawer Item
        DrawerItem();
        //Setup Drawer
        SetupDrawer();*/





    private void clickListner() {

        iv_drawer.setOnClickListener(this);
        ll_drawer.setOnClickListener(this);
        ll_nav_header.setOnClickListener(this);

    }


    private void Banner_Call(ArrayList<Navigation> hero) {


        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this, hero);
        VP_banner_slidder.setAdapter(customPagerAdapter);
        CI_indicator.setViewPager(VP_banner_slidder);
        NUM_PAGES = this.hero.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                VP_banner_slidder.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
        public void run() {
            handler.post(Update);
        }
    }, 3000, 3000);

        CI_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    //Drawer Item Array
    public void DrawerItem() {

        List_Item.clear();

       List_Item.add(new DrawerItem(R.drawable.ic_home, "Home", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.orderhistory, "Order history", R.drawable.ic_expand));
        //List_Item.add(new DrawerItem(R.drawable.ic_location, "Location", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.membership, "Subscription", R.drawable.ic_expand));
       // List_Item.add(new DrawerItem(R.drawable.offer, "Offer", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.productsheart3, "Wishlists", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.ic_about_us, "Help Centre", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.ic_menu_share_r, "Share App", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.notification, "Notification", R.drawable.ic_expand));
        List_Item.add(new DrawerItem(R.drawable.ic_logout, "LogOut", R.drawable.ic_expand));
    }

    //Navigation Drawer Method
    public void SetupDrawer() {

        //Drawer Adapter

        leftDrawerAdapter = new LeftDrawerAdapter(ActivityNavigation.this, List_Item);

        //Set Adapter
        mDrawerList.setAdapter(leftDrawerAdapter);

        //Call first bydefault Fragment
        /*fragment = new FragmentHome();
        titletxt.setText("Home");
        //  imgheader.setVisibility(View.GONE);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_id, fragment).commit();*/

        //ListView click Listner
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                int position = pos - 1;
                if (position >= 0) {
                    //Call open Fragment
                    SelectOption(position);
                }
            }
        });

    }

    //screen Position update
    /*public void UpdatePosition(int pos) {

        ScreenPos = pos;
        SelectOption(ScreenPos);

    }*/

    //Drawer option open
    public void SelectOption(int pos) {

        ScreenPos = pos;

        //Selected Value Highlighted
        leftDrawerAdapter.setSelectedIndex(pos);

        //Get List Item
        drawerItem = List_Item.get(pos);
        Log.e("Position......", String.valueOf(pos));
        String Item_Name = drawerItem.getItemName();
        Log.e("Position......", drawerItem.getItemName());
        //Call Fragment on a listview click listner
        if (Item_Name.equals("Home")) {
            Intent intent = new Intent(this, ActivityHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            mDrawerLayout.closeDrawer(mDrawerList);

        }  else if (Item_Name.equals("Order history")) {
            Intent intent = new Intent(this, OrderhistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            mDrawerLayout.closeDrawer(mDrawerList);


        } else if (Item_Name.equals("Location")) {
            Intent intent = new Intent(this, LocationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            mDrawerLayout.closeDrawer(mDrawerList);

        } else if (Item_Name.equals("Notification")) {
                Intent intent = new Intent(this, ActivityNotification.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                mDrawerLayout.closeDrawer(mDrawerList);

        } else if (Item_Name.equals("Subscription")) {
            Intent intent = new Intent(this, ActivitySubscription.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            mDrawerLayout.closeDrawer(mDrawerList);

        } else if (Item_Name.equals("Offer")) {

            Intent intent = new Intent(this, ActivityOffers.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            mDrawerLayout.closeDrawer(mDrawerList);

        }

        else if (Item_Name.equals("Help Centre")) {
            Intent intent = new Intent(this, ActivityHelpCenter.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

        else if (Item_Name.equals("Wishlists")) {
            Intent intent = new Intent(this, ActivityWishlists.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //Animatoo.animateZoom(this);
            //overridePendingTransition(R.anim.slide_top, R.anim.slide_down);
            mDrawerLayout.closeDrawer(mDrawerList);

        }

        else if (Item_Name.equals("Share App")) {
            shareApp();
        } else if (Item_Name.equals("LogOut")) {
            LogOut();
        }

    }

    private void LogOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Grossary")
                .setMessage("Are you sure, you want to logout this app");

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                session.logout();
                dialog.dismiss();

            }


        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void shareApp() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Illaj");
        share.putExtra(Intent.EXTRA_TEXT, "Welcome to Illaj! You can download app from Play Store:- https://play.google.com/store/apps/");
        startActivity(Intent.createChooser(share, "Share link!"));

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_drawer:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;

             case R.id.iv_drawer:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.ll_nav_header:
                intent = new Intent(ActivityNavigation.this, ActivityProfile.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.location:
                intent=new Intent(ActivityNavigation.this, LocationActivity.class);
             startActivity(intent);
            /*case R.id.locback:
                intent=new Intent(ActivityNavigation.this, LocationActivity.class);
                startActivity(intent);*/

        }

    }


    @Override
    public void onBackPressed() {

        Log.e("pos", "" + ScreenPos);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (ScreenPos == 0) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please Press Back again to exit the app", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {

            //Call home fragment
            SelectOption(0);

        }
    }

    @Override
    public void onRefresh() {

//        swipeToRefresh.setRefreshing(false);
//
//        if (Connectivity.isConnected(ActivityNavigation.this)) {
//            getCartItem();
//        } else {
//            Toast.makeText(ActivityNavigation.this, "Please check Internet", Toast.LENGTH_SHORT).show();
//        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
        setProfileImgName();

    }
}

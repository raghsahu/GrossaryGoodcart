package com.grossaryapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.grossaryapp.ui.adapter.CustomRecyclerAdapter2;
import com.grossaryapp.ui.adapter.NotificationAdapter;
import com.grossaryapp.ui.adapter.Pager3;
import com.grossaryapp.ui.model.Getplans;
import com.grossaryapp.ui.model.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.get_notification;

public class ActivityNotification extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    ImageView iv_back;
    RecyclerView recycler_noti;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel>notificationModels=new ArrayList<>();
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        session=new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = findViewById(R.id.iv_back);
        recycler_noti = findViewById(R.id.recycler_noti);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout1);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Offers"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager3);

        //Creating our pager adapter
        Pager3 adapter = new Pager3(getSupportFragmentManager(), tabLayout.getTabCount());
       /* viewPager.setAdapter(new Pager3(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);*/
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());

               int position = tab.getPosition();
               switch (position) {
                   case 0:

                       tabLayout.setSelectedTabIndicatorColor(Color.RED);
                       tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                       Log.e("Position", String.valueOf(0));
                       break;
                   case 1:
                       tabLayout.setSelectedTabIndicatorColor(Color.RED);
                       tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                       Log.e("Position", String.valueOf(1));
                       break;
                   /* case 2:
                        tabLayout.setSelectedTabIndicatorColor(Color.RED);
                        tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                        *//*tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*//*
                    *//* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*//*
                        Log.e("Position", String.valueOf(2));
                        break;*/
                   default:
                       break;
               }
           }



           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Connectivity.isConnected(ActivityNotification.this)){

            getAllNotification();
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void getAllNotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_notification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rr_noti", response.toString());
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
                                NotificationModel hero = new NotificationModel(
                                        heroObject.getString("id"),
                                        heroObject.getString("message"),
                                        heroObject.getString("date_time"));

                                notificationModels.add(hero);
                                //Log.d("categorysize", "" +  getplansList.size());

                            }


                            notificationAdapter = new NotificationAdapter(ActivityNotification.this, notificationModels);

                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityNotification.this, RecyclerView.VERTICAL, false);
                            recycler_noti.setLayoutManager(mLayoutManger);
                            recycler_noti.setAdapter(notificationAdapter);

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

            @Override
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


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());

        int position = tab.getPosition();
        switch (position) {
            case 0:

                tabLayout.setSelectedTabIndicatorColor(Color.RED);
                tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                Log.e("Position", String.valueOf(0));
                break;
            case 1:
                tabLayout.setSelectedTabIndicatorColor(Color.RED);
                tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                Log.e("Position", String.valueOf(1));
                break;
                   /* case 2:
                        tabLayout.setSelectedTabIndicatorColor(Color.RED);
                        tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                        *//*tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*//*
             *//* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*//*
                        Log.e("Position", String.valueOf(2));
                        break;*/
            default:
                break;
        }
    }



    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

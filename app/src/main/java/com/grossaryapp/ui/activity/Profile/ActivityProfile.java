package com.grossaryapp.ui.activity.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.grossaryapp.ui.activity.ActivityNavigation;
import com.grossaryapp.ui.activity.LocationActivity;
import com.grossaryapp.ui.activity.Login.ActivitySplash;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.adapter.AddressAdapter;
import com.grossaryapp.ui.model.AddressModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.Images_Profile;
import static com.grossaryapp.ui.activity.ActivityNavigation.nav_img_profile;

public class ActivityProfile extends AppCompatActivity {

    Session session;
    TextView tv_name,tv_mobile,tv_email;
    ImageView profile_image;
    LinearLayout ll_edit_profile,ll_logout;
    CardView card_change_pw,card_add_address;
    private Dialog Quitdialog;
    RecyclerView recycler_addres;
    ArrayList<AddressModel>addressModels=new ArrayList<>();
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session=new Session(this);

        tv_name=findViewById(R.id.tv_name);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_email=findViewById(R.id.tv_email);
        ll_edit_profile=findViewById(R.id.ll_edit_profile);
        profile_image=findViewById(R.id.profile_image);
        card_change_pw=findViewById(R.id.card_change_pw);
        ll_logout=findViewById(R.id.ll_logout);
        card_add_address=findViewById(R.id.card_add_address);
        recycler_addres=findViewById(R.id.recycler_addres);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Connectivity.isConnected(this)){
            String user_id=session.getUserId();
            Log.e("user_id", user_id);
            getProfile();
            getAddressApi();

        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutUser();

            }
        });

        ll_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ActivityProfile.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        card_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ActivityProfile.this, LocationActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        card_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePwDialog();


            }
        });
    }

    private void getAddressApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_shipping_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrProfile", response.toString());
                        try {
                            addressModels.clear();
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = obj.getJSONArray("data");
                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String id = jsonObject1.getString("id");
                                    String user_id = jsonObject1.getString("user_id");
                                    String first_name = jsonObject1.getString("first_name");
                                    String last_name = jsonObject1.getString("last_name");
                                    String email = jsonObject1.getString("email");
                                    String mobile = jsonObject1.getString("mobile");
                                    String address  = jsonObject1.getString("address");
                                    String address2 = jsonObject1.getString("address2");
                                    String city_id = jsonObject1.getString("city_id");
                                    String area_id = jsonObject1.getString("area_id");
                                    String google_location = jsonObject1.getString("google_location");
                                    String lat = jsonObject1.getString("lat");
                                    String lng = jsonObject1.getString("lng");
                                    String zipcode = jsonObject1.getString("zipcode");
                                    String country = jsonObject1.getString("country");
                                    String state = jsonObject1.getString("state");
                                    String address_image = jsonObject1.getString("address_image");
                                    String area_name = jsonObject1.getString("area_name");
                                    String city_name = jsonObject1.getString("city_name");

                                    addressModels.add(i, new AddressModel(id,user_id,first_name,last_name,email,mobile,address,
                                            address2, city_id,area_id,google_location,lat,lng,zipcode,country,state,address_image,
                                            area_name,city_name ));
                                }

                            } else {

                                // Toast.makeText(AddcartActivity.this, "No item available", Toast.LENGTH_SHORT).show();
                            }

                            addressAdapter = new AddressAdapter(ActivityProfile.this, addressModels);

                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityProfile.this, RecyclerView.HORIZONTAL, false);
                            recycler_addres.setLayoutManager(mLayoutManger);
                            recycler_addres.setItemAnimator(new DefaultItemAnimator());
                            recycler_addres.setAdapter(addressAdapter);
                            addressAdapter.notifyDataSetChanged();

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


        VolleySingleton.getInstance(ActivityProfile.this).addToRequestQueue(stringRequest);


    }

    private void logoutUser() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityProfile.this).setTitle(getString(R.string.app_name))
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
                // Shared_Pref.setUser_Id(ActivityProfile.this,"");
                Intent intent=new Intent(ActivityProfile.this, ActivitySplash.class);
                startActivity(intent);
                finish();

            }

        });
        final AlertDialog alert = dialog.create();
        alert.show();

    }

    private void ChangePwDialog() {
        Quitdialog = new Dialog(ActivityProfile.this);
        Quitdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Quitdialog.setCancelable(true);
        Quitdialog.setContentView(R.layout.change_pw_dialog);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btn_quit_yes=Quitdialog.findViewById(R.id.btn_update);
        final EditText et_password=Quitdialog.findViewById(R.id.et_password);
        final EditText et_new_password=Quitdialog.findViewById(R.id.et_new_password);
        final EditText et_conf_password=Quitdialog.findViewById(R.id.et_conf_password);


        btn_quit_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Old_pw=et_password.getText().toString();
                String New_pw=et_new_password.getText().toString();
                String Confirm_pw=et_conf_password.getText().toString();

                if (Connectivity.isConnected(ActivityProfile.this)){
                    UpdatePw(Old_pw,New_pw,Confirm_pw);
                }else {
                    Toast.makeText(ActivityProfile.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

               // Quitdialog.dismiss();
//                Intent intent=new Intent(ActivityProfile.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        try {
            if (!ActivityProfile.this.isFinishing()){
                Quitdialog.show();
            }
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }


    }

    private void UpdatePw(final String old_pw, final String new_pw, final String confirm_pw) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.change_password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrPw", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                JSONObject jsonObject1 = obj.getJSONObject("data");

                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String mobile = jsonObject1.getString("mobile");
                                String email = jsonObject1.getString("email");

                                Quitdialog.dismiss();
                                Toast.makeText(ActivityProfile.this, "Change successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                String msg = obj.getString("msg");
                                Quitdialog.dismiss();
                                Toast.makeText(ActivityProfile.this, ""+msg, Toast.LENGTH_SHORT).show();

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
                 params.put("old_password", old_pw);
                 params.put("new_password", new_pw);
                 params.put("confirm_password", confirm_pw);
                params.put("user_id", session.getUserId());

                return params;
            }
        };


        VolleySingleton.getInstance(ActivityProfile.this).addToRequestQueue(stringRequest);



    }

    private void getProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrProfile", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                JSONObject jsonObject1 = obj.getJSONObject("data");

                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String mobile = jsonObject1.getString("mobile");
                                String email = jsonObject1.getString("email");
                                String address = jsonObject1.getString("address");
                                String image = jsonObject1.getString("image");

                                tv_email.setText(email);
                                tv_name.setText(name);
                                tv_mobile.setText(mobile);


                                if (!image.isEmpty() && !image.equalsIgnoreCase("") && image!=null){

                                    Glide.with(ActivityProfile.this)
                                            .load(Images_Profile + image)
                                            .placeholder(R.drawable.orange1)
                                            //.override(300, 200)
                                           // .error(R.drawable.orange1)
                                            .into(profile_image);

                                    Glide.with(ActivityProfile.this)
                                            .load(Images_Profile + image)
                                            .placeholder(R.drawable.orange1)
                                            //.override(300, 200)
                                            // .error(R.drawable.orange1)
                                            .into(nav_img_profile);
                                }



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


        VolleySingleton.getInstance(ActivityProfile.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent=new Intent(ActivityProfile.this, ActivityNavigation.class);
//        startActivity(intent);
       finish();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getAddressApi();
    }
}

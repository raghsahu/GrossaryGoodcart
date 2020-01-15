package com.grossaryapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.Profile.Add_AddressActivity;
import com.grossaryapp.ui.gps.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.add_address_from_current_location;

public class LocationActivity extends AppCompatActivity {
    ImageView locback;
    private GPSTracker tracker;
    private Double lat;
    private Double lng;
    TextView tv_current_location,tv_find_city;
    String address,city,postalCode;

    Button btn_adddr_next,btn_my_location;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        session=new Session(this);

        tracker=new GPSTracker(LocationActivity.this);

        locback=findViewById(R.id.locback);
        tv_find_city=findViewById(R.id.tv_find_city);
        tv_current_location=findViewById(R.id.tv_current_location);
        btn_adddr_next=findViewById(R.id.btn_adddr_next);
        btn_my_location=findViewById(R.id.btn_my_location);


        locback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        btn_adddr_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LocationActivity.this, Add_AddressActivity.class);
                startActivity(intent);
            }
        });

        btn_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isConnected(LocationActivity.this)){
                    AddCurrentLocation();
                }else {
                    Toast.makeText(LocationActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (tracker.canGetLocation() == true) {
            lat = tracker.getLatitude();
            lng = tracker.getLongitude();
            Log.e("current_lat ", " " + String.valueOf(lat));
            Log.e("current_Lon ", " " + String.valueOf(lng));
            getAddress(lat,lng);
        } else if (tracker.canGetLocation() == false) {

            tracker.showSettingsAlert();
        }


    }

    private void AddCurrentLocation() {
        final ProgressDialog progressDialog = new ProgressDialog(LocationActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, add_address_from_current_location,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("add_current_loc",response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                                JSONObject obj1 = obj.getJSONObject("data");
                                String id = obj1.getString("id");
                                String user_id = obj1.getString("user_id");
                                String first_name = obj1.getString("first_name");
                                String last_name = obj1.getString("last_name");
                                String email = obj1.getString("email");
                                String mobile = obj1.getString("mobile");
                                String address = obj1.getString("address");
                                String address2 = obj1.getString("address2");
                                String city_id = obj1.getString("city_id");
                                String area_id = obj1.getString("area_id");
                                String google_location = obj1.getString("google_location");
                                String lat = obj1.getString("lat");
                                String lng = obj1.getString("lng");
                                String zipcode = obj1.getString("zipcode");
                                String country = obj1.getString("country");
                                String state = obj1.getString("state");
                                String address_image = obj1.getString("address_image");

                                onBackPressed();

                            }else{

                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session.getUserId());
                params.put("address", address);
                params.put("city_id", city);
                params.put("lat", lat.toString());
                params.put("lng", lng.toString());
                params.put("zipcode", postalCode);

                return params;
            }
        };

        VolleySingleton.getInstance(LocationActivity.this).addToRequestQueue(stringRequest);



    }


    private void getAddress(Double lat, Double lng) {

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                 city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                 postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                tv_current_location.setText(address);
                tv_find_city.setText(city);
                Log.e("address_latlng", ""+address);

            }catch (Exception e){

            }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

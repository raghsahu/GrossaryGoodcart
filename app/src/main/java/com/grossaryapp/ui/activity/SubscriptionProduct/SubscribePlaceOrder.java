package com.grossaryapp.ui.activity.SubscriptionProduct;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.grossaryapp.ui.SessionManager.Shared_Pref;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.InCart.BillingActivity;
import com.grossaryapp.ui.activity.InCart.PaymentActivity;
import com.grossaryapp.ui.activity.Profile.ActivityProfile;
import com.grossaryapp.ui.model.TimeSlotModel;
import com.grossaryapp.ui.model.Total_PayModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SubscribePlaceOrder extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_start_date,tv_end_date,tv_address,tv_number,tv_name,tv_select_address,tv_placeorder;
    private Boolean isDateApply = false;
    String TimeSlot_id;
    String Spin_ProductId;
    Session session;
    Spinner spin_time_slot,spin_subs_type;
    ArrayList<String> time_slot=new ArrayList<>();
    ArrayList<TimeSlotModel>timeSlotModels=new ArrayList<>();
    private String Addres_id_new;
    private String passArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunscribe_place_order);
        session=new Session(this);

        iv_back=findViewById(R.id.iv_back);
        tv_start_date=findViewById(R.id.tv_start_date);
        tv_end_date=findViewById(R.id.tv_end_date);
        tv_address=findViewById(R.id.tv_address);
        tv_select_address=findViewById(R.id.tv_select_address);
        tv_number=findViewById(R.id.tv_number);
        tv_name=findViewById(R.id.tv_name);
        spin_time_slot=findViewById(R.id.spin_time_slot);
        spin_subs_type=findViewById(R.id.spin_subs_type);
        tv_placeorder=findViewById(R.id.tv_placeorder);

        try {
            if (getIntent() != null) {
                Spin_ProductId = getIntent().getStringExtra("Spin_ProductId");
                passArray = getIntent().getStringExtra("passArray");

            }
        }catch (Exception e){

        }

        if(Connectivity.isConnected(SubscribePlaceOrder.this)){
            getShipping();
            getTimeSlot();

        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateApply = true;
                getDate();
            }
        });
        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateApply = false;
                getDate();
            }
        });


        tv_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SubscribePlaceOrder.this, ActivityProfile.class);
                // intent.putExtra("order_id", order_id);
                startActivity(intent);

            }
        });

        ///****set time slot
        spin_time_slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TimeSlot_id=timeSlotModels.get(position).getTimeSlot_id();

                Log.e("time_slot_id", TimeSlot_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //************tv_placeorder****
        tv_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Addres_id=Shared_Pref.getAddresId(SubscribePlaceOrder.this);
                String Spin_subs_type= spin_subs_type.getSelectedItem().toString();
                String From_date= tv_start_date.getText().toString();
                String To_date= tv_end_date.getText().toString();

                Log.e("passArray_pp", passArray);

                if (passArray==null || passArray.isEmpty()){
                    Toast.makeText(SubscribePlaceOrder.this, "Please add item", Toast.LENGTH_SHORT).show();

                }else if (Addres_id_new==null || Addres_id_new.isEmpty()){

                    Toast.makeText(SubscribePlaceOrder.this, "Please select delevery address", Toast.LENGTH_SHORT).show();

                }else if (TimeSlot_id==null || TimeSlot_id.isEmpty()){
                    Toast.makeText(SubscribePlaceOrder.this, "Please select time slot", Toast.LENGTH_SHORT).show();
                }else if (Spin_subs_type.isEmpty()) {
                    Toast.makeText(SubscribePlaceOrder.this, "Please select subscription type", Toast.LENGTH_SHORT).show();

                }else if (From_date.isEmpty() && To_date.isEmpty()) {
                    Toast.makeText(SubscribePlaceOrder.this, "Please select start and end date", Toast.LENGTH_SHORT).show();

                }
                else {

                if (Connectivity.isConnected(SubscribePlaceOrder.this)){
                       PlaceOrderApi(Addres_id_new,Spin_subs_type,From_date,To_date);
                    }else {
                        Toast.makeText(SubscribePlaceOrder.this, "Please check internet", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void PlaceOrderApi(final String addres_id_new, final String spin_subs_type, final String from_date, final String to_date) {

        final ProgressDialog progressDialog = new ProgressDialog(SubscribePlaceOrder.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.subcription_order_place,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("place_subsorder", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            String order_id = obj.getString("order_id");

                            Toast.makeText(SubscribePlaceOrder.this, ""+msg, Toast.LENGTH_SHORT).show();
                            if (result.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = obj.getJSONArray("data");
                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                }
                                //**go to pay charge
                                Intent intent=new Intent(SubscribePlaceOrder.this, PaymentActivity.class);
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("Total_amount", "500");
                                startActivity(intent);


                            } else {

                                // Toast.makeText(BillingActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", session.getUserId());
                params.put("time_slot_id", TimeSlot_id);
                params.put("address_id", addres_id_new);
                params.put("repeat", spin_subs_type);
                params.put("fromdate", from_date);
                params.put("todate", to_date);
                params.put("jsonarray", passArray);

                return params;
            }


        };


        VolleySingleton.getInstance(SubscribePlaceOrder.this).addToRequestQueue(stringRequest);

    }

    private void getTimeSlot() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_delivery_time_slots,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("time_slot", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = obj.getJSONArray("data");
                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    TimeSlot_id = jsonObject1.getString("id");
                                    String from_time = jsonObject1.getString("from_time");
                                    String to_time = jsonObject1.getString("to_time");

                                    time_slot.add(from_time);
                                    timeSlotModels.add(i, new TimeSlotModel(TimeSlot_id,from_time,to_time));
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (SubscribePlaceOrder.this, android.R.layout.simple_spinner_item, time_slot); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin_time_slot.setAdapter(spinnerArrayAdapter);


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

        };


        VolleySingleton.getInstance(SubscribePlaceOrder.this).addToRequestQueue(stringRequest);



    }

    private void getShipping() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.get_shipping_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrr_shipping", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = obj.getJSONArray("data");
                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String Address_id = jsonObject1.getString("id");
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



                                    if (Address_id.equalsIgnoreCase(Shared_Pref.getAddresId(SubscribePlaceOrder.this))){
                                        Addres_id_new=Address_id;
                                        tv_name.setText(first_name+" "+last_name);
                                        tv_address.setText(address+" "+address2+" "+area_name+" "+city_name+" "+state+" "+zipcode);
                                        tv_number.setText(mobile);
                                    }else {
                                        //Toast.makeText(BillingActivity.this, "Please add address", Toast.LENGTH_SHORT).show();

                                    }

                                }

                            } else {

                                Toast.makeText(SubscribePlaceOrder.this, ""+msg, Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(SubscribePlaceOrder.this).addToRequestQueue(stringRequest);

    }

    private void getDate() {

        // your action here
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        DatePickerDialog mDatePicker = new DatePickerDialog(SubscribePlaceOrder.this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd-MM-yyyy"; //Change as you need
               // SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");

                if (isDateApply){
                    tv_start_date.setText(sdf.format(myCalendar.getTime()));
                }else {
                    tv_end_date.setText(sdf.format(myCalendar.getTime()));
                }


                mDay[0] = selectedday;
                mMonth[0] = selectedmonth;
                mYear[0] = selectedyear;
            }
        }, mYear[0], mMonth[0], mDay[0]);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

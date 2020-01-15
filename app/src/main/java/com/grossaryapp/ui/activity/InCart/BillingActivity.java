package com.grossaryapp.ui.activity.InCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.SessionManager.Shared_Pref;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.Profile.ActivityProfile;
import com.grossaryapp.ui.model.AddCart;
import com.grossaryapp.ui.model.TimeSlotModel;
import com.grossaryapp.ui.model.Total_PayModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillingActivity extends AppCompatActivity {
    TextView tv_pay_bill,tv_address,tv_number,tv_coupon_apply,tv_name;
    TextView tv_gst,tv_shipping,tv_discount,tv_total,tv_item_total_amt,tv_select_address;
    ImageView iv_back;
    Session session;
    Spinner spin_time_slot;
    ArrayList<String>time_slot=new ArrayList<>();
    Total_PayModel total_payModel;
     String passArray;
     RecyclerView recycler_view_bill;
    ArrayList<AddCart>addCart=new ArrayList<>();
     String TimeSlot_id;
    private String Addres_id_new;

    ArrayList<TimeSlotModel>timeSlotModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        session=new Session(this);

        iv_back=findViewById(R.id.iv_back);
        tv_address=findViewById(R.id.tv_address);
        tv_select_address=findViewById(R.id.tv_select_address);
        tv_number=findViewById(R.id.tv_number);
        tv_coupon_apply=findViewById(R.id.tv_coupon_apply);
        tv_pay_bill=findViewById(R.id.tv_pay_bill);
        spin_time_slot=findViewById(R.id.spin_time_slot);
        tv_gst=findViewById(R.id.tv_gst);
        tv_shipping=findViewById(R.id.tv_shipping);
        tv_discount=findViewById(R.id.tv_discount);
        tv_total=findViewById(R.id.tv_total);
        tv_name=findViewById(R.id.tv_name);
        tv_item_total_amt=findViewById(R.id.tv_item_total_amt);
        recycler_view_bill=findViewById(R.id.recycler_view_bill);

      try {
            if (getIntent()!=null){
                total_payModel=(Total_PayModel)getIntent().getSerializableExtra("total_payModel");
                passArray =getIntent().getStringExtra("passArray");
              // addCart = getIntent().getParcelableArrayListExtra("addCartList");//array list

                //set value in views
                tv_gst.setText(total_payModel.getGst());
                tv_shipping.setText(total_payModel.getShippinig_charge());
                tv_discount.setText(total_payModel.getDiscount());
                tv_total.setText("₹ : "+total_payModel.getTotal_payable().toString());
                tv_item_total_amt.setText(total_payModel.getTotal_item_price());

                //set item in view bill recycler

                Log.e("addCart_size",""+addCart.size() );

            }

        }catch (Exception e){

        }


        if(Connectivity.isConnected(BillingActivity.this)){
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

        tv_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BillingActivity.this, ActivityProfile.class);
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


        tv_pay_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Addres_id=Shared_Pref.getAddresId(BillingActivity.this);

                if (passArray==null || passArray.isEmpty()){
                    Toast.makeText(BillingActivity.this, "Please add item", Toast.LENGTH_SHORT).show();

                }else if (Addres_id_new==null || Addres_id_new.isEmpty()){

                    Toast.makeText(BillingActivity.this, "Please select delevery address", Toast.LENGTH_SHORT).show();

                }else if (TimeSlot_id==null || TimeSlot_id.isEmpty()){
                    Toast.makeText(BillingActivity.this, "Please select time slot", Toast.LENGTH_SHORT).show();
                }else {
                    if (Connectivity.isConnected(BillingActivity.this)){
                        PlaceOrderApi(Addres_id_new);
                    }else {
                        Toast.makeText(BillingActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        tv_coupon_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillingActivity.this, "No available", Toast.LENGTH_SHORT).show();
                if (Connectivity.isConnected(BillingActivity.this)){
                    //Apply_Coupan_Code();
                }else {
                    Toast.makeText(BillingActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void PlaceOrderApi(final String addres_id) {
        final ProgressDialog progressDialog = new ProgressDialog(BillingActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.place_order,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("place_order", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            String order_id = obj.getString("order_id");

                            Toast.makeText(BillingActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                            if (result.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = obj.getJSONArray("data");
                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                }
                                //**go to pay charge
                              Intent intent=new Intent(BillingActivity.this, PaymentActivity.class);
                              intent.putExtra("order_id", order_id);
                              intent.putExtra("Total_amount", ""+total_payModel.getTotal_payable().toString());
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
                params.put("address_id", addres_id);
                params.put("shipping_charge", total_payModel.getShippinig_charge());
                params.put("discount", total_payModel.getDiscount());
                params.put("passArray", passArray);

                return params;
            }


        };


        VolleySingleton.getInstance(BillingActivity.this).addToRequestQueue(stringRequest);





    }

    private void Apply_Coupan_Code() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.Apply_Coupon,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrr_coopan", response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");
                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(BillingActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                               // String id = jsonObject1.getString("id");
                            } else {
                                 Toast.makeText(BillingActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
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
                 params.put("promoCode", "");
                 params.put("sub_total", "100");
                params.put("user_id", session.getUserId());

                return params;
            }
        };


        VolleySingleton.getInstance(BillingActivity.this).addToRequestQueue(stringRequest);


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

                                    String TimeSlot_id = jsonObject1.getString("id");
                                    String from_time = jsonObject1.getString("from_time");
                                    String to_time = jsonObject1.getString("to_time");

                                    time_slot.add(from_time);
                                    timeSlotModels.add(i, new TimeSlotModel(TimeSlot_id,from_time,to_time));


                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (BillingActivity.this, android.R.layout.simple_spinner_item, time_slot); //selected item will look like a spinner set from XML
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


        VolleySingleton.getInstance(BillingActivity.this).addToRequestQueue(stringRequest);



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

                                    String  Address_id = jsonObject1.getString("id");
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


                                    if (Address_id.equalsIgnoreCase(Shared_Pref.getAddresId(BillingActivity.this))){
                                        Addres_id_new=Address_id;
                                        tv_name.setText(first_name+" "+last_name);
                                        tv_address.setText(address+" "+address2+" "+area_name+" "+city_name+" "+state+" "+zipcode);
                                        tv_number.setText(mobile);
                                    }else {
                                        //Toast.makeText(BillingActivity.this, "Please add address", Toast.LENGTH_SHORT).show();

                                    }

                                }

                            } else {

                                Toast.makeText(BillingActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(BillingActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShipping();
    }
}

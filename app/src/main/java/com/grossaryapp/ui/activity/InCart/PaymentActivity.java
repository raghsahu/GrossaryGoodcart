package com.grossaryapp.ui.activity.InCart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.PaytmIntegration.checksum;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.SessionManager.Shared_Pref;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.ActivityNavigation;
import com.grossaryapp.ui.activity.ActivityWishlists;
import com.grossaryapp.ui.activity.Login.ActivityLogin;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.generateChecksum;
import static com.grossaryapp.ui.Retrofit.Base_Url.login;
import static com.grossaryapp.ui.Retrofit.Base_Url.order_bill_payment;

public class PaymentActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    TextView confirmorder,tv_order_id,tv_order_total;
    ImageView iv_back;
    String order_id;
    RadioButton radio_paytm,radio_cash;
    String payment_method="";
    private String Total_amount;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        session=new Session(PaymentActivity.this);

        confirmorder=findViewById(R.id.confirmorder);
        iv_back=findViewById(R.id.iv_back);
        tv_order_id=findViewById(R.id.tv_order_id);
        radio_cash=findViewById(R.id.radio_cash);
        radio_paytm=findViewById(R.id.radio_paytm);
        tv_order_total=findViewById(R.id.tv_order_total);

        try {
            if (getIntent()!=null){
                order_id=getIntent().getStringExtra("order_id");
                Total_amount=getIntent().getStringExtra("Total_amount");
                tv_order_id.setText("Order Id: "+order_id);
                tv_order_total.setText("Pay Total Rs: "+Total_amount);
            }
        }catch (Exception e)
        {
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!payment_method.isEmpty() && payment_method!=null){

                    if (Connectivity.isConnected(PaymentActivity.this)){
                       // payBillApi();
//                        Intent intent = new Intent(PaymentActivity.this, checksum.class);
//                        intent.putExtra("orderid", order_id);
//                        intent.putExtra("custid",session.getUserId() );
//                        startActivity(intent);

                        GenerateChecksum();

                    }else {
                        Toast.makeText(PaymentActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(PaymentActivity.this, "Please select payment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radio_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radio_paytm.isChecked()){
                    payment_method="Paytm";
                }
            }
        });

    }

    private void GenerateChecksum() {
        final ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, generateChecksum,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("after_pay_bill",response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");


                            if (result.equalsIgnoreCase("true")){
                                String check_sum_hash = obj.getString("check_sum_hash");
                               // Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
                                HashMap<String, String> paramMap = new HashMap<String,String>();
                                paramMap.put( "MID" , "URBHuG73911014357454");
// Key in your staging and production MID available in your dashboard
                                paramMap.put( "ORDER_ID" , order_id);
                                paramMap.put( "CUST_ID" , session.getUserId());
                              //  paramMap.put( "MOBILE_NO" , "7777777777");
                               // paramMap.put( "EMAIL" , "username@emailprovider.com");
                                paramMap.put( "CHANNEL_ID" , "WAP");
                                paramMap.put( "TXN_AMOUNT" , Total_amount);
                                paramMap.put( "WEBSITE" , "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
                                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
                                paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+order_id);
                                paramMap.put( "CHECKSUMHASH" , check_sum_hash);

                                PaytmPGService Service = PaytmPGService.getStagingService();

                                PaytmOrder Order = new PaytmOrder(paramMap);
                                //intializing the paytm service
                                Service.initialize(Order, null);

                                //finally starting the payment transaction
                                Service.startPaymentTransaction(PaymentActivity.this, true, true, PaymentActivity.this);




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
                params.put("MID", "URBHuG73911014357454");
                params.put("ORDER_ID", order_id);
                params.put("CUST_ID", session.getUserId());
                params.put("CHANNEL_ID", "WAP");
                params.put("TXN_AMOUNT", Total_amount);
                params.put("WEBSITE", "WEBSTAGING");
                params.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+order_id);
                params.put("INDUSTRY_TYPE_ID", "Retail");
                return params;
            }
        };

        VolleySingleton.getInstance(PaymentActivity.this).addToRequestQueue(stringRequest);

    }

    private void payBillApi() {
        final ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, order_bill_payment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("after_pay_bill",response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")){

                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(PaymentActivity.this, ConfirmationActivity.class);
                                intent.putExtra("order_id", order_id);
                                startActivity(intent);
                                finish();
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
                params.put("order_id", order_id);
                params.put("payment_status", "Paid");
                params.put("payment_method", payment_method);
                params.put("transaction_id", "123456");
                return params;
            }
        };

        VolleySingleton.getInstance(PaymentActivity.this).addToRequestQueue(stringRequest);
    }





    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent(PaymentActivity.this, ActivityNavigation.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Toast.makeText(this, inResponse.toString(), Toast.LENGTH_LONG).show();
        Log.e("inResponse", inResponse.toString());
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_LONG).show();
        Log.e("inErrorMessage", inErrorMessage.toString());
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_LONG).show();
        Log.e("inErrorMessage1", inErrorMessage.toString());
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_LONG).show();
        Log.e("inErrorMessage2", inErrorMessage.toString());
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(this, inErrorMessage + inResponse.toString(), Toast.LENGTH_LONG).show();
        Log.e("inErrorMessage3", inErrorMessage.toString()+" "+inResponse.toString());
    }


}

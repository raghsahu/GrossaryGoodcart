package com.grossaryapp.ui.activity.InCart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.activity.ActivityNavigation;
import com.grossaryapp.ui.activity.Category_Product.CategoryActivity;
import com.grossaryapp.ui.activity.Login.ActivityLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.add_order_rating_reviews;
import static com.grossaryapp.ui.Retrofit.Base_Url.login;

public class ConfirmationActivity extends AppCompatActivity {
    TextView Continueshopping,tv_conf_success,tv_go_history;
    ImageView back;
    String order_id;
    RatingBar ratingBar;
    EditText et_comments;
    Button btn_rating;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Continueshopping=findViewById(R.id.Continueshopping);
        tv_conf_success=findViewById(R.id.tv_conf_success);
        tv_go_history=findViewById(R.id.tv_go_history);
        ratingBar=findViewById(R.id.ratingBar);
        et_comments=findViewById(R.id.et_comments);
        btn_rating=findViewById(R.id.btn_rating);
        back=findViewById(R.id.back);
        session = new Session(ConfirmationActivity.this);

        try {
            if (getIntent()!=null){
                order_id=getIntent().getStringExtra("order_id");
                //tv_conf_success.setText("Order Id: "+order_id);
                tv_conf_success.setText("Your Order "+ order_id + " has succesfully submitted.");
            }
        }catch (Exception e)
        {
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Continueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConfirmationActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_go_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConfirmationActivity.this, OrderhistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating=String.valueOf(ratingBar.getRating());
                String Et_comment=et_comments.getText().toString();

                if (!rating.isEmpty() && rating!=null && !rating.equalsIgnoreCase("0.0")){

                    if (Connectivity.isConnected(ConfirmationActivity.this)){
                        RateReviewApi(rating,Et_comment);
                    }else {
                        Toast.makeText(ConfirmationActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(ConfirmationActivity.this, "Please enter rating", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void RateReviewApi(final String rating, final String et_comment) {
        final ProgressDialog progressDialog = new ProgressDialog(ConfirmationActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, add_order_rating_reviews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("check <>",response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                                et_comments.setText("");
                                ratingBar.setRating((float) 0.0);

                                JSONObject obj1 = obj.getJSONObject("data");
                                String id = obj1.getString("id");


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
                params.put("user_id", session.getUserId());
                params.put("rating", rating);
                params.put("review", et_comment);
                return params;
            }
        };

        VolleySingleton.getInstance(ConfirmationActivity.this).addToRequestQueue(stringRequest);
    }



    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        Intent intent=new Intent(ConfirmationActivity.this,ActivityNavigation.class);
        startActivity(intent);
        finish();

    }
}

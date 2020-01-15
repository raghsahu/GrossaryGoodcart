package com.grossaryapp.ui.activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.SessionManager.Shared_Pref;
import com.grossaryapp.ui.activity.ActivityNavigation;
import com.grossaryapp.ui.Utils.VolleySingleton;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity {
    EditText editTextUsername, editTextMobile, editTextEmail, editTextPassword;
    CheckBox ch_terms;
    ImageView iv_signUp;
    private String URL_REGISTER = "http://logicalsofttech.com/goodcart/Api/register";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        session=new Session(this);
        editTextUsername = (EditText) findViewById(R.id.et_name);
        editTextMobile = (EditText) findViewById(R.id.et_phone);
        editTextEmail = (EditText) findViewById(R.id.et_email);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        ch_terms = findViewById(R.id.ch_terms);
        iv_signUp = findViewById(R.id.iv_signUp);



        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySignUp.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.iv_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySignUp.this, ActivityNavigation.class);
                startActivity(intent);
                finish();
            }
        });
        iv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();

            }
        });
    }

    private void registerUser() {

        final String username = editTextUsername.getText().toString().trim();
        final String mobile = editTextMobile.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            editTextMobile.setError("Enter a mobile");
            editTextMobile.requestFocus();

            return;
        }

        if (editTextMobile.getText().toString().length()==10){

            if (!ch_terms.isChecked() ) {
                Toast.makeText(ActivitySignUp.this, "Please indicate that you accept the Terms and Conditions ", Toast.LENGTH_LONG).show();
            } else {
                String url = URL_REGISTER + "singUp";
                CallSignUpApi(url,username,mobile,email,password);

            }

        }else {
            Toast.makeText(this, "Please enter 10 digit mobile no.", Toast.LENGTH_SHORT).show();
        }


       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d("check error", response.toString());
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String result = obj.getString("result");
                            JSONObject obj1 = obj.getJSONObject("data");
                            String id = obj1.getString("id");
                            String user_id = obj1.getString("user_id");
                            String name = obj1.getString("name");
                            String mobile = obj1.getString("mobile");
                            String email = obj1.getString("email");
                            String password = obj1.getString("password");
                            String address = obj1.getString("address");
                            String status = obj1.getString("status");
                            String image = obj1.getString("image");
                            String email_status = obj1.getString("email_status");
                            String mobile_status = obj1.getString("mobile_status");
                            String created_at = obj1.getString("created_at");
                            String fcm_id = obj1.getString("fcm_id");

                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(getApplicationContext(), "suceesfully registered", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ActivitySignUp.this, ActivityLogin.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "not suceesfully registered", Toast.LENGTH_LONG).show();

                            }


                            if (!obj.getBoolean("error")) {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }*/

    }
    private void CallSignUpApi(String url, final String username, final String mobile, final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(ActivitySignUp.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("check_error", response.toString());
                            try {
                                progressDialog.dismiss();
                                //converting response to json object
                                JSONObject obj = new JSONObject(response);


                                String result = obj.getString("result");
                                JSONObject obj1 = obj.getJSONObject("data");
                                String id = obj1.getString("id");
                                String user_id = obj1.getString("user_id");
                                String name = obj1.getString("name");
                                String mobile = obj1.getString("mobile");
                                String email = obj1.getString("email");
                                String password = obj1.getString("password");
                                String address = obj1.getString("address");
                                String status = obj1.getString("status");
                                String image = obj1.getString("image");
                                String email_status = obj1.getString("email_status");
                                String mobile_status = obj1.getString("mobile_status");
                                String created_at = obj1.getString("created_at");
                                String fcm_id = obj1.getString("fcm_id");

                                if (result.equalsIgnoreCase("true")) {

                                    session.setLogin(true);
                                    session.setUserId(id);
                                    session.setMobile(mobile,email);
                                    session.setUser_name(name);
                                    session.setPro_Image(image);
                                    Shared_Pref.setUser_Id(ActivitySignUp.this, id);

                                    Toast.makeText(getApplicationContext(), "suceesfully registered", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ActivitySignUp.this, ActivityNavigation.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "not suceesfully registered", Toast.LENGTH_LONG).show();

                                }


//                                if (!obj.getBoolean("error")) {
//
//                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                }

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
                    params.put("name", username);
                    params.put("mobile", mobile);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        }
        }










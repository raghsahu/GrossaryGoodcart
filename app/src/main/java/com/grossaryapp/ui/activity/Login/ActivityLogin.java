package com.grossaryapp.ui.activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.grossaryapp.ui.SessionManager.Shared_Pref;
import com.grossaryapp.ui.activity.ActivityNavigation;
import com.grossaryapp.ui.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.login;

public class ActivityLogin extends AppCompatActivity {
    private String  URL_LOGIN="http://logicalsofttech.com/goodcart/Api/login";
    EditText editTextUsername, editTextPassword;
    TextView tv_forgot;
    ImageView iv_login;
    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new Session(ActivityLogin.this);
        editTextUsername = (EditText) findViewById(R.id.et_email);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        tv_forgot=findViewById(R.id.tv_forgot);
        iv_login=findViewById(R.id.iv_login);
        initView();


    }

    private void initView() {
        findViewById(R.id.tv_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignUp.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.iv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivityNavigation.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.tv_forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ForgotPasswordActivity.class);
                startActivity(intent);
               // finish();
            }
        });
        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }
    private void userLogin(){
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();


      if (TextUtils.isEmpty(username)) {
        editTextUsername.setError("Please enter your username");
        editTextUsername.requestFocus();
        return;
    }else if (TextUtils.isEmpty(password)) {
        editTextPassword.setError("Please enter your password");
        editTextPassword.requestFocus();
        return;
    }else {
          final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this,R.style.MyGravity);
          progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
          progressDialog.show();

          StringRequest stringRequest = new StringRequest(Request.Method.POST, login,
                  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          Log.d("check <>",response.toString());
                          try {
                              progressDialog.dismiss();
                              //converting response to json object
                              JSONObject obj = new JSONObject(response);
                              String result = obj.getString("result");

                              if (result.equalsIgnoreCase("true")){

                                  JSONObject obj1 = obj.getJSONObject("data");
                                  String id = obj1.getString("id");
                                  String user_id=obj1.getString("user_id");
                                  String name=obj1.getString("name");
                                  String mobile=obj1.getString("mobile");
                                  String email=obj1.getString("email");
                                  String password=obj1.getString("password");
                                  String address=obj1.getString("address");
                                  String status=obj1.getString("status");
                                  String image=obj1.getString("image");
                                  String email_status=obj1.getString("email_status");
                                  String mobile_status=obj1.getString("mobile_status");
                                  String created_at=obj1.getString("created_at");
                                  String fcm_id=obj1.getString("fcm_id");

                                  session.setLogin(true);
                                  session.setUserId(id);
                                  session.setMobile(mobile,email);
                                  session.setUser_name(name);
                                  session.setPro_Image(image);
                                  Shared_Pref.setUser_Id(ActivityLogin.this, id);

                                  Toast.makeText(getApplicationContext(),"suceesfully login",Toast.LENGTH_LONG).show();
                                  Intent intent=new Intent(ActivityLogin.this,ActivityNavigation.class);
                                  startActivity(intent);
                              }else{
                                  String msg = obj.getString("msg");
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
                  params.put("mobile_email", username);
                  params.put("password", password);
                  return params;
              }
          };

          VolleySingleton.getInstance(ActivityLogin.this).addToRequestQueue(stringRequest);
      }
    }

}







    /*private void clickListner() {

    }*/



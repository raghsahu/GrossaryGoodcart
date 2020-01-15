package com.grossaryapp.ui.activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.grossaryapp.R;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.grossaryapp.ui.Retrofit.Base_Url.forget_password;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText et_email;
    TextView tv_back;
    CircleImageView iv_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et_email=findViewById(R.id.et_email);
        tv_back=findViewById(R.id.tv_back);
        iv_reset=findViewById(R.id.iv_reset);

        iv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Et_Email=et_email.getText().toString();
                if (!Et_Email.isEmpty()){
                    if (Connectivity.isConnected(ForgotPasswordActivity.this)){
                        ForgotApi(Et_Email);

                    }else {
                        Toast.makeText(ForgotPasswordActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPasswordActivity.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void ForgotApi(final String et_email) {

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, forget_password,
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
                                Intent intent=new Intent(ForgotPasswordActivity.this, ActivityLogin.class);
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
                params.put("email", et_email);

                return params;
            }
        };

        VolleySingleton.getInstance(ForgotPasswordActivity.this).addToRequestQueue(stringRequest);
    }


}

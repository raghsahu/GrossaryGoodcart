package com.grossaryapp.ui.activity.Profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.Retrofit.Base_Url;
import com.grossaryapp.ui.Utils.Connectivity;
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.Images_Profile;
import static com.grossaryapp.ui.Utils.PathUtils.bitmapToFile;
import static com.grossaryapp.ui.activity.ActivityNavigation.nav_img_profile;

public class EditProfileActivity extends AppCompatActivity {

    EditText et_name,et_mobile,et_email;
    Button btn_update;
    ImageView profile_image,iv_back;
    Session session;
    File imgFile;
    private boolean isPermitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        session=new Session(this);
        et_name=findViewById(R.id.et_name);
        et_mobile=findViewById(R.id.et_mobile);
        et_email=findViewById(R.id.et_email);
        btn_update=findViewById(R.id.btn_update);
        profile_image=findViewById(R.id.profile_image);
        iv_back=findViewById(R.id.iv_back);


        if (Connectivity.isConnected(this)) {

            getEditProfile();
        }else {
            Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name=et_name.getText().toString();
                String Mobile=et_mobile.getText().toString();
                String Email=et_email.getText().toString();

                if (Connectivity.isConnected(EditProfileActivity.this)){
                    //UpdateProfile(Name,Mobile,Email);
                    UpdateProfile1(Name,Mobile,Email);//by android networking
                }else {
                    Toast.makeText(EditProfileActivity.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

          onBackPressed();

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkRunTimePermission();
            }
        });

    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
            selectImage();
        }
    }

    private void selectImage() {

        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                    profile_image.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = bitmapToFile(EditProfileActivity.this, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);
                    String filename = imgFile.getName();
                    Log.e("filweName = ", filename);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(EditProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(EditProfileActivity.this);
    }

    private void UpdateProfile1(String name, String mobile, String email) {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        //String url=BaseUrl+get_communities;
        AndroidNetworking.upload(Base_Url.update_profile)
                .addMultipartParameter("user_id", session.getUserId())
                 .addMultipartParameter("name",name )
                 .addMultipartParameter("email",email )
                 .addMultipartParameter("mobile",mobile )
                 .addMultipartFile("image",imgFile )
                //.setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        progressDialog.dismiss();

                        Log.d("rrrProfile", jsonObject.toString());
                        try {

                           // JSONObject obj = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(EditProfileActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String mobile = jsonObject1.getString("mobile");
                                String email = jsonObject1.getString("email");
                                String address = jsonObject1.getString("address");
                                String image = jsonObject1.getString("image");

                                session.setPro_Image(image);
                                session.setUserId(id);
                                session.setMobile(mobile,email);
                                session.setUser_name(name);

                                if (!image.isEmpty() && !image.equalsIgnoreCase("") && image!=null){

                                    Glide.with(EditProfileActivity.this)
                                            .load(Images_Profile + image)
                                            .placeholder(R.drawable.orange1)
                                            //.override(300, 200)
                                            .error(R.drawable.orange1)
                                            .into(nav_img_profile);
                                }


                                Intent intent=new Intent(EditProfileActivity.this, ActivityProfile.class);
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(EditProfileActivity.this, "Fail, please try again", Toast.LENGTH_SHORT).show();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();

                    }
                });


    }

    private void UpdateProfile(final String name, final String mobile, final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.update_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrProfile", response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if (result.equalsIgnoreCase("true")) {

                                Toast.makeText(EditProfileActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1 = obj.getJSONObject("data");

                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("name");
                                String mobile = jsonObject1.getString("mobile");
                                String email = jsonObject1.getString("email");
                                String address = jsonObject1.getString("address");
                                String image = jsonObject1.getString("image");

//                                et_email.setText(email);
//                                et_name.setText(name);
//                                et_mobile.setText(mobile);
//
//
//                                if (!image.isEmpty() && !image.equalsIgnoreCase("") && image!=null){
//
//                                    Glide.with(EditProfileActivity.this)
//                                            .load(Images_Profile + image)
//                                            .placeholder(R.drawable.orange1)
//                                            //.override(300, 200)
//                                            .error(R.drawable.orange1)
//                                            .into(profile_image);
//                                }


                                Intent intent=new Intent(EditProfileActivity.this, ActivityProfile.class);
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(EditProfileActivity.this, "Fail, please try again", Toast.LENGTH_SHORT).show();

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
                 params.put("name", name);
                 params.put("email", email);
                 params.put("mobile", mobile);
                params.put("user_id", session.getUserId());

                return params;
            }
        };


        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);


    }

    private void getEditProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.edit_profile,
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

                                et_email.setText(email);
                                et_name.setText(name);
                                et_mobile.setText(mobile);


                                if (!image.isEmpty() && !image.equalsIgnoreCase("") && image!=null){

                                    Glide.with(EditProfileActivity.this)
                                            .load(Images_Profile + image)
                                            .placeholder(R.drawable.orange1)
                                            //.override(300, 200)
                                            .error(R.drawable.orange1)
                                            .into(profile_image);
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


        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {

            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(EditProfileActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            }catch (Exception e){

            }

            if (isPermitted){
                selectImage();
                // getNumber(getActivity().getContentResolver());
            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

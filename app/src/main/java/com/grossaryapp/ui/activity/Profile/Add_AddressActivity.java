package com.grossaryapp.ui.activity.Profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.grossaryapp.ui.Utils.VolleySingleton;
import com.grossaryapp.ui.model.AreaData;
import com.grossaryapp.ui.model.CityData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.grossaryapp.ui.Retrofit.Base_Url.add_shipping_address;
import static com.grossaryapp.ui.Retrofit.Base_Url.address_images;
import static com.grossaryapp.ui.Retrofit.Base_Url.get_areas;
import static com.grossaryapp.ui.Retrofit.Base_Url.get_cities;
import static com.grossaryapp.ui.Retrofit.Base_Url.update_shipping_address;

public class Add_AddressActivity extends AppCompatActivity {

    String AreaId,CityId;
    EditText et_name,et_lastname,et_email,et_mobile,et_address,et_addres_two,et_zipcode;
    String Et_name,Et_lastname,Et_email,Et_mobile,Et_address,Et_addres_two,Et_zipcode;
    Button btn_add_address;
    ImageView iv_address,locback;
    double latitu=0.0 ,longu=0.0;
    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/Community";
    File imgFile;
    private boolean isPermitted;

    ArrayList<String> spin_city=new ArrayList<>();
    ArrayList<String> spin_aera=new ArrayList<>();
    public HashMap<Integer, CityData> CityHashMap = new HashMap<>();
    public HashMap<Integer, AreaData> AreaHashMap = new HashMap<>();
    ArrayList<CityData>cityDataArrayList=new ArrayList<>();
    ArrayList<AreaData>areaDataArrayList=new ArrayList<>();
    Spinner spinner_city,spinner_area;
    ArrayAdapter<String> spinnerArrayAdapter;
    private String path;
    Session session;
    String Address_Id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__address);
        session=new Session(this);

        et_name=findViewById(R.id.et_name);
        et_lastname=findViewById(R.id.et_lastname);
        et_mobile=findViewById(R.id.et_mobile);
        et_email=findViewById(R.id.et_email);
        et_address=findViewById(R.id.et_address);
        et_addres_two=findViewById(R.id.et_addres_two);
        et_zipcode=findViewById(R.id.et_zipcode);
        btn_add_address=findViewById(R.id.btn_add_address);
        spinner_city=findViewById(R.id.spinner_city);
        spinner_area=findViewById(R.id.spinner_area);
        iv_address=findViewById(R.id.iv_address);
        locback=findViewById(R.id.locback);



        try {
            if (getIntent()!=null){
                Address_Id=getIntent().getStringExtra("Address_Edit");

                if (!Address_Id.equalsIgnoreCase("") && Address_Id!=null){
                    btn_add_address.setText("Update address");

                    if (Connectivity.isConnected(Add_AddressActivity.this)){
                        getEditAddress(Address_Id);

                    }else {
                        Toast.makeText(this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }catch (Exception e){
            Address_Id="";
        }

        if (Connectivity.isConnected(Add_AddressActivity.this)){
            getCity();
        }else {
            Toast.makeText(this, "Please check internet", Toast.LENGTH_SHORT).show();
        }

        iv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRunTimePermission();
            }
        });

        locback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                    if(areaDataArrayList.size() !=0)
                    {
                        spin_aera.clear();
                        spinner_area.setAdapter(null);
                        spinnerArrayAdapter.notifyDataSetChanged();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < CityHashMap.size(); i++)
                {
                    if (CityHashMap.get(i).getName().equals( spinner_city.getItemAtPosition(position)))
                    {
                        CityId=CityHashMap.get(i).getId();

                        getAreaByCity(CityId);

                    }else {
                        // CityId="";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < AreaHashMap.size(); i++)
                {
                    if (AreaHashMap.get(i).getName().equals( spinner_area.getItemAtPosition(position)))
                    {
                        AreaId=AreaHashMap.get(i).getId();

                        //  getAreaByCity(CityId);

                    }else {
                        //  AreaId="";
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Et_name=et_name.getText().toString();
                Et_lastname=et_lastname.getText().toString();
                Et_email=et_email.getText().toString();
                Et_mobile=et_mobile.getText().toString();
                Et_address=et_address.getText().toString();
                Et_addres_two=et_addres_two.getText().toString();
                Et_zipcode=et_zipcode.getText().toString();

                if (!Et_name.isEmpty() && !Et_mobile.isEmpty() && ! Et_address.isEmpty()
                    && !Et_zipcode.isEmpty()){

                    findLatLng(Et_zipcode);//****find lat lng

                    if (longu!=0.0 && latitu!=0.0){//***lat lng not empty

                        if (Address_Id!=null && !Address_Id.equalsIgnoreCase("") &&
                                btn_add_address.getText().toString().equalsIgnoreCase("Update address")){

                            UpdateAddress(Address_Id,Et_name,Et_lastname,Et_email,Et_mobile,Et_address,Et_addres_two,
                                    Et_zipcode);

                        }else {

                            if (Connectivity.isConnected(Add_AddressActivity.this)){

                                AddAddress(Et_name,Et_lastname,Et_email,Et_mobile,Et_address,Et_addres_two,
                                        Et_zipcode);
                            }else {
                                Toast.makeText(Add_AddressActivity.this, "Please check internet", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else {
                        Toast.makeText(Add_AddressActivity.this, "Please enter correct zipcode", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(Add_AddressActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void UpdateAddress(final String address_Id, final String et_name, final String et_lastname, final String et_email,
                               final String et_mobile, final String et_address, final String et_addres_two,
                               final String et_zipcode) {

        final ProgressDialog progressDialog = new ProgressDialog(Add_AddressActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_shipping_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("add_shiping_update",response.toString());
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
                params.put("id", address_Id);
                params.put("first_name", et_name);
                params.put("last_name", et_lastname);
                params.put("email", et_email);
                params.put("mobile", et_mobile);
                params.put("address", et_address);
                params.put("address2", et_addres_two);
                params.put("city_id", CityId);
                params.put("area_id", AreaId);
                params.put("google_location", "");
                params.put("lat", String.valueOf(latitu));
                params.put("lng", String.valueOf(longu));
                params.put("zipcode", et_zipcode);
                params.put("address_image", "");
                return params;
            }
        };

        VolleySingleton.getInstance(Add_AddressActivity.this).addToRequestQueue(stringRequest);


    }

    private void getEditAddress(final String address_Id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Base_Url.edit_address,
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


                                et_name.setText(first_name);
                                et_lastname.setText(last_name);
                                et_mobile.setText(mobile);
                                et_email.setText(email);
                                et_address.setText(address);
                                et_addres_two.setText(address2);
                                et_zipcode.setText(zipcode);


                                if (!address_image.isEmpty() && !address_image.equalsIgnoreCase("") && address_image!=null){

                                    Glide.with(Add_AddressActivity.this)
                                            .load(address_images + address_image)
                                            .placeholder(R.drawable.orange1)
                                            //.override(300, 200)
                                            .error(R.drawable.orange1)
                                            .into(iv_address);
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
                params.put("id",address_Id );

                return params;
            }
        };


        VolleySingleton.getInstance(Add_AddressActivity.this).addToRequestQueue(stringRequest);



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
        showPictureDialog();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void getAreaByCity(final String cityId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_areas,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        Log.d("sss",response.toString());
                        try {
                            areaDataArrayList.clear();

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");


                            if (result.equalsIgnoreCase("true")) {
                                JSONArray data=obj.getJSONArray("data");

                                for (int i=0; i<data.length();i++){

                                    JSONObject jsonObject1=data.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");


                                    spin_aera.add(i,name);
                                    areaDataArrayList.add(i, new AreaData(id,name));
                                    AreaHashMap.put(i ,new AreaData(id,name) );
                                }
                                Log.e("spin_citysize", ""+spin_city.size());
                                spinnerArrayAdapter = new ArrayAdapter<String>
                                        (Add_AddressActivity.this, android.R.layout.simple_spinner_item, spin_aera); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_area.setAdapter(spinnerArrayAdapter);

                            }
                            else {
                                Toast.makeText(Add_AddressActivity.this, "No area found", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            //  progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city_id", cityId);

                return params;
            }

        };



        VolleySingleton.getInstance(Add_AddressActivity.this).addToRequestQueue(stringRequest);



    }

    private void getCity() {
        final ProgressDialog progressDialog = new ProgressDialog(Add_AddressActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, get_cities,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("sss",response.toString());
                        try {

                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");


                            if (result.equalsIgnoreCase("true")) {
                                JSONArray data=obj.getJSONArray("data");

                                for (int i=0; i<data.length();i++){

                                    JSONObject jsonObject1=data.getJSONObject(i);
                                    String id=jsonObject1.getString("id");
                                    String name=jsonObject1.getString("name");


                                    spin_city.add(i,name);
                                    cityDataArrayList.add(i, new CityData(id,name));
                                    CityHashMap.put(i ,new CityData(id,name) );
                                }
                                Log.e("spin_citysize", ""+spin_city.size());
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (Add_AddressActivity.this, android.R.layout.simple_spinner_item, spin_city); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_city.setAdapter(spinnerArrayAdapter);



                            }
                            else {
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
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };



        VolleySingleton.getInstance(Add_AddressActivity.this).addToRequestQueue(stringRequest);




    }

    public void  findLatLng(String strAddress){

            Geocoder coder = new Geocoder(this);
            List<Address> address;
           // GeoPoint p1 = null;

            try {
                address = coder.getFromLocationName(strAddress,5);
                if (address==null) {
                    //return null;
                }
                Address location=address.get(0);
              latitu=  location.getLatitude();
              longu=  location.getLongitude();

                Log.e("lat_long123", ""+location.getLatitude()+" "+location.getLongitude());
//                p1 = new lat((double) (location.getLatitude() * 1E6),
//                        (double) (location.getLongitude() * 1E6));


            }catch (Exception e){

            }
            //return p1;
        }

    private void AddAddress(final String et_name, final String et_lastname, final String et_email,
                            final String et_mobile, final String et_address, final String et_addres_two,
                            final String et_zipcode) {

        final ProgressDialog progressDialog = new ProgressDialog(Add_AddressActivity.this,R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, add_shipping_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("add_shiping",response.toString());
                        try {
                            progressDialog.dismiss();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            String msg = obj.getString("msg");

                            if (result.equalsIgnoreCase("true")){
                                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(Add_AddressActivity.this,ActivityProfile.class);
                                startActivity(intent);
                                finish();

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
                params.put("first_name", et_name);
                params.put("last_name", et_lastname);
                params.put("email", et_email);
                params.put("mobile", et_mobile);
                params.put("address", et_address);
                params.put("address2", et_addres_two);
                params.put("city_id", CityId);
                params.put("area_id", AreaId);
                params.put("google_location", "");
                params.put("lat", String.valueOf(latitu));
                params.put("lng", String.valueOf(longu));
                params.put("zipcode", et_zipcode);
                params.put("address_image", "");
                return params;
            }
        };

        VolleySingleton.getInstance(Add_AddressActivity.this).addToRequestQueue(stringRequest);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                imgFile=  new File(getPath(contentURI));

                Log.e("img_file", imgFile.getPath());

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    path = saveImage(bitmap);
                    Toast.makeText(Add_AddressActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    iv_address.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Add_AddressActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            imgFile=  new File(getPath(contentURI));

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv_address.setImageBitmap(thumbnail);
            path= saveImage(thumbnail);
            Toast.makeText(Add_AddressActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPath(Uri path) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(path, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
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
                            Toast.makeText(Add_AddressActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
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

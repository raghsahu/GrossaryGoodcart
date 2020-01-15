package com.grossaryapp.ui.activity.Login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.florent37.viewanimator.ViewAnimator;
import com.grossaryapp.R;
import com.grossaryapp.ui.SessionManager.Session;
import com.grossaryapp.ui.activity.ActivityNavigation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivitySplash extends AppCompatActivity {
    private ImageView splash_logo_image;
     private Session session;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         session = new Session(this);
        splash_logo_image = findViewById(R.id.splash_logo_image);

       /* Glide.with(this)
                .load(R.drawable.splash_logo)
                .centerCrop()
                .placeholder(R.drawable.splash_logo)
                .into(splash_logo_image);*/
        printHashKey();


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[5])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[6])
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            } else {

                ViewAnimator
                        .animate(splash_logo_image)
                        .translationY(-1000, 0)
                        .alpha(0, 1)
                        .thenAnimate(splash_logo_image)
                        .scale(1f, 0.5f, 1f)
                        .accelerate()
                        .duration(1000)
                        .start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Animatoo.animateZoom(ActivitySplash.this);


                        Log.e("sess_logi", session.getUserId());
                        if (session.isLoggedIn()) {

                            if (session.getUserId()!= null && !session.getUserId().equalsIgnoreCase("")) {
                                Log.e("sess_logi", ""+session.isLoggedIn());
                                Intent intent1=new Intent(ActivitySplash.this, ActivityNavigation.class);
                                startActivity(intent1);
                                finish();

                                Animatoo.animateFade(ActivitySplash.this);
                            } else
                                Toast.makeText(ActivitySplash.this, "User not exist", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("sess_logi", ""+session.isLoggedIn());
                            Intent intent1 = new Intent(ActivitySplash.this, ActivityWelcome.class);
                            startActivity(intent1);
                            finish();
                            Animatoo.animateFade(ActivitySplash.this);
                        }



                    }
                }, 4000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception", "" + e);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        System.out.println(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[1] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[2] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[3] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[4] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[5] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[6] == PackageManager.PERMISSION_GRANTED);


        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 7 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == PackageManager.PERMISSION_GRANTED
            ) {

                ViewAnimator
                        .animate(splash_logo_image)
                        .translationY(-1000, 0)
                        .alpha(0, 1)
                        .thenAnimate(splash_logo_image)
                        .scale(1f, 0.5f, 1f)
                        .accelerate()
                        .duration(1000)
                        .start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Animatoo.animateZoom(ActivitySplash.this);
                        Log.e("sess_logi1", ""+session.isLoggedIn());
                        if (session.isLoggedIn()) {

                            if (session.getUserId()!= null && !session.getUserId().equalsIgnoreCase("")) {


                                Intent intent1=new Intent(ActivitySplash.this,ActivityNavigation.class);
                                startActivity(intent1);
                                finish();

                                Animatoo.animateFade(ActivitySplash.this);
                            } else
                                Toast.makeText(ActivitySplash.this, "User not exist", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e("sess_logi1", ""+session.isLoggedIn());
                            Intent intent1 = new Intent(ActivitySplash.this, ActivityWelcome.class);
                            startActivity(intent1);
                            finish();
                            Animatoo.animateFade(ActivitySplash.this);
                        }


                    }
                }, 4000);

            } else {
                Toast.makeText(ActivitySplash.this, "Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.grossary",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}